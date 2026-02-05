package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class TableBlockEntity extends BaseBlockEntity {
    private static final String COLOR_TAG = "CarpetColor";
    private static final String SHOW_ITEMS = "ShowItems";

    private DyeColor color = DyeColor.WHITE;
    private final NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY);

    public TableBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.TABLE_BE, pos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putInt(COLOR_TAG, this.color.getId());
        ContainerHelper.saveAllItems(tag.child(SHOW_ITEMS), items);
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.color = DyeColor.byId(tag.getIntOr(COLOR_TAG, DyeColor.WHITE.getId()));
        if (tag.child(SHOW_ITEMS).isPresent()) {
            ContainerHelper.loadAllItems(tag.childOrEmpty(SHOW_ITEMS), items);
        }
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }
}
