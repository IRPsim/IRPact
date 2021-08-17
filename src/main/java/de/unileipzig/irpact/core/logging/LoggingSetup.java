package de.unileipzig.irpact.core.logging;

import ch.qos.logback.classic.Logger;

import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface LoggingSetup {

    void setFile(Path path);

    void apply(Logger rootLogger, Logger resultLogger);


}
