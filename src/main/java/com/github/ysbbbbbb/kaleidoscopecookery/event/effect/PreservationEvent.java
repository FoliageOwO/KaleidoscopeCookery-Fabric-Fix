package com.github.ysbbbbbb.kaleidoscopecookery.event.effect;

import com.github.ysbbbbbb.kaleidoscopecookery.init.ModEffects;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PreservationEvent {
    public static void register() {
        UseItemCallback.EVENT.register(PreservationEvent::onUseItem);
    }

    private static InteractionResult onUseItem(Player player, Level world, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.has(DataComponents.FOOD) && player.hasEffect(ModEffects.PRESERVATION)) {
            FoodProperties foodProperties = stack.get(DataComponents.FOOD);
            if (foodProperties == null) {
                return InteractionResult.PASS;
            }
            // Food effects are now handled via consumable components; keep this event as a no-op.
        }
        return InteractionResult.PASS;
    }
}
