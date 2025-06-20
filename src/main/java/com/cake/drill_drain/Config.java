package com.cake.drill_drain;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = CreateDrillDrain.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.DoubleValue FLUID_PICKUP_MODIFIER = BUILDER
        .comment("Controls how much the drill drains are able to collect (0.0 - 1.0)")
        .defineInRange("fluidPickupModifier", 0.1, 0.0, 1.0);

    static final ForgeConfigSpec SPEC = BUILDER.build();

    public static double fluidPickupModifier;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC && event.getConfig().getType() == ModConfig.Type.SERVER) {
            fluidPickupModifier = FLUID_PICKUP_MODIFIER.get();
        }
    }
}
