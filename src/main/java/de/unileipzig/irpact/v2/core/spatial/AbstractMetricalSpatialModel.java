package de.unileipzig.irpact.v2.core.spatial;

import de.unileipzig.irpact.v2.core.simulation.SimulationEntityBase;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractMetricalSpatialModel extends SimulationEntityBase implements SpatialModel {

    protected Metric metric;

    public AbstractMetricalSpatialModel() {
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Metric getMetric() {
        return metric;
    }
}
