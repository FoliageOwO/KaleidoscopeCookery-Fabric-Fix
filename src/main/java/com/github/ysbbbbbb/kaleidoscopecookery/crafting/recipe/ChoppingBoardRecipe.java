package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ChoppingBoardRecipe extends SingleItemRecipe {
    private final int cutCount;
    private final Identifier modelId;

    public ChoppingBoardRecipe(Ingredient ingredient, ItemStack result, int cutCount, Identifier modelId) {
        super("", ingredient, result);
        this.cutCount = Math.max(cutCount, 1);
        this.modelId = modelId;
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

    public int getCutCount() {
        return cutCount;
    }

    public Identifier getModelId() {
        return modelId;
    }

    @Override
    public @NotNull RecipeSerializer<? extends SingleItemRecipe> getSerializer() {
        return ModRecipes.CHOPPING_BOARD_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends SingleItemRecipe> getType() {
        return ModRecipes.CHOPPING_BOARD_RECIPE;
    }

    @Override
    public @NotNull RecipeBookCategory recipeBookCategory() {
        return new RecipeBookCategory();
    }
}
