package com.cake.drill_drain.foundation;

import static com.cake.drill_drain.CreateDrillDrain.REGISTRATE;

public class DDLangEntries {

    public static void addToLang() {
        REGISTRATE
            .addRawLang("block.drill_drain.drill_drain.tooltip.summary", "Place _behind an array of drills_ to be able to _remove fluids_ as the drill moves, _requires space in the contraption_ to store the fluid (configurable)");
    }

}
