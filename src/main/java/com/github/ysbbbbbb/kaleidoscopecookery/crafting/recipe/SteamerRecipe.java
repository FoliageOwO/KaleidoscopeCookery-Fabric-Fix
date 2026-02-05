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

public class SteamerRecipe extends SingleItemRecipe {
    private final int cookTick;

    public SteamerRecipe(Ingredient ingredient, ItemStack result, int cookTick) {
        super("", ingredient, result);
        this.cookTick = Math.max(cookTick, 1);
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

    public int getCookTick() {
        return cookTick;
    }

    @Override
    public @NotNull RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRecipes.STEAMER_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends SingleItemRecipe> getType() {
        return ModRecipes.STEAMER_RECIPE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
