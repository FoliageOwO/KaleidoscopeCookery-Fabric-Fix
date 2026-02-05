package com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase;


import com.github.ysbbbbbb.kaleidoscopecookery.api.client.render.ISoupBaseRender;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;

public class MobSoupBase extends FluidSoupBase {
    private final EntityType<?> type;

    public MobSoupBase(Identifier name, Item bucket, int bubbleColor) {
        super(name, bucket, bubbleColor);
        if (bucket instanceof MobBucketItem mobBucketItem) {
            this.type = mobBucketItem.type;
        } else {
            throw new IllegalArgumentException("Mob bucket item must have a valid entity type!");
        }
    }

    public MobSoupBase(Identifier name, Item bucket) {
        this(name, bucket, 0x3F76E4);
    }

    @Override
    public ISoupBaseRender getRender() {
        return SoupBaseRenderFactory.create(
                "com.github.ysbbbbbb.kaleidoscopecookery.client.render.soupbase.MobSoupBaseRender",
                new Class[]{net.minecraft.world.level.material.Fluid.class, EntityType.class},
                this.fluid,
                this.type
        );
    }
}
