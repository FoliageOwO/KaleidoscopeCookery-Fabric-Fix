package com.github.ysbbbbbb.kaleidoscopecookery.util.neo.client.animation;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;

@FunctionalInterface
public interface IArmPoseTransformer {
    void applyTransform(HumanoidModel<?> var1, LivingEntity var2, HumanoidArm var3);
}
