package de.unileipzig.irpact.core.postprocessing.data4;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.util.data.Bucket;
import de.unileipzig.irpact.commons.util.data.BucketFactory;
import de.unileipzig.irpact.commons.util.data.BucketMap;
import de.unileipzig.irpact.commons.util.data.DoubleBucketFactory;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.FileType;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Daniel Abitz
 */
public class BucketAnalyser extends AbstractDataHandler<InBucketAnalyser> {

    private static final IRPLogger LoGGER = IRPLogging.getLogger(BucketAnalyser.class);

    protected final Map<Double, BucketFactory<Number>> FACTORIES = new HashMap<>();
    protected boolean printHeader = true;

    public BucketAnalyser(DataProcessor4 processor, InBucketAnalyser dataConfiguration) {
        super(processor, dataConfiguration);
    }

    protected int getYear(JsonTableData3 data, int row) throws ParsingException {
        String timeStr = data.getString(row, getTimeIndex());
        LocalDateTime ldt = toTime(timeStr);
        return ldt.getYear();
    }

    protected double getValue(JsonTableData3 data, int row) throws ParsingException {
        String valueStr = data.getString(row, getValueIndex());
        return Double.parseDouble(valueStr);
    }

    protected int getTimeIndex() throws ParsingException {
        return dataConfiguration.getLoggingModule().getTimeIndex();
    }

    protected int getValueIndex() throws ParsingException {
        return dataConfiguration.getLoggingModule().getValueIndex();
    }

    protected LocalDateTime toTime(String str) throws ParsingException {
        return dataConfiguration.getLoggingModule().toTime(str);
    }

    protected BucketFactory<Number> getFactory() {
        return FACTORIES.computeIfAbsent(dataConfiguration.getBucketRange(), DoubleBucketFactory::new);
    }

    protected BucketMap<Number, Integer> newBucketMap() {
        return new BucketMap<>(getFactory(), new TreeMap<>());
    }

    protected Path getOutputPath(String extension) {
        return getTargetFile(dataConfiguration.getBaseFileName() + extension);
    }

    protected String[] buildKey(FileType type, String key) {
        return new String[] {type.name(), getResourceKey(), key};
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LoGGER;
    }

    @Override
    protected String getResourceKey() {
        return "BUCKET_ANALYSER";
    }

    @Override
    public void init() throws Throwable {
    }

    @Override
    public void execute() throws Throwable {
        JsonTableData3 data = loadData();
        Map<Integer, BucketMap<Number, Integer>> annualBucketData = createAnnualBucketData(data);
        Map<Integer, JsonTableData3> annualTableData = bucketDataToTableData(annualBucketData);

        if(dataConfiguration.isStoreCsv()) {
            storeCsv(annualTableData);
        }

        if(dataConfiguration.isStoreXlsx()) {
            storeXlsx(annualTableData);
        }
    }

    protected void storeCsv(Map<Integer, JsonTableData3> annualTableData) throws IOException {
        trace("store csv");
        JsonTableData3 csvData = toCsvTableData(annualTableData);
        Path outputPath = getOutputPath(".csv");
        info("write '{}'", outputPath);
        processor.storeCsv(
                outputPath,
                StandardCharsets.UTF_8,
                getCsvDelimiter(FileType.CSV),
                csvData
        );
    }

    protected void storeXlsx(Map<Integer, JsonTableData3> annualTableData) throws IOException {
        trace("store xlsx");
        Map<String, JsonTableData3> xlsxData = toXlsxTableData(annualTableData);
        Path outputPath = getOutputPath(".xlsx");
        info("write '{}'", outputPath);
        processor.storeXlsx(outputPath, xlsxData);
    }

    protected Map<Integer, BucketMap<Number, Integer>> createAnnualBucketData(JsonTableData3 data) throws ParsingException {
        Map<Integer, BucketMap<Number, Integer>> annualBuckets = new TreeMap<>();
        for(int r = 0; r < data.getNumberOfRows(); r++) {
            int year = getYear(data, r);
            double value = getValue(data, r);
            BucketMap<Number, Integer> bucketMap = annualBuckets.computeIfAbsent(year, _year -> newBucketMap());
            bucketMap.update(value, 0, c -> c + 1);
        }
        return annualBuckets;
    }

