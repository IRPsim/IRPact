package de.unileipzig.irpact.commons.logging;

import org.slf4j.Logger;

/**
 * @author Daniel Abitz
 */
public interface LoggingMessage {

    String format();

    void trace(Logger logger);

    void debug(Logger logger);

    void info(Logger logger);

    void warn(Logger logger);

    void error(Logger logger);

    void log(Logger logger);
}
