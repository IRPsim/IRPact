package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopologySpec extends SpecBase<InFreeNetworkTopology, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InFreeNetworkTopology> getParamType() {
        return InFreeNetworkTopology.class;
    }

    @Override
    public void toSpec(InFreeNetworkTopology instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getTopology());
        spec.setName(instance.getName());
        spec.setType("FreeNetworkTopology");
        spec.set(TAG_distanceEvaluator, instance.getDistanceEvaluator().getName());
        spec.setInitialWeight(instance.getInitialWeight());
        SpecificationHelper tieHelper = spec.getObjectSpec(TAG_numberOfTies);
        for(InNumberOfTies tie: instance.getNumberOfTies()) {
            for(InConsumerAgentGroup cag: tie.getConsumerAgentGroups()) {
                tieHelper.set(cag.getName(), tie.getCount());
            }
        }

        converter.apply(manager, instance.getDistanceEvaluator());
    }
}
