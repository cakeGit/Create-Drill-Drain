package com.cake.drill_drain.foundation;

import com.cake.drill_drain.content.DrillDrainScenes;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class DDPonderScenes {

    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);
        HELPER.forComponents(DDRegistry.DRILL_DRAIN)
            .addStoryBoard("drill_drain", DrillDrainScenes::drillDrainScene);
    }

}
