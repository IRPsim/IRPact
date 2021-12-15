package de.unileipzig.irpact.core.persistence.binaryjson2.functions;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface CollectionSupplier {

    <E> Collection<E> newCollection();
}
