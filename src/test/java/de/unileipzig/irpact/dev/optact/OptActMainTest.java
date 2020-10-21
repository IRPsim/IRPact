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
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", dir.resolve("default.out.json").toString()
        };
        Start.main(args);
    }
}