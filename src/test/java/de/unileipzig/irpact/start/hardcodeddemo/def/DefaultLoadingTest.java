package de.unileipzig.irpact.start.hardcodeddemo.def;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.InputRoot;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.gamsjson.GamsJson;
import de.unileipzig.irptools.gamsjson.MappedGamsJson;
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

    @Test
    void testLoadDefault() throws URISyntaxException {
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

        MappedGamsJson<InputRoot> mappedGams = converter.fromGamsJson(GamsJson.Type.SCENARIO, root);
        assertEquals(2, mappedGams.getEntries().size());
        assertEquals(2015, mappedGams.getEntries().get(0).getYearEntry().getYear());
        assertEquals(2016, mappedGams.getEntries().get(1).getYearEntry().getYear());
    }

    @Test
    void testLoadInput() throws URISyntaxException {
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

        MappedGamsJson<InputRoot> mappedGams = converter.fromGamsJson(GamsJson.Type.INPUT, root);
        assertEquals(1, mappedGams.getEntries().size());
        assertEquals(2016, mappedGams.getEntries().get(0).getYearEntry().getYear());
    }
}