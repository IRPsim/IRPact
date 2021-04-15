package de.unileipzig.irpact.commons.util.csv;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public abstract class CsvHandler {

    protected int numberOfInfoRows = 0;
    protected boolean keepQuotes = false;
    protected String delimiter = ";";
    protected String quote = "\"";

    protected String[] header;

    protected BufferedReader reader;

    public CsvHandler() {
    }

    public void setKeepQuotes(boolean keepQuotes) {
        this.keepQuotes = keepQuotes;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setNumberOfInfoRows(int numberOfInfoRows) {
        this.numberOfInfoRows = numberOfInfoRows;
    }

    public String[] getHeader() {
        return header;
    }

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

    protected String[] parseHeader(String headerRow) {
        String[] header = splitRow(headerRow);
        for(int i = 0; i < header.length; i++) {
            header[i] = trimQuotes(header[i]);
        }
        return header;
    }

    protected String[] splitRow(String row) {
        return row.split(delimiter);
    }

    protected void parse() throws IOException {
        for(int i = 0; i < numberOfInfoRows; i++) {
            String row = reader.readLine();
            if(row == null && i == 0) throw new IllegalArgumentException("empty sheet");
            if(row == null) throw new IllegalArgumentException("missing info row: " + i);
            handleInfoRow(row, i);
        }

        String headerRow = reader.readLine();
        if(headerRow == null && numberOfInfoRows == 0) throw new IllegalArgumentException("empty sheet");
        if(headerRow == null) throw new IllegalArgumentException("missing header");
        header = parseHeader(headerRow);
        handleHeader(header);

        int rowIndex = 0;
        String row;
        while((row = reader.readLine()) != null) {
            handleRow(row, rowIndex);
            rowIndex++;
        }
        handleEndOfDate();
    }

    protected abstract void handleInfoRow(String row, int infoRowIndex);

    protected abstract void handleHeader(String[] header);

    protected abstract void handleRow(String row, int rowIndex);

    protected abstract void handleEndOfDate();
}
