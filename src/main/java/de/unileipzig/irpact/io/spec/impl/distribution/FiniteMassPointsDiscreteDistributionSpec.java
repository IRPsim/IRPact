package de.unileipzig.irpact.io.spec.impl.distribution;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.distribution.InFiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InMassPoint;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class FiniteMassPointsDiscreteDistributionSpec
        implements ToSpecConverter<InFiniteMassPointsDiscreteDistribution>, ToParamConverter<InFiniteMassPointsDiscreteDistribution> {

    public static final FiniteMassPointsDiscreteDistributionSpec INSTANCE = new FiniteMassPointsDiscreteDistributionSpec();
    public static final String TYPE = "FiniteMassPointsDiscreteDistribution";

    @Override
    public Class<InFiniteMassPointsDiscreteDistribution> getParamType() {
        return InFiniteMassPointsDiscreteDistribution.class;
    }

    @Override
    public void toSpec(InFiniteMassPointsDiscreteDistribution input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        if(manager.getDistributions().hasNot(input.getName())) {
            create(input, manager.getDistributions().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InFiniteMassPointsDiscreteDistribution input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        SpecificationHelper mpSpec = spec.getParametersSpec();
        for(InMassPoint mp: input.getMassPoints()) {
            mpSpec.set(Double.toString(mp.getValue()), mp.getWeight());
        }
    }

    @Override
    public InFiniteMassPointsDiscreteDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);

        InFiniteMassPointsDiscreteDistribution dist = new InFiniteMassPointsDiscreteDistribution();
        dist.setName(spec.getName());

        List<InMassPoint> mps = new ArrayList<>();
        SpecificationHelper mpSpec = spec.getParametersSpec();
        for(Map.Entry<String, JsonNode> entry: mpSpec.iterateFields()) {
            double value = Double.parseDouble(entry.getKey());
            double weight = entry.getValue().doubleValue();
            mps.add(new InMassPoint("MP_" + cache.getAndInc("MP"), + value, weight));
        }
        dist.setMassPoints(mps.toArray(new InMassPoint[0]));

        return dist;
    }
}
