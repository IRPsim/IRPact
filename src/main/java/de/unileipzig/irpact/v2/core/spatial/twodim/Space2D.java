package de.unileipzig.irpact.v2.core.spatial.twodim;

import de.unileipzig.irpact.v2.core.spatial.AbstractMetricalSpatialModel;
import de.unileipzig.irpact.v2.core.spatial.Metric;
import de.unileipzig.irpact.v2.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public class Space2D extends AbstractMetricalSpatialModel {

    public Space2D() {
    }

    public Space2D(String name, Metric2D metric) {
        setName(name);
        setMetric(metric);
    }

    @Override
    public void setMetric(Metric metric) {
        if(metric instanceof Metric2D) {
            this.metric = metric;
        } else {
            throw new IllegalArgumentException("no Metric2D");
        }
    }

    @Override
    public Metric2D getMetric() {
        return (Metric2D) super.getMetric();
    }

    @Override
    public double distance(SpatialInformation from, SpatialInformation to) {
        return getMetric().distance(from, to);
    }
}
