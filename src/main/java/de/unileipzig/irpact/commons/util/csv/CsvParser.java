package de.unileipzig.irpact.commons.util.csv;

import java.io.BufferedReader;
import java.io.IOException;
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
@SuppressWarnings("FieldMayBeFinal")
public class CsvParser<T> extends CsvHandler {

    private Supplier<? extends List<List<T>>> rowsSupplier;
    private Supplier<? extends List<T>> rowListSupplier;
    private List<List<T>> rows;

    private CsvPartConverter<T> converter;

    public CsvParser() {
        this(ArrayList::new, ArrayList::new);
    }

    public CsvParser(
            Supplier<? extends List<List<T>>> rowsSupplier,
            Supplier<? extends List<T>> rowListSupplier) {
        this.rowsSupplier = rowsSupplier;
        this.rowListSupplier = rowListSupplier;
    }


    public void setConverter(CsvPartConverter<T> converter) {
        this.converter = converter;
    }

    public void reset() {
        rows = rowsSupplier.get();
        reader = null;
    }

    public void parse(Path path) throws IOException {
        parse(path, StandardCharsets.UTF_8);
    }

    public void parse(Path path, Charset charset) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(path, charset)) {
            parse(reader);
        }
    }

    public void parse(BufferedReader reader) throws IOException {
        reset();
        this.reader = reader;

        parse();
    }

    public List<List<T>> getRows() {
        return rows;
    }

    @Override
    protected void handleInfoRow(String row, int infoRowIndex) {
        //ignore
    }

    @Override
    protected void handleHeader(String[] header) {
        //ignore
    }

    @Override
    protected void handleRow(String row, int rowIndex) {
        String[] parts = splitRow(row);
        List<T> rowData = rowListSupplier.get();
        for(int i = 0; i < header.length; i++) {
            T t = converter.convert(i, header, parts[i]);
            rowData.add(t);
        }
        rows.add(rowData);
    }

    @Override
    protected void handleEndOfDate() {
        //ignore
    }
}
