package de.unileipzig.irpact.core.spatial;

import de.unileipzig.irpact.v2.commons.Check;

/**
 * @author Daniel Abitz
 */
public abstract class MetricalSpatialModelBase extends SpatialModelBase {

    protected Metric metric;

    public MetricalSpatialModelBase(String name, Metric metric) {
        super(name);
        this.metric = Check.requireNonNull(metric, "metric");
    }

    public Metric getMetric() {
        return metric;
    }
}
