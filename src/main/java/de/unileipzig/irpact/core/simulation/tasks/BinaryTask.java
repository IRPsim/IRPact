package de.unileipzig.irpact.core.simulation.tasks;

/**
 * Tasks which can be serialized.
 *
 * @author Daniel Abitz
 */
public interface BinaryTask extends Task {

    String getInfo();

    long getID();

    byte[] getBytes();
}
