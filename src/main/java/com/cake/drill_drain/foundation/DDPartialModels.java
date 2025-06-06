package com.cake.drill_drain.foundation;

import com.cake.drill_drain.CreateDrillDrain;
import com.simibubi.create.Create;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;

public class DDPartialModels {
    public static final PartialModel
        DRILL_DRAIN_ATTACHMENT = block("drill_drain_overlay");

    private static PartialModel block(String path) {
        return PartialModel.of(CreateDrillDrain.asResource("block/" + path));
    }

    public static void init() {
    }

}
