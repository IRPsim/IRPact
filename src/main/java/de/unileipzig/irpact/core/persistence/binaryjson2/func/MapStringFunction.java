package de.unileipzig.irpact.core.persistence.binaryjson2.func;

/**
 * @author Daniel Abitz
 */
public interface MapStringFunction<I> {

    String toString(I i);

    I fromString(String str);
}
