package com.github.ysbbbbbb.kaleidoscopecookery.datagen;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe.*;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeGenerator extends FabricRecipeProvider {
    public ModRecipeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        return new RootRecipeProvider(registries, output);
    }

    @Override
    public String getName() {
        return "Kaleidoscope Cookery Recipes";
    }

    private static class RootRecipeProvider extends ModRecipeProvider {
        private final List<ModRecipeProvider> providers = Lists.newArrayList();

        RootRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            super(registries, output);
            providers.add(new ChoppingBoardRecipeProvider(registries, output));
            providers.add(new DecorationRecipeProvider(registries, output));
            providers.add(new FoodBiteRecipeProvider(registries, output));
            providers.add(new PotRecipeProvider(registries, output));
            providers.add(new ShapedRecipeProvider(registries, output));
            providers.add(new ShapelessRecipeProvider(registries, output));
            providers.add(new SimpleCookingRecipeProvider(registries, output));
            providers.add(new SimplePotRecipeProvider(registries, output));
            providers.add(new StockpotRecipeProvider(registries, output));
            providers.add(new SteamerRecipeProvider(registries, output));
            providers.add(new MillstoneRecipeProvider(registries, output));
        }

        @Override
        public void buildRecipes() {
            netheriteSmithing(ModItems.DIAMOND_KITCHEN_KNIFE, RecipeCategory.TOOLS, ModItems.NETHERITE_KITCHEN_KNIFE);
            for (ModRecipeProvider provider : providers) {
                provider.buildRecipes();
            }
        }
    }
}
