package de.unileipzig.irpact.core.network.filter;

import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.agent.SpatialAgent;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class MaxDistanceNodeFilterScheme extends NameableBase implements NodeDistanceFilterScheme {

    protected double maxDistance;
    protected boolean inclusive;

    public MaxDistanceNodeFilterScheme() {
    }

    public double getMaxDistance() {
        return maxDistance;
    }

    public void setMaxDistance(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    public void setInclusive(boolean inclusive) {
        this.inclusive = inclusive;
    }

    @Override
    public MaxDistanceNodeFilter createFilter(SpatialAgent agent) {
        SpatialInformation origin = agent.getSpatialInformation();
        SpatialModel model = agent.getEnvironment().getSpatialModel();

        MaxDistanceNodeFilter filter = new MaxDistanceNodeFilter();
        filter.setName(getName());
        filter.setModel(model);
        filter.setMaxDistance(getMaxDistance());
        filter.setInclusive(isInclusive());
        filter.setOrigin(origin);
        return filter;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                getMaxDistance(),
                isInclusive()
        );
    }
}
