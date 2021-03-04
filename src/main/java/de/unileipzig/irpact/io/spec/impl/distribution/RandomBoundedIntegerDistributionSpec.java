package de.unileipzig.irpact.io.spec.impl.distribution;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistributionSpec
        implements ToSpecConverter<InRandomBoundedIntegerDistribution>, ToParamConverter<InRandomBoundedIntegerDistribution> {

    public static final RandomBoundedIntegerDistributionSpec INSTANCE = new RandomBoundedIntegerDistributionSpec();
    public static final String TYPE = "RandomBoundedIntegerDistribution";

    @Override
    public Class<InRandomBoundedIntegerDistribution> getParamType() {
        return InRandomBoundedIntegerDistribution.class;
    }

    @Override
    public void toSpec(InRandomBoundedIntegerDistribution input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        if(manager.getDistributions().hasNot(input.getName())) {
            create(input, manager.getDistributions().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InRandomBoundedIntegerDistribution input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_lowerBound, input.getLowerBound());
        spec.setParameters(TAG_upperBound, input.getUpperBound());
    }

    @Override
    public InRandomBoundedIntegerDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        return new InRandomBoundedIntegerDistribution(spec.getName(), spec.getParametersInt(TAG_lowerBound), spec.getParametersInt(TAG_upperBound));
    }
}
