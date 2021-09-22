package de.unileipzig.irpact.commons.logging.simplified;

/**
 * @author Daniel Abitz
 */
public interface SimplifiedLogger {

    boolean isStarted();

    void start();

    void stop();

    void log(String msg);

    void log(String format, Object arg);

    void log(String format, Object arg1, Object arg2);

    void log(String format, Object... args);
}
