package de.unileipzig.irpact.disabled;

import org.junit.jupiter.api.Disabled;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestFiles {

    public static final Path root = Paths.get("");
    public static final Path testfiles = root.resolve("testfiles");
    public static final Path demo1 = testfiles.resolve("demo1");
    public static final Path toolsdemo = testfiles.resolve("toolsdemo");
    public static final Path scenarios = root.resolve(Paths.get("src", "main", "resources", "scenarios"));
}