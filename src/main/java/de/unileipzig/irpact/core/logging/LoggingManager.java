package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface LoggingManager {

    //=========================
    // loggers
    //=========================

    Logger getLogger(Class<?> c);

    Logger getLogger(String name);

    Logger getResultLogger();

    //=========================
    // settings
    //=========================

    void setPath(Path path);

    void setFilterError(boolean filterError);

    void setLevel(Level level);

    //=========================
    // modes
    //=========================

    void enableClearMode();

    void enableWriteMode();

    void enableInformationMode();

    //=========================
    // target
    //=========================

    void writeToConsole();

    void writeToFile();

    void writeToConsoleAndFile();
}
