package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.MillstoneBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.MillstoneBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.model.MillstoneModel;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.RenderItemUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacerReloadListener;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class MillstoneBlockEntityRender implements BlockEntityRenderer<MillstoneBlockEntity, MillstoneBlockEntityRender.MillstoneRenderState> {
    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/block/millstone.png");

    private final MillstoneModel bodyModel;
    private final ItemModelResolver itemModelResolver;

    public MillstoneBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.bodyModel = new MillstoneModel(context.bakeLayer(MillstoneModel.LAYER_LOCATION));
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public MillstoneRenderState createRenderState() {
        return new MillstoneRenderState();
    }

    @Override
    public void extractRenderState(MillstoneBlockEntity millstone, MillstoneRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(millstone, state, breakProgress);
        Level level = millstone.getLevel();
        if (level == null) {
            state.clearItems();
            return;
        }

        Direction facing = millstone.getBlockState().getValue(MillstoneBlock.FACING);
        state.facingDeg = facing.get2DDataValue() * 90;

        if (millstone.hasEntity()) {
            float rot = state.facingDeg + millstone.getRotation(level, partialTick);
            state.wheelRot = -rot * Mth.DEG_TO_RAD;
            state.rollRot = rot * Mth.DEG_TO_RAD;
            state.rotStick = -millstone.getLiftAngle() * Mth.DEG_TO_RAD;
        } else {
            float rot = state.facingDeg + millstone.getCacheRot();
            state.wheelRot = -rot * Mth.DEG_TO_RAD;
            state.rollRot = 0;
            state.rotStick = 0;
        }

        ItemStack renderItem = millstone.getOutput().isEmpty() ? millstone.getInput() : millstone.getOutput();
        if (renderItem.isEmpty()) {
            state.clearItems();
            return;
        }

        state.seed = millstone.getBlockPos().asLong();
        state.itemCount = Math.min(renderItem.getCount(), MillstoneBlockEntity.MAX_INPUT_COUNT);

        Map<Identifier, Identifier> map = ItemRenderReplacerReloadListener.INSTANCE.millstone();
        Identifier key = BuiltInRegistries.ITEM.getKey(renderItem.getItem());
        state.hasCustomModel = map.containsKey(key);

        ItemOwner owner = RenderItemUtil.blockItemOwner(level, Vec3.atCenterOf(millstone.getBlockPos()), state.facingDeg);
        RenderItemUtil.updateItemState(
                state.itemState,
                renderItem,
                itemModelResolver,
                ItemRenderReplacer.getModel(level, renderItem, map),
                ItemDisplayContext.FIXED,
                level,
                owner,
                0
        );
    }

    @Override
    public void submit(MillstoneRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5, 1.5, 0.5);
        poseStack.mulPose(Axis.ZN.rotationDegrees(180));
        poseStack.mulPose(Axis.YN.rotationDegrees(180 - state.facingDeg));

        bodyModel.getWheel().yRot = state.wheelRot;
        bodyModel.getRoll().zRot = state.rollRot;
        bodyModel.getRotStick().xRot = state.rotStick;

        collector.submitModel(bodyModel, state, poseStack, RenderTypes.entityCutoutNoCull(TEXTURE), state.lightCoords, 0, 0, state.breakProgress);
        poseStack.popPose();

        bodyModel.getWheel().yRot = 0;
        bodyModel.getRoll().zRot = 0;
        bodyModel.getRotStick().xRot = 0;

        if (state.itemCount == 0 || state.itemState.isEmpty()) {
            return;
        }

        RandomSource source = RandomSource.create(state.seed);
        for (int i = 0; i < state.itemCount; i++) {
            poseStack.pushPose();
            poseStack.translate(0, 0.875, 0);
            poseStack.rotateAround(Axis.YP.rotationDegrees(i * 45 + source.nextInt(15)), 0.5f, 0, 0.5f);
            poseStack.mulPose(Axis.YP.rotationDegrees(source.nextInt(20)));
            if (!state.hasCustomModel) {
                poseStack.mulPose(Axis.XN.rotationDegrees(80 + source.nextInt(20)));
            }
            poseStack.scale(0.65F, 0.65F, 0.65F);
            state.itemState.submit(poseStack, collector, state.lightCoords, 0, 0);
            poseStack.popPose();
        }
    }

    public static class MillstoneRenderState extends BlockEntityRenderState {
        public int facingDeg;
        public float wheelRot;
        public float rollRot;
        public float rotStick;
        public long seed;
        public int itemCount;
        public boolean hasCustomModel;
        public final ItemStackRenderState itemState = new ItemStackRenderState();

        void clearItems() {
            itemCount = 0;
            itemState.clear();
        }
    }
}
