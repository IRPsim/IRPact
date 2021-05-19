package de.unileipzig.irpact.core.spatial.filter;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.SpatialInformation;

/**
 * @author Daniel Abitz
 */
public abstract class SpatialInformationMetricFilter extends NameableBase implements SpatialInformationDistanceFilter {

    protected SpatialInformation reference;
    protected Metric metric;

    public SpatialInformationMetricFilter() {
    }

    public void setReference(SpatialInformation reference) {
        this.reference = reference;
    }

    public SpatialInformation getReference() {
        return reference;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public Metric getMetric() {
        return metric;
    }

    protected abstract boolean test(double distance);

    @Override
    public boolean test(SpatialInformation information) {
        double distance = metric.distance(reference, information);
        return test(distance);
    }
}
