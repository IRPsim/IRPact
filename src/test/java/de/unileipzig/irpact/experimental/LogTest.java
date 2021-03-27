package de.unileipzig.irpact.experimental;

import ch.qos.logback.classic.Level;
import de.unileipzig.irpact.commons.log.Logback;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Daniel Abitz
 */
@Disabled
class LogTest {

    @Test
    void asd() {
        Logback.setupConsole();
        Logger logger = LoggerFactory.getLogger(LogTest.class);
        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");

        Logback.setLevel(Level.INFO);
        System.out.println("---");

        logger.trace("trace");
        logger.debug("debug");
        logger.info("info");
        logger.warn("warn");
        logger.error("error");
    }
}
