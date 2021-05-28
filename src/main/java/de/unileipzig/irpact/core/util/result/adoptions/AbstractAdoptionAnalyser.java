package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.data.VarCollection;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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

    public void writeEnrties(CsvPrinter<? super Object> printer) throws IOException {
        for(Object[] row: getData().iterable()) {
            List<Object> rowAsList = Arrays.asList(row);
            printer.appendRow(rowAsList);
        }
    }

    protected String findString(AdoptionInfo info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getStringValue();
    }

    protected double findDouble(AdoptionInfo info, String key) {
        return info.getAgent()
                .findAttribute(key)
                .asValueAttribute()
                .getDoubleValue();
    }
}
