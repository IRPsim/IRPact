package de.unileipzig.irpact.core.process.ra.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.core.process.ra.RAProcessPlanBase;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanMaxDistanceFilterScheme extends ProcessPlanMaxDistanceFilterScheme {

    @Override
    public MaxDistanceNodeFilter createFilter(ProcessPlan plan) {
        RAProcessPlanBase raPlan = (RAProcessPlanBase) plan;
        ConsumerAgent agent = raPlan.getAgent();
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
