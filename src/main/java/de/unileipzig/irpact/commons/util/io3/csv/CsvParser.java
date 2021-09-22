package de.unileipzig.irpact.commons.util.io3.csv;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;

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

    protected boolean trimQuotes = false;
    protected String delimiter = ";";
    protected String quote = "\"";

    protected CsvValueGetter<T> valueGetter;
    protected Supplier<? extends List<T>> rowSupplier = ArrayList::new;
    protected Consumer<? super List<T>> rowConsumer;

    protected BufferedReader reader;

    public CsvParser() {
    }

    //=========================
    //access
    //=========================

    public void setTrimQuotes(boolean trimQuotes) {
        this.trimQuotes = trimQuotes;
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

    public void setValueGetter(CsvValueGetter<T> valueGetter) {
        this.valueGetter = valueGetter;
    }

    public void setRowSupplier(Supplier<? extends List<T>> rowSupplier) {
        this.rowSupplier = rowSupplier;
    }

    public void setRowConsumer(Consumer<? super List<T>> rowConsumer) {
        this.rowConsumer = rowConsumer;
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
    protected void handleRow(String row, int rowIndex) throws IOException {
        String[] parts = splotRow(row);

        List<T> rowData = rowSupplier.get();
        for(int column = 0; column < parts.length; column++) {
            String str = parts[column];
            if(trimQuotes) {
                str = trimQuotes(str);
            }
            CsvCell cell = new CsvCell(rowIndex, column, str);
            T value = valueGetter.get(cell);
            rowData.add(value);
        }

        rowConsumer.accept(rowData);
    }

    protected void parse() throws IOException {
        int rowIndex = 0;
        String row;
        while((row = reader.readLine()) != null) {
            handleRow(row, rowIndex);
            rowIndex++;
        }
    }

    public static CsvValueGetter<JsonNode> forJson() {
        return forJson(JsonUtil.JSON.getNodeFactory());
    }

    public static CsvValueGetter<JsonNode> forJson(JsonNodeCreator creator) {
        return cell -> {
            String value = cell.getValue();
            return value == null
                    ? creator.nullNode()
                    : creator.textNode(value);
            };
        }
}
