package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelIdentifier;
import net.minecraft.resources.Identifier;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {

    @Shadow
    protected abstract void loadSpecialItemModelAndDependencies(ModelIdentifier modelLocation);

    @Unique
    private static final ModelIdentifier HONEY = ModelIdentifier.inventory(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "honey"));
    @Unique
    private static final ModelIdentifier EGG = ModelIdentifier.inventory(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "egg"));
    @Unique
    private static final ModelIdentifier RAW_DOUGH_IN_MILLSTONE = ModelIdentifier.inventory(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "raw_dough_in_millstone"));
    @Unique
    private static final ModelIdentifier OIL_IN_MILLSTONE = ModelIdentifier.inventory(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "oil_in_millstone"));
    @Unique
    private static final ModelIdentifier COLD_CUT_HAM_SLICES_GUI = ModelIdentifier.inventory(Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "cold_cut_ham_slices_in_gui"));


    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/ModelBakery;loadSpecialItemModelAndDependencies(Lnet.minecraft.client.renderer.block.model.ModelIdentifier;)V", shift = At.Shift.AFTER, ordinal = 1))
    private void loadSpecialItemModelAndDependencies(BlockColors blockColors, ProfilerFiller profilerFiller, Map<Identifier, BlockModel> modelResources, Map<Identifier, List<BlockStateModelLoader.LoadedJson>> blockStateResources, CallbackInfo ci) {
        this.loadSpecialItemModelAndDependencies(HONEY);
        this.loadSpecialItemModelAndDependencies(EGG);
        this.loadSpecialItemModelAndDependencies(RAW_DOUGH_IN_MILLSTONE);
        this.loadSpecialItemModelAndDependencies(OIL_IN_MILLSTONE);
        this.loadSpecialItemModelAndDependencies(COLD_CUT_HAM_SLICES_GUI);
    }
}
