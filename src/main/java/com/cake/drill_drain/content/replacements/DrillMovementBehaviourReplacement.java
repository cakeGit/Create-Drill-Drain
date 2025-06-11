package com.cake.drill_drain.content.replacements;

import com.cake.drill_drain.Config;
import com.cake.drill_drain.foundation.DDRegistry;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.render.ActorVisual;
import com.simibubi.create.content.kinetics.drill.DrillMovementBehaviour;
import com.simibubi.create.foundation.virtualWorld.VirtualRenderWorld;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class DrillMovementBehaviourReplacement extends DrillMovementBehaviour {

    @Override
    public void startMoving(MovementContext context) {
        super.startMoving(context);
        if (context.blockEntityData.contains("DrillDrainParent")) {
            StructureTemplate.StructureBlockInfo parentStructure =
                context.contraption.getBlocks().get(context.localPos.offset(BlockPos.of(context.blockEntityData.getLong("DrillDrainParent"))));
            if (parentStructure == null || !parentStructure.state().is(DDRegistry.DRILL_DRAIN)) {
                context.blockEntityData.remove("DrillDrainParent");
                context.data.putBoolean("Disabled", true);
            }
        }
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        super.visitNewPosition(context, pos);
        if (context.blockEntityData.contains("DrillDrainParent") && !context.world.isClientSide) {
            FluidStack currentState = getFluidFromFluidBlock(context, pos, true);

            if (currentState.isEmpty()) {
                return;
            }

            if (Config.fluidPickupModifier != 0) {
                int simulatedFill = context.contraption.getStorage().getFluids().fill(currentState, IFluidHandler.FluidAction.SIMULATE);
                if (simulatedFill < currentState.getAmount()) {
                    return;
                }

                context.contraption.getStorage().getFluids().fill(currentState, IFluidHandler.FluidAction.EXECUTE);
            }

            getFluidFromFluidBlock(context, pos, false);
            context.world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 0.1f, 1.0F);
        }
    }

    private FluidStack getFluidFromFluidBlock(MovementContext context, BlockPos pos, boolean simulate) {
        FluidStack empty = FluidStack.EMPTY;
        if (context.world == null)
            return empty;
        if (!context.world.isLoaded(pos))
            return empty;

        BlockState state = context.world.getBlockState(pos);
        FluidState fluidState = state.getFluidState();
        boolean isWaterLoggableBlock = state.hasProperty(WATERLOGGED);

        if (!isWaterLoggableBlock && !state.canBeReplaced())
            return empty;
        if (fluidState.isEmpty() || !fluidState.isSource())
            return empty;

        FluidStack stack = new FluidStack(fluidState.getType(), (int) (1000 * Config.fluidPickupModifier));

        if (simulate)
            return stack;

        if (isWaterLoggableBlock) {
            context.world.setBlock(pos, state.setValue(WATERLOGGED, false), 3);
            context.world.scheduleTick(pos, Fluids.WATER, 1);
        } else {
            context.world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            context.world.scheduleTick(pos, fluidState.getType(), 1);
        }

        return stack;
    }

    @Nullable
    @Override
    public ActorVisual createVisual(VisualizationContext visualizationContext, VirtualRenderWorld simulationWorld, MovementContext movementContext) {
        return new DrillActorVisualReplacement(visualizationContext, simulationWorld, movementContext);
    }

}
