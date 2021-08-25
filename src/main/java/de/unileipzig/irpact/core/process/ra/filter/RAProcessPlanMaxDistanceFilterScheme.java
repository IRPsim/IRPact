package de.unileipzig.irpact.core.process.ra.filter;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.network.filter.MaxDistanceNodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.filter.ProcessPlanMaxDistanceFilterScheme;
import de.unileipzig.irpact.core.process.modular.ca.SimpleConsumerAgentData;
import de.unileipzig.irpact.core.process.ra.RAProcessPlanBase;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.core.spatial.SpatialModel;
import de.unileipzig.irpact.develop.Todo;

/**
 * @author Daniel Abitz
 */
public class RAProcessPlanMaxDistanceFilterScheme extends ProcessPlanMaxDistanceFilterScheme {

    @Todo("HACK")
    @Override
    public MaxDistanceNodeFilter createFilter(ProcessPlan plan) {
        if(plan instanceof RAProcessPlanBase) {
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
        if(plan instanceof SimpleConsumerAgentData) {
            SimpleConsumerAgentData scad = (SimpleConsumerAgentData) plan;
            ConsumerAgent agent = scad.getAgent();
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
        throw new UnsupportedOperationException("not supported: " + plan.getClass());
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
