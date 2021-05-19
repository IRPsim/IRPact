package de.unileipzig.irpact.commons.util.xlsx;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public abstract class XlsxSheetHandler<T> {

    protected Supplier<? extends List<T>> rowListSupplier;

    protected CellValueConverter<Number, T> numbericConverter;
    protected CellValueConverter<String, T> textConverter;
    protected int numberOfInfoRows = 0;

    protected XSSFSheet sheet;
    protected String[] header;

    public XlsxSheetHandler() {
        this(ArrayList::new);
    }

    public XlsxSheetHandler(
            Supplier<? extends List<T>> rowListSupplier) {
        this.rowListSupplier = rowListSupplier;
    }

    public void setNumbericConverter(CellValueConverter<Number, T> numbericConverter) {
        this.numbericConverter = numbericConverter;
    }

    public void setTextConverter(CellValueConverter<String, T> textConverter) {
        this.textConverter = textConverter;
    }

    public void setNumberOfInfoRows(int numberOfInfoRows) {
        this.numberOfInfoRows = numberOfInfoRows;
    }

    protected void check() {
        if(header == null) {
            throw new NoSuchElementException();
        }
    }

    public String[] getHeader() {
        check();
        return header;
    }

    public void reset() {
        sheet = null;
        header = null;
    }

    protected Row nextRowOrNull(Iterator<Row> rowIter) {
        if(!rowIter.hasNext()) {
            return null;
        }
        return rowIter.next();
    }

    protected void parse() {
        Iterator<Row> rowIter = sheet.rowIterator();

        for(int i = 0; i < numberOfInfoRows; i++) {
            Row infoRow = nextRowOrNull(rowIter);
            if(infoRow == null && i == 0) throw new IllegalArgumentException("empty sheet");
            if(infoRow == null) throw new IllegalArgumentException("missing info row: " + i);
            handleInfoRow(infoRow, i);
        }

        Row headerRow = nextRowOrNull(rowIter);
        if(headerRow == null && numberOfInfoRows == 0) throw new IllegalArgumentException("empty sheet");
        if(headerRow == null) throw new IllegalArgumentException("missing header");
        header = XlsxUtil.extractHeader(headerRow);
        handleHeader(header);

        int rowIndex = 0;
        boolean eodCalled = false;
        while(rowIter.hasNext()) {
            Row dataRow = rowIter.next();
            List<T> rowList = XlsxUtil.extractRowData(
                    dataRow,
                    header,
                    numbericConverter,
                    textConverter,
                    rowListSupplier.get()
            );
            if(rowList == null) {
                handleEndOfData();
                eodCalled = true;
                break;
            }
            handleRow(rowList, rowIndex);
            rowIndex++;
        }
        if(!eodCalled) {
            handleEndOfData();
        }
    }

    protected abstract void handleInfoRow(Row row, int infoRowIndex);

    protected abstract void handleHeader(String[] header);

    protected abstract void handleRow(List<T> row, int rowIndex);

    protected abstract void handleEndOfData();
}
