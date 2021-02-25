package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
@Todo("besser einbauen")
public interface BinaryData {

    void setID(long id);

    long getID();

    void setBytes(byte[] data);

    byte[] getBytes();

    String print();
}
