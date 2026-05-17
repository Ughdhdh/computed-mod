package dev.propulsionteam.computed.content.nodes.widgets;

import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.WPin;
import dev.propulsionteam.computed.content.monitors.widgets.LayoutManagedWidget;
import dev.propulsionteam.computed.content.monitors.widgets.SliderWidget;
import net.minecraft.util.Mth;

public final class SliderWidgetNode extends WNode implements InteractiveWidgetNode {
    private volatile double normalized = 0.0;
    private final WidgetLayoutFields layout =
            new WidgetLayoutFields(0, 0, 80, 12, LayoutManagedWidget.Fit.AUTO);

    public SliderWidgetNode(int x, int y) {
        super(WidgetNodeIds.SLIDER_WIDGET, "Slider Widget", x, y);
        layout.addTo(this);
        addInput("Min", WPin.DataType.NUMBER, 0xFFFFCC44);
        addInput("Max", WPin.DataType.NUMBER, 0xFFFFCC44);
        addInput("Color", WPin.DataType.NUMBER, 0xFFFF66AA);
        addOutput("Widget", WPin.DataType.WIDGET, WPin.COLOR_WIDGET_DEFAULT);
        addOutput("Value", WPin.DataType.NUMBER, 0xFFFFCC44);
        setEvaluator(n -> {
            double min = n.getInputs().get(0).getValue();
            double max = n.getInputs().get(1).getValue();
            if (max < min) { double t = max; max = min; min = t; }
            int colorIn = (int) Math.round(n.getInputs().get(2).getValue());
            int color = colorIn == 0 ? 0xFF44AAFF : (colorIn | 0xFF000000);
            double v = min + Mth.clamp(normalized, 0.0, 1.0) * (max - min);
            n.getOutputs().get(0).setWidgetValue(
                    layout.wrap(new SliderWidget(n.getId(), layout.x(), layout.y(), layout.width(), layout.height(),
                            v, min, max, color)));
            n.getOutputs().get(1).setValue(v);
        });
    }

    @Override
    public void onWidgetInput(double value) {
        normalized = Mth.clamp(value, 0.0, 1.0);
    }

    @Override
    public net.minecraft.nbt.CompoundTag save() {
        net.minecraft.nbt.CompoundTag tag = super.save();
        tag.putDouble("Normalized", normalized);
        layout.saveTo(tag);
        return tag;
    }

    @Override
    public void load(net.minecraft.nbt.CompoundTag tag) {
        super.load(tag);
        if (tag.contains("Normalized")) normalized = tag.getDouble("Normalized");
        layout.loadFrom(tag);
    }
}
