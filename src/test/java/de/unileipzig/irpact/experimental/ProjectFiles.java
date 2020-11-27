package de.unileipzig.irpact.experimental;

import org.junit.jupiter.api.Disabled;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class ProjectFiles {

    public static final Path root = TestFiles.root;

    public static final Path src = root.resolve("src");
    public static final Path resources = src.resolve(Paths.get("main", "resources"));
    public static final Path input = resources.resolve("input");
    public static final Path output = resources.resolve("output");
    public static final Path scenarios = resources.resolve("scenarios");
    public static final Path inputGms = input.resolve("input.gms");
    public static final Path uiInputEdn = input.resolve("ui-input.edn");
    public static final Path uiInputDeltaEdn = input.resolve("ui-input-delta.edn");
    public static final Path outputGms = output.resolve("output.gms");
    public static final Path uiOutputEdn = output.resolve("ui-output.edn");
    public static final Path defaultJson = scenarios.resolve("default.json");

    public static final Path test = src.resolve("test");
    public static final Path testResources = test.resolve("resources");
}
