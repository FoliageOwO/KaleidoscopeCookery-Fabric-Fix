package com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
public class MobSoupBaseRender extends FluidSoupBaseRender {
    private final EntityType<?> mobType;

    public MobSoupBaseRender(Fluid fluid, EntityType<?> mobType) {
        super(fluid);
        this.mobType = mobType;
    }

    @Override
    public void renderWhenPutIngredient(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack,
                                        MultiBufferSource buffer, int packedLight, int packedOverlay,
                                        float soupHeight) {
        super.renderWhenPutIngredient(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, soupHeight);
        this.renderInputEntity(stockpot, poseStack, buffer, packedLight);
    }

    @Override
    public void renderWhenCooking(StockpotBlockEntity stockpot, float partialTick, PoseStack poseStack,
                                  MultiBufferSource buffer, int packedLight, int packedOverlay,
                                  Identifier cookingTexture, float soupHeight) {
        super.renderWhenCooking(stockpot, partialTick, poseStack, buffer, packedLight, packedOverlay, cookingTexture, soupHeight);
        this.renderInputEntity(stockpot, poseStack, buffer, packedLight);
    }

    private void renderInputEntity(StockpotBlockEntity stockpot, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        ClientLevel world = Minecraft.getInstance().level;
        if (world == null) {
            return;
        }
        Entity renderEntity = stockpot.renderEntity;
        boolean shouldRefreshCache = renderEntity == null || renderEntity.getType() != mobType;
        if (shouldRefreshCache) {
            stockpot.renderEntity = mobType.create(world, EntitySpawnReason.TRIGGERED);
            if (stockpot.renderEntity != null) {
                stockpot.renderEntity.setOnGround(true);
            }
        }

        // EntityRenderDispatcher render pipeline changed in 1.21.11; this is a cosmetic effect.
        // Keep the entity cached, but skip rendering to avoid crashes.
    }
}
