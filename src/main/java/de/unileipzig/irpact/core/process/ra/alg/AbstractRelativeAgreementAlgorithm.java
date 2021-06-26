package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractRelativeAgreementAlgorithm extends SimulationEntityBase implements RelativeAgreementAlgorithm {

    protected boolean logDataFallback;
    protected boolean disableLogging = false;

    public AbstractRelativeAgreementAlgorithm() {
    }

    protected static IRPLogger getLogger(boolean disableLogging, boolean logData, IRPLogger logger) {
        return disableLogging
                ? null
                : logData
                ? IRPLogging.getResultLogger()
                : logger;
    }

    protected static IRPSection getSection(boolean logData) {
        return IRPSection.SIMULATION_PROCESS.orGeneral(logData);
    }

    protected static Level getLevel(boolean logData) {
        return logData
                ? Level.INFO
                : Level.TRACE;
    }

    public void setDisableLogging(boolean disableLogging) {
        this.disableLogging = disableLogging;
    }

    public boolean loggingDisabled() {
        return disableLogging;
    }

    public void setLogDataFallback(boolean logDataFallback) {
        this.logDataFallback = logDataFallback;
    }

    public boolean isLogDataFallback() {
        return logDataFallback;
    }

    public boolean isLogData() {
        return environment == null
                ? logDataFallback
                : environment.getSettings().isLogRelativeAgreement();
    }
}
