package dev.propulsionteam.computed.content.nodes.widgets;

import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.WPin;
import dev.devce.websnodelib.api.elements.WLabel;
import dev.devce.websnodelib.api.elements.WTextField;

/**
 * Emits a 0xAARRGGBB color as a Number. Accepts a hex code such as {@code FF8800}, {@code #FF8800},
 * or {@code 0xFFFF8800}. Missing alpha defaults to fully opaque.
 */
public final class ColorSourceNode extends WNode {
    private final WTextField hexField = new WTextField(80);

    public ColorSourceNode(int x, int y) {
        super(WidgetNodeIds.COLOR_SOURCE, "Color", x, y);
        hexField.setValue("FFFFFF");
        addOutput("Color", WPin.DataType.NUMBER, 0xFFFF66AA);
        addElement(new WLabel("Hex (AARRGGBB or RRGGBB)", 0xFFAAAAAA));
        addElement(hexField);
        setEvaluator(n -> n.getOutputs().get(0).setValue(parseColor(hexField.getValue())));
    }

    private static double parseColor(String raw) {
        if (raw == null) return 0xFFFFFFFFL;
        String s = raw.trim();
        if (s.startsWith("#")) s = s.substring(1);
        if (s.startsWith("0x") || s.startsWith("0X")) s = s.substring(2);
        if (s.isEmpty()) return 0xFFFFFFFFL;
        try {
            long v = Long.parseLong(s, 16);
            if (s.length() <= 6) v |= 0xFF000000L;
            return (double) (int) v;
        } catch (NumberFormatException ignored) {
            return 0xFFFFFFFFL;
        }
    }
}
