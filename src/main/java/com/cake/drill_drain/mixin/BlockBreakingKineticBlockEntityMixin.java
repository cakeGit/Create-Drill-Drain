package com.cake.drill_drain.mixin;

import com.cake.drill_drain.accessor.DrillBlockEntityMixinAccess;
import com.simibubi.create.content.kinetics.base.BlockBreakingKineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(remap = false, value = BlockBreakingKineticBlockEntity.class)
public class BlockBreakingKineticBlockEntityMixin {

    @Inject(method = "write", at = @At("HEAD"))
    private void write(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (this instanceof DrillBlockEntityMixinAccess drillBlockEntity) {
            BlockPos parent = drillBlockEntity.create_Drill_Drain$getLocalDrillDrainParent();
            if (parent != null) {
                compound.putLong("DrillDrainParent", parent.asLong());
            }
        }
    }

    @Inject(method = "read", at = @At("HEAD"))
    private void read(CompoundTag compound, boolean clientPacket, CallbackInfo ci) {
        if (this instanceof DrillBlockEntityMixinAccess drillBlockEntity) {
            if (compound.contains("DrillDrainParent")) {
                BlockPos parent = BlockPos.of(compound.getLong("DrillDrainParent"));
                drillBlockEntity.create_Drill_Drain$setDrillDrainParent(parent);
            } else {
                drillBlockEntity.create_Drill_Drain$setDrillDrainParent(null);
            }
        }
    }

}
