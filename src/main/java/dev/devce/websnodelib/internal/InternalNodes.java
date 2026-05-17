package dev.devce.websnodelib.internal;

import dev.devce.websnodelib.api.NodeMenuRegistry;
import dev.devce.websnodelib.api.NodeRegistry;
import dev.devce.websnodelib.api.CounterNode;
import dev.devce.websnodelib.api.FunctionCardNode;
import dev.devce.websnodelib.api.FunctionEndNode;
import dev.devce.websnodelib.api.FunctionStartNode;
import dev.devce.websnodelib.api.MuxNode;
import dev.devce.websnodelib.api.PassOnNthRisingEdgeNode;
import dev.devce.websnodelib.api.WGraph;
import dev.devce.websnodelib.api.WNode;
import dev.devce.websnodelib.api.elements.WLabel;
import dev.devce.websnodelib.api.elements.WSlider;
import dev.devce.websnodelib.api.elements.WTextField;
import dev.devce.websnodelib.api.elements.WViewport3D;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3f;

/** https://github.com/webyep-art/webs_node_lib (MIT, webyep). */
public final class InternalNodes {
    private static final ResourceLocation CAT_MATH = id("menu_math");
    private static final ResourceLocation CAT_MATH_BINARY = id("menu_math_binary");
    private static final ResourceLocation CAT_MATH_UNARY = id("menu_math_unary");
    private static final ResourceLocation CAT_MATH_TRIG = id("menu_math_trig");
    private static final ResourceLocation CAT_SOURCES = id("menu_sources");
    private static final ResourceLocation CAT_IO = id("menu_io");
    private static final ResourceLocation CAT_VISUALS = id("menu_visuals");
    private static final ResourceLocation CAT_ORGANIZATION = id("menu_organization");
    private static final ResourceLocation CAT_LOGIC = id("menu_logic");
    private static final ResourceLocation CAT_LOGIC_BINARY = id("menu_logic_binary");
    private static final ResourceLocation CAT_LOGIC_UNARY = id("menu_logic_unary");
    private static final ResourceLocation CAT_LOGIC_COMPARISON = id("menu_logic_comparison");
    private static final ResourceLocation CAT_LOGIC_MEMORY = id("menu_logic_memory");

    @FunctionalInterface
    private interface BinaryOp {
        double apply(double a, double b);
    }

    @FunctionalInterface
    private interface UnaryOp {
        double apply(double a);
    }

    @FunctionalInterface
    private interface LogicBinaryOp {
        boolean apply(boolean a, boolean b);
    }

    @FunctionalInterface
    private interface CompareOp {
        boolean apply(double a, double b);
    }

    private InternalNodes() {}

    public static void register() {
        registerMenuCategories();
        registerMathNodes();
        registerLogicNodes();
        registerOtherNodes();
        registerExtendedNodes();
        registerFunctionNodes();
        registerMenuEntries();
    }

    /**
     * Function cards and inner boundaries are not in the add-node menu; cards are placed from the computer
     * schematic picker. Start/end types deserialize inside saved function bodies.
     */
    private static void registerFunctionNodes() {
        NodeRegistry.register(FunctionStartNode.TYPE_FN_START, FunctionStartNode::new);
        NodeRegistry.register(FunctionEndNode.TYPE_FN_END, FunctionEndNode::new);
        NodeRegistry.register(FunctionCardNode.TYPE_FUNCTION_CARD, FunctionCardNode::new);
        NodeMenuRegistry.hideFromAddMenu(FunctionStartNode.TYPE_FN_START);
        NodeMenuRegistry.hideFromAddMenu(FunctionEndNode.TYPE_FN_END);
        NodeMenuRegistry.hideFromAddMenu(FunctionCardNode.TYPE_FUNCTION_CARD);
    }

