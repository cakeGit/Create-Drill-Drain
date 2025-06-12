package com.cake.drill_drain.mixin;

import com.cake.drill_drain.content.replacements.DrillMovementBehaviourReplacement;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.tterrag.registrate.util.nullness.NonNullConsumer;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MovementBehaviour.class, remap = false)
public interface MovementBehaviourMixin {
    @Shadow
    SimpleRegistry<Block, MovementBehaviour> REGISTRY = SimpleRegistry.create();

    /**
     * @author Cakegit
     * @reason cause i can??? (inject not allowed)
     */
    @Overwrite
    static <B extends Block> NonNullConsumer<? super B> movementBehaviour(MovementBehaviour behaviour) {
        if (behaviour instanceof DrillMovementBehaviour) {
            return b -> REGISTRY.register(b, new DrillMovementBehaviourReplacement());
        }
        return b -> REGISTRY.register(b, behaviour);
    }

}
