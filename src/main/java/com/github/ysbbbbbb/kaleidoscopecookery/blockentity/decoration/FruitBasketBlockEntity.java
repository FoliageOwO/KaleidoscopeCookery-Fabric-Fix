package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.ItemUtils;
import com.github.ysbbbbbb.kaleidoscopecookery.util.neo.ItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class FruitBasketBlockEntity extends BaseBlockEntity {
    public static final String ITEMS = "BasketItems";
    private final SimpleContainer items = new SimpleContainer(8);

    public FruitBasketBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlocks.FRUIT_BASKET_BE, pPos, pBlockState);
    }

    @SuppressWarnings("all")
    public void putOn(ItemStack stack) {
        if (this.level != null && this.level.isClientSide()) {
            return;
        }
        if (!stack.getItem().canFitInsideContainerItems()) {
            return;
        }
        ItemStackHandler handler = new ItemStackHandler(this.items.items);
        ItemStack remaining = ItemUtils.insertItemStacked(handler, stack.copy(), false);
        int inserted = stack.getCount() - remaining.getCount();
        if (inserted <= 0) {
            return;
        }
        stack.shrink(inserted);
        if (this.level != null) {
            this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS);
        }
        this.refresh();
    }

    @SuppressWarnings("all")
    public void takeOut(Player player) {
        if (this.level != null && this.level.isClientSide()) {
            return;
        }
        ItemStackHandler handler = new ItemStackHandler(this.items.items);
        for (int i = 0; i < handler.getSlots(); i++) {
            ItemStack stack = handler.getStackInSlot(i);
            if (stack.isEmpty()) {
                continue;
            }
            ItemStack extracted = handler.extractItem(i, stack.getCount(), false);
            if (extracted.isEmpty()) {
                continue;
            }
            ItemUtils.getItemToLivingEntity(player, extracted);
            if (this.level != null) {
                this.level.playSound(null, this.worldPosition, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.BLOCKS);
            }
            this.refresh();
            return;
        }
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag.child(ITEMS), this.items.items);
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.items.clearContent();
        if (tag.child(ITEMS).isPresent()) {
            ContainerHelper.loadAllItems(tag.childOrEmpty(ITEMS), this.items.items);
        }
    }

    public NonNullList<ItemStack> getItems() {
        return items.items;
    }

    public void setItems(NonNullList<ItemStack> items) {
        this.items.clearContent();
        int maxSize = Math.min(items.size(), this.items.getContainerSize());
        for (int i = 0; i < maxSize; i++) {
            this.items.setItem(i, items.get(i));
        }
        this.refresh();
    }
}
