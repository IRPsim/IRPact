package de.unileipzig.irpact.experimental.irpact;

import de.unileipzig.irpact.experimental.TestFiles;
import de.unileipzig.irpact.start.irpact2.IRPact;
import de.unileipzig.irpact.v2.io.DefaultScenario;
import de.unileipzig.irpact.v2.io.input.IRoot;
import de.unileipzig.irpact.v2.io.output.ORoot;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.start.IRPtools;
import de.unileipzig.irptools.uiedn.io.EdnPrinter;
import de.unileipzig.irptools.uiedn.io.PrinterFormat;
import de.unileipzig.irptools.util.Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@Disabled
public class RealTest {

    @Test
    void createInput() throws IOException {
        Path input = TestFiles.irpact2.resolve("input.gms");
        Path edn = TestFiles.irpact2.resolve("ui-input.edn");
        Path ednDelta = TestFiles.irpact2.resolve("ui-input-delta.edn");

        DefinitionCollection dcoll = AnnotationParser.parse(IRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();
        Converter converter = new Converter(dmap);

        GamsPrinter.write(Type.INPUT, gcoll, input, StandardCharsets.UTF_8);
        EdnPrinter.writeTo(dmap.toEdn(Type.INPUT), PrinterFormat.MY_DEFAULT, edn, StandardCharsets.UTF_8);
        EdnPrinter.writeTo(dmap.toDeltaEdn(Type.INPUT), PrinterFormat.MY_DEFAULT, ednDelta, StandardCharsets.UTF_8);
    }

    @Test
    void createOutput() {
        DefinitionCollection dcoll = AnnotationParser.parse(ORoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        GamsCollection gcoll = dmap.getGamsCollection();
        Converter converter = new Converter(dmap);

        System.out.println(GamsPrinter.write(Type.OUTPUT, gcoll));
    }

    @Test
    void buildAll() throws Exception {
        Path outRoot = TestFiles.irpact;
        String[] args = {
                "--inputRootClass", IRoot.class.getName(),
                "--outputRootClass", ORoot.class.getName(),
                "--defaultScenarioClass", DefaultScenario.class.getName(),
                "--outDir", outRoot.toString(),
                "--charset", Util.windows1252().name(),
                "--validate",
                "--skipReference",
                "--dummyNomenklatur",
                "--pathToJava", TestFiles.java.toString(),
                "--pathToJar", TestFiles.frontendGeneratorJar.toString(),
                "--pathToResourceDir", outRoot.toString(),
                "--frontendOutputFile", outRoot.resolve("frontend.json").toString()
        };
        IRPtools.start(args);
    }

    @Test
    void testRun() throws IOException, InterruptedException {
        Path input = TestFiles.irpact.resolve("scenarios").resolve("default.json");
        Path output = TestFiles.irpact.resolve("out").resolve("default.out.json");
        String[] args = {
                "-i", input.toString(),
                "-o", output.toString()
        };
        IRPact.main(args);
    }
}
