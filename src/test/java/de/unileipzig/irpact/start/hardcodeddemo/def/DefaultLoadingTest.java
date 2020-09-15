package de.unileipzig.irpact.start.hardcodeddemo.def;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.InputRoot;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.input.InputData;
import de.unileipzig.irptools.io.input.InputFile;
import de.unileipzig.irptools.io.scenario.ScenarioData;
import de.unileipzig.irptools.io.scenario.ScenarioFile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DefaultLoadingTest {

    @Disabled("default.json wurde geaendert")
    @Test
    void testLoadDefaultOLD() throws URISyntaxException {
        String fileName = "scenarios/default.json";
        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
        assertNotNull(url, "not found: '" + fileName + "'");

        Path path = Paths.get(url.toURI());
        assertTrue(Files.exists(path), "not exists");

        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
        assertTrue(root.has("years"));

        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(InputRoot.CLASSES));
        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
        Converter converter = new Converter(dmap);

        ScenarioFile sfile = new ScenarioFile(root);
        ScenarioData<InputRoot> sdata = sfile.deserialize(converter);
        assertEquals(2, sdata.getList().size());
        assertEquals(2015, sdata.get(0).getYear());
        assertEquals(2016, sdata.get(1).getYear());
    }

    @Test
    void testLoadDefault() throws URISyntaxException {
        String fileName = "scenarios/default.json";
        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
        assertNotNull(url, "not found: '" + fileName + "'");

        Path path = Paths.get(url.toURI());
        assertTrue(Files.exists(path), "not exists");

        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
        assertTrue(root.has("years"));

        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(IRPactInputData.LIST));
        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
        Converter converter = new Converter(dmap);

        ScenarioFile sfile = new ScenarioFile(root);
        ScenarioData<IRPactInputData> sdata = sfile.deserialize(converter);
        assertEquals(1, sdata.getList().size());
        assertEquals(2015, sdata.get(0).getYear());
        assertEquals(3, sdata.get(0).getData().agentGroups.length);
    }

    @Disabled("default.json wurde geaendert")
    @Test
    void testLoadInputOLD() throws URISyntaxException {
        String fileName = "examples/default-year2016.json";
        URL url = DefaultLoadingTest.class.getClassLoader().getResource(fileName);
        assertNotNull(url, "not found: '" + fileName + "'");

        Path path = Paths.get(url.toURI());
        assertTrue(Files.exists(path), "not exists");

        ObjectNode root = assertDoesNotThrow(() -> JsonUtil.readJson(path));
        assertTrue(root.has("sets"));

        DefinitionCollection dcoll = assertDoesNotThrow(() -> AnnotationParser.parse(InputRoot.CLASSES));
        DefinitionMapper dmap = assertDoesNotThrow(() -> new DefinitionMapper(dcoll));
        Converter converter = new Converter(dmap);

        InputFile ifile = new InputFile(root);
        InputData<InputRoot> idata = ifile.deserialize(converter);
        assertEquals(2016, idata.getConfig().getYear());
    }
}