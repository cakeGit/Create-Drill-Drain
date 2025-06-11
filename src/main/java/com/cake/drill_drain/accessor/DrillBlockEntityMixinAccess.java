package com.cake.drill_drain.accessor;

import net.minecraft.core.BlockPos;

public interface DrillBlockEntityMixinAccess {

    BlockPos create_Drill_Drain$getDrillDrainParent();

    BlockPos create_Drill_Drain$getLocalDrillDrainParent();

    void create_Drill_Drain$setDrillDrainParent(BlockPos drillDrainParents);

}
