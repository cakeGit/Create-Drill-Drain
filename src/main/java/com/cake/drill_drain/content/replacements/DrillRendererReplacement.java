package com.cake.drill_drain.content.replacements;

import com.cake.drill_drain.accessor.DrillBlockEntityMixinAccess;
import com.cake.drill_drain.foundation.DDPartialModels;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.content.kinetics.drill.DrillRenderer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class DrillRendererReplacement extends DrillRenderer {
    public DrillRendererReplacement(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected void renderSafe(DrillBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        if (((DrillBlockEntityMixinAccess) be).create_Drill_Drain$getDrillDrainParent() != null) {
            CachedBuffers.partialFacing(DDPartialModels.DRILL_DRAIN_ATTACHMENT, be.getBlockState())
                .center()
                .rotateToFace(be.getBlockState().getValue(DrillBlock.FACING))
                .rotateXDegrees(-90)
                .uncenter()
                .light(light)
                .renderInto(ms, buffer.getBuffer(RenderType.cutout()));
        }

        super.renderSafe(be, partialTicks, ms, buffer, light, overlay);
    }
}
