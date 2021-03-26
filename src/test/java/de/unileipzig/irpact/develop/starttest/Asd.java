package de.unileipzig.irpact.develop.starttest;

import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.develop.TestFiles;
import de.unileipzig.irpact.io.param.input.InExample;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.output.OutRoot;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irpact.start.IRPactCallback;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.optact.OptActMain;
import de.unileipzig.irptools.io.annual.AnnualData;
import de.unileipzig.irptools.io.annual.AnnualFile;
import de.unileipzig.irptools.io.base.AnnualEntry;
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
public class Asd {

    @Test
    void printHelp() {
        Start.main(new String[]{"-?"});
    }

    @Test
    void runStart() throws Exception {
        Logback.setupSystemOutAndErr();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x4");
        String[] args = {
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
        IRPtools.start(args);
    }

    @Test
    void runStartWithTools() {
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
    }

    @Test
    void runItFirst() {
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");

        IRPactCallback callback = access -> {
            AnnualEntry<InRoot> i = access.getInput();
            AnnualData<OutRoot> o = access.getOutput();

            System.out.println("out len: " + o.getData().getHiddenBinaryDataLength());

            i.getData().binaryPersistData = o.getData().binaryPersistData;

            AnnualData<InRoot> nextRoot = new AnnualData<>(i.getData());
            nextRoot.getConfig().copyFrom(i.getConfig());
            nextRoot.getConfig().setYear(nextRoot.getConfig().getYear() + 1);

            AnnualFile nextFile = nextRoot.serialize(IRPact.getInputConverter(access.getCommandLineOptions()));
            Path nextPath = dir.resolve("scenarios").resolve("default." + nextRoot.getConfig().getYear() + ".json");
            nextFile.store(nextPath);
        };

        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default.json").toString(),
                "-o", dir.resolve("scenarios").resolve("default.out.2015.json").toString(),
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };
        Start.start(args, callback);
    }

    @Test
    void runItNext() {
        int startYear = 2017;
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x6");

        IRPactCallback callback = access -> {
            AnnualEntry<InRoot> i = access.getInput();
            AnnualData<OutRoot> o = access.getOutput();

            System.out.println("out len: " + o.getData().getHiddenBinaryDataLength());

            i.getData().binaryPersistData = o.getData().binaryPersistData;

            AnnualData<InRoot> nextRoot = new AnnualData<>(i.getData());
            nextRoot.getConfig().copyFrom(i.getConfig());
            nextRoot.getConfig().setYear(nextRoot.getConfig().getYear() + 1);

            AnnualFile nextFile = nextRoot.serialize(IRPact.getInputConverter(access.getCommandLineOptions()));
            Path nextPath = dir.resolve("scenarios").resolve("default." + nextRoot.getConfig().getYear() + ".json");
            nextFile.store(nextPath);
        };

        String[] args = {
                "-i", dir.resolve("scenarios").resolve("default." + startYear + ".json").toString(),
                "-o", dir.resolve("scenarios").resolve("default.out." + startYear + ".json").toString(),
                "--dataDir", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\0data").toString()
        };
        Start.start(args, callback);
    }

    @Test
    void versionEtc() {
        String[] args = {
                "-v"
        };
        Start.main(args);
    }

    @Test
    void helpEtc() {
        String[] args = {
                "-?"
        };
        Start.main(args);
    }

    @Test
    void runImage_GvRoot() {
        Path outDir = TestFiles.testfiles.resolve("uitests").resolve("x3");
        String[] args = {
                "-i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\scenarios", "default.json").toString(),
                "-o", outDir.resolve("runImage_GvRoot.json").toString(),
                "--image", outDir.resolve("runImage_GvRoot.png").toString()
        };
        OptActMain.main(args);
    }
    @Test
    void runImage_NEW() {
        Path outDir = TestFiles.testfiles.resolve("uitests").resolve("x4");
        String[] args = {
                "-i", Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\testfiles\\uitests\\x4\\scenarios", "default.json").toString(),
                "-o", outDir.resolve("runImage_NEW.json").toString(),
                "--image", outDir.resolve("runImage_NEW.png").toString()
        };
        Start.main(args);
    }

    @Test
    void testEqualsWithLoad() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(new InRoot());
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        Path file = TestFiles.testfiles.resolve("uitests").resolve("x1").resolve("scenarios").resolve("default.json");
//        AnnualEntry<InRoot> entry = ContentTypeDetector.parseFirstEntry(file, StandardCharsets.UTF_8, converter);
//
//        InRoot x = new InExample().createDefaultScenario();
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.affinityEntries), CollectionUtil.hashSetOf(entry.getData().affinityEntries)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.consumerAgentGroups), CollectionUtil.hashSetOf(entry.getData().consumerAgentGroups)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.graphTopologySchemes), CollectionUtil.hashSetOf(entry.getData().graphTopologySchemes)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.distanceEvaluators), CollectionUtil.hashSetOf(entry.getData().distanceEvaluators)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.numberOfTies), CollectionUtil.hashSetOf(entry.getData().numberOfTies)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.processModel), CollectionUtil.hashSetOf(entry.getData().processModel)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.orientationSupplier), CollectionUtil.hashSetOf(entry.getData().orientationSupplier)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.slopeSupplier), CollectionUtil.hashSetOf(entry.getData().slopeSupplier)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.productGroups), CollectionUtil.hashSetOf(entry.getData().productGroups)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.spatialModel), CollectionUtil.hashSetOf(entry.getData().spatialModel)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.spatialDistributions), CollectionUtil.hashSetOf(entry.getData().spatialDistributions)
//        );
//        Assertions.assertEquals(
//                CollectionUtil.hashSetOf(x.timeModel), CollectionUtil.hashSetOf(entry.getData().timeModel)
//        );
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(new InExample().createDefaultScenario(), new InExample().createDefaultScenario());
    }
}
