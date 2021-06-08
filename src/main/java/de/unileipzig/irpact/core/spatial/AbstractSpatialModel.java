package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.commons.util.IdManager;
import de.unileipzig.irpact.core.simulation.SimulationEntityBase;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongSupplier;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractSpatialModel extends SimulationEntityBase implements SpatialModel {

    protected final LongSupplier NEXT_ID = this::nextId;
    protected Map<String, SpatialDataCollection> spatialData;
    protected IdManager idManager = new IdManager(0L);

    public AbstractSpatialModel() {
        this(new HashMap<>());
    }

    public AbstractSpatialModel(Map<String, SpatialDataCollection> spatialData) {
        this.spatialData = spatialData;
    }

    public IdManager getIdManager() {
        return idManager;
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

    @Override
    public long nextId() {
        return idManager.nextId();
    }

    @Override
    public LongSupplier supplyNextId() {
        return NEXT_ID;
    }
}
