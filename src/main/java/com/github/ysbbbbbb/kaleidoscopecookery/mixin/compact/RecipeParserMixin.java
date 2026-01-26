package com.github.ysbbbbbb.kaleidoscopecookery.mixin.compact;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

// feat:当不加载乐事兼容时禁止数据文件的无用加载
@Mixin(RecipeManager.class)
public class RecipeParserMixin {

    @Unique
    private static final String FARMERS_DELIGHT = "farmersdelight";
    @Unique
    private static final String RUSTIC_DELIGHT = "rusticdelight";
    @Unique
    private static final String TRIALANDTALES_DELIGHT = "trailandtales_delight";
    @Unique
    private static final String BREWIN_AND_CHEWIN = "brewinandchewin";

   @WrapOperation(method = "apply(Ljava/util/Map;Lnet/minecraft/server/packs/resources/ResourceManager;Lnet/minecraft/util/profiling/ProfilerFiller;)V", at = @At(value = "INVOKE", target = "Ljava/util/Set;iterator()Ljava/util/Iterator;", shift = At.Shift.NONE))
    private static Iterator<Map.Entry<ResourceLocation, JsonElement>> checkCompact(Set<Map.Entry<ResourceLocation, JsonElement>> instance, Operation<Iterator<Map.Entry<ResourceLocation, JsonElement>>> original) {
        if (!FabricLoader.getInstance().isModLoaded(FARMERS_DELIGHT)) {
            instance.removeIf(entry -> entry.getKey().getNamespace().equals(FARMERS_DELIGHT) || entry.getKey().getNamespace().equals(RUSTIC_DELIGHT) || entry.getKey().getNamespace().equals(TRIALANDTALES_DELIGHT) || entry.getKey().getNamespace().equals(BREWIN_AND_CHEWIN));
        } else {
            if (!FabricLoader.getInstance().isModLoaded(RUSTIC_DELIGHT)) {
                instance.removeIf(entry -> entry.getKey().getNamespace().equals(RUSTIC_DELIGHT));
            }
            if (!FabricLoader.getInstance().isModLoaded(TRIALANDTALES_DELIGHT)) {
                instance.removeIf(entry -> entry.getKey().getNamespace().equals(TRIALANDTALES_DELIGHT));
            }
            if (!FabricLoader.getInstance().isModLoaded(BREWIN_AND_CHEWIN)) {
                instance.removeIf(entry -> entry.getKey().getNamespace().equals(BREWIN_AND_CHEWIN));
            }
        }
        return original.call(instance);
   }
}
