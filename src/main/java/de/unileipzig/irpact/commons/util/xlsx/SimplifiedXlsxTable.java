package de.unileipzig.irpact.commons.util.xlsx;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class SimplifiedXlsxTable {

    protected String[] header;
    protected List<double[]> rows;

    public SimplifiedXlsxTable() {
        this(new ArrayList<>());
    }

    public SimplifiedXlsxTable(List<double[]> rows) {
        this.rows = rows;
    }

    public void setHeader(String... header) {
        this.header = header;
    }

    public void addRow(double[] row) {
        if(row.length != header.length) {
            throw new IllegalArgumentException("row length mismatch");
        }
        rows.add(row);
    }

    public int getColumnIndex(String name) {
        for(int i = 0; i < header.length; i++) {
            if(Objects.equals(header[i], name)) {
                return i;
            }
        }
        return -1;
    }

    public int getRowIndex(int columnIndex, double rowValue) {
        for(int i = 0; i < rows.size(); i++) {
            double[] row = rows.get(i);
            if(row[columnIndex] == rowValue) {
                return i;
            }
        }
        return -1;
    }

    public double getRowValue(int columnIndex, int rowIndex) {
        double[] row = rows.get(rowIndex);
        return row[columnIndex];
    }

    private static String createSeparator(int len) {
        StringBuilder sb = new StringBuilder(len);
        for(int i = 0; i < len; i++) {
            sb.append('-');
        }
        return sb.toString();
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        String headerStr = Arrays.toString(header);
        sb.append(headerStr);
        sb.append('\n');
        sb.append(createSeparator(headerStr.length()));
        for(double[] row: rows) {
            sb.append('\n');
            sb.append(Arrays.toString(row));
        }
        return sb.toString();
    }
}
