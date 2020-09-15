package de.unileipzig.irpact.dev;

import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled("dev tests")
class Develop {

    @Test
    void doGams() {
        String[] args = {
                "--tools",
                "--createAllDefaults"
        };
        Start.main(args);
    }

    @Test
    void runMyTest() {
        String[] args = {
                "-i", Paths.get("/home/daniel/Stuff/Projects", "neue.json").toString(),
                "-o", Paths.get("/home/daniel/Stuff/Projects", "neue.out.json").toString()
        };
        Start.main(args);
    }
}
