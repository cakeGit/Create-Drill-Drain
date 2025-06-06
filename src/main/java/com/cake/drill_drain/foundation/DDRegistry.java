package com.cake.drill_drain.foundation;

import com.cake.drill_drain.CreateDrillDrain;
import com.cake.drill_drain.content.DrillDrainBlock;
import com.cake.drill_drain.content.DrillDrainBlockEntity;
import com.cake.drill_drain.content.DrillDrainBlockEntityRenderer;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.fluids.pump.PumpRenderer;
import com.simibubi.create.content.kinetics.base.SingleAxisRotatingVisual;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.SharedProperties;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import static com.cake.drill_drain.CreateDrillDrain.REGISTRATE;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;

public class DDRegistry {

    public static final BlockEntry<DrillDrainBlock> DRILL_DRAIN = REGISTRATE
        .block("drill_drain", DrillDrainBlock::new)
        .initialProperties(SharedProperties::stone)
        .properties(p -> p.sound(SoundType.WOOD)
            .mapColor(MapColor.DIRT))
        .properties(BlockBehaviour.Properties::noOcclusion)
        .blockstate(BlockStateGen.directionalBlockProvider(false))
        .transform(axeOrPickaxe())
        .simpleItem()
        .register();

    public static final BlockEntityEntry<DrillDrainBlockEntity> DRILL_DRAIN_BLOCK_ENTITY = REGISTRATE
        .blockEntity("drill_drain", DrillDrainBlockEntity::new)
        .visual(() -> SingleAxisRotatingVisual.ofZ(AllPartialModels.MECHANICAL_PUMP_COG))
        .validBlocks(DRILL_DRAIN)
        .renderer(() -> DrillDrainBlockEntityRenderer::new)
        .register();

    public static void init() {
        CreateDrillDrain.LOGGER.info("Registering all " + CreateDrillDrain.NAME + " entries");
    }
    
}
