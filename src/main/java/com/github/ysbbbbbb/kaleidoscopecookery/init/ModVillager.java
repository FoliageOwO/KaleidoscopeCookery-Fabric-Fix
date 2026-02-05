package com.github.ysbbbbbb.kaleidoscopecookery.init;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.npc.villager.VillagerProfession;

@SuppressWarnings("all")
public class ModVillager {
    public static final ResourceKey<VillagerProfession> CHEF_KEY = ResourceKey.create(
            Registries.VILLAGER_PROFESSION,
            Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "chef")
    );

    public static final VillagerProfession CHEF = new VillagerProfession(Component.translatable("entity.minecraft.villager.chef"),
            poi -> poi.value() == ModPoi.STOVE,
            poi -> poi.value() == ModPoi.STOVE,
            com.google.common.collect.ImmutableSet.of(), com.google.common.collect.ImmutableSet.of(), SoundEvents.VILLAGER_WORK_BUTCHER);

    public static void registerVillagerProfessions() {
        Registry.register(BuiltInRegistries.VILLAGER_PROFESSION, CHEF_KEY, CHEF);
    }
}
