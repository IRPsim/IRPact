package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class MaxNodeDistanceFilter extends NameableBase implements NodeDistanceFilter {

    protected SpatialModel model;
    protected double maxDistance;
    protected boolean inclusive;
    protected SpatialInformation origin;

    public MaxNodeDistanceFilter() {
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
