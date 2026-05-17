package dev.propulsionteam.computed.content.nodes.widgets;

/**
 * Implemented by widget nodes that receive user input from a monitor click.
 * The widget id used for hit-testing is the node's own UUID, so the click handler can dispatch
 * directly by looking up the node and casting.
 */
public interface InteractiveWidgetNode {
    /**
     * Called on the server when the player clicks the widget on a monitor.
     * @param value normalized payload; for buttons the value is ignored (1.0 by convention); for sliders
     *              it is the click's local fraction along the bar in [0, 1].
     */
    void onWidgetInput(double value);
}
