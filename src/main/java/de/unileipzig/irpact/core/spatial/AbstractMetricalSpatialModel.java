package de.unileipzig.irpact.core.spatial;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractMetricalSpatialModel extends AbstractSpatialModel {

    protected Metric metric;

    public AbstractMetricalSpatialModel() {
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    @Override
    public Metric getMetric() {
        return metric;
    }
}
