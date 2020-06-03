package de.unileipzig.irpact.disabled.toolstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.disabled.TestFiles;
import de.unileipzig.irpact.start.Start;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.GlobalRoot;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.GlobalScalars;
import de.unileipzig.irpact.start.hardcodeddemo.HardCodedAgentDemo;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import de.unileipzig.irptools.defstructure.*;
import de.unileipzig.irptools.gamsjson.MappedGamsJson;
import de.unileipzig.irptools.uiedn.Sections;
import de.unileipzig.irptools.uiedn.io.EdnPrinter;
import de.unileipzig.irptools.uiedn.io.PrinterFormat;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
@Disabled
class SimpleExample {

    @Test
    void asd() {
        System.out.println(TestFiles.scenarios.toAbsolutePath());
        System.out.println(Files.exists(TestFiles.scenarios));
    }

    @Test
    void startMyDemo() throws IOException {
        String[] args = {
                "-i", TestFiles.scenarios.resolve("default.json").toString(),
                "-o", TestFiles.toolsdemo.resolve("default-out.json").toString()
        };
        Start.main(args);
    }

    @Test
    void runInit() throws IOException {
        handleInput();
        handleOutput();
        buildSmallDemo1();
    }

    @Test
    void handleInput() throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(GlobalRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);

        Path gamsOut = TestFiles.toolsdemo.resolve("input.gms");
        Printer.writeGams(dmap.getGamsCollection(), gamsOut, StandardCharsets.UTF_8);

        Sections sections = dmap.toEdn();
        Path sectionsOut = TestFiles.toolsdemo.resolve("ui-input.edn");
        EdnPrinter.writeTo(sections, PrinterFormat.MY_DEFAULT, sectionsOut, StandardCharsets.UTF_8);

        Sections deltaSections = dmap.toDeltaEdn();
        Path deltaSectionsOut = TestFiles.toolsdemo.resolve("ui-input-delta.edn");
        EdnPrinter.writeTo(deltaSections, PrinterFormat.MY_DEFAULT, deltaSectionsOut, StandardCharsets.UTF_8);
    }

    @Test
    void handleOutput() throws IOException {
        DefinitionCollection dcoll = AnnotationParser.parse(de.unileipzig.irpact.start.hardcodeddemo.def.out.GlobalRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);

        Path gamsOut = TestFiles.toolsdemo.resolve("output.gms");
        Printer.writeGams(dmap.getGamsCollection(), gamsOut, StandardCharsets.UTF_8);

        Sections sections = dmap.toEdn();
        Path sectionsOut = TestFiles.toolsdemo.resolve("ui-output.edn");
        EdnPrinter.writeTo(sections, PrinterFormat.MY_DEFAULT, sectionsOut, StandardCharsets.UTF_8);
    }

    @Test
    void buildSmallDemo1() throws IOException {
        GlobalScalars scalars = new GlobalScalars(123);
        AgentGroup[] groups = {
                new AgentGroup(
                        "gx10x5",
                        10,
                        0.5
                ),
                new AgentGroup(
                        "gx100x01",
                        100,
                        0.01
                ),
                new AgentGroup(
                        "gx50x75",
                        50,
                        0.75
                )
        };
        Product[] products = {
                new Product("Auto"),
                new Product("Haus")
        };
        GlobalRoot root = new GlobalRoot(scalars, groups, products);

        DefinitionCollection dcoll = AnnotationParser.parse(GlobalRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        ObjectNode temp = new ObjectMapper().createObjectNode();
        converter.toGamsJsonYear(root, temp);
        GlobalRoot root2 = converter.fromGamsJsonYear(temp);

        System.out.println("equals?: " + root.toString().endsWith(root2.toString()));
        root2.getAgentGroups()[1].adaptionRate = 0.3;

        MappedGamsJson<GlobalRoot> mappedGams = new MappedGamsJson<>();
        mappedGams.add(2015, root);
        mappedGams.add(2016, root2);
        converter.toGamsJson(mappedGams);

        Path out = TestFiles.toolsdemo.resolve("test1.json");
        JsonUtil.writeJson(mappedGams.getGamsJson().getRoot(), out, JsonUtil.defaultPrinter);

        System.out.println(root);
    }

    @Test
    void buildSmallDemo2_OneYear() throws IOException {
        GlobalScalars scalars = new GlobalScalars(123);
        AgentGroup[] groups = {
                new AgentGroup(
                        "gx10x5",
                        10,
                        0.5
                ),
                new AgentGroup(
                        "gx100x01",
                        100,
                        0.01
                ),
                new AgentGroup(
                        "gx50x75",
                        50,
                        0.75
                )
        };
        Product[] products = {
                new Product("Auto"),
                new Product("Haus")
        };
        GlobalRoot root = new GlobalRoot(scalars, groups, products);

        DefinitionCollection dcoll = AnnotationParser.parse(GlobalRoot.CLASSES);
        DefinitionMapper dmap = new DefinitionMapper(dcoll);
        Converter converter = new Converter(dmap);

        MappedGamsJson<GlobalRoot> mappedGams = new MappedGamsJson<>();
        mappedGams.getGamsJson().getDescription().setBusinessModelDescription("Testszenario");
        mappedGams.add(2015, root);
        converter.toGamsJson(mappedGams);

        Path out = TestFiles.toolsdemo.resolve("test2.json");
        JsonUtil.writeJson(mappedGams.getGamsJson().getRoot(), out, JsonUtil.defaultPrinter);

        System.out.println(root);
    }
}
