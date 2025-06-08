package com.cake.drill_drain.mixin;

import com.cake.drill_drain.accessor.DrillBlockEntityMixinAccess;
import com.cake.drill_drain.accessor.DrillBlockMixinAccess;
import com.cake.drill_drain.content.DrillDrainBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = DrillBlock.class)
public class DrillBlockMixin implements DrillBlockMixinAccess {

    /**
     * Try to search for a neighboring drill that this is to connect via
     */
    @Inject(method = "neighborChanged", at = @At("RETURN"))
    private void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving, CallbackInfo ci) {
        create_Drill_Drain$searchForDrillPumpSource(worldIn, pos, state.getBlock());
    }

    @Unique
    public void create_Drill_Drain$searchForDrillPumpSource(LevelAccessor world, BlockPos thisPos, Block block) {
        if (!(world.getBlockEntity(thisPos) instanceof DrillBlockEntity drillBlockEntity)) return;

        BlockPos existingDrillDrainPos = ((DrillBlockEntityMixinAccess) drillBlockEntity).create_Drill_Drain$getDrillDrainParent();
        BlockState thisState = world.getBlockState(thisPos);

        if (existingDrillDrainPos != null) {
            BlockState existingDrillDrainState = world.getBlockState(existingDrillDrainPos);
            if (isValidConnection(existingDrillDrainState, thisState, existingDrillDrainPos, thisPos)) {
                //Look for orphaned drills around to share the drain with
                for (Direction dir : Direction.values()) {
                    if (dir.getAxis() == thisState.getValue(DirectionalBlock.FACING).getAxis()) continue;

                    BlockPos neighborPos = thisPos.relative(dir);
                    BlockState neighborState = world.getBlockState(neighborPos);

                    if (neighborState.getBlock() instanceof DrillBlock &&

                        neighborState.getValue(DirectionalBlock.FACING) == thisState.getValue(DirectionalBlock.FACING)) {
                        BlockEntity blockEntity = world.getBlockEntity(neighborPos);

                        if (blockEntity instanceof DrillBlockEntity otherDrillBlockEntity) {
                            @Nullable BlockPos parent = ((DrillBlockEntityMixinAccess) otherDrillBlockEntity).create_Drill_Drain$getDrillDrainParent();
                            if (parent == null && isValidConnection(existingDrillDrainState, neighborState, existingDrillDrainPos, neighborPos)) {
                                ((DrillBlockEntityMixinAccess) otherDrillBlockEntity).create_Drill_Drain$setDrillDrainParent(existingDrillDrainPos);
                                world.blockUpdated(neighborPos, block);
                            }
                        }
                    }
                }
                return;
            }
            ((DrillBlockEntityMixinAccess) drillBlockEntity).create_Drill_Drain$setDrillDrainParent(null);
            world.blockUpdated(thisPos, block);
            return;
        }

        //Search for a drill drain to connect to
        ((DrillBlockEntityMixinAccess) drillBlockEntity).create_Drill_Drain$setDrillDrainParent(null);
        for (Direction dir : Direction.values()) {

            BlockPos neighbor = thisPos.relative(dir);
            BlockState neighborState = world.getBlockState(neighbor);
            
            //Look for a potential drain
            if (dir.getAxis() == thisState.getValue(DirectionalBlock.FACING).getAxis()) {
                if (dir == thisState.getValue(DirectionalBlock.FACING).getOpposite() && isValidConnection(neighborState, thisState, neighbor, thisPos)) {
                    ((DrillBlockEntityMixinAccess) drillBlockEntity).create_Drill_Drain$setDrillDrainParent(neighbor);
                    world.blockUpdated(thisPos, block);
                    break;
                }
                continue;
            }
            
            //Look for a drill that has a parent
            if (neighborState.getBlock() instanceof DrillBlock) {
                //Get the block entity and see if the drill has a parent
                BlockEntity blockEntity = world.getBlockEntity(neighbor);
                if (blockEntity instanceof DrillBlockEntity otherDrillBlockEntity) {
                    @Nullable BlockPos parent = ((DrillBlockEntityMixinAccess) otherDrillBlockEntity).create_Drill_Drain$getDrillDrainParent();
                    if (parent != null && isValidConnection(world.getBlockState(parent), thisState, parent, thisPos)) {
                        ((DrillBlockEntityMixinAccess) drillBlockEntity).create_Drill_Drain$setDrillDrainParent(parent);
                        world.blockUpdated(thisPos, block);
                        break;
                    }
                }
            }
        }
    }

    private boolean isValidConnection(BlockState drainState, BlockState thisState, BlockPos drainPos, BlockPos thisPos) {
        return drainState.getBlock() instanceof DrillDrainBlock &&
            drainState.getValue(DrillDrainBlock.FACING) == thisState.getValue(DirectionalBlock.FACING) &&
            drainPos.distManhattan(thisPos) < 4;
    }

}
