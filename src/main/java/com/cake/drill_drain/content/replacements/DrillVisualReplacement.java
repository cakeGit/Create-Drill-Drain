package com.cake.drill_drain.content.replacements;

import com.cake.drill_drain.accessor.DrillBlockEntityMixinAccess;
import com.cake.drill_drain.accessor.DrillBlockMixinAccess;
import com.cake.drill_drain.foundation.DDPartialModels;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityVisual;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.content.kinetics.drill.DrillActorVisual;
import com.simibubi.create.content.kinetics.drill.DrillBlock;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.model.Model;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.TransformedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.function.Consumer;

public class DrillVisualReplacement<T extends KineticBlockEntity> extends OrientedRotatingVisual<T> {
    TransformedInstance drillDrainAttachment;

    final Direction facing;

    public DrillVisualReplacement(VisualizationContext context, T blockEntity, float partialTick) {
        super(
            context, blockEntity, partialTick, Direction.SOUTH,
            blockEntity.getBlockState().getValue(BlockStateProperties.FACING),
            Models.partial(AllPartialModels.DRILL_HEAD)
        );
        facing = blockEntity.getBlockState().getValue(DrillBlock.FACING);
        updateExistenceOfAttachment();
    }

    @Override
    public void update(float pt) {
        super.update(pt);

        updateExistenceOfAttachment();
    }

    private void updateExistenceOfAttachment() {
        boolean shouldHaveAttachment = ((DrillBlockEntityMixinAccess) blockEntity).create_Drill_Drain$getDrillDrainParent() != null;
        if (shouldHaveAttachment && drillDrainAttachment == null) {
            drillDrainAttachment = instancerProvider().instancer(InstanceTypes.TRANSFORMED, Models.partial(DDPartialModels.DRILL_DRAIN_ATTACHMENT))
                .createInstance()
                .translate(getVisualPosition())
                .center()
                .rotateToFace(facing)
                .rotateXDegrees(-90)
                .uncenter();
            drillDrainAttachment.setChanged();
            relight(drillDrainAttachment);
        } else if (!shouldHaveAttachment && drillDrainAttachment != null) {
            drillDrainAttachment.delete();
            drillDrainAttachment = null;
        }
    }

    @Override
    public void updateLight(float partialTick) {
        super.updateLight(partialTick);
        if (drillDrainAttachment == null) return;
        relight(drillDrainAttachment);
    }

    @Override
    protected void _delete() {
        super._delete();
        if (drillDrainAttachment == null) return;
        drillDrainAttachment.delete();
    }

    @Override
    public void collectCrumblingInstances(Consumer<Instance> consumer) {
        super.collectCrumblingInstances(consumer);
        if (drillDrainAttachment == null) return;
        consumer.accept(drillDrainAttachment);
    }

}
