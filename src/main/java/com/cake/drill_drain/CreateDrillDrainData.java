package com.cake.drill_drain;

import com.cake.drill_drain.foundation.DDPonderPlugin;
import com.simibubi.create.AllKeys;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.ponder.CreatePonderPlugin;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateDataProvider;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CreateDrillDrainData {

    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        CreateDrillDrain.REGISTRATE.addDataGenerator(ProviderType.LANG, provider -> {
            BiConsumer<String, String> langConsumer = provider::add;

            PonderIndex.addPlugin(new DDPonderPlugin());
            PonderIndex.getLangAccess().provideLang(CreateDrillDrain.MOD_ID, langConsumer);
        });
        event.getGenerator().addProvider(true,
            CreateDrillDrain.REGISTRATE.setDataProvider(
                new RegistrateDataProvider(CreateDrillDrain.REGISTRATE, CreateDrillDrain.MOD_ID, event)
            )
        );
    }

}
