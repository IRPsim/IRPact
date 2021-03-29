package de.unileipzig.irpact.core.process.ra.filter;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.filter.MaxNodeDistanceFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.core.process.ra.RAProcessPlan;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanMaxDistanceFilterScheme extends ProcessPlanMaxDistanceFilterScheme {

    @Override
    public MaxNodeDistanceFilter createFilter(ProcessPlan plan) {
        RAProcessPlan raPlan = (RAProcessPlan) plan;
        ConsumerAgent agent = raPlan.getAgent();
        SpatialInformation origin = agent.getSpatialInformation();
        SpatialModel model = raPlan.getEnvironment().getSpatialModel();

        MaxNodeDistanceFilter filter = new MaxNodeDistanceFilter();
        filter.setName(getName());
        filter.setModel(model);
        filter.setMaxDistance(getMaxDistance());
        filter.setInclusive(isInclusive());
        filter.setOrigin(origin);
        return filter;
    }
}
