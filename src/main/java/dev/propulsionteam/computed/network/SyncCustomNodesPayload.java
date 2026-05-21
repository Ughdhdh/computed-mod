package dev.propulsionteam.computed.network;

import dev.propulsionteam.computed.Computed;
import java.util.List;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Server→client push of the server's data-driven node definitions (raw JSON), making the server authoritative. */
public record SyncCustomNodesPayload(List<String> definitions) implements CustomPacketPayload {
    /** Per-definition JSON cap; generous so large nodes fit but a single payload can't be unbounded. */
    private static final int MAX_DEFINITION_CHARS = 1 << 20;

    public static final CustomPacketPayload.Type<SyncCustomNodesPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(Computed.MODID, "sync_custom_nodes"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SyncCustomNodesPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.stringUtf8(MAX_DEFINITION_CHARS).apply(ByteBufCodecs.list()),
            SyncCustomNodesPayload::definitions,
            SyncCustomNodesPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
