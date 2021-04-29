package de.unileipzig.irpact.commons.util.csv;

import de.unileipzig.irpact.commons.util.StringUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class CsvPrinter<T> {

    protected CsvValuePrinter<? super T> toString;
    protected String lineSeparator = StringUtil.lineSeparator();
    protected String delimiter = ";";
    protected String quote = "\"";
    protected boolean forceQuote = false;
    protected boolean autoFlush = true;

    public CsvPrinter(Function<? super T, ? extends String> toString) {
        this((columnIndex, header, value) -> toString.apply(value));
    }

    public CsvPrinter(CsvValuePrinter<? super T> toString) {
        this.toString = toString;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getQuote() {
        return quote;
    }

    public void setForceQuote(boolean forceQuote) {
        this.forceQuote = forceQuote;
    }

    public boolean isForceQuote() {
        return forceQuote;
    }

    public void setAutoFlush(boolean autoFlush) {
        this.autoFlush = autoFlush;
    }

    public boolean isAutoFlush() {
        return autoFlush;
    }

    protected String quoteIfRequired(String input) {
        if(forceQuote || input.contains(delimiter)) {
            return quote + input + quote;
        } else {
            return input;
        }
    }

    protected void flushIfRequired(Writer writer) throws IOException {
        if(autoFlush) {
            writer.flush();
        }
    }

    public String toString(List<String> header, List<List<T>> rows) {
        try {
            StringWriter sw = new StringWriter();
            write(sw, header, rows);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void write(Path path, Charset charset, List<String> header, List<List<T>> rows) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            write(writer, header, rows);
        }
    }

    public void write(Writer writer, List<String> header, List<List<T>> rows) throws IOException {
        boolean first = true;
        for(String h: header) {
            if(!first) {
                writer.write(delimiter);
            }
            first = false;
            writer.write(quoteIfRequired(h));
        }

        String[] headerArr = header.toArray(new String[0]);;
        for(List<T> row: rows) {
            writer.write(lineSeparator);
            first = true;
            int columnIndex = 0;
            for(T value: row) {
                if(!first) {
                    writer.write(delimiter);
                }
                first = false;
                String valueStr = toString.toString(columnIndex++, headerArr, value);
                writer.write(quoteIfRequired(valueStr));
            }
        }
    }
}
