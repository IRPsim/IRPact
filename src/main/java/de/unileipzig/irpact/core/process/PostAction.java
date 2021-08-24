package de.unileipzig.irpact.core.process;

/**
 * @author Daniel Abitz
 */
public interface PostAction<I> {

    boolean isSupported(Class<?> type);

    I getInput();

    String getInputName();

    void execute() throws Throwable;
}
