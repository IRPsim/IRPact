package de.unileipzig.irpact.start.hardcodeddemo.def;

import de.unileipzig.irpact.v2.develop.ToDo;

/**
 * @author Daniel Abitz
 */
@ToDo("neue io-file und daten typen einbauen")
class DefaultLoadingTest {
//
//    @Disabled("example1.json wurde geaendert")
//    @Test
//    void testLoadDefaultOLD() throws URISyntaxException {
//        String fileName = "scenarios/example1.json";
//        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
//        assertNotNull(url, "not found: '" + fileName + "'");
//
//        Path path = Paths.get(url.toURI());
//        assertTrue(Files.exists(path), "not exists");
//
//        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
//        assertTrue(root.has("years"));
//
//        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(InputRoot.CLASSES));
//        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
//        Converter converter = new Converter(dmap);
//
//        ScenarioFile sfile = new ScenarioFile(root);
//        ScenarioData<InputRoot> sdata = sfile.deserialize(converter);
//        assertEquals(2, sdata.getList().size());
//        assertEquals(2015, sdata.get(0).getYear());
//        assertEquals(2016, sdata.get(1).getYear());
//    }
//
//    @Test
//    void testLoadDefault() throws URISyntaxException {
//        String fileName = "scenarios/example1.json";
//        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
//        assertNotNull(url, "not found: '" + fileName + "'");
//
//        Path path = Paths.get(url.toURI());
//        assertTrue(Files.exists(path), "not exists");
//
//        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
//        assertTrue(root.has("years"));
//
//        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(IRPactInputData.LIST));
//        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
//        Converter converter = new Converter(dmap);
//
//        ScenarioFile sfile = new ScenarioFile(root);
//        ScenarioData<IRPactInputData> sdata = sfile.deserialize(converter);
//        assertEquals(1, sdata.getList().size());
//        assertEquals(2015, sdata.get(0).getYear());
//        assertEquals(3, sdata.get(0).getData().agentGroups.length);
//    }
//
//    @Disabled("example1.json wurde geaendert")
//    @Test
//    void testLoadInputOLD() throws URISyntaxException {
//        String fileName = "examples/default-year2016.json";
//        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
//        assertNotNull(url, "not found: '" + fileName + "'");
//
//        Path path = Paths.get(url.toURI());
//        assertTrue(Files.exists(path), "not exists");
//
//        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
//        assertTrue(root.has("sets"));
//
//        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(InputRoot.CLASSES));
//        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
//        Converter converter = new Converter(dmap);
//
//        InputFile ifile = new InputFile(root);
//        InputData<InputRoot> idata = ifile.deserialize(converter);
//        assertEquals(2016, idata.getConfig().getYear());
//    }
}