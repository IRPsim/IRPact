package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.IOConstants;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.*;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Disabled
class MultiAsd {

    @Test
    void runStartWithTools() throws IOException {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
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
                "--sortAfterPriority"
        };
        Start.main(args);

        Path scen = dir.resolve("scenarios").resolve("default.json");
        Path scenX = dir.resolve("scenariosX").resolve("input-2015.json");
        Files.copy(scen, scenX, StandardCopyOption.REPLACE_EXISTING);
    }

    @SuppressWarnings("unchecked")
    @Test
    void runMulti() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        X x2015 = new X(dir);
        X x2016 = new X(dir);
        X x2017 = new X(dir);
        X x2018 = new X(dir);
        X x2019 = new X(dir);

        runIt(dir, 2015, x2015);
        runIt(dir, 2016, x2016);
        runIt(dir, 2017, x2017);
        runIt(dir, 2018, x2018);
        runIt(dir, 2019, x2019);

        Path perennialFile = dir.resolve("scenariosX").resolve("full_scenario.json");
        storePerennial(
                perennialFile, x2015.options,
                x2015.in,
                x2016.in,
                x2017.in,
                x2018.in,
                x2019.in
        );
        Path perennialFile2 = dir.resolve("scenariosX").resolve("full_scenario2.json");
        storePerennial(
                perennialFile2, x2015.options,
                x2015.in,
                x2015.in,
                x2015.in,
                x2015.in,
                x2015.in
        );
    }

    @Test
    void runSingle() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        X x2015 = new X(dir);

        runIt(dir, 2015, x2015);
    }

    private void runIt(
            Path dir,
            int startYear,
            IRPactCallback callback) {
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };
        Start.start(args, callback);
    }

    @Test
    void runImage_NEW() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        int startYear = 2019;
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString(),
                "--image", dir.resolve("scenariosX").resolve("image-" + startYear + ".png").toString(),
                "--noSimulation"
        };
        Start.main(args);
    }

    @SuppressWarnings("unchecked")
    private void storePerennial(
            Path outputFile,
            CommandLineOptions options,
            AnnualEntry<InRoot>... input) throws IOException {
        PerennialData<InRoot> perennialData = new PerennialData<>();
        for(AnnualEntry<InRoot> entry: input) {
            perennialData.add(entry);
        }
        PerennialFile perennialFile = perennialData.serialize(IRPact.getInputConverter(options));
        perennialFile.store(outputFile);
    }

    private static class X implements IRPactCallback {

        protected Path dir;
        protected AnnualEntry<InRoot> in;
        protected AnnualData<OutRoot> out;
        protected CommandLineOptions options;

        public X(Path dir) {
            this.dir = dir;
        }

        @Override
        public void onFinished(IRPActAccess access) throws Exception {
            in = access.getInput();
            out = access.getOutput();
            options = access.getCommandLineOptions();

            System.out.println("out len: " + out.getData().getHiddenBinaryDataLength());

            in.getData().binaryPersistData = out.getData().binaryPersistData;

            AnnualData<InRoot> nextRoot = new AnnualData<>(in.getData());
            nextRoot.getConfig().copyFrom(in.getConfig());
            nextRoot.getConfig().setYear(nextRoot.getConfig().getYear() + 1);

            AnnualFile nextFile = nextRoot.serialize(IRPact.getInputConverter(options));
            Path nextPath = dir.resolve("scenariosX").resolve("input-" + nextRoot.getConfig().getYear() + ".json");
            nextFile.store(nextPath);
        }
    }

    @Test
    public void asd() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        Path p0 = dir.resolve("scenariosX").resolve("full_scenario.json");
        Path p1 = dir.resolve("scenariosX").resolve("backup").resolve("full_scenario.json");
        check(p0, p1);

        Path p2 = dir.resolve("scenariosX").resolve("output-2015.json");
        Path p3 = dir.resolve("scenariosX").resolve("backup").resolve("output-2015.json");
        check2(p2, p3);
    }

    @Test
    public void asd2() throws IOException {
        Path dir0 = TestFiles.testfiles.resolve("uitests").resolve("x6");
        Path dir1 = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0uberJarTest\\2021-03-02");

        Path p0 = dir0.resolve("scenariosX").resolve("output-2019.json");
        Path p1 = dir1.resolve("output-2019.2.json");
        check(p0, p1);

        Path p2 = dir0.resolve("scenariosX").resolve("output-2015.json");
        Path p3 = dir1.resolve("output-2015.2.json");
        check(p2, p3);
    }

    private void check(Path p0, Path p1) throws IOException {
        String str0 = Util.readString(p0, StandardCharsets.UTF_8);
        String str1 = Util.readString(p1, StandardCharsets.UTF_8);

        System.out.println(str0.hashCode());
        System.out.println(str1.hashCode());
    }

    private void check2(Path p0, Path p1) throws IOException {
        String str0 = Util.readString(p0, StandardCharsets.UTF_8);
        String str1 = Util.readString(p1, StandardCharsets.UTF_8);

        assertEquals(str0, str1);
    }

    @Test
    void runToSpec() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default.json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example1").toString()
        };
        Start.main(args);
    }
    @Test
    void runToSpec2() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "-i", dir.resolve("scenariosS").resolve("default.spec1.json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example2").toString()
        };
        Start.main(args);
    }

    @Test
    void runToParam() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "--specToParam", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosS").resolve("default.spec1.json").toString()
        };
        Start.main(args);
    }

    @Test
    void testContent() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6").resolve("spec");
        Path dir1 = dir.resolve("example1");
        Path dir2 = dir.resolve("example2");

        List<Path> paths1;
        try(Stream<Path> stream = Files.walk(dir1, Integer.MAX_VALUE)) {
            paths1 = stream.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        List<Path> paths2;
        try(Stream<Path> stream = Files.walk(dir2, Integer.MAX_VALUE)) {
            paths2 = stream.filter(Files::isRegularFile)
                    .collect(Collectors.toList());
        }

        assertEquals(paths1.size(), paths2.size());
        for(int i = 0; i < paths1.size(); i++) {
            Path p1 = paths1.get(i);
            Path p2 = paths2.get(i);
            assertEquals(p1.getFileName(), p2.getFileName());

            String content1 = Util.readString(p1, StandardCharsets.UTF_8);
            String content2 = Util.readString(p2, StandardCharsets.UTF_8);

            System.out.println("check: " + p1 + " | " + p2);
            assertEquals(content1, content2);
        }
    }

    //===========================


    @Test
    void runToSpec3() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + 2015 + ".json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example1").toString()
        };
        Start.main(args);
    }

    @Test
    void runToParam3() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "--specToParam", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosX").resolve("input-spec-" + 2015 + ".json").toString()
        };
        Start.main(args);
    }

    @Test
    void runIt3() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "-i", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-spec-2015.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runIt4() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        String[] args = {
                "-i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\demos\\Demo1_Entscheidungsprozess_Bewusstsein").toString(),
                "-o", dir.resolve("scenariosX").resolve("Demo1_Entscheidungsprozess_Bewusstsein.out.json").toString()
        };
        Start.main(args);
    }

    @Test
    void checkNames() {
        IOConstants.validateNames();
    }
}
