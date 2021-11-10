package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.logging.simplified.SimplifiedLogger;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.LoggingResourceAccess;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.simulation.CloseableSimulationEntity;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CsvValueLoggingModule2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2, CloseableSimulationEntity, Reevaluator<ConsumerAgentData2>, LoggingResourceAccess {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CsvValueLoggingModule2.class);

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    public static final int AGENT_INDEX = 0;
    public static final int ID_INDEX = 1;
    public static final int AGENT_GROUP_INDEX = 2;
    public static final int PRODUCT_INDEX = 3;
    public static final int PRODUCT_GROUP_INDEX = 4;
    public static final int TIME_INDEX = 5;
    public static final int VALUE_INDEX = 6;

    protected Path dir;
    protected String baseName;
    protected SimplifiedLogger valueLogger;
    protected boolean skipReevaluatorCall;
    protected boolean storeXlsx;

    protected JsonResource resource;

    public static LocalDateTime toTime(String input) {
        return LocalDateTime.parse(input, FORMATTER);
    }

    public static String fromTime(ZonedDateTime time) {
        return fromTime(time.toLocalDateTime());
    }

    public static String fromTime(LocalDateTime time) {
        return time.format(FORMATTER);
    }

    public void setValueLogger(SimplifiedLogger valueLogger) {
        this.valueLogger = valueLogger;
    }

    public SimplifiedLogger getValueLogger() {
        return valueLogger;
    }

    @Override
    public JsonResource getLocalizedData() {
        return resource;
    }

    @Override
    public String getResourceType() {
        return "CsvValueLoggingModule2";
    }

    public void setDir(Path dir) {
        this.dir = dir;
    }

    public Path getDir() {
        return dir;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setSkipReevaluatorCall(boolean skipReevaluatorCall) {
        this.skipReevaluatorCall = skipReevaluatorCall;
    }

    public boolean isSkipReevaluatorCall() {
        return skipReevaluatorCall;
    }

    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }

    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    protected void createCsvLogger(Path dir, String baseName) throws IOException {
        Path target = dir.resolve(baseName + ".csv");
        trace("create logger '{}', target: {}", baseName, target);
        SimplifiedLogger valueLogger = new SimplifiedFileLogger(
                baseName + "_LOGGER",
                SimplifiedFileLogger.createNew(
                        baseName + "_APPENDER",
                        "%msg%n",
                        target
                )
        );
        valueLogger.start();
        setValueLogger(valueLogger);
        writeHeader();
    }

    protected void writeHeader() {
        log(
                getLocalizedString("agentName"),
                getLocalizedString("agentId"),
                getLocalizedString("agentGroupName"),
                getLocalizedString("productName"),
                getLocalizedString("productGroupName"),
                getLocalizedString("time"),
                getLocalizedString("value")
        );
    }

    @Override
    public void closeEntity() {
        if(valueLogger != null) {
            valueLogger.stop();
            if(storeXlsx) {
                storeXlsx();
            }
        }
    }

    protected void storeXlsx() {
        try {
            trace("[{}] store xlsx", getBaseName());
            storeXlsx0();
        } catch (Throwable t) {
            error("store xlsx failed", t);
        }
    }

    protected void storeXlsx0() throws IOException {
        Path csvPath = getDir().resolve(getBaseName() + ".csv");
        trace("try load '{}'", csvPath);
        if(Files.exists(csvPath)) {
            JsonTableData3 csvData = loadCsv(csvPath, ";");
            Map<String, JsonTableData3> xlsxData = toXlsxData(csvData);

            Path xlsxPath = getDir().resolve(getBaseName() + ".xlsx");
            trace("try store '{}'", xlsxPath);
            storeXlsxWithTime(xlsxPath, FORMATTER, xlsxData);
            trace("stored '{}': {}", xlsxPath, Files.exists(xlsxPath));
        } else {
            info("file '{}' not found", csvPath);
        }
    }

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
    protected void validateSelf() throws Throwable {
        if(baseName == null) {
            throw new NullPointerException("missing baseName");
        }
        if(dir == null) {
            throw new NullPointerException("missing dir");
        }
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
        trace("register on close: {}", environment.registerIfNotRegistered(this));
        resource = load(environment);
        if(resource == null) {
            throw new NullPointerException("resource not found");
        }
        createCsvLogger(dir, baseName);
    }

    @Override
    public void initializeReevaluator(SimulationEnvironment environment) throws Throwable {
        initialize(environment);
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        double value = getNonnullSubmodule().calculate(input, actions);
        if(!(isReevaluatorCall() && isSkipReevaluatorCall())) {
            double logValue = Double.isNaN(value) ? 0 : value;
            log(
                    input.getAgentName(), getId(input), input.getAgentGroupName(),
                    input.getProductName(), input.getProductGroupName(),
                    fromTime(getTime(input)),
                    logValue
            );
        } else {
            trace("[{}] skip reevaluator call", getName());
        }
        return value;
    }

    protected void log(
            Object agentName, Object id, Object agentGroupName,
            Object productName, Object productGroupName,
            Object time,
            Object value) {
        getValueLogger().log(
                "{};{};{};{};{};{};{}",
                agentName, id, agentGroupName,
                productName, productGroupName,
                time,
                value
        );
    }

    protected LocalDateTime getTime(ConsumerAgentData2 input) {
        return getCurrentSimulationTime(input).toLocalDateTime();
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        calculate(input, actions);
    }
}
