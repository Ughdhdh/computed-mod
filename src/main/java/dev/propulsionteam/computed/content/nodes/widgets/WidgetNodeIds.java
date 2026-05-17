package dev.propulsionteam.computed.content.nodes.widgets;

import dev.propulsionteam.computed.Computed;
import net.minecraft.resources.ResourceLocation;

public final class WidgetNodeIds {
    public static final ResourceLocation TEXT_SOURCE =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "text_source");
    public static final ResourceLocation PERIPHERAL =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "peripheral");
    public static final ResourceLocation TEXT_WIDGET =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "text_widget");
    public static final ResourceLocation CLOCK_WIDGET =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "clock_widget");
    public static final ResourceLocation BUTTON_WIDGET =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "button_widget");
    public static final ResourceLocation SLIDER_WIDGET =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "slider_widget");
    public static final ResourceLocation PROGRESS_BAR_WIDGET =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "progress_bar_widget");
    public static final ResourceLocation COLOR_SOURCE =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "color_source");

    private WidgetNodeIds() {}
}
