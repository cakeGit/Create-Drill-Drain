package com.cake.drill_drain.mixin;

import com.cake.drill_drain.accessor.DrillBlockEntityMixinAccess;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(remap = false, value = DrillBlockEntity.class)
public class DrillBlockEntityMixin extends BlockBreakingKineticBlockEntity implements DrillBlockEntityMixinAccess {

    @Unique @Nullable
    private BlockPos create_Drill_Drain$drillDrainParent = null;

    public DrillBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public BlockPos create_Drill_Drain$getDrillDrainParent() {
        return create_Drill_Drain$drillDrainParent;
    }

    @Override
    public void create_Drill_Drain$setDrillDrainParent(BlockPos drillDrainParents) {
        create_Drill_Drain$drillDrainParent = drillDrainParents;
        sendData();
    }

    @Shadow
    @Override
    protected BlockPos getBreakingPos() {
        return null;
    }
}
