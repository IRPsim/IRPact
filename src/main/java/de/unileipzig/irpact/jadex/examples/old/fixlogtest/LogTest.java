package de.unileipzig.irpact.jadex.examples.old.fixlogtest;

import de.unileipzig.irpact.commons.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @author Daniel Abitz
 */
public class LogTest {

    private static void setId(long id) {
        if(id < 0) {
            LocalDateTime ldt = LocalDateTime.now();
            String ldtStr = ldt.format(TimeUtil.FILE_DATE_TIME_FORMATTER);
            System.setProperty("irpact.id", ldtStr);
        } else {
            System.setProperty("irpact.id", Long.toString(id));
        }
    }

    private static void setupAppender() {
    }

    private static void print(Logger logger) {
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }

    public static void main(String[] args) {
        setId(321);

        Logger simuLogger = LoggerFactory.getLogger("de.unileipzig.irpact.jadex.start.StartSimulation");
        Logger agentLogger = LoggerFactory.getLogger("de.unileipzig.irpact.jadex.agent.company.JadexCompanyAgent");

        print(simuLogger);
        print(agentLogger);
    }
}
