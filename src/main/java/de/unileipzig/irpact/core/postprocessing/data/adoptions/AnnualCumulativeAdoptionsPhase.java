package de.unileipzig.irpact.core.postprocessing.data.adoptions;

import de.unileipzig.irpact.commons.util.csv.CsvPrinter;
import de.unileipzig.irpact.commons.util.csv.CsvValuePrinter;
import de.unileipzig.irpact.core.util.AdoptionPhase;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class AnnualCumulativeAdoptionsPhase extends AbstractAnnualCumulativeAdoptions1<AdoptionPhase> {

    public static final int INDEX_PHASE = INDEX_FIRST_VALUE;

    public static final CsvValuePrinter<Object> VALUE_PRINTER = buildValuePrinter(Enum::name);
    public static final CsvValuePrinter<Object> CUMULATIVE_VALUE_PRINTER = buildCumulativeValuePrinter(Enum::name);
    public static final CsvValuePrinter<Object> VALUE_AND_CUMULATIVE_VALUE_PRINTER = buildValueAndCumulativeValuePrinter(Enum::name);

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
                    return value.toString();

                case INDEX_PHASE:
                    return phasePrinter.apply(((AdoptionPhase) value));

                case INDEX_ADOPTIONS:
                    return ((AdoptionResultInfo) value).printValue();

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
                    return value.toString();

                case INDEX_PHASE:
                    return phasePrinter.apply(((AdoptionPhase) value));

                case INDEX_ADOPTIONS:
                    return ((AdoptionResultInfo) value).printCumulativeValue();

                default:
                    throw new IllegalArgumentException("unsupported index: " + columnIndex);
            }
        };
    }

    public static CsvValuePrinter<Object> buildValueAndCumulativeValuePrinter(Function<? super AdoptionPhase, ? extends String> phasePrinter) {
        if(phasePrinter == null) {
            throw new NullPointerException("phasePrinter");
        }

        return (columnIndex, header, value) -> {
            if(value == null) {
                throw new NullPointerException("value at index '" + columnIndex + "' is null");
            }
            switch (columnIndex) {
                case INDEX_YEAR:
                    return value.toString();

                case INDEX_PHASE:
                    return phasePrinter.apply(((AdoptionPhase) value));

                case INDEX_ADOPTIONS:
                    return ((AdoptionResultInfo) value).printValue();

                case INDEX_ADOPTIONS2:
                    return ((AdoptionResultInfo) value).printCumulativeValue();

                default:
                    throw new IllegalArgumentException("unsupported index: " + columnIndex);
            }
        };
    }

    //=========================
    //CAYearZipPhase2
    //=========================

    public AnnualCumulativeAdoptionsPhase() {
        super();
    }

    @Override
    protected Class<AdoptionPhase> getXClass() {
        return AdoptionPhase.class;
    }

    public void initCsvPrinterForValue(CsvPrinter<Object> printer) {
        printBoth = false;
        printer.setValuePrinter(VALUE_PRINTER);
        setCsvHeader(new String[]{"year", "phase", "adoptions"});
    }

    public void initCsvPrinterForCumulativeValue(CsvPrinter<Object> printer) {
        printBoth = false;
        printer.setValuePrinter(CUMULATIVE_VALUE_PRINTER);
        setCsvHeader(new String[]{"year", "phase", "adoptionsCumulative"});
    }

    public void initCsvPrinterForValueAndCumulativeValue(CsvPrinter<Object> printer) {
        printBoth = true;
        printer.setValuePrinter(VALUE_AND_CUMULATIVE_VALUE_PRINTER);
        setCsvHeader(new String[]{"year", "phase", "adoptions", "adoptionsCumulative"});
    }

    @Override
    public void writeHeader(CsvPrinter<?> printer) throws IOException {
        printer.writeHeader(csvHeader);
    }

    @Override
    public String printHeader(CsvPrinter<?> printer) {
        return printer.printHeader(csvHeader);
    }

    @Override
    protected void add(boolean currentYear, int year, AdoptionEntry info) {
        data.varUpdate(
                AdoptionResultInfo.ZERO, currentYear ? AdoptionResultInfo.ADD_BOTH : AdoptionResultInfo.ADD_CUMULATIVE,
                year,
                info.getPhase(),
                AdoptionResultInfo.ONE
        );
    }
}
