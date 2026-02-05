package com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.BaseBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class ChairBlockEntity extends BaseBlockEntity {
    private static final String COLOR_TAG = "CarpetColor";
    private DyeColor color = DyeColor.WHITE;

    public ChairBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlocks.CHAIR_BE, pos, blockState);
    }

    @Override
    protected void saveAdditional(ValueOutput tag) {
        super.saveAdditional(tag);
        tag.putInt(COLOR_TAG, this.color.getId());
    }

    @Override
    public void loadAdditional(ValueInput tag) {
        super.loadAdditional(tag);
        this.color = DyeColor.byId(tag.getIntOr(COLOR_TAG, DyeColor.WHITE.getId()));
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }
}
