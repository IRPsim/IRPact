package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.calc;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedFileLogger;
import de.unileipzig.irpact.commons.logging.simplified.SimplifiedLogger;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractUniformCAMultiModule1_2;
import de.unileipzig.irpact.core.process2.modular.modules.core.CalculationModule2;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.simulation.CloseableSimulationEntity;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class CsvValueLoggingModule2
        extends AbstractUniformCAMultiModule1_2<Number, Number, CalculationModule2<ConsumerAgentData2>>
        implements CalculationModule2<ConsumerAgentData2>, RAHelperAPI2, CloseableSimulationEntity, Reevaluator<ConsumerAgentData2> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CsvValueLoggingModule2.class);

    protected Path dir;
    protected String baseName;
    protected SimplifiedLogger valueLogger;
    protected boolean storeXlsx;

    public void setValueLogger(SimplifiedLogger valueLogger) {
        this.valueLogger = valueLogger;
    }

    public SimplifiedLogger getValueLogger() {
        return valueLogger;
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
        trace("TODO");
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
        environment.register(this);
        createCsvLogger(dir, baseName);
    }

    @Override
    public double calculate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        double value = getNonnullSubmodule().calculate(input, actions);
        Timestamp now = input.now();
        getValueLogger().log(
                "{};{};{};{};{};{};{}",
                input.getAgentName(), input.getAgentGroupName(),
                input.getProductName(), input.getProductGroupName(),
                now, now.getYear(),
                value
        );
        return value;
    }

    @Override
    public void reevaluate(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        calculate(input, actions);
    }
}
