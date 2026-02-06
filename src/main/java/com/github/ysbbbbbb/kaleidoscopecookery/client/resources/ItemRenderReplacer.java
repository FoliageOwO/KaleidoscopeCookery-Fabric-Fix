package com.github.ysbbbbbb.kaleidoscopecookery.client.resources;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;

public record ItemRenderReplacer(Map<Identifier, Identifier> pot,
                                 Map<Identifier, Identifier> stockpotCooking,
                                 Map<Identifier, Identifier> stockpotFinished,
                                 Map<Identifier, Identifier> millstone,
                                 Map<Identifier, Identifier> steamer) {
    public static final Codec<Identifier> RL_CODEC = Codec.STRING.comapFlatMap(ItemRenderReplacer::toLocation, ItemRenderReplacer::fromLocation).stable();
    public static final Codec<ItemRenderReplacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("pot").forGetter(ItemRenderReplacer::pot),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("stockpot_cooking").forGetter(ItemRenderReplacer::stockpotCooking),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("stockpot_finished").forGetter(ItemRenderReplacer::stockpotFinished),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("millstone").forGetter(ItemRenderReplacer::millstone),
            Codec.unboundedMap(Identifier.CODEC, RL_CODEC).fieldOf("steamer").forGetter(ItemRenderReplacer::steamer)
    ).apply(instance, ItemRenderReplacer::new));

    private static Function<Identifier, ItemModel> CACHE = createNewCache();

    public ItemRenderReplacer() {
        this(Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap(), Maps.newHashMap());
    }

    private static Function<Identifier, ItemModel> createNewCache() {
        return Util.memoize(id -> {
            ModelManager modelManager = Minecraft.getInstance().getModelManager();
            return modelManager.getItemModel(id);
        });
    }

    public static void resetCache() {
        CACHE = createNewCache();
    }

    public static ItemModel getModel(@Nullable Level level, ItemStack stack,
                                      Map<Identifier, Identifier> models) {
        Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
        @Nullable Identifier location = models.get(key);
        if (location == null) {
            ModelManager modelManager = Minecraft.getInstance().getModelManager();
            return modelManager.getItemModel(key);
        }
        return CACHE.apply(location);
    }

    private static DataResult<Identifier> toLocation(String input) {
        String[] split = input.split("#");
        if (split.length > 1) {
            return DataResult.success(Identifier.parse(split[0]));
        }
        return DataResult.success(Identifier.parse(input));
    }

    private static String fromLocation(Identifier input) {
        return input.toString();
    }

    public void addAll(ItemRenderReplacer other) {
        this.pot.putAll(other.pot);
        this.stockpotCooking.putAll(other.stockpotCooking);
        this.stockpotFinished.putAll(other.stockpotFinished);
        this.millstone.putAll(other.millstone);
        this.steamer.putAll(other.steamer);
    }

    public void clear() {
        this.pot.clear();
        this.stockpotCooking.clear();
        this.stockpotFinished.clear();
        this.millstone.clear();
        this.steamer.clear();
    }
}
