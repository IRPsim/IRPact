package de.unileipzig.irpact.develop;

import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled("dev tests")
class Develop {

    @Test
    void doGams() throws IOException {
        String[] args = {
                "--tools",
                "--createAllDefaults"
        };
        Start.main(args);
    }

    @Test
    void runMyTest() throws IOException {
        String[] args = {
                "-i", Paths.get("/home/daniel/Stuff/Projects", "neue.json").toString(),
                "-o", Paths.get("/home/daniel/Stuff/Projects", "neue.out.json").toString()
        };
        Start.main(args);
    }

    @Test
    void asd() {
        String str = "hällö";
        byte[] b = str.getBytes(StandardCharsets.UTF_8);
        String x = new String(b, StandardCharsets.ISO_8859_1);
        System.out.println(x);
    }
}
