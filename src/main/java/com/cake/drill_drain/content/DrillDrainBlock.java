package com.cake.drill_drain.content;

import com.cake.drill_drain.accessor.DrillBlockMixinAccess;
import com.cake.drill_drain.foundation.DDRegistry;
import com.cake.drill_drain.foundation.DDShapes;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DrillDrainBlock extends DirectionalKineticBlock implements IBE<DrillDrainBlockEntity>, ICogWheel {

    public DrillDrainBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    public Class<DrillDrainBlockEntity> getBlockEntityClass() {
        return DrillDrainBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends DrillDrainBlockEntity> getBlockEntityType() {
        return DDRegistry.DRILL_DRAIN_BLOCK_ENTITY.get();
    }

    @Override
    public Direction getPreferredFacing(BlockPlaceContext context) {
        Direction preferredFacing = super.getPreferredFacing(context);
        return preferredFacing == null ? null : preferredFacing.getOpposite();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(DirectionalBlock.FACING)
            .getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return DDShapes.DRILL_DRAIN.get(state.getValue(DirectionalBlock.FACING));
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        Block newBlock = level.getBlockState(neighborPos).getBlock();
        if (newBlock instanceof DrillBlockMixinAccess access) {
            access.create_Drill_Drain$searchForDrillPumpSource(level, neighborPos, newBlock);
        }
    }
}
