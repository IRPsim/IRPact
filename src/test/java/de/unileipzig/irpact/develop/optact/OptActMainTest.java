package de.unileipzig.irpact.develop.optact;

import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
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

    private static final Path java11 = Paths.get("C:\\MyProgs\\Java\\jdk-11.0.2\\bin", "java.exe");
    private static final Path IRPTools = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPtools");
    private static final Path cljlibs = IRPTools.resolve("cljlibs");
    private static final Path frontendGeneratorJar = cljlibs.resolve("frontend-generator.jar");
    private static final Path backendGeneratorJar = cljlibs.resolve("backend-generator.jar");

    @Test
    void runIt() {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.noimage.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runImageNoSimu() {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.nosimu.json").toString(),
                "--image", outDir.resolve("testimage.png").toString(),
                "--noSimulation"
        };
        Start.main(args);
    }

    @Test
    void runImageWithSimu() {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.json").toString(),
                "--image", outDir.resolve("default.image.png").toString()
        };
        Start.main(args);
    }

    @Test
    void runDoNothing() {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.nope.json").toString(),
                "--noSimulation"
        };
        Start.main(args);
    }

    @Test
    void createPerennialAnnualTestFiles() throws IOException {
        Path defaultJson = Paths.get("src", "main", "resources", "scenarios", Constants.DEFAULT_JSON);
        Path outDir = TestFiles.testfiles.resolve("annualPerennial");

        PerennialFile defaultFile = PerennialFile.parse(defaultJson);

        AnnualFile annualFile = defaultFile.toAnnual();
        PerennialFile perennialFile2 = annualFile.toPerennial();
        perennialFile2.getDescription().copyFrom(defaultFile.getDescription());

        Assertions.assertEquals(defaultFile.root(), perennialFile2.root());

        annualFile.store(outDir.resolve("default-annual.json"));
        perennialFile2.store(outDir.resolve("default-perennial.json"));
    }

    @Test
    void runImageWithSimu_perennial() {
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", TestFiles.testfiles.resolve("annualPerennial").resolve("default-perennial.json").toString(),
                "-o", outDir.resolve("default-perennial.out.json").toString(),
                "--image", outDir.resolve("default-perennial.image.png").toString()
        };
        Start.main(args);
    }

    @Test
    void runImageWithSimu_annual() {
        Path outDir = Paths.get("testfiles", "fasttests");
        String[] args = {
                "-i", TestFiles.testfiles.resolve("annualPerennial").resolve("default-annual.json").toString(),
                "-o", outDir.resolve("default-annual.out.json").toString(),
                "--image", outDir.resolve("default-annual.image.png").toString()
        };
        Start.main(args);
    }

    @Test
    void createFiles4GV() throws Exception {
        Logback.setupConsole();
        Path dir = TestFiles.testfiles.resolve("optact").resolve("gv");
        String[] args = {
                "--inputRootClass", GvInRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", GvInRoot.class.getName(),
                "--outDir", dir.toString(),
                "--charset", Util.windows1252().name(),
                //"--validate",
                "--skipReference",
                "--dummyNomenklatur",
                "--pathToJava", java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString()
        };
        IRPtools.start(args);
    }

    @Test
    void rebuildStuff() throws Exception {
        Logback.setupConsole();
        Path dir = Paths.get("exppriv", "optact", "gv_test");
        String[] args = {
//                "--inputRootClass", GvInRoot.class.getName(),
//                "--outputRootClass", OutRoot.class.getName(),
//                "--defaultScenarioClass", GvInRoot.class.getName(),
//                "--outDir", dir.toString(),
//                "--charset", Util.windows1252().name(),
//                "--validate",
//                "--skipReference",
//                "--dummyNomenklatur",
                "--pathToJava", java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString()
        };
        IRPtools.start(args);
    }

    @Test
    void asdIRPopt() throws Exception {
        Logback.setupConsole();
        Path dir = Paths.get("D:\\SUSIC\\SUSIC\\Programming\\Research\\model-HEAD-758fe47\\IRPsim");
        String[] args = {
//                "--inputRootClass", GvInRoot.class.getName(),
//                "--outputRootClass", OutRoot.class.getName(),
//                "--defaultScenarioClass", GvInRoot.class.getName(),
//                "--outDir", dir.toString(),
//                "--charset", Util.windows1252().name(),
//                "--validate",
//                "--skipReference",
//                "--dummyNomenklatur",
                "--pathToJava", java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", backendGeneratorJar.toString(),
                "--backendOutputFile", dir.resolve("backend.json").toString()
        };
        IRPtools.start(args);
    }
}