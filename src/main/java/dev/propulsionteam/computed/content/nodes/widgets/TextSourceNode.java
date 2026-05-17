package dev.propulsionteam.computed.content.nodes.widgets;

import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.WPin;
import dev.devce.websnodelib.api.elements.WLabel;
import dev.devce.websnodelib.api.elements.WTextField;

public final class TextSourceNode extends WNode {
    private final WTextField textField;

    public TextSourceNode(int x, int y) {
        super(WidgetNodeIds.TEXT_SOURCE, "Text", x, y);
        addOutput("Text", WPin.DataType.STRING, WPin.COLOR_STRING_DEFAULT);
        textField = new WTextField(160);
        addElement(new WLabel("Text", 0xFFAAAAAA));
        addElement(textField);
        setEvaluator(n -> n.getOutputs().get(0).setStringValue(textField.getValue()));
    }
}
