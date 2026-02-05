package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MillstoneRecipe extends SingleItemRecipe {
    public MillstoneRecipe(Ingredient ingredient, ItemStack result) {
        super("", ingredient, result);
    }

    @Override
    public boolean matches(SingleRecipeInput inv, Level level) {
        return this.input().test(inv.getItem(0));
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    public Ingredient getIngredient() {
        return this.input();
    }

    public ItemStack getResult() {
        return this.result();
    }

    @Override
    public @NotNull RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRecipes.MILLSTONE_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends SingleItemRecipe> getType() {
        return ModRecipes.MILLSTONE_RECIPE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
