package de.unileipzig.irpact.develop.starttest;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.develop.TestFiles;
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
@Disabled("TEMP")
class ScenarioTest {

    private static final Path root = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\newScenarios");
    private static final Path locDeFile = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpactdata", "loc_de.yaml");

    @Test
    void createIt() {
        IRPLogging.initConsole();
        Path dir = root.resolve("2021-04-19__optact");
        String[] args = {
                "--irptools",
                "--inputRootClass", InRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", InExample.class.getName(),
                "--outDir", dir.toString(),
                "--charset", Util.windows1252().name(),
                "--validate",
                "--skipReference",
                "--skipGamsIdentifier",
                "--dummyNomenklatur",
                "--pathToJava", TestFiles.java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", TestFiles.frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", TestFiles.backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString(),
                "--sortAfterPriority",
                "--pathToResourceFile", locDeFile.toString()
        };
        Start.main(args);
    }

    @Test
    void tryRunBuggyInput() {
        IRPLogging.initConsole();
        Path dir = Paths.get("E:\\Downloads\\0Skype\\329\\329\\model-1\\year-0");
        String[] args = {
                "-i", dir.resolve("input_0.json").toString(),
                "-o", dir.resolve("xxxout.json").toString()
        };
        Start.main(args);
    }
}
