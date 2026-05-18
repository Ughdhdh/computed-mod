package dev.propulsionteam.computed.content.nodes.vanilla;

import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.WPin;
import dev.devce.websnodelib.api.elements.WLabel;
import dev.propulsionteam.computed.Computed;
import net.minecraft.resources.ResourceLocation;

public final class ConcatenateTextNode extends WNode {

    public static final ResourceLocation TYPE_ID =
            ResourceLocation.fromNamespaceAndPath(Computed.MODID, "concatenate_strings");

    public ConcatenateTextNode(int x, int y) {
        super(TYPE_ID, "Concatenate", x, y);
        addInput("A", WPin.DataType.STRING, 0xFFFF0000);
        addInput("B", WPin.DataType.STRING, 0xFF0000FF);
        addOutput("text",WPin.DataType.STRING ,0xFF00FF00);

        addElement(new WLabel("A + B = AB"));

        setEvaluator(n -> {
            String concatedString = n.getInputs().get(0).getStringValue() + n.getInputs().get(1).getStringValue();
            n.getOutputs().get(0).setStringValue(concatedString);
        });
    }
}