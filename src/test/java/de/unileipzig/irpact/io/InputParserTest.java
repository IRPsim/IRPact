package de.unileipzig.irpact.io;

import de.unileipzig.irpact.disabled.TestPaths;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Daniel Abitz
 */
@Disabled("Herausfinden, wie ich die Pfade effektiver hinbekomme")
class InputParserTest {

    private static final Path def1Dir = TestPaths.test.resolve(Paths.get("java", "de", "unileipzig", "irpact", "testdata", "def1"));

    @Test
    void testParseSingleInput() throws IOException {
        Input input = InputParser.parse(
                def1Dir.resolve("Data1.java"),
                StandardCharsets.UTF_8
        );

        Input customInput = new Input();
        customInput.putParam("description", "Data-Tiefe 1");
        customInput.putParam("type", "String");
        customInput.putParam("identifier", "Data-Tiefe 1");
        customInput.putParam("unit", "");
        customInput.putParam("domain", "");
        customInput.putParam("validation", "");
        customInput.putParam("hidden", "");
        customInput.putParam("processing", "");
        customInput.setType("class");
        customInput.setName("Data1");

        InputField field1 = new InputField();
        field1.putParam("description", "");
        field1.putParam("type", "float");
        field1.putParam("identifier", "");
        field1.putParam("unit", "");
        field1.putParam("domain", "");
        field1.putParam("validation", "");
        field1.putParam("hidden", "");
        field1.putParam("processing", "");
        field1.setType("double");
        field1.setName("value1");
        field1.setArray(false);
        customInput.addField(field1);

        InputField field2 = new InputField();
        field2.putParam("description", "Zugeh\u00f6rigkeit zu Data 2");
        field2.putParam("identifier", "Zugeh\u00f6rigkeit zu Data 2");
        field2.setType("Data2");
        field2.setName("data2s");
        field2.setArray(true);
        customInput.addField(field2);

        assertEquals(customInput, input);

        List<MappedInput> mapped = input.toMappedInput();
        assertEquals(3, mapped.size());

        MappedInput mi0 = new MappedInput();
        mi0.putParam("description", "Data-Tiefe 1");
        mi0.putParam("type", "String");
        mi0.putParam("identifier", "Data-Tiefe 1");
        mi0.putParam("unit", "");
        mi0.putParam("domain", "");
        mi0.putParam("validation", "");
        mi0.putParam("hidden", "");
        mi0.putParam("processing", "");
        mi0.setPrefix("SET");
        mi0.setName("set_Data1");
        assertEquals(mi0, mapped.get(0));

        MappedInput mi1 = new MappedInput();
        mi1.putParam("description", "");
        mi1.putParam("type", "float");
        mi1.putParam("identifier", "");
        mi1.putParam("unit", "");
        mi1.putParam("domain", "");
        mi1.putParam("validation", "");
        mi1.putParam("hidden", "");
        mi1.putParam("processing", "");
        mi1.setPrefix("PARAMETER");
        mi1.setName("par_Data1_value1");
        mi1.addDep("set_Data1");
        assertEquals(mi1, mapped.get(1));

        MappedInput mi2 = new MappedInput();
        mi2.putParam("description", "Zugeh\u00f6rigkeit zu Data 2");
        mi2.putParam("identifier", "Zugeh\u00f6rigkeit zu Data 2");
        mi2.putParam("type", "Boolean"); //!
        mi2.setPrefix("PARAMETER");
        mi2.setName("par_Data1_Data2");
        mi2.addDep("set_Data1");
        mi2.addDep("set_Data2");
        assertEquals(mi2, mapped.get(2));

        String mi2Gams = "* - description: Zugeh\u00f6rigkeit zu Data 2"
                + "\n* - identifier: Zugeh\u00f6rigkeit zu Data 2"
                + "\n* - type: Boolean"
                + "\nPARAMETER par_Data1_Data2(set_Data1,set_Data2)";
        assertEquals(mi2Gams, mi2.printGAMS());
    }

    @Test
    void testGlobal() throws IOException {
        Input input = InputParser.parse(
                def1Dir.resolve("_Global.java"),
                StandardCharsets.UTF_8
        );
        InputMapping im = new InputMapping();
        im.put(input);
        InputMapping.Result result = im.getResult();
        System.out.println(result.printGAMS());
    }

    @Test
    void testParse() throws IOException {
        InputMapping mapping = InputMapping.parseDir(def1Dir);
        InputMapping.Result result = mapping.getResult();
        assertEquals(23, result.size());

        List<MappedInput> tablesEntries = result.getMapping().stream()
                .filter(mi -> mi.getDeps().size() == 2)
                .collect(Collectors.toList());
        assertEquals(5, tablesEntries.size());

        List<MappedInput> scalarEntries = result.getMapping().stream()
                .filter(mi -> Constants.PREFIX_SCALAR.equals(mi.getPrefix()))
                .collect(Collectors.toList());
        assertEquals(3, scalarEntries.size());

        System.out.println(result.printGAMS());
    }
}