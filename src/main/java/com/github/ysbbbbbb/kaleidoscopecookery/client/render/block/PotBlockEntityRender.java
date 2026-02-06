package com.github.ysbbbbbb.kaleidoscopecookery.client.render.block;

import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.PotBlockEntity;
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
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class PotBlockEntityRender implements BlockEntityRenderer<PotBlockEntity, PotBlockEntityRender.PotRenderState> {
    private final BlockEntityRendererProvider.Context context;
    private final ItemModelResolver itemModelResolver;

    public PotBlockEntityRender(BlockEntityRendererProvider.Context context) {
        this.context = context;
        this.itemModelResolver = context.itemModelResolver();
    }

    @Override
    public PotRenderState createRenderState() {
        return new PotRenderState();
    }

    @Override
    public void extractRenderState(PotBlockEntity pot, PotRenderState state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderState.extractBase(pot, state, breakProgress);
        Level level = pot.getLevel();
        if (level == null) {
            state.clearItems();
            return;
        }

        RandomSource source = RandomSource.create(pot.getSeed());
        PotBlockEntity.StirFryAnimationData data = pot.animationData;
        long time = System.currentTimeMillis() - data.timestamp;

        if (data.preSeed == -1L) {
            data.preSeed = pot.getSeed();
        }
        if (data.preSeed != pot.getSeed()) {
            data.preSeed = pot.getSeed();
            if (time > 1000) {
                data.timestamp = System.currentTimeMillis();
                data.randomHeights = new float[9];
                for (int i = 0; i < 9; i++) {
                    data.randomHeights[i] = 0.25f + source.nextFloat() * 1;
                }
            }
        }

        state.rotation = pot.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).get2DDataValue() * 90;
        state.seed = pot.getSeed();
        state.randomHeights = data.randomHeights;
        state.time = time;
        state.status = pot.getStatus();
        state.currentTick = pot.getCurrentTick();

        boolean showInputs = pot.getStatus() != PotBlockEntity.FINISHED && pot.getStatus() != PotBlockEntity.BURNT;
        List<ItemStack> stacks = showInputs || pot.hasCarrier() ? pot.getInputs() : List.of(pot.getResult());

        state.ensureItemCapacity(stacks.size());
        ItemOwner owner = RenderItemUtil.blockItemOwner(level, Vec3.atCenterOf(pot.getBlockPos()), state.rotation);
        for (int i = 0; i < stacks.size(); i++) {
            ItemStack stack = stacks.get(i);
            ItemStackRenderState itemState = state.itemStates.get(i);
            RenderItemUtil.updateItemState(
                    itemState,
                    stack,
                    itemModelResolver,
                    ItemRenderReplacer.getModel(level, stack, ItemRenderReplacerReloadListener.INSTANCE.pot()),
                    ItemDisplayContext.FIXED,
                    level,
                    owner,
                    i
            );
        }
        state.itemCount = stacks.size();
    }

    @Override
    public void submit(PotRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        if (state.itemCount == 0) {
            return;
        }

        RandomSource source = RandomSource.create(state.seed);
        poseStack.pushPose();
        poseStack.translate(0.5, 0.1, 0.5);
        poseStack.mulPose(Axis.YN.rotationDegrees(state.rotation));
        poseStack.mulPose(Axis.XN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);

        for (int i = 0; i < state.itemCount; i++) {
            ItemStackRenderState itemState = state.itemStates.get(i);
            if (itemState.isEmpty()) {
                continue;
            }

            poseStack.pushPose();
            int count = 90 + source.nextInt(90);
            poseStack.mulPose(Axis.ZN.rotationDegrees(i * count));
            if (state.time < 1000 && state.randomHeights != null && i < state.randomHeights.length) {
                poseStack.translate(0, 0, state.randomHeights[i] * Mth.sin(Mth.PI * state.time / 1000f));
                poseStack.mulPose(Axis.XN.rotationDegrees(720f / 1000f * state.time));
            }
            itemState.submit(poseStack, collector, state.lightCoords, 0, 0);
            poseStack.popPose();

            poseStack.translate(0, 0, 0.025);
        }

        poseStack.popPose();
    }

    public static class PotRenderState extends BlockEntityRenderState {
        public int rotation;
        public long time;
        public int status;
        public int currentTick;
        public long seed;
        public float[] randomHeights;
        public final List<ItemStackRenderState> itemStates = new ArrayList<>();
        public int itemCount;

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
