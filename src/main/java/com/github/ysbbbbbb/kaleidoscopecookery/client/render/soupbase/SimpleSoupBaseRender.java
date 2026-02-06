package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class SimpleSoupBaseRender implements ISoupBaseRender {
    private final Identifier soupBaseTexture;

    public SimpleSoupBaseRender(Identifier soupBaseTexture) {
        this.soupBaseTexture = soupBaseTexture;
    }

    @Override
    public void renderWhenPutIngredient(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack,
                                        MultiBufferSource buffer, int packedLight, int packedOverlay,
                                        float soupHeight) {
        ISoupBaseRender.renderSurface(this.getSprite(), 0xFFFFFFFF, poseStack, buffer, packedLight, soupHeight);
    }

    @Override
    public void renderWhenCooking(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack,
                                  MultiBufferSource buffer, int packedLight, int packedOverlay,
                                  Identifier cookingTexture, float soupHeight) {
        var atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(ModelManager.BLOCK_OR_ITEM);
        TextureAtlasSprite sprite = atlas.getSprite(cookingTexture);
        ISoupBaseRender.renderSurface(sprite, 0xFFFFFFFF, poseStack, buffer, packedLight, soupHeight);
    }

    @Override
    public void renderWhenFinished(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack,
                                   MultiBufferSource buffer, int packedLight, int packedOverlay,
                                   Identifier finishedTexture, float soupHeight) {
        var atlas = Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(ModelManager.BLOCK_OR_ITEM);
        TextureAtlasSprite sprite = atlas.getSprite(finishedTexture);
        ISoupBaseRender.renderSurface(sprite, 0xFFFFFFFF, poseStack, buffer, packedLight, soupHeight);
    }

    private TextureAtlasSprite getSprite() {
        return Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(ModelManager.BLOCK_OR_ITEM).getSprite(this.soupBaseTexture);
    }
}
