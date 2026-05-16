package dev.propulsionteam.computed.content.nodes.vanilla;

import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.elements.WDropdown;
import dev.devce.websnodelib.api.elements.WLabel;
import dev.propulsionteam.computed.Computed;
import java.util.List;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

/**
 * Peripheral node: when Tick input &gt; 0.5, emits weak redstone toward the chosen face of the computer block.
 */
public final class RedstonePortNode extends WNode {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "redstone_emitter");

    private Direction emitFace = Direction.NORTH;
    private final WDropdown<Direction> faceDropdown;

    public RedstonePortNode(int x, int y) {
        super(TYPE_ID, "Redstone", x, y);
        addInput("Tick", 0xFF00FF88);
        addInput("Level", 0xFFFF6655);
        faceDropdown = new WDropdown<>(
                88,
                List.of(Direction.values()),
                d -> "Face: " + d.getName(),
                emitFace,
                d -> emitFace = d);
        addElement(faceDropdown);
        addElement(new WLabel("Weak power 0-15 to neighbor", 0xFF888888));
        setEvaluator(n -> {});
    }

    public Direction getEmitDirection() {
        return emitFace;
    }

    @Override
    public net.minecraft.nbt.CompoundTag save() {
        net.minecraft.nbt.CompoundTag tag = super.save();
        tag.putString("computedEmitFace", emitFace.getName());
        return tag;
    }

    @Override
    public void load(net.minecraft.nbt.CompoundTag tag) {
        super.load(tag);
        if (tag.contains("computedEmitFace")) {
            Direction parsed = Direction.byName(tag.getString("computedEmitFace"));
            if (parsed != null) {
                emitFace = parsed;
                faceDropdown.setSelected(parsed);
            }
        }
    }
}
