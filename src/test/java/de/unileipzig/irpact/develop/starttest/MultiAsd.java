package de.unileipzig.irpact.develop.starttest;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
@Disabled
class MultiAsd {

    private static final Path java11 = Paths.get("C:\\MyProgs\\Java\\jdk-11.0.2\\bin", "java.exe");
    private static final Path IRPTools = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPtools");
    private static final Path cljlibs = IRPTools.resolve("cljlibs");
    private static final Path frontendGeneratorJar = cljlibs.resolve("frontend-generator.jar");
    private static final Path backendGeneratorJar = cljlibs.resolve("backend-generator.jar");

    private static final String XXX = "x8";

    @Test
    void runStartWithTools() throws IOException {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
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
                "--maxGamsNameLength", DefinitionMapper.MAX_GAMS_NAME_LENGTH_AS_STR,
                "--autoTrimGamsNames",
                "--dummyNomenklatur",
                "--pathToJava", java11.toString(),
                "--pathToResourceDir", dir.toString(),
                "--pathToJar", frontendGeneratorJar.toString(),
                "--frontendOutputFile", dir.resolve("frontend.json").toString(),
                "--pathToBackendJar", backendGeneratorJar.toString(),
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
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
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
                perennialFile,
                x2015.in,
                x2016.in,
                x2017.in,
                x2018.in,
                x2019.in
        );
        Path perennialFile2 = dir.resolve("scenariosX").resolve("full_scenario2.json");
        storePerennial(
                perennialFile2,
                x2015.in,
                x2015.in,
                x2015.in,
                x2015.in,
                x2015.in
        );
    }

    @Test
    void runSingle() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        X x2015 = new X(dir);

        runIt(dir, 2015, x2015);
    }

    private void runIt(
            Path dir,
            int startYear,
            BiConsumer<AnnualEntry<InRoot>, AnnualData<OutRoot>> consumer) {
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };
        IRPact.resultConsumer = consumer;
        Start.main(args);
    }

    @Test
    void runImage_NEW() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
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
            AnnualEntry<InRoot>... input) throws IOException {
        PerennialData<InRoot> perennialData = new PerennialData<>();
        for(AnnualEntry<InRoot> entry: input) {
            perennialData.add(entry);
        }
        PerennialFile perennialFile = perennialData.serialize(IRPact.getInputConverter());
        perennialFile.store(outputFile);
    }

    private static class X implements BiConsumer<AnnualEntry<InRoot>, AnnualData<OutRoot>> {

        protected Path dir;
        protected AnnualEntry<InRoot> in;
        protected AnnualData<OutRoot> out;

        public X(Path dir) {
            this.dir = dir;
        }

        @Override
        public void accept(AnnualEntry<InRoot> i, AnnualData<OutRoot> o) {
            in = i;
            out = o;

            System.out.println("out len: " + o.getData().getHiddenBinaryDataLength());

            i.getData().binaryPersistData = o.getData().binaryPersistData;

            AnnualData<InRoot> nextRoot = new AnnualData<>(i.getData());
            nextRoot.getConfig().copyFrom(i.getConfig());
            nextRoot.getConfig().setYear(nextRoot.getConfig().getYear() + 1);

            AnnualFile nextFile = nextRoot.serialize(IRPact.getInputConverter());
            Path nextPath = dir.resolve("scenariosX").resolve("input-" + nextRoot.getConfig().getYear() + ".json");
            try {
                nextFile.store(nextPath);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    @Test
    public void asd() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        Path p0 = dir.resolve("scenariosX").resolve("full_scenario.json");
        Path p1 = dir.resolve("scenariosX").resolve("backup").resolve("full_scenario.json");
        check(p0, p1);

        Path p2 = dir.resolve("scenariosX").resolve("output-2015.json");
        Path p3 = dir.resolve("scenariosX").resolve("backup").resolve("output-2015.json");
        check2(p2, p3);
    }

    @Test
    public void asd2() throws IOException {
        Path dir0 = TestFiles.testfiles.resolve("uitests").resolve(XXX);
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
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default.json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example1").toString()
        };
        Start.main(args);
    }
    @Test
    void runToSpec2() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "-i", dir.resolve("scenariosS").resolve("default.spec1.json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example2").toString()
        };
        Start.main(args);
    }

    @Test
    void runToParam() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "--specToParam", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosS").resolve("default.spec1.json").toString()
        };
        Start.main(args);
    }

    @Test
    void testContent() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX).resolve("spec");
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
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + 2015 + ".json").toString(),
                "--paramToSpec", dir.resolve("spec").resolve("example1").toString()
        };
        Start.main(args);
    }

    @Test
    void runToParam3() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "--specToParam", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosX").resolve("input-spec-" + 2015 + ".json").toString()
        };
        Start.main(args);
    }

    @Test
    void runIt3() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "-i", dir.resolve("spec").resolve("example1").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-spec-2015.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runIt4() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(XXX);
        String[] args = {
                "-i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\demos\\Demo1_Entscheidungsprozess_Bewusstsein").toString(),
                "-o", dir.resolve("scenariosX").resolve("Demo1_Entscheidungsprozess_Bewusstsein.out.json").toString()
        };
        Start.main(args);
    }


    //
}