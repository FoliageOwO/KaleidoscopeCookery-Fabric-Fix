package com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.SteamerRecipe;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SteamerBuilder implements RecipeBuilder {
    private static final String NAME = "steamer";

    private final RegistryLookup<Item> items;
    private Ingredient ingredient;
    private ItemStack result = ItemStack.EMPTY;
    private int cookTick = 60 * 20;

    private SteamerBuilder(RegistryLookup<Item> items) {
        this.items = items;
    }

    public static SteamerBuilder builder(HolderLookup.Provider registries) {
        return new SteamerBuilder(registries.lookupOrThrow(Registries.ITEM));
    }

    public SteamerBuilder setIngredient(ItemLike itemLike) {
        this.ingredient = Ingredient.of(itemLike);
        return this;
    }

    public SteamerBuilder setIngredient(TagKey<Item> itemLike) {
        this.ingredient = Ingredient.of(items.getOrThrow(itemLike));
        return this;
    }

    public SteamerBuilder setResult(ItemStack stack) {
        this.result = stack;
        return this;
    }

    public SteamerBuilder setResult(ItemLike itemLike) {
        this.result = new ItemStack(itemLike);
        return this;
    }

    public SteamerBuilder setResult(ItemLike itemLike, int count) {
        this.result = new ItemStack(itemLike, count);
        return this;
    }

    public SteamerBuilder setCookTick(int cookTick) {
        this.cookTick = Math.max(cookTick, 1);
        return this;
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.result.getItem();
    }

    @Override
    public void save(RecipeOutput output) {
        String path = RecipeBuilder.getDefaultRecipeId(this.getResult()).getPath();
        Identifier filePath = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + path);
        this.save(output, ResourceKey.create(Registries.RECIPE, filePath));
    }

    @Override
    public void save(RecipeOutput output, String recipeId) {
        Identifier filePath = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, NAME + "/" + recipeId);
        this.save(output, ResourceKey.create(Registries.RECIPE, filePath));
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
        Objects.requireNonNull(this.ingredient, "ingredient");
        SteamerRecipe recipe = new SteamerRecipe(this.ingredient, this.result, this.cookTick);
        recipeOutput.accept(id, recipe, null);
    }
}
