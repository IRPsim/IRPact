package de.unileipzig.irpact.core.simulation.tasks;

/**
 * @author Daniel Abitz
 */
public interface Task {

    String getInfo();

    <R> R toBinary(Class<R> c) throws Exception;
}