    protected JsonTableData3 loadData() throws Throwable {
        String loggingFileName = dataConfiguration.getLoggingModule().getFileName();
        Path dataPath = getTargetFile(loggingFileName);
        if(Files.exists(dataPath)) {
            if(processor.isCached(dataPath)) {
                return processor.retrieveFromCache(dataPath);
            } else {
                info("load csv '{}'", dataPath);
                JsonTableData3 data = processor.loadCsv(dataPath, StandardCharsets.UTF_8, getCsvDelimiter(FileType.CSV));
                trace("cache loaded data '{}'", dataPath);
                processor.storeInCache(dataPath, data);
                return data;
            }
        } else {
            warn("skip '{}', logging file '{}' not found", dataConfiguration.getName(), dataPath);
            throw new NoSuchFileException(dataPath.toString());
        }
    }

    protected Map<Integer, JsonTableData3> bucketDataToTableData(Map<Integer, BucketMap<Number, Integer>> bucketData) {
        Map<Integer, JsonTableData3> tableData = new TreeMap<>();
        for(Map.Entry<Integer, BucketMap<Number, Integer>> entry: bucketData.entrySet()) {
            tableData.put(
                    entry.getKey(),
                    bucketDataToTableData(entry.getValue())
            );
        }
        return tableData;
    }

    protected Map<String, JsonTableData3> toXlsxTableData(Map<Integer, JsonTableData3> tableData) {
        Map<String, JsonTableData3> xlsxTableData = new TreeMap<>();
        for(Map.Entry<Integer, JsonTableData3> entry: tableData.entrySet()) {
            JsonTableData3 data;
            if(printHeader) {
                data = entry.getValue().copy();
                data.insertRow(0);
                data.setString(0, 0, getLocalizedString(FileType.XLSX, "bucket"));
                data.setString(0, 1, getLocalizedString(FileType.XLSX, "value"));
            } else {
                data = entry.getValue();
            }
            xlsxTableData.put(
                    Integer.toString(entry.getKey()),
                    data
            );
        }
        return xlsxTableData;
    }

    protected JsonTableData3 toCsvTableData(Map<Integer, JsonTableData3> tableData) {
        JsonTableData3 csvData = new JsonTableData3();
        int rowIndex = 0;
        if(printHeader) {
            csvData.setString(rowIndex, 0, getLocalizedString(FileType.CSV, "year"));
            csvData.setString(rowIndex, 1, getLocalizedString(FileType.CSV, "bucket"));
            csvData.setString(rowIndex, 2, getLocalizedString(FileType.CSV, "value"));
            rowIndex++;
        }
        for(Map.Entry<Integer, JsonTableData3> entry: tableData.entrySet()) {
            int year = entry.getKey();
            JsonTableData3 annualData = entry.getValue();
            for(int r = 0; r < annualData.getNumberOfRows(); r++) {
                String bucketStr = annualData.getString(r, 0);
                int count = annualData.getInt(r, 1);
                csvData.setInt(rowIndex, 0, year);
                csvData.setString(rowIndex, 1, bucketStr);
                csvData.setInt(rowIndex, 2, count);
                rowIndex++;
            }
        }
        return csvData;
    }

    protected JsonTableData3 bucketDataToTableData(BucketMap<Number, Integer> bucketMap) {
        JsonTableData3 tableData = new JsonTableData3();
        int rowIndex = 0;
        for(Map.Entry<Bucket<Number>, Integer> entry: bucketMap.entries()) {
            String bucketStr = entry.getKey().toString();
            int count = entry.getValue();
            tableData.setString(rowIndex, 0, bucketStr);
            tableData.setInt(rowIndex, 1, count);
            rowIndex++;
        }
        return tableData;
    }
}
