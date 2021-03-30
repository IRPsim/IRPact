package de.unileipzig.irpact.commons.util.xlsx;

import de.unileipzig.irpact.commons.exception.UncheckedException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class SimpleXlsxTableParser<T> {

    private Supplier<? extends List<List<T>>> list0Supplier;
    private Supplier<? extends List<T>> list1Supplier;
    private ParserFunction<Number, T> numbericConverter;
    private ParserFunction<String, T> textConverter;

    private List<List<T>> rows;
    private String comment;
    private String[] header;
    private XSSFSheet sheet;

    public SimpleXlsxTableParser() {
        this(ArrayList::new, ArrayList::new);
    }

    public SimpleXlsxTableParser(
            Supplier<? extends List<List<T>>> list0Supplier,
            Supplier<? extends List<T>> list1Supplier) {
        this.list0Supplier = list0Supplier;
        this.list1Supplier = list1Supplier;
    }

    public void setNumbericConverter(ParserFunction<Number, T> numbericConverter) {
        this.numbericConverter = numbericConverter;
    }

    public void setTextConverter(ParserFunction<String, T> textConverter) {
        this.textConverter = textConverter;
    }

    protected void check() {
        if(header == null) {
            throw new NoSuchElementException();
        }
    }

    public String getComment() {
        check();
        return comment;
    }

    public String[] getHeader() {
        check();
        return header;
    }

    public List<List<T>> getRows() {
        check();
        return rows;
    }

    public void reset() {
        sheet = null;
        rows = list0Supplier.get();
        header = null;
        comment = null;
    }

    public void parse(Path path, int sheetIndex) throws IOException, InvalidFormatException {
        try(XSSFWorkbook book = new XSSFWorkbook(path.toFile())) {
            parse(book.getSheetAt(sheetIndex));
        }
    }

    public void parse(XSSFSheet sheet) {
        reset();
        this.sheet = sheet;

        parse();
    }

    private void parse() {
        Iterator<Row> rowIter = sheet.rowIterator();
        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("empty sheet");
        }
        Row infoRow = rowIter.next(); //erstmal skippen

        if(!rowIter.hasNext()) {
            throw new IllegalArgumentException("missing header");
        }
        Row headerRow = rowIter.next();
        header = XlsxUtil.extractHeader(headerRow);

        while(rowIter.hasNext()) {
            Row dataRow = rowIter.next();
            List<T> rowList = extractRowData(dataRow, header, sheet.getSheetName());
            if(rowList == null) {
                //blank row -> end of data
                break;
            }
            rows.add(rowList);
        }
    }

    private List<T> extractRowData(Row row, String[] header, String sheetName) {
        List<T> out = list1Supplier.get();
        Iterator<Cell> cellIter = row.cellIterator();
        int index = 0;
        while(cellIter.hasNext() && index < header.length) {
            Cell cell = cellIter.next();
            switch(cell.getCellType()) {
                case NUMERIC:
                case FORMULA:
                    double numValue = cell.getNumericCellValue();

                    //apply cell format
                    CellStyle cs = cell.getCellStyle();
                    String format = cs.getDataFormatString();
                    if(!"General".equals(format)) {
                        DecimalFormat df = new DecimalFormat(format);
                        String formatted = df.format(numValue);
                        try {
                            numValue = df.parse(formatted).doubleValue();
                        } catch (ParseException e) {
                            throw new IllegalArgumentException(e);
                        }
                    }
                    //===
                    T numEntry = numbericConverter.convert(index, header, numValue);
                    out.add(numEntry);
                    break;

                case STRING:
                    String strValue = cell.getStringCellValue();
                    T textEntry = textConverter.convert(index, header, strValue);
                    out.add(textEntry);
                    break;

                default:
                    if(cell.getCellType() == CellType.BLANK) {
                        if(index == 0) {
                            if(XlsxUtil.isBlankRow(cellIter, header.length)) {
                                return null;
                            } else {
                                throw new IllegalArgumentException("unsupported cell type: "
                                        + cell.getCellType()
                                        + " (" + sheetName + ", " + index + ")"
                                );
                            }
                        } else {
                            String blankValue = cell.getStringCellValue();
                            T blankEntry = textConverter.convert(index, header, blankValue);
                            out.add(blankEntry);
                        }
                    } else {
                        throw new IllegalArgumentException("unsupported cell type: "
                                + cell.getCellType()
                                + " (" + sheetName + ", " + index + ")"
                        );
                    }
                    break;
            }
            index++;
        }
        return out;
    }

    /**
     * @author Daniel Abitz
     */
    public interface ParserFunction<S, T> {

        T convert(int columnIndex, String[] header, S value);
    }
}
