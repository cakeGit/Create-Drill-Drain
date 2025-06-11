package com.cake.drill_drain.content.replacements;

import com.cake.drill_drain.foundation.DDPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.drill.DrillActorVisual;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;

public class DrillActorVisualReplacement extends DrillActorVisual {
    TransformedInstance drillDrainAttachment;

    final Direction facing;

    public DrillActorVisualReplacement(VisualizationContext visualizationContext, VirtualRenderWorld contraption, MovementContext context) {
        super(visualizationContext, contraption, context);

        BlockState state = context.state;
        facing = state.getValue(DrillBlock.FACING);

        updateExistenceOfAttachment(context);
    }

    private void updateExistenceOfAttachment(MovementContext context) {
        if (context.blockEntityData.contains("DrillDrainParent") && !context.data.getBoolean("Disabled"))
            drillDrainAttachment = drillDrainAttachment != null ? drillDrainAttachment : instancerProvider.instancer(InstanceTypes.TRANSFORMED, Models.partial(DDPartialModels.DRILL_DRAIN_ATTACHMENT))
                .createInstance();
        else drillDrainAttachment = null;
    }

    @Override
    public void tick() {
        super.tick();
        updateExistenceOfAttachment(context);
    }

    @Override
    public void beginFrame() {
        super.beginFrame();
        if (drillDrainAttachment != null)
            drillDrainAttachment.setIdentityTransform()
                .translate(context.localPos)
                .center()
                .rotateToFace(facing)
                .rotateXDegrees(-90)
                .uncenter()
                .setChanged();
    }

    @Override
    protected void _delete() {
        super._delete();
        if (drillDrainAttachment == null)
            return;
        drillDrainAttachment.delete();
    }
}
