package dev.propulsionteam.computed.content;

import dev.devce.websnodelib.api.NodeMenuRegistry;
import dev.devce.websnodelib.api.NodeRegistry;
import dev.propulsionteam.computed.Computed;
import dev.propulsionteam.computed.content.nodes.create.CreateRedstoneLinkReceiverNode;
import dev.propulsionteam.computed.content.nodes.create.CreateRedstoneLinkSenderNode;
import dev.propulsionteam.computed.content.nodes.vanilla.BlockPresenceNode;
import dev.propulsionteam.computed.content.nodes.vanilla.ComparatorReadNode;
import dev.propulsionteam.computed.content.nodes.vanilla.RedstoneInputNode;
import dev.propulsionteam.computed.content.nodes.vanilla.RedstonePortNode;
import dev.propulsionteam.computed.content.nodes.vanilla.WorldTimeNode;
import dev.propulsionteam.computed.content.nodes.widgets.ButtonWidgetNode;
import dev.propulsionteam.computed.content.nodes.widgets.ClockWidgetNode;
import dev.propulsionteam.computed.content.nodes.widgets.ColorSourceNode;
import dev.propulsionteam.computed.content.nodes.widgets.PeripheralNode;
import dev.propulsionteam.computed.content.nodes.widgets.ProgressBarWidgetNode;
import dev.propulsionteam.computed.content.nodes.widgets.SliderWidgetNode;
import dev.propulsionteam.computed.content.nodes.widgets.TextSourceNode;
import dev.propulsionteam.computed.content.nodes.widgets.TextWidgetNode;
import dev.propulsionteam.computed.content.nodes.widgets.WidgetNodeIds;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ComputedNodes {
    private static final ResourceLocation MENU_VANILLA =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_vanilla");
    private static final ResourceLocation MENU_CREATE =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_create");
    private static final ResourceLocation MENU_CREATE_REDSTONE_LINK =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_create_redstone_link");
    private static final ResourceLocation MENU_SOURCES =
            ResourceLocation.fromNamespaceAndPath("websnodelib", "menu_sources");
    private static final ResourceLocation MENU_WIDGETS =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_widgets");
    private static final ResourceLocation MENU_PERIPHERALS =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_peripherals");

    private ComputedNodes() {}

    public static void register() {
        NodeMenuRegistry.registerCategory(
                MENU_VANILLA, Component.literal("Vanilla"), NodeMenuRegistry.ROOT);

        NodeRegistry.register(CreateRedstoneLinkSenderNode.TYPE_ID, CreateRedstoneLinkSenderNode::new);
        NodeRegistry.register(CreateRedstoneLinkReceiverNode.TYPE_ID, CreateRedstoneLinkReceiverNode::new);
        if (net.neoforged.fml.ModList.get().isLoaded("create")) {
            NodeMenuRegistry.registerCategory(MENU_CREATE, Component.literal("Create"), NodeMenuRegistry.ROOT);
            NodeMenuRegistry.registerCategory(
                    MENU_CREATE_REDSTONE_LINK, Component.literal("Redstone Link"), MENU_CREATE);
            NodeMenuRegistry.addNodeEntry(
                    MENU_CREATE_REDSTONE_LINK, CreateRedstoneLinkSenderNode.TYPE_ID, Component.literal("Sender"));
            NodeMenuRegistry.addNodeEntry(
                    MENU_CREATE_REDSTONE_LINK, CreateRedstoneLinkReceiverNode.TYPE_ID, Component.literal("Receiver"));
        }

        NodeRegistry.register(RedstonePortNode.TYPE_ID, RedstonePortNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_VANILLA, RedstonePortNode.TYPE_ID, Component.literal("Redstone Output"));

        NodeRegistry.register(RedstoneInputNode.TYPE_ID, RedstoneInputNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_VANILLA, RedstoneInputNode.TYPE_ID, Component.literal("Redstone Input"));

        NodeRegistry.register(WorldTimeNode.TYPE_ID, WorldTimeNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_VANILLA, WorldTimeNode.TYPE_ID, Component.literal("World Time"));

        NodeRegistry.register(ComparatorReadNode.TYPE_ID, ComparatorReadNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_VANILLA, ComparatorReadNode.TYPE_ID, Component.literal("Comparator Read"));

        NodeRegistry.register(BlockPresenceNode.TYPE_ID, BlockPresenceNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_VANILLA, BlockPresenceNode.TYPE_ID, Component.literal("Block Presence"));

        NodeMenuRegistry.registerCategory(MENU_WIDGETS, Component.literal("Widgets"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(MENU_PERIPHERALS, Component.literal("Peripherals"), NodeMenuRegistry.ROOT);

        NodeRegistry.register(WidgetNodeIds.TEXT_SOURCE, TextSourceNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_SOURCES, WidgetNodeIds.TEXT_SOURCE, Component.literal("Text"));

        NodeRegistry.register(WidgetNodeIds.COLOR_SOURCE, ColorSourceNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_SOURCES, WidgetNodeIds.COLOR_SOURCE, Component.literal("Color"));

        NodeRegistry.register(WidgetNodeIds.PERIPHERAL, PeripheralNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_PERIPHERALS, WidgetNodeIds.PERIPHERAL, Component.literal("Monitor"));

        NodeRegistry.register(WidgetNodeIds.TEXT_WIDGET, TextWidgetNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_WIDGETS, WidgetNodeIds.TEXT_WIDGET, Component.literal("Text Widget"));

        NodeRegistry.register(WidgetNodeIds.CLOCK_WIDGET, ClockWidgetNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_WIDGETS, WidgetNodeIds.CLOCK_WIDGET, Component.literal("Clock Widget"));

        NodeRegistry.register(WidgetNodeIds.BUTTON_WIDGET, ButtonWidgetNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_WIDGETS, WidgetNodeIds.BUTTON_WIDGET, Component.literal("Button Widget"));

        NodeRegistry.register(WidgetNodeIds.SLIDER_WIDGET, SliderWidgetNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_WIDGETS, WidgetNodeIds.SLIDER_WIDGET, Component.literal("Slider Widget"));

        NodeRegistry.register(WidgetNodeIds.PROGRESS_BAR_WIDGET, ProgressBarWidgetNode::new);
        NodeMenuRegistry.addNodeEntry(MENU_WIDGETS, WidgetNodeIds.PROGRESS_BAR_WIDGET, Component.literal("Progress Bar Widget"));
    }
}
