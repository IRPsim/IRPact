package de.unileipzig.irpact.core.postprocessing.image3.base;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.util.Quantile;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.commons.util.io3.csv.CsvParser;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InConsumerAgentCalculationLoggingModule2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InQuantileRange;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractQuantilRangeImageHandler<I extends InOutputImage2>
        extends AbstractImageHandler<I>
        implements LoggingHelper {

    public AbstractQuantilRangeImageHandler(
            ImageProcessor2 processor,
            I imageConfiguration) {
        super(processor, imageConfiguration);
    }

    public void init() throws Throwable {
        validate();
    }

    protected abstract void validate() throws Throwable;

    protected Path getLoggingFile(InConsumerAgentCalculationLoggingModule2 module) {
        return getTargetFile(module.getFileName());
    }

    protected Map<Integer, Quantile<Number>> getQuantileData(InConsumerAgentCalculationLoggingModule2 module) throws IOException {
        Path loggingFile = getLoggingFile(module);
        String loggingFileName = loggingFile.getFileName().toString();
        String key = loggingFileName + "@QuantilRange";
        if(processor.isCached(key)) {
            return processor.retrieveFromCache(key);
        } else {
            info("load csv '{}'", loggingFile);
            Map<Integer, Quantile<Number>> quantiles = calculateAnnualQuantils(
                    loggingFile,
                    module.getTimeIndex(),
                    module.getValueIndex(),
                    module::toTime
            );
            trace("cache processed csv data '{}', key={}", loggingFile, key);
            processor.storeInCache(key, quantiles);
            return quantiles;
        }
    }

    public static Map<Integer, Quantile<Number>> calculateAnnualQuantils(
            Path input,
            int timeIndex,
            int valueIndex,
            Function<? super String, ? extends LocalDateTime> toTimeFunction) throws IOException {
        CsvParser<JsonNode> csvParser = new CsvParser<>();
        csvParser.setValueGetter(CsvParser.forJson());
        JsonTableData3 rawData = new JsonTableData3(csvParser.parseToList(input, StandardCharsets.UTF_8));
        return calculateAnnualQuantils(rawData, timeIndex, valueIndex, toTimeFunction);
    }

    protected static Map<Integer, Quantile<Number>> calculateAnnualQuantils(
            JsonTableData3 rawData,
            int timeIndex,
            int valueIndex,
            Function<? super String, ? extends LocalDateTime> toTimeFunction) {
        Map<Integer, List<Number>> values = new TreeMap<>();
        Map<Integer, Quantile<Number>> quantiles = new TreeMap<>();

        for(int r = 0; r < rawData.getNumberOfRows(); r++) {
            String timeStr = rawData.getString(r, timeIndex);
            LocalDateTime time = toTimeFunction.apply(timeStr);
            int year = time.getYear();

            String valueStr = rawData.getString(r, valueIndex);
            double value = Double.parseDouble(valueStr);

            List<Number> list = values.computeIfAbsent(year, _year -> new ArrayList<>());
            list.add(value);
        }

        for(Map.Entry<Integer, List<Number>> entry: values.entrySet()) {
            Quantile<Number> quantile = quantiles.computeIfAbsent(entry.getKey(), _year -> new Quantile<>(Number::doubleValue));
            quantile.set(entry.getValue());
        }

        return quantiles;
    }

    public static Map<Integer, Double> calculateAverage(
            Map<Integer, Quantile<Number>> quantiles,
            double lowerBound,
            double upperBound) {
        Map<Integer, Double> avgMap = new TreeMap<>();
        for(Map.Entry<Integer, Quantile<Number>> entry: quantiles.entrySet()) {
            double avg = entry.getValue().average(lowerBound, upperBound, 0);
            avgMap.put(entry.getKey(), avg);
        }
        return avgMap;
    }

    public static Map<Integer, Double> calculateAverage(
            Map<Integer, Quantile<Number>> quantiles) {
        Map<Integer, Double> avgMap = new TreeMap<>();
        for(Map.Entry<Integer, Quantile<Number>> entry: quantiles.entrySet()) {
            double avg = entry.getValue().average(0);
            avgMap.put(entry.getKey(), avg);
        }
        return avgMap;
    }

    public static JsonTableData3 mapToCsv(
            IRPLogger logger,
            Collection<Integer> years,
            Map3<InQuantileRange, Integer, Double> avgQuantileValues) {
        return mapToCsv(logger, years, avgQuantileValues, null);
    }

    public static JsonTableData3 mapToCsv(
            IRPLogger logger,
            Collection<Integer> years,
            Map3<InQuantileRange, Integer, Double> avgQuantileValues,
            Map<Integer, Double> avgValues) {

        JsonTableData3 data = new JsonTableData3();
        data.setString(0, 0, "year");
        //avg
        int rowIndex = 1;
        for(int year: years) {
            data.setInt(rowIndex, 0, year);
            if(avgValues != null) {
                data.setDouble(rowIndex, 1, avgValues.getOrDefault(year, 0.0));
            }

            rowIndex++;
        }
        //avg-quantile
        int columnIndex = avgValues == null ? 1 : 2;
        for(InQuantileRange range: avgQuantileValues.keySet()) {
            data.setString(0, columnIndex, range.getName());
            rowIndex = 1;
            for(int year: years) {

                double value = avgQuantileValues.getOrDefault(range, year, Double.NaN);
                if(Double.isNaN(value)) {
                    if(logger == null) {
                        throw new IllegalArgumentException("missing value for year " + year);
                    } else {
                        logger.warn("missing value for year '{}', set 0", year);
                    }

                }
                data.setDouble(rowIndex, columnIndex, value);

                rowIndex++;
            }

            columnIndex++;
        }
        return data;
    }
}
