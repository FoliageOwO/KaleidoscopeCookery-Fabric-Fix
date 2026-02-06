package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.SteamerBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.tag.TagCommon;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;

public class SteamerRecipeProvider extends ModRecipeProvider {
    public SteamerRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        RecipeOutput consumer = this.output;
        SteamerBuilder.builder(this.registries)
                .setIngredient(ModItems.STUFFED_DOUGH_FOOD)
                .setResult(ModItems.BAOZI)
                .save(consumer);

        SteamerBuilder.builder(this.registries)
                .setIngredient(TagCommon.DOUGH)
                .setResult(ModItems.MANTOU)
                .save(consumer);
    }
}
