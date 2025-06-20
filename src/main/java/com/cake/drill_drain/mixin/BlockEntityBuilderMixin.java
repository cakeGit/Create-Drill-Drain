package com.cake.drill_drain.mixin;

import com.cake.drill_drain.content.replacements.DrillRendererReplacement;
import com.cake.drill_drain.foundation.DDClientSideRenderers;
import com.tterrag.registrate.AbstractRegistrate;
import com.tterrag.registrate.builders.AbstractBuilder;
import com.tterrag.registrate.builders.BlockEntityBuilder;
import com.tterrag.registrate.builders.BuilderCallback;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonnullType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

import static com.cake.drill_drain.foundation.DDClientSideRenderers.drillRendererSafeIGuess;

@Mixin(remap = false, value = BlockEntityBuilder.class)
public abstract class BlockEntityBuilderMixin<T extends BlockEntity, P> extends AbstractBuilder<BlockEntityType<?>, BlockEntityType<T>, P, BlockEntityBuilder<T, P>> {

    @Shadow
    @Nullable
    private NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer;
    @Shadow
    @Final
    private BlockEntityBuilder.BlockEntityFactory<T> factory;

    public BlockEntityBuilderMixin(AbstractRegistrate<?> owner, P parent, String name, BuilderCallback callback, ResourceKey<Registry<BlockEntityType<?>>> registryKey) {
        super(owner, parent, name, callback, registryKey);
    }

    @Inject(method = "renderer", at = @At("RETURN"))
    protected void renderSafe(NonNullSupplier<NonNullFunction<BlockEntityRendererProvider.Context, BlockEntityRenderer<? super T>>> renderer, CallbackInfoReturnable<BlockEntityBuilder<T, P>> cir) {
        if (this.getOwner().getModid().equals("create") && this.getName().equals("drill")) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                this.renderer = DDClientSideRenderers::drillRendererSafeIGuess);
        }
    }

    @Shadow
    @Override
    protected @NonnullType BlockEntityType<T> createEntry() {
        return null;
    }

}
