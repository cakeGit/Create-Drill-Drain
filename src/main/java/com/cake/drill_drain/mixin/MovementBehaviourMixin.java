package com.cake.drill_drain.mixin;

import com.cake.drill_drain.content.replacements.DrillMovementBehaviourReplacement;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MovementBehaviour.class)
public interface MovementBehaviourMixin {
    @Shadow
    SimpleRegistry<Block, MovementBehaviour> REGISTRY = SimpleRegistry.create();

    @Inject(method = "movementBehaviour", at = @At("HEAD"), remap = false, cancellable = true)
    private static <B extends Block> void movementBehaviour(MovementBehaviour behaviour, CallbackInfoReturnable<NonNullConsumer<? super B>> cir) {
        if (behaviour instanceof DrillMovementBehaviour) {
            cir.setReturnValue(b -> REGISTRY.register(b, new DrillMovementBehaviourReplacement()));
            cir.cancel();
        }
    }

}
