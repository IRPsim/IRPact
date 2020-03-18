package de.unileipzig.irpact.core.message;

/**
 * @author Daniel Abitz
 */
public interface MessageContent {

    void process();

    boolean isSerializable();

    String serializeToString();
}
