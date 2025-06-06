package com.cake.drill_drain.accessor;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public interface DrillBlockMixinAccess {

    void create_Drill_Drain$searchForDrillPumpSource(LevelAccessor world, BlockPos pos, Block block);

}
