package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.renderstates.MillstoneBlockEntityRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class MillstoneBlockEntityRender implements BlockEntityRenderer<MillstoneBlockEntity, MillstoneBlockEntityRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/block/millstone.png");

    private final MillstoneModel bodyModel;
    private final ItemModelResolver itemModelResolver;

    public MillstoneBlockEntityRender(BlockEntityRendererProvider.Context context) {
        itemModelResolver = context.itemModelResolver();
        this.bodyModel = new MillstoneModel(context.bakeLayer(MillstoneModel.LAYER_LOCATION));
    }

    @Override
    public void extractRenderState(MillstoneBlockEntity blockEntity, MillstoneBlockEntityRenderState state, float partialTick, @NotNull Vec3 cameraPos, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, breakProgress);
        Direction facing = state.blockState.getValue(MillstoneBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        int seed = (int) blockEntity.getBlockPos().asLong();

        state.levelAccessor = blockEntity.getLevel();
        state.hasEntity = blockEntity.hasEntity();
        state.cacheRot = blockEntity.getCacheRot();
        state.rot = blockEntity.getLevel() != null ? facingDeg + blockEntity.getRotation(blockEntity.getLevel(), partialTick) : 0f;
        state.input = blockEntity.getInput();
        state.liftAngle = blockEntity.getLiftAngle();

        int maxCount = Math.min(state.input.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);
        state.itemsToRender.clear();
        for (int i = 0; i < maxCount; i++) {
            ItemStackRenderState itemState = new ItemStackRenderState();
            this.itemModelResolver.updateForTopItem(itemState, state.input, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, seed + i);
            state.itemsToRender.add(itemState);
        }
    }

    @Override
    public MillstoneBlockEntityRenderState createRenderState() {
        return new MillstoneBlockEntityRenderState();
    }

    @Override
    public void submit(MillstoneBlockEntityRenderState state, @NotNull PoseStack poseStack, @NotNull SubmitNodeCollector collector, @NotNull CameraRenderState camera) {
        if (state.levelAccessor == null) {
            return;
        }
        Direction facing = state.blockState.getValue(MillstoneBlock.FACING);
        int facingDeg = facing.get2DDataValue() * 90;
        MillstoneModel.State modelState = new MillstoneModel.State(
                state.levelAccessor,
                state.hasEntity,
                state.cacheRot,
                state.rot,
                state.input,
                state.liftAngle
        );
        if (state.hasEntity) {
            this.bodyModel.setupAnim(modelState);
        } else {
            float rot = facingDeg + state.cacheRot;
            this.bodyModel.getWheel().yRot = -rot * Mth.DEG_TO_RAD;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - facingDeg));
        RenderType renderType = RenderTypes.entityCutoutNoCull(TEXTURE);
        collector.submitModel(
                bodyModel,
                modelState,
                poseStack,
                renderType,
                state.lightCoords,
                OverlayTexture.NO_OVERLAY,
                0,
                state.breakProgress
        );
        poseStack.popPose();
        this.bodyModel.getWheel().yRot = 0;
        this.bodyModel.getRoll().zRot = 0;
        this.bodyModel.getRotStick().xRot = 0;

        if (!state.input.isEmpty()) {
            ItemStack renderItem = state.input;
            RandomSource source = RandomSource.create(state.hashCode());
            int maxCount = Math.min(renderItem.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);
            for (int i = 0; i < maxCount; i++) {
                ItemStackRenderState itemState = state.itemsToRender.get(i);
                poseStack.pushPose();
                poseStack.translate(0, 0.875, 0);
                poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45 + source.nextInt(15)), 0.5f, 0, 0.5f);
                poseStack.mulPose(Axis.YP.rotationDegrees(source.nextInt(20)));
                poseStack.mulPose(Axis.XN.rotationDegrees(80 + source.nextInt(20)));
                poseStack.scale(0.65F, 0.65F, 0.65F);
                itemState.submit(
                        poseStack,
                        collector,
                        state.lightCoords,
                        OverlayTexture.NO_OVERLAY,
                        0
                );
                poseStack.popPose();
            }
        }
    }

    @Override
    public boolean shouldRenderOffScreen() {
        return true;
    }

}
