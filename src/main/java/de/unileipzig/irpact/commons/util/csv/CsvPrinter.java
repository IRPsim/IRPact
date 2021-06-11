package de.unileipzig.irpact.commons.util.csv;

import de.unileipzig.irpact.commons.util.StringUtil;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class CsvPrinter<T> {

    public static CsvValuePrinter<String> STRING_IDENTITY = (columnIndex, header, value) -> value;

    protected CsvValuePrinter<? super T> valuePrinter;
    protected String lineSeparator = StringUtil.lineSeparator();
    protected String delimiter = ";";
    protected String quote = "\"";
    protected boolean forceQuote = false;
    protected boolean autoFlush = true;

    protected Writer writer;
    protected String[] header;

    public CsvPrinter() {
    }

    public CsvPrinter(Function<? super T, ? extends String> toString) {
        this((columnIndex, header, value) -> toString.apply(value));
    }

    public CsvPrinter(CsvValuePrinter<? super T> valuePrinter) {
        setValuePrinter(valuePrinter);
    }

    public void setValuePrinter(CsvValuePrinter<? super T> valuePrinter) {
        this.valuePrinter = valuePrinter;
    }

    public CsvValuePrinter<? super T> getValuePrinter() {
        return valuePrinter;
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

    public String toString(String[] header, Iterable<? extends Iterable<? extends T>> rows) {
        try {
            StringWriter sw = new StringWriter();
            write(sw, header, rows);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void write(Path path, Charset charset, String[] header, Iterable<? extends Iterable<? extends T>> rows) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, charset)) {
            write(writer, header, rows);
        }
    }

    public void write(Writer writer, String[] header, Iterable<? extends Iterable<? extends T>> rows) throws IOException {
        setWriter(writer);
        setHeader(header);
        writeHeader();
        appendRows(rows);
        setHeader(null);
        setWriter(null);
    }

    public void writeToString() {
        setWriter(new StringWriter());
    }

    public void setWriter(Writer writer) {
        this.writer = writer;
    }

    public Writer getWriter() {
        return writer;
    }

    public <W extends Writer> W getWriterAs(Class<W> c) {
        return c.cast(writer);
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    public void writeHeader() throws IOException {
        writeHeader(writer, true);
    }

    public void writeHeader(Collection<? extends String> header) throws IOException {
        writeHeader(header.toArray(new String[0]));
    }

    public void writeHeader(String[] header) throws IOException {
        writeHeader(writer, header, true);
    }

    protected void writeHeader(Writer writer, String[] header, boolean newline) throws IOException {
        String[] current = this.header;
        setHeader(header);
        writeHeader(writer, newline);
        setHeader(current);
    }

    protected void writeHeader(Writer writer, boolean newline) throws IOException {
        boolean first = true;
        for(String h: header) {
            if(!first) {
                writer.write(delimiter);
            }
            first = false;
            writer.write(quoteIfRequired(h));
        }
        if(newline) {
            writer.write(lineSeparator);
        }
        flushIfRequired(writer);
    }

    public String printHeader() {
        try {
            StringWriter sw = new StringWriter();
            writeHeader(sw, false);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public String printHeader(Collection<? extends String> header) {
        return printHeader(header.toArray(new String[0]));
    }

    public String printHeader(String[] header) {
        try {
            StringWriter sw = new StringWriter();
            writeHeader(sw, header, false);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void appendRows(Iterable<? extends Iterable<? extends T>> rows) throws IOException {
        for(Iterable<? extends T> row: rows) {
            appendRow(row);
        }
    }

    public void appendRowUnchecked(Iterable<? extends T> row) throws UncheckedIOException {
        try {
            appendRow(row);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public void appendRow(Iterable<? extends T> row) throws IOException {
        appendRow(writer, row, true);
    }

    public String printRow(Iterable<? extends T> row) {
        try {
            StringWriter sw = new StringWriter();
            appendRow(sw, row, false);
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void appendRow(Writer writer, Iterable<? extends T> row, boolean newline) throws IOException {
        if(valuePrinter == null) {
            throw new NullPointerException("valuePrinter");
        }

        boolean first = true;
        int columnIndex = 0;
        for(T value: row) {
            if(!first) {
                writer.write(delimiter);
            }
            first = false;
            String valueStr = valuePrinter.toString(columnIndex++, header, value);
            writer.write(quoteIfRequired(valueStr));
        }
        if(newline) {
            writer.write(lineSeparator);
        }
        flushIfRequired(writer);
    }
}
