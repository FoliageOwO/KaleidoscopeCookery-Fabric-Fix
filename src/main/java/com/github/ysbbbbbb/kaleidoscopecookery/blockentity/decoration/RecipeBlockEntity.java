package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import com.github.ysbbbbbb.kaleidoscopecookery.util.neo.ItemStackHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class RecipeBlockEntity extends BaseBlockEntity {
    private static final String SHOW_ITEMS = "ShowItems";
    private final ItemStackHandler items = new ItemStackHandler(1);

    public RecipeBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.RECIPE_BLOCK_BE, pos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.store(SHOW_ITEMS, CompoundTag.CODEC, this.items.serializeNBT(this.getLevel().registryAccess()));
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        tag.read(SHOW_ITEMS, CompoundTag.CODEC).ifPresent(compound -> this.items.deserializeNBT(tag.lookup(), compound));
    }

    public ItemStackHandler getItems() {
        return items;
    }
}
