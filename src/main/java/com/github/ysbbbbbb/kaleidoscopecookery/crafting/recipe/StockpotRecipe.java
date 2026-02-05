package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.StockpotInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeMatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.*;
import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_COOKING_BUBBLE_COLOR;
import static com.github.ysbbbbbb.kaleidoscopecookery.crafting.serializer.StockpotRecipeSerializer.DEFAULT_FINISHED_BUBBLE_COLOR;

public record StockpotRecipe(NonNullList<Ingredient> ingredients,
                             Identifier soupBase, ItemStack result, int time,
                             Ingredient carrier, Identifier cookingTexture, Identifier finishedTexture,
                             int cookingBubbleColor, int finishedBubbleColor) implements BaseRecipe<StockpotInput> {
    public StockpotRecipe(List<Ingredient> ingredients, Identifier soupBase, ItemStack result,
                          int time, Ingredient carrier, Identifier cookingTexture, Identifier finishedTexture,
                          int cookingBubbleColor, int finishedBubbleColor) {
        this(copyIngredients(ingredients),
                soupBase, result, time, carrier, cookingTexture, finishedTexture,
                cookingBubbleColor, finishedBubbleColor);
    }

    private static NonNullList<Ingredient> copyIngredients(List<Ingredient> ingredients) {
        NonNullList<Ingredient> copied = NonNullList.create();
        copied.addAll(ingredients);
        return copied;
    }

    public StockpotRecipe(NonNullList<Ingredient> ingredients, ItemStack result, int time, ItemStack container) {
        this(ingredients, DEFAULT_SOUP_BASE, result, time, Ingredient.of(container.getItem()),
                DEFAULT_COOKING_TEXTURE, DEFAULT_FINISHED_TEXTURE,
                DEFAULT_COOKING_BUBBLE_COLOR, DEFAULT_FINISHED_BUBBLE_COLOR);
    }

    @Override
    public boolean matches(StockpotInput container, Level level) {
        List<ItemStack> nonEmptyInputs = container.getInputs().stream()
                .filter(stack -> !stack.isEmpty())
                .toList();
        return container.getSoupBase().equals(this.soupBase)
               && RecipeMatcher.findMatches(nonEmptyInputs, ingredients) != null;
    }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public @NotNull ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return this.result;
    }

    @Override
    public @NotNull RecipeSerializer<? extends net.minecraft.world.item.crafting.Recipe<StockpotInput>> getSerializer() {
        return ModRecipes.STOCKPOT_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends net.minecraft.world.item.crafting.Recipe<StockpotInput>> getType() {
        return ModRecipes.STOCKPOT_RECIPE;
    }
}
