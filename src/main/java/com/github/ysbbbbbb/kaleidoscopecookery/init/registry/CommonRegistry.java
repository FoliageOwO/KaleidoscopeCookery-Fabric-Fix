package com.github.ysbbbbbb.kaleidoscopecookery.init.registry;

import com.github.ysbbbbbb.kaleidoscopecookery.block.dispenser.OilPotDispenseBehavior;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.food.FoodBiteOneByTwoBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.event.*;
import com.github.ysbbbbbb.kaleidoscopecookery.event.effect.FlatulenceServerEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.event.effect.PreservationEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.event.effect.SatiatedShieldEvent;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.item.BowlFoodBlockItem;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class CommonRegistry {
    private static BlockBehaviour.Properties blockProperties(Identifier id) {
        return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, id));
    }

    private static Item.Properties bowlFoodItemProperties(Identifier id, net.minecraft.world.food.FoodProperties food) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, id))
                .stacksTo(16)
                .food(food);
    }

    public static void init() {
        addComposter();
        registerFoodBiteBlocks();
        registerServerEvents();
        addDispenserBehavior();
        modCompat();
    }

    public static void registerServerEvents() {
        SatiatedShieldEvent.register();
        FlatulenceServerEvent.register();
        PreservationEvent.register();
        ArmorEffectHandler.register();
        AddVillageStructuresEvent.register();
        ScarecrowFarmlandTrampleEvent.register();
        EntityJoinWorldEvent.register();
        SickleHarvestNetherWartEvent.register();
        HoeUseEvent.register();
        RightClickEvent.register();
        LeftClickEvent.register();
        ExtraLootTableDrop.register();
    }

    private static void registerFoodBiteBlocks() {
        FoodBiteRegistry.init();

        FoodBiteRegistry.FOOD_DATA_MAP.forEach((resourceLocation, data) -> {
                FoodBiteBlock biteBlock = getFoodBiteBlock(resourceLocation, data);
                Registry.register(BuiltInRegistries.BLOCK, resourceLocation, biteBlock);

                Block block = BuiltInRegistries.BLOCK.getValue(resourceLocation);
                // 选取第一个掉落物作为 usingConvertsTo
                ItemLike first = data.getLootItems().getFirst();
                Registry.register(BuiltInRegistries.ITEM, resourceLocation, new BowlFoodBlockItem(block, bowlFoodItemProperties(resourceLocation, data.itemFood()), first));
        });
    }

    private static @NotNull FoodBiteBlock getFoodBiteBlock(Identifier id, FoodBiteRegistry.FoodData data) {
        FoodBiteBlock biteBlock;
        if (data.blockType() == FoodBiteRegistry.BlockType.ONE_BY_TWO) {
            biteBlock = new FoodBiteOneByTwoBlock(blockProperties(id), data.blockFood(), data.maxBites(), data.animateTick());
        } else {
            biteBlock = new FoodBiteBlock(blockProperties(id), data.blockFood(), data.maxBites(), data.animateTick());
        }

        VoxelShape aabb = data.getAABB();
        if (aabb != null) {
            biteBlock.setAABB(aabb);
        }
        return biteBlock;
    }

    private static void addComposter() {
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.CHILI_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.LETTUCE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.WILD_RICE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RICE_SEED, 0.3F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.TOMATO, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RED_CHILI, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.GREEN_CHILI, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.LETTUCE, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.RICE_PANICLE, 0.65F);
        CompostingChanceRegistry.INSTANCE.add(ModItems.CATERPILLAR, 1.0F);
    }

    private static void modCompat() {
        invokeCompat("com.github.ysbbbbbb.kaleidoscopecookery.compat.farmersdelight.FarmersDelightCompat");
        invokeCompat("com.github.ysbbbbbb.kaleidoscopecookery.compat.harvest.HarvestCompat");
    }

    private static void invokeCompat(String className) {
        try {
            Class<?> clazz = Class.forName(className);
            clazz.getMethod("init").invoke(null);
        } catch (Throwable ignored) {
            // Compat modules are optional during porting.
        }
    }

    private static void addDispenserBehavior() {
        DispenserBlock.registerBehavior(ModItems.OIL_POT, new OilPotDispenseBehavior());
    }
}
