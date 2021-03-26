package de.unileipzig.irpact.experimental;

import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.start.Start;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demo1 {

    @Test
    void runDemo1() throws IOException {
        Path demo1 = TestFiles.testfiles.resolve("demo1");
        Path in1 = demo1.resolve("input_wrong.json");
        Path out1 = demo1.resolve("output_wrong.json");

        Start.main(new String[] {
                "--i", in1.toString(),
                "--o", out1.toString()
        });

        //===

        Path in2 = demo1.resolve("input.json");
        Path out2 = demo1.resolve("output.json");

        Start.main(new String[] {
                "--i", in2.toString(),
                "--o", out2.toString()
        });
    }
}
