package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.util.StringUtil;
import org.apache.poi.common.usermodel.fonts.FontCharset;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class XlsxToCsvConverterOLD extends XlsxSheetHandler<String> {

    protected String lineSeparator = StringUtil.lineSeparator();
    protected String delimiter = ";";
    protected String quote = "\"";
    protected boolean forceQuote = false;
    protected boolean flush = true;

    protected Writer output;

    public XlsxToCsvConverterOLD() {
        this(ArrayList::new);
    }

    public XlsxToCsvConverterOLD(Supplier<? extends List<String>> rowListSupplier) {
        super(rowListSupplier);
        setTextConverter((columnIndex, header, value) -> value);
        setNumbericConverter((columnIndex, header, value) -> value.toString());
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setForceQuote(boolean forceQuote) {
        this.forceQuote = forceQuote;
    }

    public void setFlush(boolean flush) {
        this.flush = flush;
    }

    @Override
    public void reset() {
        super.reset();
        output = null;
    }

    protected String quoteIfRequired(String input) {
        if(forceQuote || input.contains(delimiter)) {
            return quote + input + quote;
        } else {
            return input;
        }
    }

    protected void flushIfRequired() throws IOException {
        if(flush) {
            output.flush();
        }
    }

    public void convert(Path xlsxPath, Path csvPath) throws IOException {
        convert(xlsxPath, 0, csvPath, StandardCharsets.UTF_8);
    }

    public void convert(Path xlsxPath, int sheetIndex, Path csvPath, Charset csvCharset) throws IOException {
        try(InputStream xlsxStream = Files.newInputStream(xlsxPath);
            BufferedWriter output = Files.newBufferedWriter(csvPath, csvCharset)) {

            XSSFWorkbook book = new XSSFWorkbook(xlsxStream);
            XSSFFont font = book.createFont();
            font.setCharSet(FontCharset.ANSI);
            XSSFSheet sheet = book.getSheetAt(sheetIndex);

            convert(sheet, output);
        }
    }

    public void convert(XSSFSheet sheet, Writer output) throws IOException {
        reset();
        this.sheet = sheet;
        this.output = output;

        try {
            parse();
        } catch (UncheckedIOException e) {
            throw e.getCause();
        }
    }

    @Override
    protected void handleHeader(String[] header) {
        try {
            handleHeader0(header);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void handleHeader0(String[] header) throws IOException {
        for(int i = 0; i < header.length; i++) {
            if(i > 0) {
                output.write(delimiter);
            }
            output.write(quoteIfRequired(header[i]));
        }
        output.write(lineSeparator);
        flushIfRequired();
    }

    @Override
    protected void handleInfoRow(Row row, int infoRowIndex) {
        try {
            handleInfoRow0(row, infoRowIndex);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void handleInfoRow0(Row row, int infoRowIndex) throws IOException {
        output.write("//info line " + infoRowIndex);
        output.write(lineSeparator);
        flushIfRequired();
    }

    @Override
    protected void handleRow(List<String> row, int rowIndex) {
        try {
            handleRow0(row, rowIndex);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void handleRow0(List<String> row, int rowIndex) throws IOException {
        for(int i = 0; i < row.size(); i++) {
            if(i > 0) {
                output.write(delimiter);
            }
            output.write(quoteIfRequired(row.get(i)));
        }
        output.write(lineSeparator);
        flushIfRequired();
    }

    @Override
    protected void handleEndOfData() {
        try {
            handleEndOfData0();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void handleEndOfData0() throws IOException {
        output.flush();
    }
}
