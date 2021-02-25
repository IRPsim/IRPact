package de.unileipzig.irpact.experimental.toolstest;

import de.unileipzig.irpact.util.TodoOLD;
import org.junit.jupiter.api.Disabled;

/**
 * @author Daniel Abitz
 */
@Disabled
@TodoOLD("neue io-file und daten typen einbauen")
class SimpleExample {

//    @Test
//    void asd() {
//        System.out.println(TestFiles.scenarios.toAbsolutePath());
//        System.out.println(Files.exists(TestFiles.scenarios));
//    }
//
//    @Test
//    void startMyDemoWithScenario() throws IOException {
//        String[] args = {
//                "-i", TestFiles.scenarios.resolve("default.json").toString(),
//                "-o", TestFiles.toolsdemo.resolve("default-out5.json").toString()
//        };
//        Start.main(args);
//    }
//
//    @Test
//    void startMyDemoWithData() throws IOException {
//        String[] args = {
//                "-i", TestFiles.toolsdemo.resolve("test123.json").toString(),
//                "-o", TestFiles.toolsdemo.resolve("test12-out.json").toString()
//        };
//        Start.main(args);
//    }
//
//    @Test
//    void startMyDemoWithSingleYear() throws IOException {
//        String[] args = {
//                "-i", TestFiles.toolsdemo.resolve("year2015.json").toString(),
//                "-o", TestFiles.toolsdemo.resolve("year2015-out3.json").toString()
//        };
//        Start.main(args);
//    }
//
//    @Test
//    void runInit() throws IOException {
//        handleInput();
//        handleOutput();
//        buildSmallDemo1();
//    }
//
//    @Test
//    void handleInput() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(InputRoot.CLASSES);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//
//        Path gamsOut = TestFiles.toolsdemo.resolve("input.gms");
//        GamsPrinter.write(dmap.getGamsCollection(), gamsOut, StandardCharsets.UTF_8);
//
//        Sections sections = dmap.toEdn(GamsType.INPUT);
//        Path sectionsOut = TestFiles.toolsdemo.resolve("ui-input.edn");
//        EdnPrinter.writeTo(sections, PrinterFormat.MY_DEFAULT, sectionsOut, StandardCharsets.UTF_8);
//
//        Sections deltaSections = dmap.toDeltaEdn(GamsType.INPUT);
//        Path deltaSectionsOut = TestFiles.toolsdemo.resolve("ui-input-delta.edn");
//        EdnPrinter.writeTo(deltaSections, PrinterFormat.MY_DEFAULT, deltaSectionsOut, StandardCharsets.UTF_8);
//    }
//
//    @Test
//    void handleOutput() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(OutputRoot.CLASSES);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//
//        Path gamsOut = TestFiles.toolsdemo.resolve("output.gms");
//        GamsPrinter.write(GamsType.OUTPUT, dmap.getGamsCollection(), gamsOut, StandardCharsets.UTF_8);
//
//        Sections sections = dmap.toEdn(GamsType.OUTPUT);
//        Path sectionsOut = TestFiles.toolsdemo.resolve("ui-output.edn");
//        EdnPrinter.writeTo(sections, PrinterFormat.MY_DEFAULT, sectionsOut, StandardCharsets.UTF_8);
//    }
//
//    @Test
//    void buildSmallDemo1() throws IOException {
//        InputScalars scalars = new InputScalars(123);
//        AgentGroup[] groups = {
//                new AgentGroup(
//                        "gx10x5",
//                        10,
//                        0.5
//                ),
//                new AgentGroup(
//                        "gx100x01",
//                        100,
//                        0.01
//                ),
//                new AgentGroup(
//                        "gx50x75",
//                        50,
//                        0.75
//                )
//        };
//        Product[] products = {
//                new Product("Auto"),
//                new Product("Haus")
//        };
//        InputRoot root = new InputRoot(scalars, groups, products);
//
//        DefinitionCollection dcoll = AnnotationParser.parse(InputRoot.CLASSES);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        ObjectNode temp = new ObjectMapper().createObjectNode();
//        converter.toGamsJson(root, temp);
//        InputRoot root2 = converter.fromGamsJson(temp);
//
//        System.out.println("equals?: " + root.toString().endsWith(root2.toString()));
//        root2.getAgentGroups()[1].adaptionRate = 0.3;
//
//        ScenarioData<InputRoot> mappedGams = new ScenarioData<>();
//        mappedGams.add(2015, root);
//        mappedGams.add(2016, root2);
//        ScenarioFile sfile = mappedGams.serialize(converter);
//
//        Path out = TestFiles.toolsdemo.resolve("test1.json");
//        sfile.store(out);
//
//        Path out1 = TestFiles.toolsdemo.resolve("year2015.json");
//        JsonUtil.writeJson(sfile.getYear(0).root(), out1, JsonUtil.defaultPrinter);
//
//        Path out2 = TestFiles.toolsdemo.resolve("year2016.json");
//        JsonUtil.writeJson(sfile.getYear(1).root(), out2, JsonUtil.defaultPrinter);
//
//        System.out.println(root);
//    }

//    @Test
//    void buildSmallDemo2_OneYear() throws IOException {
//        InputScalars scalars = new InputScalars(123);
//        AgentGroup[] groups = {
//                new AgentGroup(
//                        "gx10x5",
//                        10,
//                        0.5
//                ),
//                new AgentGroup(
//                        "gx100x01",
//                        100,
//                        0.01
//                ),
//                new AgentGroup(
//                        "gx50x75",
//                        50,
//                        0.75
//                )
//        };
//        Product[] products = {
//                new Product("Auto"),
//                new Product("Haus")
//        };
//        InputRoot root = new InputRoot(scalars, groups, products);
//
//        DefinitionCollection dcoll = AnnotationParser.parse(InputRoot.CLASSES);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        ScenarioData<InputRoot> mappedGams = new ScenarioData<>();
//
//
//        MappedGamsJson<InputRoot> mappedGams = new MappedGamsJson<>(GamsJson.Type.INPUT);
//        mappedGams.getGamsJson().getDescription().setBusinessModelDescription("Testszenario");
//        mappedGams.add(2015, root);
//        converter.toGamsJson(mappedGams);
//
//        ScenarioFile sfile = mappedGams.serialize(converter);
//        //mappedGams.getGamsJson().getDescription().setBusinessModelDescription("Testszenario");
//
//        Path out = TestFiles.toolsdemo.resolve("test2.json");
//        sfile.store(out);
//        //JsonUtil.writeJson(mappedGams.getGamsJson().getRoot(), out, JsonUtil.defaultPrinter);
//
//        System.out.println(root);
//    }
}
