package de.unileipzig.irpact.core.persistence.binaryjson2.func;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface CollectionSupplier {

    <E> Collection<E> newCollection();
}
