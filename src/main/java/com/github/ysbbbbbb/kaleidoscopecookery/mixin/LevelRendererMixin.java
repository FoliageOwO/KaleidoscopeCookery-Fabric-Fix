package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import com.github.ysbbbbbb.kaleidoscopecookery.util.RenderCulling;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    @Final
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Shadow
    @Final
    private Set<BlockEntity> globalBlockEntities;

    @ModifyVariable(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
                    ordinal = 0
            ),
            ordinal = 0
    )
    private List<BlockEntity> modifyBlockEntityList(List<BlockEntity> list, @Local Frustum frustum) {
        //过滤掉不可见的 BlockEntity
        return list.stream()
                .filter(blockEntity -> RenderCulling.isBlockEntityRendererVisible(
                        blockEntityRenderDispatcher,
                        blockEntity,
                        // 这里需要获取frustum，可能需要通过其他方式
                        frustum
                ))
                .collect(Collectors.toList());
    }

    @ModifyVariable(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Set;iterator()Ljava/util/Iterator;",
                    ordinal = 0
            ),
            ordinal = 0
    )private Set<BlockEntity> modifyGlobalBlockEntityList(@Local Frustum frustum) {
        return this.globalBlockEntities.stream()
                .filter(blockEntity -> RenderCulling.isBlockEntityRendererVisible(
                        blockEntityRenderDispatcher,
                        blockEntity,
                        frustum
                ))
                .collect(Collectors.toSet());
    }
}
