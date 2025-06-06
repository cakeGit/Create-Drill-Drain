package com.cake.drill_drain.content;

import com.cake.drill_drain.accessor.DrillBlockMixinAccess;
import com.cake.drill_drain.foundation.DDRegistry;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.simpleRelays.ICogWheel;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
    public Direction.Axis getRotationAxis(BlockState state) {
        return state.getValue(DirectionalBlock.FACING)
            .getAxis();
    }

    @Override
    protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block neighborBlock, BlockPos neighborPos, boolean movedByPiston) {
        super.neighborChanged(state, level, pos, neighborBlock, neighborPos, movedByPiston);
        Block newBlock = level.getBlockState(neighborPos).getBlock();
        if (newBlock instanceof DrillBlockMixinAccess access) {
            access.create_Drill_Drain$searchForDrillPumpSource(level, neighborPos, newBlock);
        }
    }
}
