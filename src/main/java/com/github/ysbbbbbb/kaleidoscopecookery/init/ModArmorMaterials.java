package com.github.ysbbbbbb.kaleidoscopecookery.init;

import net.minecraft.core.Holder;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorMaterials;

public class ModArmorMaterials {
    public static final Holder<ArmorMaterial> FARMER = Holder.direct(ArmorMaterials.LEATHER);

    public static void registerArmorMaterials() {
        // Keep legacy entrypoint; custom armor material registry changed in modern versions.
    }
}
