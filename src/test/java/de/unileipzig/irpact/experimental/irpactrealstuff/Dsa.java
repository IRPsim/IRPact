package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.optact.gvin.GvInRoot;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Dsa {

    @Test
    void runStart() throws Exception {
        Logback.setupSystemOutAndErr();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x2");
        String[] args = {
                "--inputRootClass", GvInRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", GvInRoot.class.getName(),
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
        IRPtools.start(args);
    }
}
