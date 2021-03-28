package de.unileipzig.irpact.commons.util.data;

/**
 * @author Daniel Abitz
 */
public interface BinaryData {

    void setID(long id);

    long getID();

    void setBytes(byte[] data);

    byte[] getBytes();

    String print();
}
