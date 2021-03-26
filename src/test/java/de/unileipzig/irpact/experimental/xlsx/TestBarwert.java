package de.unileipzig.irpact.experimental.xlsx;

import de.unileipzig.irpact.commons.util.xlsx.ComplexXlsxTable;
import de.unileipzig.irpact.commons.util.xlsx.KeyValueXlsxTable;
import de.unileipzig.irpact.commons.util.xlsx.SimplifiedXlsxTable;
import de.unileipzig.irpact.commons.util.xlsx.XlsxUtil;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.develop.TestFiles;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
@Disabled
public class TestBarwert {

    @Test
    void testGis() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("0data").resolve("GIS_final_1.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        XSSFSheet sheet = book.getSheetAt(0);
        ComplexXlsxTable table = new ComplexXlsxTable();
        table.parse(sheet);

        System.out.println(Arrays.toString(table.getHeader()));
        System.out.println(table.getNumberOfRows());
    }

    @Test
    void testGisCsv() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("0data").resolve("GIS_final_1.csv");
        int i = 0;
        try(BufferedReader br = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String line;
            while((line = br.readLine()) != null) {
                i++;
            }
        }
        System.out.println(i);
    }

    @Test
    void testGisCsv2() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("0data").resolve("GIS_final_2.csv");
        int i = 0;
        try(BufferedReader br = Files.newBufferedReader(path, StandardCharsets.ISO_8859_1)) {
            String line;
            while((line = br.readLine()) != null) {
                i++;
                System.out.println(line);
                if(i == 35) {
                    return;
                }
            }
        }
        System.out.println(i);
    }

    @Test
    void testKeyValue() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        KeyValueXlsxTable table = XlsxUtil.extractKeyValueTable(book.getSheetAt(0));
        System.out.println(table.print());
    }

    @Test
    void testSimple() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        SimplifiedXlsxTable table = XlsxUtil.extractTableWithWithTwoHeaderLines(book.getSheetAt(3));
        System.out.println(table.print());
    }

    @Test
    void testMulti() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        Map<String, SimplifiedXlsxTable> tables = XlsxUtil.extractTablesWithTwoHeaderLines(book, 1);
        tables.forEach((name, table) -> {
            System.out.println(name);
            System.out.println(table.print());
            System.out.println();
        });
    }

    @Test
    void testNPVData() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        NPVXlsxData data = new NPVXlsxData();
        data.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
        data.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));

//        double N = 70;
//        double A = 45;
//        int t0 = 2020;

        double N = 70;
        double A = 0;
        int t0 = 2020;

        System.out.println(data.getGlobalstrahlung(N));
        System.out.println(data.getAusrichtungsfaktor(A));
        System.out.println();
        System.out.println(data.getModulwirkungsgradProzent(t0));
        System.out.println(data.getPerformanceRatioProzent(t0));
        System.out.println(data.getDegradation());
        System.out.println();
        System.out.println(data.getEinspeiseverguetung(t0));
        System.out.println(data.getStrompreis(t0));
        System.out.println(data.getStromkostenanstieg());
        System.out.println(data.getEigenverbrauch());
        System.out.println();
        System.out.println(data.getNettosystempreis(t0));
        System.out.println();
        System.out.println(data.getZinssatzProzent(t0));
    }

    @Test
    void testNPVData2() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        NPVXlsxData data = new NPVXlsxData();
        data.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
        data.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));
        NPVCalculator calculator = new NPVCalculator();
        calculator.setData(data);

//        double N = 70;
//        double A = 45;
//        int t0 = 2020;

        double N = 24;
        double A = 42;
        int t0 = 2011;

        double sumNutzertrag = 0.0;
        double nbwEinnahmen = 0.0;
        double zins = data.getZinssatzProzent(t0);
        for(int t = 0; t <= 20; t++) {
            double nutzertrag = calculator.E(t0, N, A, t);
            double einnahmen = calculator.calcEinnahmen(t0, N, A, t);
            System.out.println(t + " " + nutzertrag + " " + einnahmen);
            sumNutzertrag += nutzertrag;
            nbwEinnahmen += einnahmen / Math.pow(1 + zins, t + 1);
        }
        System.out.println("nutzertrag: " + sumNutzertrag);
        System.out.println("einnahmen: " + nbwEinnahmen);
        System.out.println("NPV: " + calculator.NPV(t0, N, A));
    }

    @Test
    void testNPMatrix() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        NPVXlsxData data = new NPVXlsxData();
        data.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
        data.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));
        NPVCalculator calculator = new NPVCalculator();
        calculator.setData(data);

        long start = System.currentTimeMillis();
        Map<Integer, NPVMatrix> matrices = new LinkedHashMap<>();
        for(int t0 = 2006; t0 <= 2020; t0++) {
            NPVMatrix NPVMatrix = new NPVMatrix();
            NPVMatrix.calculate(calculator, t0);
            matrices.put(t0, NPVMatrix);
        }
        long ende = System.currentTimeMillis();
        System.out.println(matrices.keySet());
        System.out.println("time: " + (ende - start));
    }

    @Test
    void createExcel() throws IOException, InvalidFormatException {
        Path path = TestFiles.testfiles.resolve("xlsxtest").resolve("BarwertrechnerMini_ES.xlsx");
        XSSFWorkbook book = XlsxUtil.load(path);
        NPVXlsxData data = new NPVXlsxData();
        data.setAllgemeinSheet(XlsxUtil.extractKeyValueTable(book.getSheetAt(0)));
        data.putAllTables(XlsxUtil.extractTablesWithTwoHeaderLines(book, 1));
        NPVCalculator calculator = new NPVCalculator();
        calculator.setData(data);
        Map<Integer, NPVMatrix> matrices = new LinkedHashMap<>();
        for(int t0 = 2006; t0 <= 2020; t0++) {
            NPVMatrix NPVMatrix = new NPVMatrix();
            NPVMatrix.calculate(calculator, t0);
            matrices.put(t0, NPVMatrix);
        }

        XSSFWorkbook outBook = new XSSFWorkbook();
        for(Map.Entry<Integer, NPVMatrix> entry: matrices.entrySet()) {
            XSSFSheet sheet = outBook.createSheet(Integer.toString(entry.getKey()));
            NPVMatrix matrix = entry.getValue();
            createFirstRow(matrix, sheet);
            for(int N = 0; N < matrix.getNmax(); N++) {
                createRow(matrix, N, sheet);
            }
        }

        Path outPath = TestFiles.testfiles.resolve("xlsxtest").resolve("Barwertmatrizen_neu.xlsx");
        Files.deleteIfExists(outPath);
        XlsxUtil.store(outPath, outBook);
    }

    private void createFirstRow(NPVMatrix matrix, XSSFSheet sheet) {
        Row row = sheet.createRow(0);
        int cellNum = 0;
        Cell cell0 = row.createCell(cellNum++);
        cell0.setCellValue("Ausrichtung/Neigung");
        for(int A = 0; A < matrix.getAmax(); A++) {
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(A);
        }
    }

    private void createRow(NPVMatrix matrix, int N, XSSFSheet sheet) {
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.createRow(rowNum + 1);
        int cellNum = 0;
        Cell cell0 = row.createCell(cellNum++);
        cell0.setCellValue(N);
        for(int A = 0; A < matrix.getAmax(); A++) {
            double npv = matrix.getValue(N, A);
            Cell cell = row.createCell(cellNum++);
            cell.setCellValue(npv);
        }
    }

    //N=70, A=45, t=2020 -> 3529
    //N=30, A=0, t=2011 -> 2197
}
