package com.github.ysbbbbbb.kaleidoscopecookery.datagen.recipe;

import com.github.ysbbbbbb.kaleidoscopecookery.datagen.builder.PotRecipeBuilder;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import java.util.Optional;

public class SimplePotRecipeProvider extends ModRecipeProvider {
    public SimplePotRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    public void buildRecipes() {
        RecipeOutput consumer = this.output;
        PotRecipeBuilder.builder(this.registries).addInput(Items.POTATO).setResult(Items.BAKED_POTATO).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.KELP).setResult(Items.DRIED_KELP).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.CHORUS_FRUIT).setResult(Items.POPPED_CHORUS_FRUIT).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.BEEF).setResult(Items.COOKED_BEEF).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.CHICKEN).setResult(Items.COOKED_CHICKEN).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.COD).setResult(Items.COOKED_COD).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.SALMON).setResult(Items.COOKED_SALMON).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.MUTTON).setResult(Items.COOKED_MUTTON).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.PORKCHOP).setResult(Items.COOKED_PORKCHOP).save(consumer);
        PotRecipeBuilder.builder(this.registries).addInput(Items.RABBIT).setResult(Items.COOKED_RABBIT).save(consumer);

        addSingleItemRecipe(Items.EGG, ModItems.FRIED_EGG, "egg", consumer);
        addSingleItemRecipe(ModItems.STUFFED_DOUGH_FOOD, ModItems.MEAT_PIE, "stuffed_dough_food", consumer);
    }

    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Optional.empty(), consumer);
    }

    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, RecipeOutput consumer) {
        this.addSingleItemRecipe(inputItem, outputItem, idInput, Optional.empty(), consumer);
    }

    @SuppressWarnings("all")
    public void addSingleItemRecipe(TagKey<Item> inputItem, Item outputItem, String idInput, Optional<net.minecraft.world.item.crafting.Ingredient> carrier, RecipeOutput consumer) {
        for (int i = 1; i <= 9; i++) {
            TagKey<Item>[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder builder = PotRecipeBuilder.builder(this.registries).addInput(inputs).setResult(output);
            carrier.ifPresent(builder::setCarrier);
            builder.save(consumer, id);
        }
    }

    @SuppressWarnings("all")
    public void addSingleItemRecipe(ItemLike inputItem, Item outputItem, String idInput, Optional<net.minecraft.world.item.crafting.Ingredient> carrier, RecipeOutput consumer) {
        for (int i = 1; i <= 9; i++) {
            ItemLike[] inputs = this.getItemsWithCount(inputItem, i);
            ItemStack output = new ItemStack(outputItem, i);
            String idOutput = this.getRecipeIdWithCount(outputItem, i);
            String id = String.format("%s_to_%s", idInput, idOutput);
            PotRecipeBuilder builder = PotRecipeBuilder.builder(this.registries).addInput(inputs).setResult(output);
            carrier.ifPresent(builder::setCarrier);
            builder.save(consumer, id);
        }
    }
}
