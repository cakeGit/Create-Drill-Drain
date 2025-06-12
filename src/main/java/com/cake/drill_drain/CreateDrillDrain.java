package com.cake.drill_drain;

import com.cake.drill_drain.foundation.DDLangEntries;
import com.cake.drill_drain.foundation.DDPartialModels;
import com.cake.drill_drain.foundation.DDPonderPlugin;
import com.cake.drill_drain.foundation.DDRegistry;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import net.createmod.catnip.lang.FontHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.world.item.CreativeModeTab;

@Mod(CreateDrillDrain.MOD_ID)
public class CreateDrillDrain {
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CreateDrillDrain.MOD_ID)
        .defaultCreativeTab((ResourceKey<CreativeModeTab>) null)
        .setTooltipModifierFactory(item ->
            new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                .andThen(TooltipModifier.mapNull(KineticStats.create(item)))
        );
    ;
    public static final String MOD_ID = "drill_drain";
    public static final String NAME = "Create: Drill Drain";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CreateDrillDrain(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();
        CreateDrillDrain.REGISTRATE.registerEventListeners(modEventBus);

        DDRegistry.init();
        DDPartialModels.init();
        DDLangEntries.addToLang();
        PonderIndex.addPlugin(new DDPonderPlugin());
        modEventBus.addListener(CreateDrillDrainData::gatherData);

        context.registerConfig(ModConfig.Type.SERVER, Config.SPEC);
    }

    public static ResourceLocation asResource(String s) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, s);
    }

}
