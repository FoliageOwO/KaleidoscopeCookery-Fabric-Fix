package com.github.ysbbbbbb.kaleidoscopecookery.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;

@Environment(EnvType.CLIENT)
@Mixin(Gui.class)
public class GuiLayerRegistrationMixin {
}
