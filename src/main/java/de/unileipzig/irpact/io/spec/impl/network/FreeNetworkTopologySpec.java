package de.unileipzig.irpact.io.spec.impl.network;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FreeNetworkTopologySpec
        implements ToSpecConverter<InFreeNetworkTopology>, ToParamConverter<InFreeNetworkTopology> {

    public static final FreeNetworkTopologySpec INSTANCE = new FreeNetworkTopologySpec();
    public static final String TYPE = "FreeNetworkTopology";

    @Override
    public Class<InFreeNetworkTopology> getParamType() {
        return InFreeNetworkTopology.class;
    }

    @Override
    public void toSpec(InFreeNetworkTopology input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getSocialNetwork().get());
        create(input, spec.getObject(TAG_topology), manager, converter, inline);
    }

    @Override
    public void create(InFreeNetworkTopology input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_distanceEvaluator, input.getDistanceEvaluator().getName());
        spec.setParameters(TAG_initialWeight, input.getInitialWeight());

        SpecificationHelper paramSpec = spec.getParametersSpec();
        SpecificationHelper tieSpec = paramSpec.getObjectSpec(TAG_numberOfTies);
        for(InNumberOfTies tie: input.getNumberOfTies()) {
            for(InConsumerAgentGroup cag: tie.getConsumerAgentGroups()) {
                tieSpec.set(cag.getName(), tie.getCount());
            }
        }
    }

    @Override
    public InFreeNetworkTopology[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper rootSpec = new SpecificationHelper(manager.getSocialNetwork().get());
        SpecificationHelper spec = rootSpec.getObjectSpec(TAG_topology);

        InDistanceEvaluator eval = getDistance(spec.getParametersText(TAG_distanceEvaluator), cache);
        double initialWeight = spec.getParametersDouble(TAG_initialWeight);

        List<InNumberOfTies> ties = new ArrayList<>();
        SpecificationHelper paramSpec = spec.getParametersSpec();
        SpecificationHelper tieSpec = paramSpec.getObjectSpec(TAG_numberOfTies);
        for(Map.Entry<String, JsonNode> entry: tieSpec.iterateFields()) {
            String cagName = entry.getKey();
            InConsumerAgentGroup cag = converter.getConsumerAgentGroup(cagName, manager, cache);
            int count = entry.getValue().intValue();
            ties.add(new InNumberOfTies(cag.getName() + "_" + count, cag, count));
        }

        InFreeNetworkTopology topo = new InFreeNetworkTopology(
                spec.getName(),
                eval,
                ties.toArray(new InNumberOfTies[0]),
                initialWeight
        );
        return new InFreeNetworkTopology[] {topo};
    }

    private InDistanceEvaluator getDistance(String key, SpecificationCache cache) {
        if("NoDistance".equals(key)) {
            return new InNoDistance("NoDistance_" + cache.getAndInc("NoDistance"));
        }
        if("Inverse".equals(key)) {
            return new InInverse("Inverse_" + cache.getAndInc("Inverse"));
        }
        throw new IllegalArgumentException("unknown distance eval: " + key);
    }
}
