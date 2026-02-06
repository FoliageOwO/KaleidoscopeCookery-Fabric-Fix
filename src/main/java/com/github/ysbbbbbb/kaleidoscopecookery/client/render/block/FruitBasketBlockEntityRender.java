package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.decoration.FruitBasketBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.RenderItemUtil;
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
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class FruitBasketBlockEntityRender implements BlockEntityRenderer<FruitBasketBlockEntity, FruitBasketBlockEntityRender.BasketRenderState> {
    private static final int MAX_SLOTS = 8;
    private static final float ITEM_SCALE = 0.35f;
    private static final float BASE_Y = 0.25f;
    private static final float Z_OFFSET = 0.18f;
    private static final float X_STEP = 0.2f;
    private static final float X_START = -0.3f;
    private static final Map<Identifier, Identifier> EMPTY_REPLACER = Map.of();

    private final ItemModelResolver itemModelResolver;

    public FruitBasketBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public BasketRenderState createRenderState() {
        return new BasketRenderState();
    }

    @Override
    public void extractRenderState(FruitBasketBlockEntity basket, BasketRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(basket, state, breakProgress);
        Level level = basket.getLevel();
        if (level == null) {
            state.clearItems();
            return;
        }

        NonNullList<ItemStack> items = basket.getItems();
        state.ensureItemCapacity(items.size());
        state.itemCount = items.size();

        state.rotationDeg = basket.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING)
                .getOpposite()
                .get2DDataValue() * 90;

        ItemOwner owner = RenderItemUtil.blockItemOwner(level, Vec3.atCenterOf(basket.getBlockPos()), state.rotationDeg);
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            ItemStackRenderState itemState = state.itemStates.get(i);
            RenderItemUtil.updateItemState(
                    itemState,
                    stack,
                    itemModelResolver,
                    com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer.getModel(level, stack, EMPTY_REPLACER),
                    ItemDisplayContext.FIXED,
                    level,
                    owner,
                    i
            );
        }
    }

    @Override
    public void submit(BasketRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.itemCount == 0) {
            return;
        }

        poseStack.pushPose();
        poseStack.translate(0.5, 0.0, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotationDeg));

        int slots = Math.min(state.itemCount, MAX_SLOTS);
        for (int i = 0; i < slots; i++) {
            ItemStackRenderState itemState = state.itemStates.get(i);
            if (itemState.isEmpty()) {
                continue;
            }

            int row = i / 4;
            int col = i % 4;
            float x = X_START + (col * X_STEP);
            float z = row == 0 ? -Z_OFFSET : Z_OFFSET;

            poseStack.pushPose();
            poseStack.translate(x, BASE_Y + (i * 0.001f), z);
            poseStack.mulPose(Axis.XN.rotationDegrees(90));
            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
            itemState.submit(poseStack, collector, state.lightCoords, 0, 0);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    public static class BasketRenderState extends BlockEntityRenderState {
        public int itemCount;
        public int rotationDeg;
        public final List<ItemStackRenderState> itemStates = new ArrayList<>();

        void ensureItemCapacity(int count) {
            while (itemStates.size() < count) {
                itemStates.add(new ItemStackRenderState());
            }
        }

        void clearItems() {
            itemCount = 0;
            for (ItemStackRenderState state : itemStates) {
                state.clear();
            }
        }
    }
}
