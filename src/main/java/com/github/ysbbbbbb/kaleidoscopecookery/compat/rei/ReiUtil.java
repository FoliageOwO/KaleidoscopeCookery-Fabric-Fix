package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei;

import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.StreamSupport;

public class ReiUtil {
    public static EntryIngredient ofIngredient(Ingredient ingredient) {
        return EntryIngredient.of(ingredient.items().map(holder -> EntryStacks.of(holder.value())).toList());
    }

    public static EntryIngredient ofTag(TagKey<Item> tag) {
        var holders = BuiltInRegistries.ITEM.getTagOrEmpty(tag);
        return EntryIngredient.of(StreamSupport.stream(holders.spliterator(), false)
                .map(holder -> EntryStacks.of(holder.value()))
                .toList());
    }

    public static EntryIngredient ofItem(Item item) {
        return EntryIngredient.of(EntryStacks.of(item));
    }

    public static EntryIngredient ofItemStack(ItemStack itemStack) {
        return EntryIngredient.of(EntryStacks.of(itemStack));
    }

    public static List<EntryIngredient> ofItems(Item... items) {
        return Arrays.stream(items).map(ReiUtil::ofItem).toList();
    }

    public static List<EntryIngredient> ofItemStacks(ItemStack... items) {
        return Arrays.stream(items).map(ReiUtil::ofItemStack).toList();
    }

    public static List<EntryIngredient> ofItems(List<Item> items) {
        return items.stream().map(ReiUtil::ofItem).toList();
    }

    public static List<EntryIngredient> ofIngredients(Ingredient... ingredients) {
        return Arrays.stream(ingredients).map(ReiUtil::ofIngredient).toList();
    }

    public static List<EntryIngredient> ofIngredients(List<Ingredient> ingredients) {
        return ingredients.stream().map(ReiUtil::ofIngredient).toList();
    }
}
