package de.unileipzig.irpact.experimental;

import org.junit.jupiter.api.Disabled;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestFiles {

    //wurzelverzeichnis, hinweis: funktioniert in intellij
    public static final Path root = Paths.get("").toAbsolutePath();

    //testfiles
    public static final Path experimental = root.resolve("experimental");
    public static final Path demo1 = experimental.resolve("demo1");
    public static final Path toolsdemo = experimental.resolve("toolsdemo");
    public static final Path irpact = experimental.resolve("irpact");
    public static final Path irpact2 = experimental.resolve("irpact2");

    //private testfiles - allgemein grosse dateien und backups
    public static final Path exppriv = root.resolve("exppriv");
    public static final Path gis = exppriv.resolve("gis");

    public static final Path resources = root.resolve(Paths.get("src", "main", "resources"));
    public static final Path scenarios = resources.resolve("scenarios");

    public static final Path testResources = root.resolve(Paths.get("src", "test", "resources"));

    //div
    public static final Path java = Paths.get("C:\\MyProgs\\Java\\jdk-11.0.2\\bin", "java.exe");
    public static final Path cljlibs = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPtools\\cljlibs");
    public static final Path frontendGeneratorJar = cljlibs.resolve("frontend-generator.jar");

    public static Path resolve(Path path, String first, String... more) {
        Path other = Paths.get(first, more);
        return path.resolve(other);
    }
}