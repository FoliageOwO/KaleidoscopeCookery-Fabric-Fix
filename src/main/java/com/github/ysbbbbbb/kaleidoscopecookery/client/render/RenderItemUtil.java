package com.github.ysbbbbbb.kaleidoscopecookery.client.render;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public final class RenderItemUtil {
    private RenderItemUtil() {
    }

    public static ItemOwner blockItemOwner(Level level, Vec3 pos, float yaw) {
        return new ItemOwner() {
            @Override
            public Level level() {
                return level;
            }

            @Override
            public Vec3 position() {
                return pos;
            }

            @Override
            public float getVisualRotationYInDegrees() {
                return yaw;
            }
        };
    }

    public static void updateItemState(ItemStackRenderState state,
                                       ItemStack stack,
                                       ItemModelResolver resolver,
                                       ItemModel model,
                                       ItemDisplayContext context,
                                       @Nullable Level level,
                                       ItemOwner owner,
                                       int seed) {
        state.clear();
        if (stack.isEmpty()) {
            return;
        }
        if (level instanceof ClientLevel clientLevel) {
            model.update(state, stack, resolver, context, clientLevel, owner, seed);
        }
    }
}
