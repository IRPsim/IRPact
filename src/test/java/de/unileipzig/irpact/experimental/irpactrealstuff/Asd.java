package de.unileipzig.irpact.experimental.irpactrealstuff;

import de.unileipzig.irpact.commons.CollectionUtil;
import de.unileipzig.irpact.commons.log.Logback;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.io.input.InExample;
import de.unileipzig.irpact.io.input.InRoot;
import de.unileipzig.irpact.io.input.InputParserX;
import de.unileipzig.irpact.io.output.OutRoot;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.optact.OptActMain;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.io.ContentTypeDetector;
import de.unileipzig.irptools.io.base.AnnualEntry;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
@Disabled
public class Asd {

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
    void runStartWithTools() throws Exception {
        Logback.setupSystemOutAndErr();
        Path dir = TestFiles.testfiles.resolve("uitests").resolve("x4");
        String[] args = {
                "--irptools",
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
        DefinitionCollection dcoll = AnnotationParser.parse(new InRoot());
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        Path file = TestFiles.testfiles.resolve("uitests").resolve("x1").resolve("scenarios").resolve("default.json");
        AnnualEntry<InRoot> entry = ContentTypeDetector.parseFirstEntry(file, StandardCharsets.UTF_8, converter);

        InRoot x = new InExample().createDefaultScenario();
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.affinityEntries), CollectionUtil.hashSetOf(entry.getData().affinityEntries)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.consumerAgentGroups), CollectionUtil.hashSetOf(entry.getData().consumerAgentGroups)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.graphTopologySchemes), CollectionUtil.hashSetOf(entry.getData().graphTopologySchemes)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.distanceEvaluators), CollectionUtil.hashSetOf(entry.getData().distanceEvaluators)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.numberOfTies), CollectionUtil.hashSetOf(entry.getData().numberOfTies)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.processModel), CollectionUtil.hashSetOf(entry.getData().processModel)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.orientationSupplier), CollectionUtil.hashSetOf(entry.getData().orientationSupplier)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.slopeSupplier), CollectionUtil.hashSetOf(entry.getData().slopeSupplier)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.productGroups), CollectionUtil.hashSetOf(entry.getData().productGroups)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.spatialModel), CollectionUtil.hashSetOf(entry.getData().spatialModel)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.spatialDistributions), CollectionUtil.hashSetOf(entry.getData().spatialDistributions)
        );
        Assertions.assertEquals(
                CollectionUtil.hashSetOf(x.timeModel), CollectionUtil.hashSetOf(entry.getData().timeModel)
        );
    }

    @Test
    void testEquals() {
        Assertions.assertEquals(new InExample().createDefaultScenario(), new InExample().createDefaultScenario());
    }

    @Test
    void testParse() throws ValidationException {
        InRoot root = InExample.createExample();
        root.general.logGraphCreation = true;
        root.general.logAgentCreation = true;
        InputParserX parser = new InputParserX();
        SimulationEnvironment environment = parser.parse(root);
        environment.initialize();
        environment.validate();
        environment.setup();
    }
}
