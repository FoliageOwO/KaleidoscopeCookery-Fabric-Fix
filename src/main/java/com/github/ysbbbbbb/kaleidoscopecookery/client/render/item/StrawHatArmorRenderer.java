package com.github.ysbbbbbb.kaleidoscopecookery.client.render.item;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.StrawHatModel;
import com.github.ysbbbbbb.kaleidoscopecookery.item.StrawHatItem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

@Environment(EnvType.CLIENT)
public class StrawHatArmorRenderer implements ArmorRenderer {
    private static final Identifier NORMAL = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat.png");
    private static final Identifier FLOWER = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/models/armor/straw_hat_flower.png");
    private StrawHatModel cachedModel = null;

    @Override
    public void render(PoseStack matrices, SubmitNodeCollector collector, ItemStack stack, HumanoidRenderState renderState,
                       EquipmentSlot slot, int light, HumanoidModel<HumanoidRenderState> contextModel) {
        if (slot != EquipmentSlot.HEAD) {
            return;
        }
        if (cachedModel == null) {
            cachedModel = new StrawHatModel(Minecraft.getInstance().getEntityModels().bakeLayer(StrawHatModel.LAYER_LOCATION));
        }
        Identifier texture = getArmorTexture(stack);
        matrices.pushPose();
        matrices.scale(1.275f, 1.275f, 1.275f);
        ArmorRenderer.submitTransformCopyingModel(
                contextModel,
                renderState,
                cachedModel,
                renderState,
                true,
                collector,
                matrices,
                RenderTypes.entityCutoutNoCull(texture),
                light,
                0,
                0xFFFFFFFF,
                null
        );
        matrices.popPose();
    }

    public Identifier getArmorTexture(ItemStack stack) {
        if (stack.getItem() instanceof StrawHatItem hatItem && hatItem.hasFlower()) {
            return FLOWER;
        }
        return NORMAL;
    }
}
