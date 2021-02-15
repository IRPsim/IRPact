package de.unileipzig.irpact.commons;

/**
 * @author Daniel Abitz
 */
public interface BinaryData {

    void setID(long id);

    long getID();

    void setBytes(byte[] data);

    byte[] getBytes();
}
