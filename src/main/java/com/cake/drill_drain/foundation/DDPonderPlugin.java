package com.cake.drill_drain.foundation;

import com.cake.drill_drain.CreateDrillDrain;
import net.createmod.ponder.api.level.PonderLevel;
import net.createmod.ponder.api.registration.*;
import net.minecraft.resources.ResourceLocation;

public class DDPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() {
        return CreateDrillDrain.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        DDPonderScenes.register(helper);
    }
}
