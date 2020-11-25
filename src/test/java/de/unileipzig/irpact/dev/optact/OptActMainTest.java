package de.unileipzig.irpact.dev.optact;

import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.optact.OptActRes;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irpact.v2.commons.log.Logback;
import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.Util;
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
        Path outDir = Paths.get("exppriv");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.noimage.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runImageNoSimu() {
        Path dir = Paths.get("src", "main", "resources", "scenarios");
        Path outDir = Paths.get("exppriv");
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
        Path outDir = Paths.get("exppriv");
        String[] args = {
                "-i", dir.resolve(Constants.DEFAULT_JSON).toString(),
                "-o", outDir.resolve("default.out.simu.json").toString(),
                "--image", outDir.resolve("testimage.png").toString()
        };
        Start.main(args);
    }

    @Test
    void printStuff() {
        OptActRes res = new OptActRes();
        res.entries().forEach(p -> System.out.println(p.getKey() + ": " + p.getValue()));
    }

    @Test
    void createFiles4GV() throws Exception {
        Logback.setupSystemOutAndErr();
        Path dir = Paths.get("exppriv", "optact", "gv");
        String[] args = {
                "--inputRootClass", GvInRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", GvInRoot.class.getName(),
                "--outDir", dir.toString(),
                "--charset", Util.windows1252().name(),
                "--validate",
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
}