package com.cake.drill_drain;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CreateDrillDrain.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue FLUID_PICKUP_MODIFIER = BUILDER
        .comment("Controls how much the drill drains are able to collect (0.0 - 1.0)")
        .defineInRange("fluidPickupModifier", 0.1, 0.0, 1.0);

    static final ModConfigSpec SPEC = BUILDER.build();

    public static double fluidPickupModifier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        fluidPickupModifier = FLUID_PICKUP_MODIFIER.get();
    }
}
