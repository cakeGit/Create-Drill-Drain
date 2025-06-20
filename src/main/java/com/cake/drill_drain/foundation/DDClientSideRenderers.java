package com.cake.drill_drain.foundation;

import com.cake.drill_drain.content.replacements.DrillRendererReplacement;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class DDClientSideRenderers {

    public static <T extends BlockEntity> @NotNull NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>> drillRendererSafeIGuess() {
        return (ctx) -> (BlockEntityRenderer<T>) new DrillRendererReplacement(ctx);
    }

}
