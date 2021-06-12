package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpatialModel extends SimulationEntityBase implements SpatialModel {

    protected Map<String, SpatialDataCollection> spatialData;

    public AbstractSpatialModel() {
        this(new HashMap<>());
    }

    public AbstractSpatialModel(Map<String, SpatialDataCollection> spatialData) {
        this.spatialData = spatialData;
    }

    @Override
    public boolean hasData(String name) {
        return spatialData.containsKey(name);
    }

    @Override
    public SpatialDataCollection getData(String name) {
        return spatialData.get(name);
    }

    @Override
    public void storeData(SpatialDataCollection data) {
        spatialData.put(data.getName(), data);
    }
}
