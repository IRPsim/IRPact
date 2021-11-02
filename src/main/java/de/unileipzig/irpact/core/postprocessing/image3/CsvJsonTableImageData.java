package de.unileipzig.irpact.core.postprocessing.image3;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.csv.CsvPrinter;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;

import java.io.IOException;
import java.io.StringWriter;
import java.io.UncheckedIOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public class CsvJsonTableImageData implements ImageData {

    protected JsonTableData3 data;
    protected String delimiter = ";";

    public CsvJsonTableImageData() {
    }

    public CsvJsonTableImageData(JsonTableData3 data) {
        this(data, ";");
    }

    public CsvJsonTableImageData(JsonTableData3 data, String delimiter) {
        setData(data);
        setDelimiter(delimiter);
    }

    public void setData(JsonTableData3 data) {
        this.data = data;
    }

    public JsonTableData3 getData() {
        return data;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    @Override
    public void writeTo(Path target, Charset charset) throws IOException {
        CsvPrinter<JsonNode> printer = new CsvPrinter<>();
        printer.setValueSetter(CsvPrinter.forJson);
        printer.setDelimiter(delimiter);
        printer.write(target, charset, data);
    }

    public String print() {
        try {
            StringWriter writer = new StringWriter();
            CsvPrinter<JsonNode> printer = new CsvPrinter<>();
            printer.setValueSetter(CsvPrinter.forJson);
            printer.setDelimiter(delimiter);
            printer.write(writer, data);
            writer.close();
            return writer.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
