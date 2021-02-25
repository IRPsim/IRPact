package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Demos {

    @Test
    void runStartWithTools() {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x5_task");
        String[] args = {
                "--irptools",
                "--inputRootClass", InRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", InExample.class.getName(),
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
    void runIRPactImageNoSim() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x5_task").resolve("scenarios");
        int id = 1;
        String[] args = {
                "-i", dir.resolve("default.json").toString(),
                "-o", dir.resolve("default.out." + id + ".json").toString(),
                "--image", dir.resolve("default.image." + id + ".png").toString(),
                "--noSimulation"
        };
        Start.main(args);
    }
    @Test
    void runIRPact() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x5_task").resolve("scenarios");
        int id = 1;
        String[] args = {
                "-i", dir.resolve("default.json").toString(),
                "-o", dir.resolve("default.out." + id + ".json").toString()
        };
        Start.main(args);
    }
    @Test
    void runToSpec() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x5_task").resolve("scenarios");
        String[] args = {
                "-i", dir.resolve("default.json").toString(),
                "--paramToSpec", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\spezifikation\\spec", "test").toString()
        };
        Start.main(args);
    }
    @Test
    void runToParam() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x5_task").resolve("scenarios");
        String[] args = {
                "--specToParam", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\spezifikation\\spec", "test").toString(),
                "-o", dir.resolve("default.outtest.json").toString()
        };
        Start.main(args);
    }
}
