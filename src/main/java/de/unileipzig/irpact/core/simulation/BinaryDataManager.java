package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.commons.BinaryData;

import java.util.Collection;

/**
 * @author Daniel Abitz
 */
public interface BinaryDataManager {

    void handle(Collection<? extends BinaryData> rawData) throws Exception;
}
