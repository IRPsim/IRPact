package de.unileipzig.irpact.experimental;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.start.optact.in.InRoot;
import de.unileipzig.irpact.start.optact.out.OutRoot;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.Clj;
import de.unileipzig.irptools.util.ProcessResult;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * @author Daniel Abitz
 */
@Disabled
class TestUI {

    @Test
    void testCustomUI() throws IOException, InterruptedException {
        ProcessResult result = Clj.runClojureJar(
                TestFiles.java11,
                TestFiles.frontendGeneratorJar,
                Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\spezifikation\\imageTest")
        );
        Charset charset = Util.windows1252();

        if(result.isError()) {
            throw new RuntimeException("clj canceled");
        } else {
            ObjectNode root = Util.parseJson(result.getData(), charset);
            Path outputPath = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\spezifikation\\imageTest", "frontend.json");
            Util.writeJson(
                    root,
                    outputPath,
                    charset,
                    Util.defaultPrinter
            );
        }
    }

    @Test
    void runStart() throws Exception {
        Logback.setupSystemOutAndErr();
        Path dir = TestFiles.testfiles.resolve("optact").resolve("test2");
        String[] args = {
                "--inputRootClass", InRoot.class.getName(),
                "--outputRootClass", OutRoot.class.getName(),
                "--defaultScenarioClass", InRoot.class.getName(),
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
                "--backendOutputFile", dir.resolve("backend.json").toString()
        };
        IRPtools.start(args);
    }

    private static boolean isEquals(Path p0, Path p1) throws IOException {
        byte[] b0 = Files.readAllBytes(p0);
        byte[] b1 = Files.readAllBytes(p1);
        return Arrays.equals(b0, b1);
    }

    private static void isEquals(Path p0, Path p1, Charset c) throws IOException {
        String str0 = Util.readString(p0, c);
        String str1 = Util.readString(p1, c);
        Assertions.assertEquals(str0, str1);
    }

    @Test
    void testFiles() throws IOException {
        Path dir2 = TestFiles.testfiles.resolve("optact").resolve("test1");
        Path dir2input = dir2.resolve("input");
        Path dir2output = dir2.resolve("output");
        Path dir2scenarios = dir2.resolve("scenarios");
        Path dir4 = TestFiles.testfiles.resolve("optact").resolve("test2");
        Path dir4input = dir4.resolve("input");
        Path dir4output = dir4.resolve("output");
        Path dir4scenarios = dir4.resolve("scenarios");

        System.out.println(isEquals(dir2input.resolve("input.gms"), dir4input.resolve("input.gms")));
        System.out.println(isEquals(dir2input.resolve("ui-input.edn"), dir4input.resolve("ui-input.edn")));
        System.out.println(isEquals(dir2input.resolve("ui-input-delta.edn"), dir4input.resolve("ui-input-delta.edn")));
        System.out.println();

        System.out.println(isEquals(dir2output.resolve("output.gms"), dir4output.resolve("output.gms")));
        System.out.println(isEquals(dir2output.resolve("ui-output.edn"), dir4output.resolve("ui-output.edn")));
        System.out.println();

        System.out.println(isEquals(dir2scenarios.resolve("default.json"), dir4scenarios.resolve("default.json")));

        isEquals(dir2input.resolve("input.gms"), dir4input.resolve("input.gms"), Util.windows1252());
        isEquals(dir2input.resolve("ui-input.edn"), dir4input.resolve("ui-input.edn"), Util.windows1252());
        isEquals(dir2input.resolve("ui-input-delta.edn"), dir4input.resolve("ui-input-delta.edn"), Util.windows1252());

        isEquals(dir2output.resolve("output.gms"), dir4output.resolve("output.gms"), Util.windows1252());
        isEquals(dir2output.resolve("ui-output.edn"), dir4output.resolve("ui-output.edn"), Util.windows1252());

        isEquals(dir2scenarios.resolve("default.json"), dir4scenarios.resolve("default.json"), StandardCharsets.UTF_8);
    }
}
