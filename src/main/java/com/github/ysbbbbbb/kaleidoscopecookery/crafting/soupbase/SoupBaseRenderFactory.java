package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;

import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.lang.reflect.Constructor;

public final class SoupBaseRenderFactory {
    private SoupBaseRenderFactory() {
    }

    @SuppressWarnings("unchecked")
    public static ISoupBaseRender create(String className, Class<?>[] parameterTypes, Object... args) {
        if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) {
            return null;
        }
        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> constructor = clazz.getConstructor(parameterTypes);
            return (ISoupBaseRender) constructor.newInstance(args);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }
}
