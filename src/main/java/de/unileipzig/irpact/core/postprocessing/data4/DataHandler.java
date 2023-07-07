package de.unileipzig.irpact.core.postprocessing.data4;

/**
 * @author Daniel Abitz
 */
public interface DataHandler {

    void init() throws Throwable;

    void execute() throws Throwable;
}
