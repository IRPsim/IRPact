package de.unileipzig.irpact.core.logging;

import org.slf4j.Logger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface LoggingController {

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

    void setRootLevel(IRPLevel level);

    void setResultLevel(IRPLevel level);

    //=========================
    // modes
    //=========================

    void enableClearMode();

    void enableWriteMode();

    void enableInformationMode();

    //=========================
    // target
    //=========================

    boolean isWritingToFileAndNotConsole();

    void stopWriting();

    void writeToConsole();

    void writeToFile();

    void writeToConsoleAndFile();
}
