package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.misc.InitalizablePart;
import de.unileipzig.irpact.core.simulation.SimulationEntity;

/**
 * @author Daniel Abitz
 */
public interface SpatialModel extends SimulationEntity, InitalizablePart {

    //=========================
    // distance
    //=========================

    double distance(SpatialInformation from, SpatialInformation to);

    //=========================
    // data
    //=========================

    boolean hasData(String name);

    SpatialDataCollection getData(String name);

    void storeData(SpatialDataCollection data);
}
