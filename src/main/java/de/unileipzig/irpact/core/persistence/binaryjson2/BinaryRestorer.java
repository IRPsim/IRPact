package de.unileipzig.irpact.core.persistence.binaryjson2;

import com.fasterxml.jackson.databind.node.ArrayNode;

/**
 * @author Daniel Abitz
 */
public interface BinaryRestorer<I> {

    void restore(I input, RestoreHelper helper, ArrayNode root) throws Throwable;
}
