package de.unileipzig.irpact.experimental.tempdemo;

import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
@Disabled
class TestCreateDefaultScenario {

//    //=========================
//    //all
//    //=========================
//
//    @Test
//    void createAll() {
//        createContinous();
//        createDiscrete();
//        createContinousForTest();
//        createDiscreteForTest();
//    }
//
//    @Test
//    void createFilesAll() throws IOException {
//        createContinousFile();
//        createDiscreteFile();
//        createContinousFileForTest();
//        createDiscreteFileForTest();
//    }
//
//    //=========================
//    //Continous
//    //=========================
//
//    @Test
//    void createContinous() {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData inputData = DefaultScenarioFactory.createContinousInputData();
//        ObjectNode rootNode = JsonUtil.mapper.createObjectNode();
//        converter.toGamsJson(inputData, rootNode);
//
//        System.out.println(JsonUtil.writeJson(rootNode));
//
//        IRPactInputData inputData2 = new IRPactInputData();
//        converter.fromGamsJson(rootNode, inputData2);
//
//        assertEquals(inputData, inputData2);
//
//
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, inputData);
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        ScenarioData<IRPactInputData> scenarioData2 = scenarioFile.deserialize(converter);
//
//        assertEquals(
//                scenarioData.get(0).getData(),
//                scenarioData2.get(0).getData()
//        );
//    }
//
//    @Test
//    void createContinousFile() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData data = DefaultScenarioFactory.createContinousInputData();
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, data);
//
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        scenarioFile.store(TestFiles.experimental.resolve("tempdemo").resolve("continous.json"));
//    }
//
//    @Test
//    void startContinous() throws IOException {
//        Path inputPath = TestFiles.experimental.resolve("tempdemo").resolve("continous.json");
//        Path outputPath = TestFiles.experimental.resolve("tempdemo").resolve("continous.output.json");
//        IRPactStarter starter = new IRPactStarter(inputPath, outputPath);
//        starter.start();
//    }
//
//    //=========================
//    //Discret
//    //=========================
//
//    @Test
//    void createDiscrete() {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData inputData = DefaultScenarioFactory.createDiscreteInputData();
//        ObjectNode rootNode = JsonUtil.mapper.createObjectNode();
//        converter.toGamsJson(inputData, rootNode);
//
//        System.out.println(JsonUtil.writeJson(rootNode));
//
//        IRPactInputData inputData2 = new IRPactInputData();
//        converter.fromGamsJson(rootNode, inputData2);
//
//        assertEquals(inputData, inputData2);
//
//
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, inputData);
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        ScenarioData<IRPactInputData> scenarioData2 = scenarioFile.deserialize(converter);
//
//        assertEquals(
//                scenarioData.get(0).getData(),
//                scenarioData2.get(0).getData()
//        );
//    }
//
//    @Test
//    void createDiscreteFile() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData data = DefaultScenarioFactory.createDiscreteInputData();
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, data);
//
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        scenarioFile.store(TestFiles.experimental.resolve("tempdemo").resolve("discrete.json"));
//    }
//
//    @Test
//    void startDiscrete() throws IOException {
//        Path inputPath = TestFiles.experimental.resolve("tempdemo").resolve("discrete.json");
//        Path outputPath = TestFiles.experimental.resolve("tempdemo").resolve("discrete.output.json");
//        IRPactStarter starter = new IRPactStarter(inputPath, outputPath);
//        starter.start();
//    }
//
//    //=========================
//    //Test-Continous
//    //=========================
//
//    private static final int continous_x = 3;
//
//    @Test
//    void createContinousForTest() {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData inputData = DefaultScenarioFactory.createContinousInputDataForTest(continous_x);
//        ObjectNode rootNode = JsonUtil.mapper.createObjectNode();
//        converter.toGamsJson(inputData, rootNode);
//
//        System.out.println(JsonUtil.writeJson(rootNode));
//
//        IRPactInputData inputData2 = new IRPactInputData();
//        converter.fromGamsJson(rootNode, inputData2);
//
//        assertEquals(inputData, inputData2);
//
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, inputData);
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        ScenarioData<IRPactInputData> scenarioData2 = scenarioFile.deserialize(converter);
//
//        assertEquals(
//                scenarioData.get(0).getData(),
//                scenarioData2.get(0).getData()
//        );
//    }
//
//    @Test
//    void createContinousFileForTest() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData data = DefaultScenarioFactory.createContinousInputDataForTest(continous_x);
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, data);
//
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        scenarioFile.store(TestFiles.experimental.resolve("tempdemo").resolve("continous_x.json"));
//    }
//
//    @Test
//    void startContinousForTest() throws IOException {
//        Path inputPath = TestFiles.experimental.resolve("tempdemo").resolve("continous_x.json");
//        Path outputPath = TestFiles.experimental.resolve("tempdemo").resolve("continous_x.output.json");
//        IRPactStarter starter = new IRPactStarter(inputPath, outputPath);
//        starter.start();
//    }
//
//    //=========================
//    //Test-Discrete
//    //=========================
//
//    private static final int discrete_x = 3;
//
//    @Test
//    void createDiscreteForTest() {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData inputData = DefaultScenarioFactory.createDiscreteInputDataForTest(discrete_x);
//        ObjectNode rootNode = JsonUtil.mapper.createObjectNode();
//        converter.toGamsJson(inputData, rootNode);
//
//        System.out.println(JsonUtil.writeJson(rootNode));
//
//        IRPactInputData inputData2 = new IRPactInputData();
//        converter.fromGamsJson(rootNode, inputData2);
//
//        assertEquals(inputData, inputData2);
//
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, inputData);
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        ScenarioData<IRPactInputData> scenarioData2 = scenarioFile.deserialize(converter);
//
//        assertEquals(
//                scenarioData.get(0).getData(),
//                scenarioData2.get(0).getData()
//        );
//    }
//
//    @Test
//    void createDiscreteFileForTest() throws IOException {
//        DefinitionCollection dcoll = AnnotationParser.parse(IRPactInputData.LIST);
//        DefinitionMapper dmap = new DefinitionMapper(dcoll);
//        Converter converter = new Converter(dmap);
//
//        IRPactInputData data = DefaultScenarioFactory.createDiscreteInputDataForTest(discrete_x);
//        ScenarioData<IRPactInputData> scenarioData = new ScenarioData<>();
//        scenarioData.add(2015, data);
//
//        ScenarioFile scenarioFile = scenarioData.serialize(converter);
//        scenarioFile.store(TestFiles.experimental.resolve("tempdemo").resolve("discrete_x.json"));
//    }
//
//    @Test
//    void startDiscreteForTest() throws IOException {
//        Path inputPath = TestFiles.experimental.resolve("tempdemo").resolve("discrete_x.json");
//        Path outputPath = TestFiles.experimental.resolve("tempdemo").resolve("discrete_x.output.json");
//        IRPactStarter starter = new IRPactStarter(inputPath, outputPath);
//        starter.start();
//    }
}
