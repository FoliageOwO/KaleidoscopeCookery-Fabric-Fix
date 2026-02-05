package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeInput;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface BaseRecipe<C extends RecipeInput> extends Recipe<C> {
    int RECIPES_SIZE = 9;

    ItemStack getResultItem(HolderLookup.Provider registryAccess);

    List<Ingredient> getIngredients();

    @Override
    default @NotNull ItemStack assemble(C container, HolderLookup.Provider registryAccess) {
        return getResultItem(registryAccess).copy();
    }

    @Override
    default boolean isSpecial() {
        return true;
    }

    @Override
    default PlacementInfo placementInfo() {
        return PlacementInfo.create(this.getIngredients());
    }

    @Override
    default RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
