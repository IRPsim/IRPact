package de.unileipzig.irpact.experimental.xlsx;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.TextNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.commons.util.xlsx.XlsxSheetParser;
import de.unileipzig.irpact.commons.util.xlsx.SimpleXlsxTableWriter;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Disabled
class Xxsxs {

    @Test
    void runIt() throws IOException, InvalidFormatException {
        Path in = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "210225_Datensatz.xlsx");
        Path out = Paths.get("D:\\Prog\\JetBrains\\SUSICProjects\\IRPact\\src\\main\\resources\\irpacttempdata", "210225_Datensatz.out.xlsx");

        JsonNodeCreator creator = IRPactJson.JSON.getNodeFactory();

        XlsxSheetParser<JsonNode> parser = new XlsxSheetParser<>();
        parser.setTextConverter((columnIndex, header, value) -> {
            if("Eigentum".equals(header[columnIndex])) {
                double v = "Privat".equals(value) ? 1 : 0;
                return creator.numberNode(v);
            } else {
                return creator.textNode(value);
            }
        });
        parser.setNumbericConverter((columnIndex, header, value) -> {
            if("HH_Anzahl".equals(header[columnIndex])) {
                double v = value.doubleValue() == 1 || value.doubleValue() == 2 ? 1 : 0;
                return creator.numberNode(v);
            } else {
                return creator.numberNode(value.doubleValue());
            }
        });
        parser.parse(in, 0);
        String[] header = parser.getHeader();
        List<List<JsonNode>> rows = parser.getRows();

        SimpleXlsxTableWriter<JsonNode> writer = new SimpleXlsxTableWriter<>();
        writer.registerForNumber(DoubleNode.class);
        writer.registerForText(TextNode.class);
        writer.setNumberConverter(JsonNode::doubleValue);
        writer.setTextConverter(JsonNode::textValue);

        writer.write(
                null,
                header,
                rows,
                out
        );
    }
}
