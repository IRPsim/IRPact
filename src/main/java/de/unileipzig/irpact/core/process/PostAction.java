package de.unileipzig.irpact.core.process;

/**
 * @author Daniel Abitz
 */
public interface PostAction {

    String getInputName();

    void execute() throws Throwable;
}
