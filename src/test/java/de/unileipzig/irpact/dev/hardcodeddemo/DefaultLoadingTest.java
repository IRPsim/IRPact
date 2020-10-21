package de.unileipzig.irpact.dev.hardcodeddemo;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.JsonUtil;
import de.unileipzig.irpact.start.irpact.input.IRPactInputData;
import de.unileipzig.irptools.defstructure.AnnotationParser;
import de.unileipzig.irptools.defstructure.Converter;
import de.unileipzig.irptools.defstructure.DefinitionCollection;
import de.unileipzig.irptools.defstructure.DefinitionMapper;
import de.unileipzig.irptools.io.perennial.PerennialData;
import de.unileipzig.irptools.io.perennial.PerennialFile;
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

    @Disabled("UI-TEST")
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

        PerennialFile sfile = new PerennialFile(root);
        PerennialData<IRPactInputData> sdata = sfile.deserialize(converter);
        assertEquals(1, sdata.getList().size());
        assertEquals(2015, sdata.get(0).getConfig().getYear());
        assertEquals(3, sdata.get(0).getData().agentGroups.length);
    }
}