package com.cake.drill_drain;

import com.cake.drill_drain.foundation.DDPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateDrillDrainClient {

    public static void clientData(final FMLClientSetupEvent event) {
        PonderIndex.addPlugin(new DDPonderPlugin());
    }

}
