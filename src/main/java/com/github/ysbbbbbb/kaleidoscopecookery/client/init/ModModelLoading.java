package com.github.ysbbbbbb.kaleidoscopecookery.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class ModModelLoading {
    private static final String MODELS = "models/";
    private static final String MODELS_CHOPPING_BOARD = MODELS + "chopping_board";
    private static final String MODELS_CARPET = MODELS + "block/carpet";
    private static final String JSON = ".json";

    public static void register() {
        // ModelLoadingPlugin API changed in 1.21.11; model JSONs are now picked up via assets/items
        // and normal blockstate/model discovery. Keep this as a no-op to avoid hard dependency on
        // a now-removed context API.
        ModelLoadingPlugin.register(context -> {
        });
    }

    private static Identifier handleModelId(Identifier input) {
        String namespace = input.getNamespace();
        String path = input.getPath();
        return Identifier.fromNamespaceAndPath(namespace, path.substring(MODELS.length(), path.length() - JSON.length()));
    }
}
