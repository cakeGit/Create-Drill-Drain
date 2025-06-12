package com.cake.drill_drain.mixin;

import com.cake.drill_drain.accessor.SimpleRegistryImplMixinAccess;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.api.behaviour.movement.MovementBehaviour;
import com.simibubi.create.api.registry.SimpleRegistry;
import com.simibubi.create.impl.registry.SimpleRegistryImpl;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = SimpleRegistryImpl.class, remap = false)
public abstract class SimpleRegistryImplMixin<K, V> implements SimpleRegistry<K, V>, SimpleRegistryImplMixinAccess<K, V> {

    @Shadow @Final protected Map<K, V> registrations;

    @Override
    public void create_Drill_Drain$overrideRegister(K object, V value) {
        registrations.put(object, value);
    }
}
