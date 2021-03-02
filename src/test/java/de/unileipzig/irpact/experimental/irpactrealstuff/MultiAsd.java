package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.function.BiConsumer;

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
                perennialFile,
                x2015.in,
                x2016.in,
                x2017.in,
                x2018.in,
                x2019.in
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
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");
        Path p0 = dir.resolve("scenariosX").resolve("full_scenario.json");
        Path p1 = dir.resolve("scenariosX").resolve("backup").resolve("full_scenario.json");
        check(p0, p1);

        Path p2 = dir.resolve("scenariosX").resolve("output-2015.json");
        Path p3 = dir.resolve("scenariosX").resolve("backup").resolve("output-2015.json");
        check2(p2, p3);
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

        Assertions.assertEquals(str0, str1);
    }
}
