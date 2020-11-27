package de.unileipzig.irpact.experimental;

import org.junit.jupiter.api.Disabled;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestFiles {

    public static final Path root = Paths.get("").toAbsolutePath();

    public static final Path testfiles = root.resolve("testfiles");
    public static final Path fasttests = testfiles.resolve("fasttests");
    public static final Path gis = testfiles.resolve("gis");
    public static final Path resources = testfiles.resolve("resources");

    //spezielles
    public static final Path java11 = Paths.get("C:\\MyProgs\\Java\\jdk-11.0.2\\bin", "java.exe");
    public static final Path IRPTools = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPtools");
    public static final Path cljlibs = IRPTools.resolve("cljlibs");
    public static final Path frontendGeneratorJar = cljlibs.resolve("frontend-generator.jar");
    public static final Path backendGeneratorJar = cljlibs.resolve("backend-generator.jar");
}