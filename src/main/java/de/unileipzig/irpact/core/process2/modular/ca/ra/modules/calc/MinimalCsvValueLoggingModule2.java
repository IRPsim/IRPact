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
public class MinimalCsvValueLoggingModule2
        extends AbstractCANumberLogging2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(MinimalCsvValueLoggingModule2.class);

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final int AGENT_INDEX = 0;
    public static final int ID_INDEX = 1;
    public static final int PRODUCT_INDEX = 2;
    public static final int TIME_INDEX = 3;
    public static final int VALUE_INDEX = 4;

    public MinimalCsvValueLoggingModule2() {
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
                getLocalizedString("value")
        );
    }

    @Override
    protected Map<String, JsonTableData3> toXlsxData(JsonTableData3 csvData) {
        Map<String, JsonTableData3> xlsxSheetData = new HashMap<>();

        JsonTableData3 xlsxData = csvData.copy();
        //skip header
        xlsxData.mapStringColumnToDouble(VALUE_INDEX, 1, Double::parseDouble);
        xlsxData.mapStringColumnToLong(ID_INDEX, 1, Long::parseLong);

        xlsxSheetData.put("Data", xlsxData);

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
        double logValue = mapToLogValue(value);
        log(
                input.getAgentName(),
                getId(input),
                input.getProductName(),
                fromTime(getTime(input)),
                logValue
        );
    }

    protected void log(Object agentName, Object agentId, Object productName, Object time, Object value) {
        getValueLogger().log(
                "{};{};{};{};{}",
                agentName,
                agentId,
                productName,
                time,
                value
        );
    }

//    @Override
//    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
//        calculate(input, actions);
//    }
}
