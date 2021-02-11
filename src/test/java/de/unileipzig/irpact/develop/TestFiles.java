package de.unileipzig.irpact.develop;

import org.junit.jupiter.api.Disabled;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public final class TestFiles {

    public static final Path root = Paths.get("").toAbsolutePath();
    public static final Path testfiles = root.resolve("testfiles");
}
