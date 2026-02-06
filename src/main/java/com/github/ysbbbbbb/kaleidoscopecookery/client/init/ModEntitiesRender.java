package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import com.github.ysbbbbbb.kaleidoscopecookery.entity.SitEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.entity.ThrowableBaoziEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Environment(EnvType.CLIENT)
public class ModEntitiesRender {
    public static void register() {
        // 注册实体渲染器
        EntityRendererRegistry.register(SitEntity.TYPE, NoopRenderer::new);
        EntityRendererRegistry.register(ThrowableBaoziEntity.TYPE, ThrownItemRenderer::new);
    }
}