    private static void registerMenuCategories() {
        NodeMenuRegistry.registerCategory(CAT_MATH, Component.literal("Math"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_MATH_BINARY, Component.literal("Binary"), CAT_MATH);
        NodeMenuRegistry.registerCategory(CAT_MATH_UNARY, Component.literal("Unary & rounding"), CAT_MATH);
        NodeMenuRegistry.registerCategory(CAT_MATH_TRIG, Component.literal("Trig"), CAT_MATH);
        NodeMenuRegistry.registerCategory(CAT_SOURCES, Component.literal("Sources"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_IO, Component.literal("I/O"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_VISUALS, Component.literal("Visuals"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_ORGANIZATION, Component.literal("Organization"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_LOGIC, Component.literal("Logic"), NodeMenuRegistry.ROOT);
        NodeMenuRegistry.registerCategory(CAT_LOGIC_BINARY, Component.literal("Binary"), CAT_LOGIC);
        NodeMenuRegistry.registerCategory(CAT_LOGIC_UNARY, Component.literal("Unary"), CAT_LOGIC);
        NodeMenuRegistry.registerCategory(CAT_LOGIC_COMPARISON, Component.literal("Comparison"), CAT_LOGIC);
        NodeMenuRegistry.registerCategory(CAT_LOGIC_MEMORY, Component.literal("Memory"), CAT_LOGIC);
    }

    private static void registerMathNodes() {
        registerBinary("math_add", "Add", (a, b) -> a + b);
        registerBinary("math_subtract", "Subtract", (a, b) -> a - b);
        registerBinary("math_multiply", "Multiply", (a, b) -> a * b);
        registerBinary("math_divide", "Divide", (a, b) -> b != 0 ? a / b : 0);
        registerBinary("math_mod", "Modulo", (a, b) -> b != 0 ? a % b : 0);
        registerBinary("math_min", "Min", Math::min);
        registerBinary("math_max", "Max", Math::max);
        registerBinary("math_pow", "Power", Math::pow);
        registerBinary("math_atan2", "Atan2", Math::atan2);

        registerUnary("math_abs", "Abs", Math::abs);
        registerUnary("math_sqrt", "Sqrt", a -> Math.sqrt(Math.max(0, a)));
        registerUnary("math_floor", "Floor", a -> Math.floor(a));
        registerUnary("math_ceil", "Ceil", a -> Math.ceil(a));
        registerUnary("math_round", "Round", a -> Math.rint(a));
        registerUnary("math_sin", "Sin", a -> Math.sin(a));
        registerUnary("math_cos", "Cos", a -> Math.cos(a));
        registerUnary("math_tan", "Tan", a -> Math.tan(a));
        registerUnary("math_negate", "Negate", a -> -a);
        registerUnary("math_log", "Log (ln)", a -> a > 0 ? Math.log(a) : 0);
        registerUnary("math_log10", "Log10", a -> a > 0 ? Math.log10(a) : 0);
        registerUnary("math_exp", "Exp", a -> Math.exp(a));
        registerUnary("math_sign", "Sign", a -> Math.signum(a));

        registerRandom();
    }

    private static void registerLogicNodes() {
        registerLogicBinary("logic_and", "AND", (a, b) -> a && b);
        registerLogicBinary("logic_or", "OR", (a, b) -> a || b);
        registerLogicBinary("logic_xor", "XOR", (a, b) -> a ^ b);
        registerLogicBinary("logic_nand", "NAND", (a, b) -> !(a && b));
        registerLogicBinary("logic_nor", "NOR", (a, b) -> !(a || b));
        registerLogicBinary("logic_xnor", "XNOR", (a, b) -> !(a ^ b));

        ResourceLocation notId = id("logic_not");
        NodeRegistry.register(notId, (x, y) -> {
            WNode node = new WNode(notId, "NOT", x, y);
            node.addInput("A", 0xFF00FF88);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("NOT"));
            node.setEvaluator(
                    n -> n.getOutputs().get(0).setValue(n.getInputs().get(0).getValue() > 0.5 ? 0.0 : 1.0));
            return node;
        });

        registerCompare("cmp_eq", "=", "A = B", (a, b) -> a == b);
        registerCompare("cmp_gt", ">", "A > B", (a, b) -> a > b);
        registerCompare("cmp_lt", "<", "A < B", (a, b) -> a < b);
        registerCompare("cmp_ge", ">=", "A >= B", (a, b) -> a >= b);
        registerCompare("cmp_le", "<=", "A <= B", (a, b) -> a <= b);

        ResourceLocation approxId = id("cmp_approx");
        NodeRegistry.register(approxId, (x, y) -> {
            WNode node = new WNode(approxId, "~=", x, y);
            node.addInput("A", 0xFF00FF88);
            node.addInput("B", 0xFF88CCFF);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("A ~= B"));
            WSlider tolerance = new WSlider("Tolerance", 0.0, 15.0, 80);
            tolerance.setValue(0.5);
            node.addElement(tolerance);
            node.setEvaluator(n -> {
                double a = n.getInputs().get(0).getValue();
                double b = n.getInputs().get(1).getValue();
                double tol = tolerance.getValue();
                n.getOutputs().get(0).setValue(Math.abs(a - b) <= tol ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerExtendedNodes() {
        registerSrLatch();
        registerDFlipFlop();
        registerEdgeRise();
        registerEdgeFall();
        registerSchmitt();
        NodeRegistry.register(MuxNode.TYPE_ID, MuxNode::new);

        registerClamp();
        registerMap();
        registerLerp();
        registerAverage();

        registerDelay();
        registerSampleHold();

        registerBoolToLevel();
        registerLevelToBool();
        registerQuantizeRedstone();
    }

    private static void registerSrLatch() {
        ResourceLocation nid = id("sr_latch");
        NodeRegistry.register(nid, (x, y) -> {
            boolean[] q = {false};
            boolean[] prevSet = {false};
            boolean[] prevReset = {false};
            WNode node = new WNode(nid, "SR Latch", x, y) {
                @Override
                public CompoundTag save() {
                    CompoundTag tag = super.save();
                    tag.putBoolean("Q", q[0]);
                    return tag;
                }

                @Override
                public void load(CompoundTag tag) {
                    super.load(tag);
                    if (tag.contains("Q")) q[0] = tag.getBoolean("Q");
                }
            };
            node.addInput("Set", 0xFF00FF88);
            node.addInput("Reset", 0xFFFF6666);
            node.addOutput("Q", 0xFFFF5555);
            node.addElement(new WLabel("SR Latch (rising edges)"));
            node.setEvaluator(n -> {
                boolean s = n.getInputs().get(0).getValue() > 0.5;
                boolean r = n.getInputs().get(1).getValue() > 0.5;
                if (r && !prevReset[0]) q[0] = false;
                else if (s && !prevSet[0]) q[0] = true;
                prevSet[0] = s;
                prevReset[0] = r;
                n.getOutputs().get(0).setValue(q[0] ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerDFlipFlop() {
        ResourceLocation nid = id("d_flipflop");
        NodeRegistry.register(nid, (x, y) -> {
            boolean[] q = {false};
            boolean[] prevClock = {false};
            WNode node = new WNode(nid, "D Flip-flop", x, y) {
                @Override
                public CompoundTag save() {
                    CompoundTag tag = super.save();
                    tag.putBoolean("Q", q[0]);
                    return tag;
                }

                @Override
                public void load(CompoundTag tag) {
                    super.load(tag);
                    if (tag.contains("Q")) q[0] = tag.getBoolean("Q");
                }
            };
            node.addInput("Data", 0xFF88CCFF);
            node.addInput("Clock", 0xFF00FF88);
            node.addOutput("Q", 0xFFFF5555);
            node.addElement(new WLabel("Latches Data on Clock rise"));
            node.setEvaluator(n -> {
                boolean d = n.getInputs().get(0).getValue() > 0.5;
                boolean c = n.getInputs().get(1).getValue() > 0.5;
                if (c && !prevClock[0]) q[0] = d;
                prevClock[0] = c;
                n.getOutputs().get(0).setValue(q[0] ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerEdgeRise() {
        ResourceLocation nid = id("edge_rise");
        NodeRegistry.register(nid, (x, y) -> {
            boolean[] prev = {false};
            WNode node = new WNode(nid, "Edge Rise", x, y);
            node.addInput("In", 0xFF88CCFF);
            node.addOutput("Pulse", 0xFF00FF88);
            node.addElement(new WLabel("1.0 for one eval on rising edge"));
            node.setEvaluator(n -> {
                boolean now = n.getInputs().get(0).getValue() > 0.5;
                boolean rise = now && !prev[0];
                prev[0] = now;
                n.getOutputs().get(0).setValue(rise ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerEdgeFall() {
        ResourceLocation nid = id("edge_fall");
        NodeRegistry.register(nid, (x, y) -> {
            boolean[] prev = {false};
            WNode node = new WNode(nid, "Edge Fall", x, y);
            node.addInput("In", 0xFF88CCFF);
            node.addOutput("Pulse", 0xFF00FF88);
            node.addElement(new WLabel("1.0 for one eval on falling edge"));
            node.setEvaluator(n -> {
                boolean now = n.getInputs().get(0).getValue() > 0.5;
                boolean fall = !now && prev[0];
                prev[0] = now;
                n.getOutputs().get(0).setValue(fall ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerSchmitt() {
        ResourceLocation nid = id("schmitt");
        NodeRegistry.register(nid, (x, y) -> {
            boolean[] on = {false};
            WSlider onThresh = new WSlider("On threshold", 0.0, 15.0, 80);
            onThresh.setValue(10.0);
            WSlider offThresh = new WSlider("Off threshold", 0.0, 15.0, 80);
            offThresh.setValue(5.0);
            WNode node = new WNode(nid, "Schmitt", x, y) {
                @Override
                public CompoundTag save() {
                    CompoundTag tag = super.save();
                    tag.putBoolean("On", on[0]);
                    return tag;
                }

                @Override
                public void load(CompoundTag tag) {
                    super.load(tag);
                    if (tag.contains("On")) on[0] = tag.getBoolean("On");
                }
            };
            node.addInput("In", 0xFF88CCFF);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("Hysteresis (on >= on, off <= off)"));
            node.addElement(onThresh);
            node.addElement(offThresh);
            node.setEvaluator(n -> {
                double v = n.getInputs().get(0).getValue();
                double hi = onThresh.getValue();
                double lo = offThresh.getValue();
                if (on[0]) {
                    if (v <= lo) on[0] = false;
                } else {
                    if (v >= hi) on[0] = true;
                }
                n.getOutputs().get(0).setValue(on[0] ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerClamp() {
        ResourceLocation nid = id("math_clamp");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Clamp", x, y);
            node.addInput("x", 0xFF88CCFF);
            node.addInput("Min", 0xFF00FF88);
            node.addInput("Max", 0xFFFF6666);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("clamp(x, min, max)"));
            node.setEvaluator(n -> {
                double v = n.getInputs().get(0).getValue();
                double a = n.getInputs().get(1).getValue();
                double b = n.getInputs().get(2).getValue();
                double lo = Math.min(a, b);
                double hi = Math.max(a, b);
                n.getOutputs().get(0).setValue(Mth.clamp(v, lo, hi));
            });
            return node;
        });
    }

    private static void registerMap() {
        ResourceLocation nid = id("math_map");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Map", x, y);
            node.addInput("x", 0xFF88CCFF);
            node.addInput("In min", 0xFF00FF88);
            node.addInput("In max", 0xFF00FF88);
            node.addInput("Out min", 0xFFFFBB00);
            node.addInput("Out max", 0xFFFFBB00);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("Linear remap"));
            node.setEvaluator(n -> {
                double x_ = n.getInputs().get(0).getValue();
                double i0 = n.getInputs().get(1).getValue();
                double i1 = n.getInputs().get(2).getValue();
                double o0 = n.getInputs().get(3).getValue();
                double o1 = n.getInputs().get(4).getValue();
                if (i1 == i0) {
                    n.getOutputs().get(0).setValue(o0);
                } else {
                    n.getOutputs().get(0).setValue(o0 + (x_ - i0) * (o1 - o0) / (i1 - i0));
                }
            });
            return node;
        });
    }

    private static void registerLerp() {
        ResourceLocation nid = id("math_lerp");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Lerp", x, y);
            node.addInput("A", 0xFF88CCFF);
            node.addInput("B", 0xFF88CCFF);
            node.addInput("T", 0xFF00FF88);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("A + (B - A) * T"));
            node.setEvaluator(n -> {
                double a = n.getInputs().get(0).getValue();
                double b = n.getInputs().get(1).getValue();
                double t = n.getInputs().get(2).getValue();
                n.getOutputs().get(0).setValue(a + (b - a) * t);
            });
            return node;
        });
    }

    private static void registerAverage() {
        ResourceLocation nid = id("math_average");
        NodeRegistry.register(nid, (x, y) -> {
            WSlider window = new WSlider("Window (ticks)", 1, 100, 80);
            window.setValue(20);
            double[][] buf = {new double[1]};
            int[] head = {0};
            int[] size = {0};
            int[] cap = {1};
            double[] sum = {0.0};
            WNode node = new WNode(nid, "Average", x, y);
            node.addInput("In", 0xFF88CCFF);
            node.addOutput("Mean", 0xFFFF5555);
            node.addElement(new WLabel("Windowed mean (per tick)"));
            node.addElement(window);
            node.setEvaluator(n -> {
                int w = Math.max(1, (int) window.getValue());
                if (w != cap[0]) {
                    buf[0] = new double[w];
                    head[0] = 0;
                    size[0] = 0;
                    sum[0] = 0.0;
                    cap[0] = w;
                }
                WGraph g = n.evaluationGraph();
                if (g != null && g.isEvalTickPulseGate()) {
                    double in = n.getInputs().get(0).getValue();
                    if (size[0] == cap[0]) {
                        sum[0] -= buf[0][head[0]];
                    } else {
                        size[0]++;
                    }
                    buf[0][head[0]] = in;
                    sum[0] += in;
                    head[0] = (head[0] + 1) % cap[0];
                }
                double mean = size[0] == 0 ? 0.0 : sum[0] / size[0];
                n.getOutputs().get(0).setValue(mean);
            });
            return node;
        });
    }

    private static void registerDelay() {
        ResourceLocation nid = id("delay");
        NodeRegistry.register(nid, (x, y) -> {
            WSlider delay = new WSlider("Delay (ticks)", 0, 200, 80);
            delay.setValue(1);
            double[][] buf = {new double[1]};
            int[] head = {0};
            int[] cap = {1};
            WNode node = new WNode(nid, "Delay", x, y);
            node.addInput("In", 0xFF88CCFF);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel("Delays input N ticks"));
            node.addElement(delay);
            node.setEvaluator(n -> {
                int d = Math.max(0, (int) delay.getValue());
                int needed = Math.max(1, d + 1);
                if (needed != cap[0]) {
                    buf[0] = new double[needed];
                    head[0] = 0;
                    cap[0] = needed;
                }
                WGraph g = n.evaluationGraph();
                if (g != null && g.isEvalTickPulseGate()) {
                    buf[0][head[0]] = n.getInputs().get(0).getValue();
                    head[0] = (head[0] + 1) % cap[0];
                }
                n.getOutputs().get(0).setValue(buf[0][head[0]]);
            });
            return node;
        });
    }

    private static void registerSampleHold() {
        ResourceLocation nid = id("sample_hold");
        NodeRegistry.register(nid, (x, y) -> {
            double[] held = {0.0};
            boolean[] prevClock = {false};
            WNode node = new WNode(nid, "Sample & Hold", x, y) {
                @Override
                public CompoundTag save() {
                    CompoundTag tag = super.save();
                    tag.putDouble("Held", held[0]);
                    return tag;
                }

                @Override
                public void load(CompoundTag tag) {
                    super.load(tag);
                    if (tag.contains("Held")) held[0] = tag.getDouble("Held");
                }
            };
            node.addInput("In", 0xFF88CCFF);
            node.addInput("Clock", 0xFF00FF88);
            node.addOutput("Held", 0xFFFF5555);
            node.addElement(new WLabel("Captures In on Clock rise"));
            node.setEvaluator(n -> {
                boolean c = n.getInputs().get(1).getValue() > 0.5;
                if (c && !prevClock[0]) {
                    held[0] = n.getInputs().get(0).getValue();
                }
                prevClock[0] = c;
                n.getOutputs().get(0).setValue(held[0]);
            });
            return node;
        });
    }

    private static void registerBoolToLevel() {
        ResourceLocation nid = id("bool_to_level");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Bool -> Level", x, y);
            node.addInput("In", 0xFF00FF88);
            node.addOutput("Level", 0xFFFFBB00);
            node.addElement(new WLabel("> 0.5 -> 15, else 0"));
            node.setEvaluator(n ->
                    n.getOutputs().get(0).setValue(n.getInputs().get(0).getValue() > 0.5 ? 15.0 : 0.0));
            return node;
        });
    }

    private static void registerLevelToBool() {
        ResourceLocation nid = id("level_to_bool");
        NodeRegistry.register(nid, (x, y) -> {
            WSlider thresh = new WSlider("Threshold", 0, 15, 80);
            thresh.setValue(8);
            WNode node = new WNode(nid, "Level -> Bool", x, y);
            node.addInput("Level", 0xFFFFBB00);
            node.addOutput("Bool", 0xFF00FF88);
            node.addElement(new WLabel("1.0 if Level >= threshold"));
            node.addElement(thresh);
            node.setEvaluator(n -> n.getOutputs().get(0).setValue(
                    n.getInputs().get(0).getValue() >= thresh.getValue() ? 1.0 : 0.0));
            return node;
        });
    }

    private static void registerQuantizeRedstone() {
        ResourceLocation nid = id("quantize_redstone");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Quantize 0-15", x, y);
            node.addInput("x", 0xFF88CCFF);
            node.addOutput("Level", 0xFFFFBB00);
            node.addElement(new WLabel("round + clamp to 0-15"));
            node.setEvaluator(n -> {
                double v = n.getInputs().get(0).getValue();
                int q = Mth.clamp((int) Math.round(v), 0, 15);
                n.getOutputs().get(0).setValue(q);
            });
            return node;
        });
    }

    private static void registerCompare(String path, String title, String label, CompareOp op) {
        ResourceLocation nid = id(path);
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, title, x, y);
            node.addInput("A", 0xFF00FF88);
            node.addInput("B", 0xFF88CCFF);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel(label));
            node.setEvaluator(n -> {
                double a = n.getInputs().get(0).getValue();
                double b = n.getInputs().get(1).getValue();
                n.getOutputs().get(0).setValue(op.apply(a, b) ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerLogicBinary(String path, String title, LogicBinaryOp op) {
        ResourceLocation nid = id(path);
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, title, x, y);
            node.addInput("A", 0xFF00FF88);
            node.addInput("B", 0xFF88CCFF);
            node.addOutput("Out", 0xFFFF5555);
            node.addElement(new WLabel(title));
            node.setEvaluator(n -> {
                boolean a = n.getInputs().get(0).getValue() > 0.5;
                boolean b = n.getInputs().get(1).getValue() > 0.5;
                n.getOutputs().get(0).setValue(op.apply(a, b) ? 1.0 : 0.0);
            });
            return node;
        });
    }

    private static void registerRandom() {
        ResourceLocation nid = id("math_random");
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, "Random", x, y);
            node.addOutput("Result", 0xFFFFAA00);
            WTextField minF = new WTextField(88);
            WTextField maxF = new WTextField(88);
            minF.setValue("0");
            maxF.setValue("1");
            node.addElement(new WLabel("Uniform in [min, max]"));
            node.addElement(new WLabel("Min"));
            node.addElement(minF);
            node.addElement(new WLabel("Max"));
            node.addElement(maxF);
            node.setEvaluator(n -> {
                double a = parseLooseDouble(minF, 0.0);
                double b = parseLooseDouble(maxF, 1.0);
                double lo = Math.min(a, b);
                double hi = Math.max(a, b);
                double span = hi - lo;
                double r =
                        lo
                                + (span > 0
                                        ? java.util.concurrent.ThreadLocalRandom.current().nextDouble() * span
                                        : 0.0);
                n.getOutputs().get(0).setValue(r);
            });
            return node;
        });
    }

    private static double parseLooseDouble(WTextField field, double fallback) {
        try {
            String s = field.getValue().trim().replace(',', '.');
            if (s.isEmpty()) {
                return fallback;
            }
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            return fallback;
        }
    }

    private static void registerBinary(String path, String title, BinaryOp op) {
        ResourceLocation nid = id(path);
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, title, x, y);
            node.addInput("A", 0xFF00FF88);
            node.addInput("B", 0xFF00FF88);
            node.addOutput("Result", 0xFFFF5555);
            node.addElement(new WLabel(title));
            node.setEvaluator(n -> {
                double a = n.getInputs().get(0).getValue();
                double b = n.getInputs().get(1).getValue();
                n.getOutputs().get(0).setValue(op.apply(a, b));
            });
            return node;
        });
    }

    private static void registerUnary(String path, String title, UnaryOp op) {
        ResourceLocation nid = id(path);
        NodeRegistry.register(nid, (x, y) -> {
            WNode node = new WNode(nid, title, x, y);
            node.addInput("A", 0xFF00FF88);
            node.addOutput("Result", 0xFFFF5555);
            node.addElement(new WLabel(title));
            node.setEvaluator(
                    n -> n.getOutputs().get(0).setValue(op.apply(n.getInputs().get(0).getValue())));
            return node;
        });
    }

    private static void registerOtherNodes() {
        NodeRegistry.register(id("display"), (x, y) -> {
            WNode node = new WNode(id("display"), "Display", x, y);
            node.addInput("Value", 0xFF5555FF);

            WLabel valLabel = new WLabel("0.00", 0xFF00FF88);
            node.addElement(new WLabel("Current value:"));
            node.addElement(valLabel);

            node.setEvaluator(n -> {
                double val = n.getInputs().get(0).getValue();
                valLabel.setText(String.format("%.2f", val));
            });

            return node;
        });

        NodeRegistry.register(id("constant"), (x, y) -> {
            WNode node = new WNode(id("constant"), "Constant", x, y);
            node.addOutput("Value", 0xFFFFBB00);

            WTextField valField = new WTextField(60);
            valField.setValue("10.0");
            node.addElement(new WLabel("Value:"));
            node.addElement(valField);

            node.setEvaluator(n -> {
                try {
                    n.getOutputs().get(0).setValue(Double.parseDouble(valField.getValue()));
                } catch (Exception e) {
                    // ignore parse errors
                }
            });

            return node;
        });

        NodeRegistry.register(id("oscillator"), (x, y) -> {
            WNode node = new WNode(id("oscillator"), "Oscillator", x, y);
            node.addOutput("Wave", 0xFF00FFFF);

            WSlider freqSlider = new WSlider("Freq", 0.1, 5.0, 80);
            WSlider ampSlider = new WSlider("Amp", 1.0, 100.0, 80);
            node.addElement(new WLabel("Sine Wave Generator"));
            node.addElement(freqSlider);
            node.addElement(ampSlider);

            node.setEvaluator(n -> {
                double time = System.currentTimeMillis() / 1000.0;
                double val = Math.sin(time * freqSlider.getValue() * Math.PI * 2) * ampSlider.getValue();
                n.getOutputs().get(0).setValue(val);
            });

            return node;
        });

        NodeRegistry.register(WGraph.TICK_NODE_TYPE, (x, y) -> {
            WNode node = new WNode(WGraph.TICK_NODE_TYPE, "Tick", x, y);
            node.addOutput("Tick", 0xFF00FF88);
            node.addOutput("Delta time", 0xFF88CCFF);
            WSlider rate = new WSlider("Rate", 0, WGraph.MAX_TICK_RATE, 100);
            rate.setValue(WGraph.MAX_TICK_RATE);
            node.addElement(new WLabel("Graph clock"));
            node.addElement(rate);
            node.addElement(new WLabel("Rate: updates per second (0 = pause)"));
            node.setEvaluator(n -> {});
            return node;
        });

        NodeRegistry.register(id("pulse"), (x, y) -> {
            WNode node = new WNode(id("pulse"), "Pulse", x, y);
            node.addOutput("Tick", 0xFF00FF88);
            WSlider cooldown = new WSlider("Cooldown (ticks)", 1, 20, 100);
            cooldown.setValue(20);
            node.addElement(new WLabel("Pulses 1.0 every N ticks"));
            node.addElement(cooldown);
            int[] phase = {0};
            node.setEvaluator(n -> {
                int cd = (int) cooldown.getValue();
                if (cd <= 0) {
                    n.getOutputs().get(0).setValue(1.0);
                    phase[0] = 0;
                    return;
                }
                WGraph g = n.evaluationGraph();
                if (g != null && g.isEvalTickPulseGate()) {
                    phase[0] = (phase[0] + 1) % (2 * cd);
                }
                n.getOutputs().get(0).setValue(phase[0] < cd ? 1.0 : 0.0);
            });
            return node;
        });

        NodeRegistry.register(CounterNode.TYPE_ID, CounterNode::new);
        NodeRegistry.register(PassOnNthRisingEdgeNode.TYPE_ID, PassOnNthRisingEdgeNode::new);

        NodeRegistry.register(id("3d_preview"), (x, y) -> {
            WNode node = new WNode(id("3d_preview"), "3D Viewport", x, y);
            node.setWidth(150);

            WViewport3D viewport = new WViewport3D(140, 100);
            WSlider rotX = new WSlider("Rot X", 0, 360, 130);
            WSlider rotY = new WSlider("Rot Y", 0, 360, 130);

            ItemStack stack = new ItemStack(net.minecraft.world.level.block.Blocks.DIAMOND_BLOCK);
            viewport.addModel(stack, new Vector3f(0, 0, 0), new Vector3f(0, 0, 0), 1.0f);

            node.addElement(viewport);
            node.addElement(rotX);
            node.addElement(rotY);

            node.setEvaluator(n -> {
                if (!viewport.getModels().isEmpty()) {
                    viewport.getModels().get(0).rot.x = (float) rotX.getValue();
                    viewport.getModels().get(0).rot.y = (float) rotY.getValue();
                }
            });

            return node;
        });

        NodeRegistry.register(id("rgb_preview"), (x, y) -> {
            WNode node = new WNode(id("rgb_preview"), "RGB Preview", x, y);
            node.addInput("R", 0xFFFF0000);
            node.addInput("G", 0xFF00FF00);
            node.addInput("B", 0xFF0000FF);

            node.addElement(new WLabel("Color Result:"));
            node.addElement(new dev.devce.websnodelib.api.WElement() {
                {
                    this.width = 60;
                    this.height = 30;
                }

                @Override
                public void render(net.minecraft.client.gui.GuiGraphics g, int x, int y, int mx, int my, float pt) {
                    int r = (int) net.minecraft.util.Mth.clamp(node.getInputs().get(0).getValue(), 0, 255);
                    int g1 = (int) net.minecraft.util.Mth.clamp(node.getInputs().get(1).getValue(), 0, 255);
                    int b = (int) net.minecraft.util.Mth.clamp(node.getInputs().get(2).getValue(), 0, 255);
                    g.fill(x, y, x + width, y + height, 0xFF000000 | (r << 16) | (g1 << 8) | b);
                    g.renderOutline(x, y, width, height, 0xFFFFFFFF);
                }
            });

            return node;
        });
    }

    private static void registerMenuEntries() {
        add(CAT_MATH_BINARY, "math_add", "Add");
        add(CAT_MATH_BINARY, "math_subtract", "Subtract");
        add(CAT_MATH_BINARY, "math_multiply", "Multiply");
        add(CAT_MATH_BINARY, "math_divide", "Divide");
        add(CAT_MATH_BINARY, "math_mod", "Modulo");
        add(CAT_MATH_BINARY, "math_min", "Min");
        add(CAT_MATH_BINARY, "math_max", "Max");
        add(CAT_MATH_BINARY, "math_pow", "Power");

        add(CAT_MATH_UNARY, "math_abs", "Abs");
        add(CAT_MATH_UNARY, "math_sqrt", "Sqrt");
        add(CAT_MATH_UNARY, "math_floor", "Floor");
        add(CAT_MATH_UNARY, "math_ceil", "Ceil");
        add(CAT_MATH_UNARY, "math_round", "Round");
        add(CAT_MATH_UNARY, "math_negate", "Negate");
        add(CAT_MATH_UNARY, "math_log", "Log (ln)");
        add(CAT_MATH_UNARY, "math_log10", "Log10");
        add(CAT_MATH_UNARY, "math_exp", "Exp");
        add(CAT_MATH_UNARY, "math_sign", "Sign");
        add(CAT_MATH_UNARY, "math_random", "Random");

        add(CAT_MATH_TRIG, "math_sin", "Sin");
        add(CAT_MATH_TRIG, "math_cos", "Cos");
        add(CAT_MATH_TRIG, "math_tan", "Tan");
        add(CAT_MATH_TRIG, "math_atan2", "Atan2");

        add(CAT_SOURCES, "constant", "Constant");
        add(CAT_SOURCES, "tick", "Tick");
        add(CAT_SOURCES, "pulse", "Pulse");
        add(CAT_SOURCES, "oscillator", "Oscillator");
        add(CAT_SOURCES, "counter", "Counter");
        add(CAT_SOURCES, "pass_every_n", "Pass on Nth rise");

        add(CAT_IO, "display", "Display");

        add(CAT_VISUALS, "3d_preview", "3D Viewport");
        add(CAT_VISUALS, "rgb_preview", "RGB Preview");
        add(CAT_ORGANIZATION, "tool_section", "Section");

        add(CAT_LOGIC_UNARY, "logic_not", "NOT");
        add(CAT_LOGIC_BINARY, "logic_and", "AND");
        add(CAT_LOGIC_BINARY, "logic_or", "OR");
        add(CAT_LOGIC_BINARY, "logic_xor", "XOR");
        add(CAT_LOGIC_BINARY, "logic_nand", "NAND");
        add(CAT_LOGIC_BINARY, "logic_nor", "NOR");
        add(CAT_LOGIC_BINARY, "logic_xnor", "XNOR");

        add(CAT_LOGIC_COMPARISON, "cmp_eq", "=");
        add(CAT_LOGIC_COMPARISON, "cmp_gt", ">");
        add(CAT_LOGIC_COMPARISON, "cmp_lt", "<");
        add(CAT_LOGIC_COMPARISON, "cmp_ge", ">=");
        add(CAT_LOGIC_COMPARISON, "cmp_le", "<=");
        add(CAT_LOGIC_COMPARISON, "cmp_approx", "~=");

        add(CAT_LOGIC_BINARY, "edge_rise", "Edge Rise");
        add(CAT_LOGIC_BINARY, "edge_fall", "Edge Fall");
        add(CAT_LOGIC_BINARY, "schmitt", "Schmitt");
        add(CAT_LOGIC_BINARY, "mux", "MUX");

        add(CAT_LOGIC_MEMORY, "sr_latch", "SR Latch");
        add(CAT_LOGIC_MEMORY, "d_flipflop", "D Flip-flop");

        add(CAT_MATH_BINARY, "math_clamp", "Clamp");
        add(CAT_MATH_BINARY, "math_map", "Map");
        add(CAT_MATH_BINARY, "math_lerp", "Lerp");
        add(CAT_MATH_UNARY, "math_average", "Average");
        add(CAT_MATH_UNARY, "quantize_redstone", "Quantize 0-15");

        add(CAT_SOURCES, "delay", "Delay");
        add(CAT_SOURCES, "sample_hold", "Sample & Hold");

        add(CAT_IO, "bool_to_level", "Bool -> Level");
        add(CAT_IO, "level_to_bool", "Level -> Bool");
    }

    private static void add(ResourceLocation category, String nodePath, String label) {
        NodeMenuRegistry.addNodeEntry(category, id(nodePath), Component.literal(label));
    }

    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath("websnodelib", path);
    }
}
