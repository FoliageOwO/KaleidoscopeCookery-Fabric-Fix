package com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.category;

import com.github.ysbbbbbb.kaleidoscopecookery.KaleidoscopeCookery;
import com.github.ysbbbbbb.kaleidoscopecookery.compat.rei.ReiUtil;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.display.DisplaySerializer;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReiMillstoneRecipeCategory implements DisplayCategory<ReiMillstoneRecipeCategory.MillstoneRecipeDisplay> {
    public static final CategoryIdentifier<MillstoneRecipeDisplay> ID = CategoryIdentifier.of(KaleidoscopeCookery.MOD_ID, "plugin/millstone");
    private static final Identifier BG = Identifier.fromNamespaceAndPath(KaleidoscopeCookery.MOD_ID, "textures/gui/jei/millstone.png");
    private static final MutableComponent TITLE = Component.translatable("block.kaleidoscope_cookery.millstone");

    public static final int WIDTH = 176;
    public static final int HEIGHT = 95;

    @Override
    public CategoryIdentifier<MillstoneRecipeDisplay> getCategoryIdentifier() {
        return ID;
    }

    @Override
    public List<Widget> setupDisplay(MillstoneRecipeDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        int startX = bounds.x;
        int startY = bounds.y;

        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createTexturedWidget(BG, startX, startY, 0, 0, WIDTH, HEIGHT));
        widgets.add(Widgets.createSlot(new Point(startX + 69, startY + 39))
                .entries(display.getInputEntries().getFirst())
                .markInput());
        widgets.add(Widgets.createSlot(new Point(startX + 146, startY + 47))
                .entries(display.getOutputEntries().getFirst())
                .disableBackground()
                .markOutput());

        return widgets;
    }

    @Override
    public int getDisplayWidth(MillstoneRecipeDisplay display) {
        return WIDTH;
    }

    @Override
    public int getDisplayHeight() {
        return HEIGHT;
    }

    @Override
    public Component getTitle() {
        return TITLE;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(ModItems.MILLSTONE);
    }

    public static void registerCategories(CategoryRegistry registry) {
        registry.add(new ReiMillstoneRecipeCategory());
        registry.addWorkstations(ReiMillstoneRecipeCategory.ID,
                ReiUtil.ofItem(ModItems.MILLSTONE)
        );
    }

    public static void registerDisplays(DisplayRegistry registry) {
        var connection = Minecraft.getInstance().getConnection();
        if (connection == null) {
            return;
        }
        connection.recipes().getSynchronizedRecipes().getAllOfType(ModRecipes.MILLSTONE_RECIPE)
                .forEach(r -> {
                    List<EntryIngredient> input = ReiUtil.ofIngredients(List.of(r.value().getIngredient()));
                    List<EntryIngredient> output = ReiUtil.ofItemStacks(r.value().getResult());

                    registry.add(new MillstoneRecipeDisplay(Optional.of(r.id().identifier()), input, output));
                });
    }

    public static class MillstoneRecipeDisplay extends BasicDisplay {
        public static final MapCodec<MillstoneRecipeDisplay> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        EntryIngredient.codec().listOf().fieldOf("inputs").forGetter(MillstoneRecipeDisplay::getInputEntries),
                        EntryIngredient.codec().listOf().fieldOf("outputs").forGetter(MillstoneRecipeDisplay::getOutputEntries),
                        Identifier.CODEC.optionalFieldOf("location").forGetter(MillstoneRecipeDisplay::getDisplayLocation)
                ).apply(instance, (inputs, outputs, location) -> new MillstoneRecipeDisplay(location, inputs, outputs))
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, MillstoneRecipeDisplay> STREAM_CODEC = StreamCodec.composite(
                EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), MillstoneRecipeDisplay::getInputEntries,
                EntryIngredient.streamCodec().apply(ByteBufCodecs.list()), MillstoneRecipeDisplay::getOutputEntries,
                ByteBufCodecs.optional(Identifier.STREAM_CODEC.cast()), MillstoneRecipeDisplay::getDisplayLocation,
                (inputs, outputs, location) -> new MillstoneRecipeDisplay(location, inputs, outputs)
        );
        public static final DisplaySerializer<MillstoneRecipeDisplay> SERIALIZER = DisplaySerializer.of(CODEC, STREAM_CODEC);

        public MillstoneRecipeDisplay(Optional<Identifier> location, List<EntryIngredient> inputs, List<EntryIngredient> outputs) {
            super(inputs, outputs, location);
        }

        @Override
        public CategoryIdentifier<?> getCategoryIdentifier() {
            return ID;
        }

        @Override
        public DisplaySerializer<MillstoneRecipeDisplay> getSerializer() {
            return SERIALIZER;
        }
    }
}
