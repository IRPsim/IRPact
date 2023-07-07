package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.Metric;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class MaxDistanceNodeFilter extends NameableBase implements NodeDistanceFilter {

    protected SpatialModel model;
    protected double maxDistance;
    protected boolean inclusive;
    protected SpatialInformation origin;

    public MaxDistanceNodeFilter() {
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                getMaxDistance(),
                isInclusive(),
                ChecksumComparable.getNameChecksum(getModel()),
                getOrigin()
        );
    }

    public void setModel(SpatialModel model) {
        this.model = model;
    }

    public SpatialModel getModel() {
        return model;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setOrigin(SpatialInformation origin) {
        this.origin = origin;
    }

    public SpatialInformation getOrigin() {
        return origin;
    }

    public Metric getMetric() {
        return model.getMetric();
    }

    @Override
    public boolean test(SocialGraph.Node node) {
        if(node.is(SpatialAgent.class)) {
            SpatialAgent agent = node.getAgent(SpatialAgent.class);
            SpatialInformation agentInfo = agent.getSpatialInformation();
            double dist = model.distance(origin, agentInfo);
            return inclusive
                    ? dist <= maxDistance
                    : dist < maxDistance;
        } else {
            return false;
        }
    }
}
