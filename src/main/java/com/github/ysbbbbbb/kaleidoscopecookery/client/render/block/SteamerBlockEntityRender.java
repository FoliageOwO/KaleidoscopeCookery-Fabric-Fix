package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.block.kitchen.SteamerBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.SteamerBlockEntity;
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
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class SteamerBlockEntityRender implements BlockEntityRenderer<SteamerBlockEntity, SteamerBlockEntityRender.SteamerRenderState> {
    private final ItemModelResolver itemModelResolver;

    public SteamerBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public SteamerRenderState createRenderState() {
        return new SteamerRenderState();
    }

    @Override
    public void extractRenderState(SteamerBlockEntity steamer, SteamerRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(steamer, state, breakProgress);
        Level level = steamer.getLevel();
        if (level == null) {
            state.clearItems();
            return;
        }
        if (steamer.getBlockState().getValue(SteamerBlock.HAS_LID)) {
            state.clearItems();
            return;
        }

        NonNullList<ItemStack> items = steamer.getItems();
        state.ensureItemCapacity(items.size());

        Map<Identifier, Identifier> map = ItemRenderReplacerReloadListener.INSTANCE.steamer();
        ItemOwner owner = RenderItemUtil.blockItemOwner(level, Vec3.atCenterOf(steamer.getBlockPos()), 0);
        for (int i = 0; i < items.size(); i++) {
            ItemStack stack = items.get(i);
            ItemStackRenderState itemState = state.itemStates.get(i);
            RenderItemUtil.updateItemState(
                    itemState,
                    stack,
                    itemModelResolver,
                    ItemRenderReplacer.getModel(level, stack, map),
                    ItemDisplayContext.FIXED,
                    level,
                    owner,
                    i
            );
            if (!stack.isEmpty()) {
                Identifier key = BuiltInRegistries.ITEM.getKey(stack.getItem());
                state.hasCustomModel.set(i, map.containsKey(key));
            } else {
                state.hasCustomModel.set(i, false);
            }
        }
        state.itemCount = items.size();
    }

    @Override
    public void submit(SteamerRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.itemCount == 0) {
            return;
        }

        for (int i = 0; i < state.itemCount; i++) {
            ItemStackRenderState itemState = state.itemStates.get(i);
            if (itemState.isEmpty()) {
                continue;
            }
            double x = (i % 2) * 0.3 + 0.35;
            double y = (i / 4) * 0.5 + 0.25 + (i % 4) * 0.01;
            double z = ((i / 2) % 2) * 0.3 + 0.35;
            poseStack.pushPose();
            poseStack.translate(x, y, z);
            if (!state.hasCustomModel.get(i)) {
                poseStack.mulPose(Axis.XN.rotationDegrees(90));
            } else {
                poseStack.translate(0, 0.4375, 0.4375);
            }
            poseStack.scale(0.5F, 0.5F, 0.5F);
            itemState.submit(poseStack, collector, state.lightCoords, 0, 0);
            poseStack.popPose();
        }
    }

    public static class SteamerRenderState extends BlockEntityRenderState {
        public int itemCount;
        public final List<ItemStackRenderState> itemStates = new ArrayList<>();
        public final List<Boolean> hasCustomModel = new ArrayList<>();

        void ensureItemCapacity(int count) {
            while (itemStates.size() < count) {
                itemStates.add(new ItemStackRenderState());
                hasCustomModel.add(false);
            }
        }

        void clearItems() {
            itemCount = 0;
            for (ItemStackRenderState state : itemStates) {
                state.clear();
            }
            for (int i = 0; i < hasCustomModel.size(); i++) {
                hasCustomModel.set(i, false);
            }
        }
    }
}
