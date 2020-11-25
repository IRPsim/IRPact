package de.unileipzig.irpact.dev.optact;

import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.Constants;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
class OptActMainTest {

    @Test
    void runIt() throws IOException {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("exppriv");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runImage() throws IOException {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("exppriv");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.json").toString(),
                "--image", outDir.resolve("testimage.png").toString(),
                "--noSimulation"
        };
        Start.main(args);
    }

    @Test
    void createFiles() {

    }
}