package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.List;

public class StrawHatItem extends Item {

    private final boolean hasFlower;

    public StrawHatItem(Item.Properties properties, boolean hasFlower) {
        super(properties.stacksTo(1).humanoidArmor(ArmorMaterials.LEATHER, ArmorType.HELMET));
        this.hasFlower = hasFlower;
    }

    public boolean hasFlower() {
        return hasFlower;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tooltipDisplay, java.util.function.Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.straw_hat").withStyle(ChatFormatting.GRAY));
    }
}
