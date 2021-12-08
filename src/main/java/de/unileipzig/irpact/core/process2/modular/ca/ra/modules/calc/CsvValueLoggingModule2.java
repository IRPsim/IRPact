package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCANumberLogging2;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CsvValueLoggingModule2
        extends AbstractCANumberLogging2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CsvValueLoggingModule2.class);

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final int AGENT_INDEX = 0;
    public static final int ID_INDEX = 1;
    public static final int PRODUCT_INDEX = 2;
    public static final int TIME_INDEX = 3;
    public static final int VALUE_INDEX = 4;
    public static final int ISNAN_INDEX = 5;

    public CsvValueLoggingModule2() {
    }

    @Override
    protected ConsumerAgentData2 castInput(ConsumerAgentData2 input) {
        return input;
    }

    @Override
    protected void initializeNewInputSelf(ConsumerAgentData2 input) throws Throwable {
    }

    public static LocalDateTime toTime(String input) {
        return LocalDateTime.parse(input, FORMATTER);
    }

    public static String fromTime(ZonedDateTime time) {
        return fromTime(time.toLocalDateTime());
    }

    public static String fromTime(LocalDateTime time) {
        return time.format(FORMATTER);
    }

    @Override
    public String getResourceType() {
        return "MinimalCsvValueLoggingModule2";
    }

    @Override
    protected void writeHeader() {
        log(
                getLocalizedString("agentName"),
                getLocalizedString("agentId"),
                getLocalizedString("productName"),
                getLocalizedString("time"),
                getLocalizedString("value"),
                getLocalizedString("nan")
        );
    }

    @Override
    protected Map<String, JsonTableData3> toXlsxData(JsonTableData3 csvData) {
        Map<String, JsonTableData3> xlsxSheetData = new HashMap<>();

        JsonTableData3 xlsxData = csvData.copy();
        int from = startIndexInFile();
        xlsxData.mapStringColumnToDouble(VALUE_INDEX, from, Double::parseDouble);
        xlsxData.mapStringColumnToLong(ID_INDEX, from, Long::parseLong);
        xlsxData.mapStringColumnToBoolean(ISNAN_INDEX, from, Boolean::parseBoolean);

        xlsxSheetData.put(getLocalizedString("sheetName"), xlsxData);

        return xlsxSheetData;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DateTimeFormatter getFormatter() {
        return FORMATTER;
    }

    //    @Override
//    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
//        initialize(environment);
//    }

    @Override
    protected void runLog(ConsumerAgentData2 input, double value) {
        log(
                input.getAgentName(),
                getId(input),
                input.getProductName(),
                fromTime(getTime(input)),
                mapToLogValue(value),
                isNaN(value)
        );
    }

    protected void log(Object agentName, Object agentId, Object productName, Object time, Object value, Object isNaN) {
        getValueLogger().log(
                "{};{};{};{};{}",
                agentName,
                agentId,
                productName,
                time,
                value,
                isNaN
        );
    }

//    @Override
//    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
//        calculate(input, actions);
//    }
}
