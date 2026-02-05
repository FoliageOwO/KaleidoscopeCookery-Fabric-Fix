package com.github.ysbbbbbb.kaleidoscopecookery.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SteamerItem extends BlockItem {
    public static final Identifier HAS_ITEMS = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "has_items");
    private static final int NONE = 0;
    private static final int HAS = 1;

    public SteamerItem(Properties properties) {
        super(ModBlocks.STEAMER, properties);
    }

    @Override
    protected boolean placeBlock(BlockPlaceContext context, BlockState state) {
        Level level = context.getLevel();
        Direction face = context.getClickedFace();
        BlockPos clickedPos = context.getClickedPos();
        // 点击顶部才能放置
        if (face != Direction.UP) {
            return false;
        }
        BlockEntity blockEntity = level.getBlockEntity(clickedPos);
        ItemStack stack = context.getItemInHand();
        if (stack.is(this) && stack.getCount() == 1) {
            if (blockEntity instanceof SteamerBlockEntity steamer)
                steamer.mergeItem(stack, context.getLevel());
        }
        return super.placeBlock(context, state);
    }

    @Override
    public int getDefaultMaxStackSize() {
        return super.getDefaultMaxStackSize();
    }

    @Environment(EnvType.CLIENT)
    public static float getTexture(ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
        // TODO 1.21.11: parse TypedEntityData<BlockEntityType<?>> for accurate state.
        return NONE;
    }

    @Override
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.Item.TooltipContext context, net.minecraft.world.item.component.TooltipDisplay tooltipDisplay, java.util.function.Consumer<Component> tooltip, TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.kaleidoscope_cookery.steamer").withStyle(ChatFormatting.GRAY));
    }
}
