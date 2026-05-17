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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public final class ComputedNodes {
    private static final ResourceLocation MENU_VANILLA =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_vanilla");
    private static final ResourceLocation MENU_CREATE =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_create");
    private static final ResourceLocation MENU_CREATE_REDSTONE_LINK =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "menu_create_redstone_link");

    private ComputedNodes() {}

    public static void register() {
        NodeMenuRegistry.registerCategory(
                MENU_VANILLA, Component.literal("Vanilla"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(MENU_CREATE, Component.literal("Create"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(
                MENU_CREATE_REDSTONE_LINK, Component.literal("Redstone Link"), MENU_CREATE);

        NodeRegistry.register(CreateRedstoneLinkSenderNode.TYPE_ID, CreateRedstoneLinkSenderNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_CREATE_REDSTONE_LINK, CreateRedstoneLinkSenderNode.TYPE_ID, Component.literal("Sender"));

        NodeRegistry.register(CreateRedstoneLinkReceiverNode.TYPE_ID, CreateRedstoneLinkReceiverNode::new);
        NodeMenuRegistry.addNodeEntry(
                MENU_CREATE_REDSTONE_LINK, CreateRedstoneLinkReceiverNode.TYPE_ID, Component.literal("Receiver"));

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
    }
}
