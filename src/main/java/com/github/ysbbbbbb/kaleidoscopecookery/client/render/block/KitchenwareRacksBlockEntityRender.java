package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.KitchenwareRacksBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.client.render.RenderItemUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.client.resources.ItemRenderReplacer;
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
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraft.resources.Identifier;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class KitchenwareRacksBlockEntityRender implements BlockEntityRenderer<KitchenwareRacksBlockEntity, KitchenwareRacksBlockEntityRender.RackRenderState> {
    private static final Map<Identifier, Identifier> EMPTY_REPLACER = Map.of();
    private static final float ITEM_SCALE = 0.5f;
    private static final float ITEM_Z_OFFSET = 0.3125f;
    private static final float ITEM_Y_OFFSET = 0.75f;
    private static final float ITEM_X_OFFSET = 0.175f;

    private final ItemModelResolver itemModelResolver;

    public KitchenwareRacksBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public RackRenderState createRenderState() {
        return new RackRenderState();
    }

    @Override
    public void extractRenderState(KitchenwareRacksBlockEntity racks, RackRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(racks, state, breakProgress);
        Level level = racks.getLevel();
        if (level == null) {
            state.clearItems();
            return;
        }

        ItemStack left = racks.getItemLeft();
        ItemStack right = racks.getItemRight();
        if (left.isEmpty() && right.isEmpty()) {
            state.clearItems();
            return;
        }

        state.front = racks.getBlockState()
                .getValue(BlockStateProperties.HORIZONTAL_FACING)
                .getOpposite();

        ItemOwner owner = RenderItemUtil.blockItemOwner(level, Vec3.atCenterOf(racks.getBlockPos()), state.front.toYRot());
        RenderItemUtil.updateItemState(
                state.leftItem,
                left,
                itemModelResolver,
                ItemRenderReplacer.getModel(level, left, EMPTY_REPLACER),
                ItemDisplayContext.FIXED,
                level,
                owner,
                0
        );
        RenderItemUtil.updateItemState(
                state.rightItem,
                right,
                itemModelResolver,
                ItemRenderReplacer.getModel(level, right, EMPTY_REPLACER),
                ItemDisplayContext.FIXED,
                level,
                owner,
                1
        );
    }

    @Override
    public void submit(RackRenderState state, com.mojang.blaze3d.vertex.PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.leftItem.isEmpty() && state.rightItem.isEmpty()) {
            return;
        }

        poseStack.pushPose();
        Direction front = state.front;
        if (front == null) {
            poseStack.popPose();
            return;
        }
        Direction left = front.getCounterClockWise();

        float baseX = 0.5f + front.getStepX() * ITEM_Z_OFFSET;
        float baseZ = 0.5f + front.getStepZ() * ITEM_Z_OFFSET;
        float leftX = left.getStepX() * ITEM_X_OFFSET;
        float leftZ = left.getStepZ() * ITEM_X_OFFSET;

        renderItem(state.leftItem, poseStack, collector, state.lightCoords,
                baseX + leftX, ITEM_Y_OFFSET, baseZ + leftZ, front);
        renderItem(state.rightItem, poseStack, collector, state.lightCoords,
                baseX - leftX, ITEM_Y_OFFSET, baseZ - leftZ, front);

        poseStack.popPose();
    }

    private static void renderItem(ItemStackRenderState itemState,
                                   com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   SubmitNodeCollector collector,
                                   int light,
                                   float x,
                                   float y,
                                   float z,
                                   Direction front) {
        if (itemState.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        float rot = front.toYRot();
        if (front.getAxis() == Direction.Axis.X) {
            rot += 180.0f;
        }
        poseStack.mulPose(com.mojang.math.Axis.YP.rotationDegrees(rot));
        poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
        itemState.submit(poseStack, collector, light, 0, 0);
        poseStack.popPose();
    }

    public static class RackRenderState extends BlockEntityRenderState {
        public Direction front;
        public final ItemStackRenderState leftItem = new ItemStackRenderState();
        public final ItemStackRenderState rightItem = new ItemStackRenderState();

        void clearItems() {
            leftItem.clear();
            rightItem.clear();
        }
    }
}
