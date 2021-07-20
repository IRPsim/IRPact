package de.unileipzig.irpact.core.postprocessing.image;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class CsvBasedImageData implements ImageData {

    protected List<List<String>> dataWithHeader;
    protected String delimiter;

    public CsvBasedImageData(String delimiter, List<List<String>> dataWithHeader) {
        this.dataWithHeader = dataWithHeader;
        this.delimiter = delimiter;
    }

    @Override
    public void writeTo(Path target, Charset charset) throws IOException {
        CsvPrinter<String> printer = new CsvPrinter<>(CsvPrinter.STRING_IDENTITY);
        printer.setDelimiter(delimiter);
        try(BufferedWriter writer = Files.newBufferedWriter(target, charset)) {
            printer.setWriter(writer);
            printer.writeHeader(dataWithHeader.get(0));
            for(int i = 1; i < dataWithHeader.size(); i++) {
                printer.appendRow(dataWithHeader.get(i));
            }
        }
    }
}
