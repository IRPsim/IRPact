package de.unileipzig.irpact.misc.logfile.filters;

/**
 * @author Daniel Abitz
 */
public interface LogFileLineFilter {

    boolean accept(String line);
}
