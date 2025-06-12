package com.cake.drill_drain.mixin;

import com.cake.drill_drain.content.replacements.DrillRendererReplacement;
import com.cake.drill_drain.content.replacements.DrillVisualReplacement;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.content.kinetics.drill.DrillBlockEntity;
import com.simibubi.create.foundation.data.CreateBlockEntityBuilder;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import dev.engine_room.flywheel.api.visual.BlockEntityVisual;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.visualization.SimpleBlockEntityVisualizer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.common.util.NonNullPredicate;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.function.Predicate;

@Mixin(remap = false, value = CreateBlockEntityBuilder.class)
public abstract class CreateBlockEntityBuilderMixin<T extends BlockEntity, P> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<T>, P, BlockEntityBuilder<T, P>> {

    @Shadow public abstract CreateBlockEntityBuilder<T, P> visual(NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory, NonNullPredicate<T> renderNormally);

    public CreateBlockEntityBuilderMixin(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<Registry<BlockEntityType<?>>> registryKey) {
        super(owner, parent, name, callback, registryKey);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "visual(Lcom/tterrag/registrate/util/nullness/NonNullSupplier;Z)Lcom/simibubi/create/foundation/data/CreateBlockEntityBuilder;", at = @At("RETURN"))
    protected void renderSafe(NonNullSupplier<SimpleBlockEntityVisualizer.Factory<T>> visualFactory, boolean renderNormally, CallbackInfoReturnable<CreateBlockEntityBuilder<T, P>> cir) {
        if (this.getOwner().getModid().equals("create") && this.getName().equals("drill")) {
            this.visual(() ->
                    (context, blockEntity, partialTick) ->
                        (BlockEntityVisual<? super T>) new DrillVisualReplacement<>(context, (DrillBlockEntity) blockEntity, partialTick),
                be -> renderNormally);
        }
    }

}
