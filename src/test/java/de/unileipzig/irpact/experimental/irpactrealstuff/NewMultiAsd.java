package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.develop.TestFiles;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * @author Daniel Abitz
 */
@Disabled
public class NewMultiAsd {

    private static final Path locDeFile = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpactdata", "loc_de.yaml");
    private static final String xDir = "x9";

    @Test
    void runStartWithTools() throws IOException {
        IRPLogging.initConsole();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(xDir);
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

//                "--autoTrimGamsNames",
//                "--maxGamsNameLength", DefinitionMapper.MAX_GAMS_NAME_LENGTH_AS_STR,
//                "--minGamsPartLength", DefinitionMapper.MIN_PART_LENGTH_AS_STR,

                "--pathToResourceFile", locDeFile.toString()
        };
        Start.start(args);

        Path scen = dir.resolve("scenarios").resolve("default.json");
        Path scenX = dir.resolve("scenariosX").resolve("input-2015.json");
        Files.copy(scen, scenX, StandardCopyOption.REPLACE_EXISTING);
    }

    @Test
    void runSingle() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(xDir);
        X x2015 = new X(dir);

        runIt(dir, 2015, x2015);
    }

    @SuppressWarnings("unchecked")
    @Test
    void runMulti() throws IOException {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve(xDir);
        X x2015 = new X(dir);
        X x2016 = new X(dir);
        X x2017 = new X(dir);
        X x2018 = new X(dir);
        X x2019 = new X(dir);

        runItImage(dir, 2015, x2015);
        runItImage(dir, 2016, x2016);
        runItImage(dir, 2017, x2017);
        runItImage(dir, 2018, x2018);
        runItImage(dir, 2019, x2019);

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

    private void runIt(
            Path dir,
            int startYear,
            IRPactCallback callback) {
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--logPath", dir.resolve("scenariosX").resolve("log-" + startYear + ".log").toString(),
                "--logConsoleAndFile",
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };
        Start.start(args, callback);
    }

    private void runItInitialImage(
            Path dir,
            int startYear,
            IRPactCallback callback) {
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--logPath", dir.resolve("scenariosX").resolve("log-" + startYear + ".log").toString(),
                "--logConsoleAndFile",
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString(),
                "--image", dir.resolve("image-" + startYear + ".png").toString(),
                "--noSimulation"
        };
        Start.start(args, callback);
    }

    private void runItImage(
            Path dir,
            int startYear,
            IRPactCallback callback) {
        String[] args = {
                "-i", dir.resolve("scenariosX").resolve("input-" + startYear + ".json").toString(),
                "-o", dir.resolve("scenariosX").resolve("output-" + startYear + ".json").toString(),
                "--logPath", dir.resolve("scenariosX").resolve("log-" + startYear + ".log").toString(),
                "--logConsoleAndFile",
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString(),
                "--image", dir.resolve("image-" + startYear + ".png").toString()
        };
        Start.start(args, callback);
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
}
