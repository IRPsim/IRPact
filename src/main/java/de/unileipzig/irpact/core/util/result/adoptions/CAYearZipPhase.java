package de.unileipzig.irpact.core.util.result.adoptions;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.csv.CsvValuePrinter;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class CAYearZipPhase extends AbstractCAYear2<String, AdoptionPhase> {

    public static final int INDEX_ZIP = INDEX_FIRST_VALUE;
    public static final int INDEX_PHASE = INDEX_SECOND_VALUE;

    public static final CsvValuePrinter<Object> VALUE_PRINTER = buildValuePrinter(Enum::name);
    public static final CsvValuePrinter<Object> CUMULATIVE_VALUE_PRINTER = buildCumulativeValuePrinter(Enum::name);

    public static CsvValuePrinter<Object> buildValuePrinter(Function<? super AdoptionPhase, ? extends String> phasePrinter) {
        if(phasePrinter == null) {
            throw new NullPointerException("phasePrinter");
        }

        return (columnIndex, header, value) -> {
            if(value == null) {
                throw new NullPointerException("value at index '" + columnIndex + "' is null");
            }
            switch (columnIndex) {
                case INDEX_YEAR:
                case INDEX_ZIP:
                    return value.toString();

                case INDEX_PHASE:
                    return phasePrinter.apply(((AdoptionPhase) value));

                case INDEX_ADOPTIONS:
                    return ((AdoptionData) value).printValue();

                default:
                    throw new IllegalArgumentException("unsupported index: " + columnIndex);
            }
        };
    }

    public static CsvValuePrinter<Object> buildCumulativeValuePrinter(Function<? super AdoptionPhase, ? extends String> phasePrinter) {
        if(phasePrinter == null) {
            throw new NullPointerException("phasePrinter");
        }

        return (columnIndex, header, value) -> {
            if(value == null) {
                throw new NullPointerException("value at index '" + columnIndex + "' is null");
            }
            switch (columnIndex) {
                case INDEX_YEAR:
                case INDEX_ZIP:
                    return value.toString();

                case INDEX_PHASE:
                    return phasePrinter.apply(((AdoptionPhase) value));

                case INDEX_ADOPTIONS:
                    return ((AdoptionData) value).printCumulativeValue();

                default:
                    throw new IllegalArgumentException("unsupported index: " + columnIndex);
            }
        };
    }

    //=========================
    //CAYearZipPhase2
    //=========================

    protected String zipKey;

    public CAYearZipPhase() {
        super();
        setZipKey(RAConstants.ZIP);
        setCsvHeader(new String[]{"year", "zip", "phase", "adoptions"});
    }

    @Override
    protected Class<String> getXClass() {
        return String.class;
    }

    @Override
    protected Class<AdoptionPhase> getYClass() {
        return AdoptionPhase.class;
    }

    public void setZipKey(String zipKey) {
        this.zipKey = zipKey;
    }

    public String getZipKey() {
        return zipKey;
    }

    public void initCsvPrinterForValue(CsvPrinter<Object> printer) {
        printer.setValuePrinter(VALUE_PRINTER);
    }

    public void initCsvPrinterForCumulativeValue(CsvPrinter<Object> printer) {
        printer.setValuePrinter(CUMULATIVE_VALUE_PRINTER);
    }

    @Override
    public void writeHeader(CsvPrinter<?> printer) throws IOException {
        printer.writeHeader(csvHeader);
    }

    @Override
    protected void add(boolean currentYear, int year, AdoptionInfo info) {
        data.varUpdate(
                AdoptionData.ZERO, currentYear ? AdoptionData.ADD_BOTH : AdoptionData.ADD_CUMULATIVE,
                year,
                findString(info, getZipKey()),
                info.getPhase(),
                AdoptionData.ONE
        );
    }
}
