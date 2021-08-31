package de.unileipzig.irpact.core.logging;

import de.unileipzig.irpact.commons.logging.simplified.SimplifiedLogger;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class InfoLoggingManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InfoLoggingManager.class);

    protected SimulationEnvironment environment;
    protected Map<String, SimplifiedLogger> loggers = new HashMap<>();

    public InfoLoggingManager() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public void register(String infoTag, SimplifiedLogger logger) {
        loggers.put(infoTag, logger);
    }

    protected void handleMissingLogger(String infoTag) {
        LOGGER.warn(IRPSection.GENERAL, "missing logger for info tag '{}'", infoTag);
    }

    public void log(String infoTag, String msg) {
        SimplifiedLogger logger = loggers.get(infoTag);
        if(logger == null) {
            handleMissingLogger(infoTag);
        } else {
            logger.log(msg);
        }
    }

    public void log(String infoTag, String format, Object arg) {
        SimplifiedLogger logger = loggers.get(infoTag);
        if(logger == null) {
            handleMissingLogger(infoTag);
        } else {
            logger.log(format, arg);
        }
    }

    public void log(String infoTag, String format, Object arg1, Object arg2) {
        SimplifiedLogger logger = loggers.get(infoTag);
        if(logger == null) {
            handleMissingLogger(infoTag);
        } else {
            logger.log(format, arg1, arg2);
        }
    }

    public void log(String infoTag, String format, Object... args) {
        SimplifiedLogger logger = loggers.get(infoTag);
        if(logger == null) {
            handleMissingLogger(infoTag);
        } else {
            logger.log(format, args);
        }
    }
}
