package de.unileipzig.irpact.experimental.demos;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.input.InExample;
import de.unileipzig.irpact.io.input.InRoot;
import de.unileipzig.irpact.io.output.OutRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@Disabled
public class DemosX {

    @Test
    void runStartWithTools() {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "--irptools",
                "--inputRootClass", InRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", Demo1.class.getName(),
                "--outDir", dir.toString(),
                "--charset", Util.windows1252().name(),
                "--validate",
                "--skipReference",
                "--dummyNomenklatur",
                "--pathToJava", TestFiles.java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", TestFiles.frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", TestFiles.backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString(),
                "--sortAfterPriority"
        };
        Start.main(args);
    }
    @Test
    void runToSpec() {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default.json").toString(),
                "--paramToSpec", dir.resolve("spec").toString()
        };
        Start.main(args);
    }
    @Test
    void runToParam() {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String[] args = {
                "--specToParam", dir.resolve("spec").toString(),
                "-o", dir.resolve("scenarios").resolve("default.spec.json").toString()
        };
        Start.main(args);
    }

    @Test
    void wtf() throws IOException {
        Path dir = TestFiles.testfiles.resolve("demos").resolve("demo1");
        String content0 = Util.readString(dir.resolve("scenarios").resolve("default.json"), StandardCharsets.UTF_8);
        String content1 = Util.readString(dir.resolve("scenarios").resolve("default.spec.json"), StandardCharsets.UTF_8);
        Assertions.assertEquals(content0, content1);
    }
}
