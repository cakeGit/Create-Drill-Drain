package com.cake.drill_drain.mixin;

import com.cake.drill_drain.FluidFlowSuppression;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/** Cancels scheduled vanilla fluid simulation only at positions temporarily protected by the drill drain. */
@Mixin(FlowingFluid.class)
public abstract class FlowingFluidTickMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void drill_drain$suppressScheduledFluidTick(Level level, BlockPos pos, FluidState state, CallbackInfo ci) {
        if (FluidFlowSuppression.isProtected(level, pos)) {
            ci.cancel();
        }
    }
}
