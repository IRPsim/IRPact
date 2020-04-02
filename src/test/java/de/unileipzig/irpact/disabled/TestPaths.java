package de.unileipzig.irpact.disabled;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestPaths {

    public static final Path root = Paths.get("").toAbsolutePath();
    public static final Path src = root.resolve("src");
    public static final Path main = src.resolve("main");
    public static final Path resInput = main.resolve(
            Paths.get("resources", "irpact", "examples", "input")
    );
    public static final Path test = src.resolve("test");
    public static final Path tesResources = test.resolve("resources");
    public static final Path testInput = tesResources.resolve(
            Paths.get("irpact", "examples", "input")
    );

    @Test
    void asd() {
        System.out.println(resInput + " " + Files.exists(resInput));
        System.out.println(testInput + " " + Files.exists(testInput));
    }

    @Test
    void asd2() {
        Path p = Paths.get("../src/main/resources/irpact/examples/output/input.json");
        System.out.println(p);
        System.out.println(p.toAbsolutePath().normalize());
        System.out.println(Paths.get("").resolve(p).toAbsolutePath().normalize());
        System.out.println(Files.exists(p));
        System.out.println(Files.exists(p.toAbsolutePath()));
    }
}
