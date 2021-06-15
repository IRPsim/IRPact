package de.unileipzig.irpact.commons.util.io;

import java.util.List;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface Header {

    String getLabel(int index);
    
    default boolean isLabel(int index, String label) {
        return Objects.equals(label, getLabel(index));
    }

    int length();

    String[] toArray();

    List<String> toList();
}
