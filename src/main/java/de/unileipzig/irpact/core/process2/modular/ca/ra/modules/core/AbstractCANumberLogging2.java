package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.logging.simplified.SimplifiedLogger;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.LoggingResourceAccess;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.simulation.CloseableSimulationEntity;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractCANumberLogging2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, LoggingResourceAccess, RAHelperAPI2, CloseableSimulationEntity {

    protected Path dir;
    protected String baseName;
    protected SimplifiedLogger valueLogger;
    protected boolean enabled = true;
    protected boolean logReevaluatorCall;
    protected boolean logDefaultCall;
    protected boolean printHeader;
    protected boolean storeXlsx;
    protected JsonResource resource;

    public SimplifiedLogger getValueLogger() {
        return valueLogger;
    }

    @Override
    public JsonResource getLocalizedData() {
        return resource;
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

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isDisabled() {
        return !enabled;
    }

    public void setLogReevaluatorCall(boolean logReevaluatorCall) {
        this.logReevaluatorCall = logReevaluatorCall;
    }

    public boolean isLogReevaluatorCall() {
        return logReevaluatorCall;
    }

    public void setLogDefaultCall(boolean logDefaultCall) {
        this.logDefaultCall = logDefaultCall;
    }

    public boolean isLogDefaultCall() {
        return logDefaultCall;
    }

    public void setPrintHeader(boolean printHeader) {
        this.printHeader = printHeader;
    }

    public boolean isPrintHeader() {
        return printHeader;
    }

    public void setStoreXlsx(boolean storeXlsx) {
        this.storeXlsx = storeXlsx;
    }

    public boolean isStoreXlsx() {
        return storeXlsx;
    }

    protected static Path getCsvPath(Path dir, String baseName) {
        return dir.resolve(baseName + ".csv");
    }

    protected static Path getXlsxPath(Path dir, String baseName) {
        return dir.resolve(baseName + ".xlsx");
    }

    protected void createCsvLogger(Path dir, String baseName) throws IOException {
        Path target = dir.resolve(baseName + ".csv");
        logClassInfo(target);
        SimplifiedLogger valueLogger = new SimplifiedFileLogger(
                baseName + "_LOGGER",
                SimplifiedFileLogger.createNew(
                        baseName + "_APPENDER",
                        "%msg%n",
                        target
                )
        );
        valueLogger.start();
        this.valueLogger = valueLogger;
        trace("[{}] print header: {}", getName(), isPrintHeader());
        if(isPrintHeader()) {
            writeHeader();
        }
    }

    protected void logClassInfo(Path target) {
        trace(
                "[{}] create logger '{}' (logDefaultCall={}, logReevaluatorCall={}, printHeader={}, storeXlsx={}), target: {}",
                getName(),
                getName(),
                isLogDefaultCall(), isLogReevaluatorCall(),
                isPrintHeader(), isStoreXlsx(),
                target
        );
    }

    protected abstract void writeHeader();

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
            storeXlsxWithTime(xlsxPath, getFormatter(), xlsxData);
            trace("stored '{}': {}", xlsxPath, Files.exists(xlsxPath));
        } else {
            info("file '{}' not found", csvPath);
        }
    }

    protected abstract DateTimeFormatter getFormatter();

    protected abstract Map<String, JsonTableData3> toXlsxData(JsonTableData3 csvData);

    protected int startIndexInFile() {
        return isPrintHeader() ? 1 : 0;
    }

    @Override
    protected void validateSelf() throws Throwable {
        if(isEnabled()) {
            if(baseName == null) {
                throw new NullPointerException("missing baseName");
            }
            if(dir == null) {
                throw new NullPointerException("missing dir");
            }
        }
    }

    @Override
    protected void initializeSelf(SimulationEnvironment environment) throws Throwable {
        if(isEnabled()) {
            trace("[{}] register on close: {}", getName(), environment.registerIfNotRegistered(this));
            resource = load(environment);
            if(resource == null) {
                throw new NullPointerException("resource not found");
            }
            createCsvLogger(dir, baseName);
        } else {
            trace("[{}] disabled", getName());
        }
    }

    @Override
    protected void setupSelf(SimulationEnvironment environment) throws Throwable {
    }

    protected boolean doLog() {
        if(isReevaluatorCall()) {
            return isLogReevaluatorCall();
        } else {
            return isLogDefaultCall();
        }
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        double value = getNonnullSubmodule().calculate(input, actions);
        if(isEnabled()) {
            if(doLog()) {
                runLog(input, value);
            } else {
                trace("[{}] skip call", getName());
            }
        }
        return value;
    }

    protected double mapToLogValue(double input) {
        return Double.isNaN(input) ? 0 : input;
    }

    protected abstract void runLog(ConsumerAgentData2 input, double value);

    protected LocalDateTime getTime(ConsumerAgentData2 input) {
        return getCurrentSimulationTime(input).toLocalDateTime();
    }
}
