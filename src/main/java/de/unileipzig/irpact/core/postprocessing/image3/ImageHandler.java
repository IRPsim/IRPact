package de.unileipzig.irpact.core.postprocessing.image3;

/**
 * @author Daniel Abitz
 */
public interface ImageHandler {

    void init() throws Throwable;

    void execute() throws Throwable;
}
