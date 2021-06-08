package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAdoptionAnalyser implements AdoptionAnalyser {

    protected VarCollection data;

    protected AbstractAdoptionAnalyser() {
    }

    public AbstractAdoptionAnalyser(VarCollection data) {
        this.data = data;
    }

    @Override
    public VarCollection getData() {
        return data;
    }

    public abstract void writeHeader(CsvPrinter<?> printer) throws IOException;

    public abstract String printHeader(CsvPrinter<?> printer);

    protected List<Object> arrayToList(Object[] arr) {
        return Arrays.asList(arr);
    }

    public void writeEntries(CsvPrinter<? super Object> printer) throws IOException {
        for(Object[] row: getData().iterable()) {
            List<Object> rowAsList = arrayToList(row);
            printer.appendRow(rowAsList);
        }
    }

    public void printEntries(CsvPrinter<? super Object> printer, Consumer<? super String> target) {
        for(Object[] row: getData().iterable()) {
            List<Object> rowAsList = arrayToList(row);
            String printed = printer.printRow(rowAsList);
            target.accept(printed);
        }
    }

    protected String findString(AdoptionEntry info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getStringValue();
    }

    protected double findDouble(AdoptionEntry info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getDoubleValue();
    }
}
