package dev.propulsionteam.computed.customnodes;

import dev.propulsionteam.computed.Computed;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.neoforged.fml.loading.FMLPaths;

public final class ComputedCustomNodes {
    private static final CustomNodeRegistrar REGISTRAR = new CustomNodeRegistrar();

    private ComputedCustomNodes() {}

    public static Path rootPath() {
        return FMLPaths.GAMEDIR.get().resolve("config").resolve(Computed.MODID).resolve("nodes");
    }

    /** Reads the raw JSON of every definition file under {@link #rootPath()} (server side, for sync to clients). */
    public static List<String> readRawDefinitions() {
        Path root = rootPath();
        List<String> out = new ArrayList<>();
        if (!Files.isDirectory(root)) {
            return out;
        }
        try (var stream = Files.walk(root)) {
            stream.filter(Files::isRegularFile)
                    .filter(p -> p.getFileName().toString().toLowerCase(Locale.ROOT).endsWith(".json"))
                    .sorted()
                    .forEach(p -> {
                        try {
                            out.add(Files.readString(p));
                        } catch (IOException e) {
                            Computed.LOGGER.warn("[custom-nodes] could not read {} for sync", p, e);
                        }
                    });
        } catch (IOException e) {
            Computed.LOGGER.warn("[custom-nodes] could not walk {} for sync", root, e);
        }
        return out;
    }

    /** Replaces local custom-node registrations with the definitions received from the server. */
    public static CustomNodeRegistrar.ReloadSummary applyServerDefinitions(List<String> rawDefinitions) {
        CustomNodeLoader loader = new CustomNodeLoader();
        List<CustomNodeDefinition> defs = new ArrayList<>();
        for (String raw : rawDefinitions) {
            try {
                defs.add(loader.parseRaw(raw, "server-synced"));
            } catch (Exception e) {
                Computed.LOGGER.warn("[custom-nodes] skipping invalid synced definition: {}", e.getMessage());
            }
        }
        CustomNodeRegistrar.ReloadSummary summary = REGISTRAR.applyDefinitions(defs);
        Computed.LOGGER.info(
                "[custom-nodes] applied server definitions: loaded={}, skipped={}, errors={}",
                summary.loaded(), summary.skipped(), summary.errors());
        return summary;
    }

    public static CustomNodeRegistrar.ReloadSummary reload() {
        Path root = rootPath();
        try {
            Files.createDirectories(root);
        } catch (Exception e) {
            Computed.LOGGER.warn("Could not create custom node directory {}", root, e);
        }
        CustomNodeRegistrar.ReloadSummary summary = REGISTRAR.reload(root);
        for (String message : summary.messages()) {
            if (message.startsWith("ERROR ")) {
                Computed.LOGGER.error("[custom-nodes] {}", message.substring(6));
            } else if (message.startsWith("WARN ")) {
                Computed.LOGGER.warn("[custom-nodes] {}", message.substring(5));
            } else {
                Computed.LOGGER.info("[custom-nodes] {}", message);
            }
        }
        Computed.LOGGER.info(
                "[custom-nodes] reload complete: loaded={}, skipped={}, warnings={}, errors={}, root={}",
                summary.loaded(),
                summary.skipped(),
                summary.warnings(),
                summary.errors(),
                root);
        return summary;
    }
}
