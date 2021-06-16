package de.unileipzig.irpact.commons.util.csv;

import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.commons.util.io.Header;
import de.unileipzig.irpact.commons.util.io.SimpleHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class CsvParser<T> {

    protected int numberOfInfoRows = 0;
    protected boolean keepQuotes = false;
    protected String delimiter = ";";
    protected String quote = "\"";

    protected SimpleHeader header;
    protected CsvValueConverter<T> converter;
    protected Supplier<? extends List<T>> rowSupplier = ArrayList::new;
    protected Consumer<? super List<T>> rowConsumer;

    protected BufferedReader reader;

    public CsvParser() {
    }

    //=========================
    //access
    //=========================

    public void setNumberOfInfoRows(int numberOfInfoRows) {
        this.numberOfInfoRows = numberOfInfoRows;
    }

    public void setKeepQuotes(boolean keepQuotes) {
        this.keepQuotes = keepQuotes;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setConverter(CsvValueConverter<T> converter) {
        this.converter = converter;
    }

    public void setRowSupplier(Supplier<? extends List<T>> rowSupplier) {
        this.rowSupplier = rowSupplier;
    }

    public void setRowConsumer(Consumer<? super List<T>> rowConsumer) {
        this.rowConsumer = rowConsumer;
    }

    public Header getHeader() {
        return header;
    }

    //=========================
    //parse
    //=========================

    public List<List<T>> parseToList(Path input, Charset charset) throws IOException {
        List<List<T>> rows = new ArrayList<>();
        collect(input, charset, rows);
        return rows;
    }

    public List<List<T>> parseToList(InputStream in, Charset charset) throws IOException {
        try(InputStreamReader reader = new InputStreamReader(in, charset);
            BufferedReader bReader = new BufferedReader(reader)) {
            return parseToList(bReader);
        }
    }

    public List<List<T>> parseToList(BufferedReader reader) throws IOException {
        List<List<T>> rows = new ArrayList<>();
        collect(reader, rows);
        return rows;
    }

    public boolean collect(Path input, Charset charset, Collection<? super List<T>> target) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(input, charset)) {
            MutableBoolean changed = MutableBoolean.falseValue();
            setRowConsumer(row -> changed.or(target.add(row)));
            setReader(reader);
            parse();
            return changed.get();
        } finally {
            setReader(null);
            setRowConsumer(null);
        }
    }

    public boolean collect(BufferedReader reader, Collection<? super List<T>> target) throws IOException {
        try {
            MutableBoolean changed = MutableBoolean.falseValue();
            setRowConsumer(row -> changed.or(target.add(row)));
            setReader(reader);
            parse();
            return changed.get();
        } finally {
            setReader(null);
            setRowConsumer(null);
        }
    }

    //=========================
    //intern
    //=========================

    protected String trimQuotes(String input) {
        if(keepQuotes) {
            return input;
        }
        boolean startsWith = input.startsWith(quote);
        boolean endsWith = input.endsWith(quote);
        if(startsWith && endsWith) {
            return input.substring(
                    quote.length(),
                    input.length() - quote.length()
            );
        } else {
            return input;
        }
    }

    protected String[] splotRow(String row) {
        return row.split(delimiter);
    }

    @SuppressWarnings("RedundantThrows")
    protected void handleInfo(int infoRowIndex, String infoRow) throws IOException {
    }

    @SuppressWarnings("RedundantThrows")
    protected void handleHeader(String headerRow) throws IOException {
        String[] headerData = splotRow(headerRow);
        header = new SimpleHeader();
        for(String headerLabel: headerData) {
            header.add(trimQuotes(headerLabel));
        }
    }

    @SuppressWarnings("RedundantThrows")
    protected void handleRow(String row, int rowIndex) throws IOException {
        String[] parts = splotRow(row);
        if(parts.length != header.length()) {
            throw new IllegalArgumentException("(row " + rowIndex + ") length = " + parts.length + " != " + header.length() + " (header)");
        }

        List<T> rowData = rowSupplier.get();
        for(int i = 0; i < header.length(); i++) {
            T value = converter.convert(header, i, trimQuotes(parts[i]));
            rowData.add(value);
        }
        rowConsumer.accept(rowData);
    }

    @SuppressWarnings("RedundantThrows")
    protected void handleEndOfData() throws IOException {
    }

    protected void resetParse() {
        header = null;
    }

    protected void parse() throws IOException {
        resetParse();

        for(int i = 0; i < numberOfInfoRows; i++) {
            String row = reader.readLine();
            if(row == null && i == 0) throw new IllegalArgumentException("empty");
            if(row == null) throw new IllegalArgumentException("missing info row: " + i);
            handleInfo(i, row);
        }

        String headerRow = reader.readLine();
        if(headerRow == null && numberOfInfoRows == 0) throw new IllegalArgumentException("empty");
        if(headerRow == null) throw new IllegalArgumentException("missing header");
        handleHeader(headerRow);

        int rowIndex = 0;
        String row;
        while((row = reader.readLine()) != null) {
            handleRow(row, rowIndex);
            rowIndex++;
        }

        handleEndOfData();
    }
}
