package de.unileipzig.irpact.core.persistence.binaryjson2;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Daniel Abitz
 */
public interface BinaryPersister<I> {

    void peek(I input, PersistHelper helper);

    void persist(I input, PersistHelper helper, ArrayNode root) throws Throwable;
}
