package de.unileipzig.irpact.util.logfile.filters;

/**
 * @author Daniel Abitz
 */
public interface LogFileLineFilter {

    boolean accept(String line);
}
