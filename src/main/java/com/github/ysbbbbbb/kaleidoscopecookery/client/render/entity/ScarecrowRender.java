package com.github.ysbbbbbb.kaleidoscopecookery.client.render.entity;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.ScarecrowModel;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ScarecrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ScarecrowRender extends LivingEntityRenderer<ScarecrowEntity, ScarecrowRender.ScarecrowRenderState, ScarecrowModel<ScarecrowRender.ScarecrowRenderState>> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/entity/scarecrow.png");

    public ScarecrowRender(EntityRendererProvider.Context context) {
        super(context, new ScarecrowModel<>(context.bakeLayer(ScarecrowModel.LAYER_LOCATION)), 0);
        this.addLayer(new CustomHeadLayer<ScarecrowRenderState, ScarecrowModel<ScarecrowRenderState>>(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    }

    @Override
    protected void setupRotations(ScarecrowRenderState state, PoseStack poseStack, float bodyRot, float partialTicks) {
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - state.yRot));
        float time = state.hitTime + partialTicks;
        if (time < 5.0F) {
            poseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(time / 1.5F * Mth.PI) * 3.0F));
        }
    }

    @Override
    protected boolean shouldShowName(ScarecrowEntity scarecrow, double distance) {
        return distance < 4096 && scarecrow.isCustomNameVisible();
    }

    @Override
    public Identifier getTextureLocation(ScarecrowRenderState state) {
        return TEXTURE;
    }

    @Override
    public ScarecrowRenderState createRenderState() {
        return new ScarecrowRenderState();
    }

    @Override
    public void extractRenderState(ScarecrowEntity entity, ScarecrowRenderState state, float partialTick) {
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTick);
        state.hitTime = entity.hurtTime + partialTick;
    }

    public static class ScarecrowRenderState extends ArmedEntityRenderState {
        public float hitTime;
    }
}
