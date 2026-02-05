package com.github.ysbbbbbb.kaleidoscopecookery.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class WithTooltipsBlockItem extends BlockItem {
    private final String key;

    public WithTooltipsBlockItem(Block block, Properties properties, String name) {
        super(block, properties);
        this.key = "tooltip.kaleidoscope_cookery." + name;
    }

    public WithTooltipsBlockItem(Block block, String name) {
        this(block, new Properties(), name);
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tooltipDisplay, java.util.function.Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable(key).withStyle(ChatFormatting.GRAY));
    }
}
