package com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.crafting.container.SimpleInput;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.github.ysbbbbbb.kaleidoscopecookery.util.RecipeMatcher;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public record PotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                        NonNullList<Ingredient> ingredients, ItemStack result) implements BaseRecipe<SimpleInput> {
    public PotRecipe(int time, int stirFryCount, Optional<Ingredient> carrier,
                     List<Ingredient> ingredients, ItemStack result) {
        this(time, stirFryCount, carrier, copyIngredients(ingredients), result);
    }

    private static NonNullList<Ingredient> copyIngredients(List<Ingredient> ingredients) {
        NonNullList<Ingredient> copied = NonNullList.create();
        copied.addAll(ingredients);
        return copied;
    }

    @Override
    public boolean matches(SimpleInput simpleInput, Level level) {
        List<net.minecraft.world.item.ItemStack> nonEmptyInputs = simpleInput.getInputs().stream()
                .filter(stack -> !stack.isEmpty())
                .toList();
        return RecipeMatcher.findMatches(nonEmptyInputs, ingredients) != null;
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
    public @NotNull RecipeSerializer<? extends Recipe<SimpleInput>> getSerializer() {
        return ModRecipes.POT_SERIALIZER;
    }

    @Override
    public @NotNull RecipeType<? extends Recipe<SimpleInput>> getType() {
        return ModRecipes.POT_RECIPE;
    }
}
