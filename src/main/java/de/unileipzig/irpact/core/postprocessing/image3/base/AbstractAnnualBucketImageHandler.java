package de.unileipzig.irpact.core.postprocessing.image3.base;

import de.unileipzig.irpact.commons.color.ColorPalette;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.*;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualBucketImage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAnnualBucketImageHandler
        extends AbstractImageHandler<InAnnualBucketImage>
        implements LoggingHelper {

    protected DoubleBucketFactory bucketFactory;

    protected DecimalFormat bucketPrintFormat;

    public AbstractAnnualBucketImageHandler(ImageProcessor2 processor, InAnnualBucketImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    protected DecimalFormat getBucketPrintFormat() {
        if(bucketPrintFormat == null) {
            NumberFormat nf = NumberFormat.getNumberInstance(processor.getLocale());
            DecimalFormat df = (DecimalFormat) nf;
            df.setMinimumFractionDigits(imageConfiguration.getFractionDigits());
            df.setMaximumFractionDigits(imageConfiguration.getFractionDigits());

            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(getLocalizedString("decimalSeparator").charAt(0));
            df.setDecimalFormatSymbols(symbols);
            bucketPrintFormat = df;
        }
        return bucketPrintFormat;
    }

    protected BucketFactory<Number> getFactory() {
        if(bucketFactory == null) {
            bucketFactory = new DoubleBucketFactory(imageConfiguration.getBucketSize(), true);
        }
        return bucketFactory;
    }

    protected BucketMap<Number, Integer> newBucketMap() {
        return new BucketMap<>(getFactory(), new TreeMap<>());
    }

    protected InConsumerAgentCalculationLoggingModule2 getLoggingModule() {
        try {
            return imageConfiguration.getLoggingModule();
        } catch (ParsingException e) {
            throw new IllegalStateException("missing logging module", e);
        }
    }

    protected int getYear(JsonTableData3 data, int row) {
        return data.getStringAsInt(
                row,
                getLoggingModule().getTimeIndex(),
                timeStr -> {
                    LocalDateTime ldt = getLoggingModule().toTime(timeStr);
                    return ldt.getYear();
                }
        );
    }

    protected double getValue(JsonTableData3 data, int row) {
        return data.getStringAsDouble(
                row,
                getLoggingModule().getValueIndex(),
                Double::parseDouble
        );
    }

    protected String printBucket(Bucket<Number> bucket) {
        DecimalFormat df = getBucketPrintFormat();
        String fromStr = df.format(bucket.getFrom().doubleValue());
        String toStr = df.format(bucket.getTo().doubleValue());
        return "["
                + fromStr
                + getLocalizedString("rangeSeparator")
                + toStr
                + ")";
    }

    protected Double getMinYOrDefault(Double ifMissing) {
        return imageConfiguration.isUseCustomYRange()
                ? imageConfiguration.getMinY()
                : ifMissing;
    }

    protected Double getMaxYOrDefault(Double ifMissing) {
        return imageConfiguration.isUseCustomYRange()
                ? imageConfiguration.getMaxY()
                : ifMissing;
    }

    public static Map<Integer, BucketMap<Number, Integer>> createAnnualBucketData(
            JsonTableData3 data,
            boolean hasHeader,
            List<Integer> years,
            Function<? super Integer, ? extends BucketMap<Number, Integer>> bucketMapSupplier,
            ToIntBiFunction<? super JsonTableData3, ? super Integer> getYearFunction,
            ToDoubleBiFunction<? super JsonTableData3, ? super Integer> getValueFunction) {

        //init
        Map<Integer, BucketMap<Number, Integer>> annualBuckets = new TreeMap<>();
        if(years != null) {
            for(int year: years) {
                annualBuckets.put(year, bucketMapSupplier.apply(year));
            }
        }
        //update
        for(int row = hasHeader ? 1 : 0; row < data.getNumberOfRows(); row++) {
            int year = getYearFunction.applyAsInt(data, row);
            double value = getValueFunction.applyAsDouble(data, row);

            BucketMap<Number, Integer> bucketMap;
            if(years == null) {
                bucketMap = annualBuckets.computeIfAbsent(year, bucketMapSupplier);
            } else {
                bucketMap = annualBuckets.get(year);
            }

            if(bucketMap == null) {
                continue;
            }

            bucketMap.update(value, 0, c -> c + 1);
        }
        //result
        return annualBuckets;
    }

    protected JsonTableData3 loadLoggingData() throws IOException {
        Path loggingFile = getTargetFile(getLoggingModule().getFileName());
        return processor.loadOrGetCsv(
                loggingFile,
                StandardCharsets.UTF_8,
                getCsvDelimiter(),
                loggingFile.toString()
        );
    }

    protected String getCsvDelimiter() {
        return getLocalizedString("sep");
    }

    protected void addHeader(JsonTableData3 data, Collection<? extends Bucket<Number>> buckets) {
        data.insertRow(0);
        int columnIndex = 0;
        data.setString(0, columnIndex++, getLocalizedString("headerYear"));
        for(Bucket<Number> bucket: buckets) {
            String bucketStr = printBucket(bucket);
            data.setString(0, columnIndex++, bucketStr);
        }
    }

    protected Collection<? extends Bucket<Number>> getAllBuckets() {
        return getAllBuckets(bucketFactory);
    }

    public static Collection<? extends Bucket<Number>> getAllBuckets(BucketFactory<Number> fac) {
        fac.createBuckets(0, 1);
        return fac.getAllBuckets();
    }

    public static JsonTableData3 createCsvData(
            Map<Integer, BucketMap<Number, Integer>> annualBuckets,
            Collection<? extends Integer> years,
            Collection<? extends Bucket<Number>> buckets) {

        JsonTableData3 data = new JsonTableData3();

        int rowIndex = 0;
        for(int year: years) {
            BucketMap<Number, Integer> bucketMap = annualBuckets.get(year);
            int columnIndex = 0;
            data.setInt(rowIndex, columnIndex++, year);
            for(Bucket<Number> bucket: buckets) {
                int count = bucketMap == null
                        ? 0
                        : bucketMap.getOrDefault(bucket, 0);
                data.setInt(rowIndex, columnIndex++, count);
            }
            rowIndex++;
        }

        return data;
    }

    public static void findMinMax(JsonTableData3 csvData, MutableDouble min, MutableDouble max) {
        for(int c = 1; c < csvData.getNumberOfColumns(); c++) {
            csvData.getMinMax(c, 1, min, max);
        }
    }
}
