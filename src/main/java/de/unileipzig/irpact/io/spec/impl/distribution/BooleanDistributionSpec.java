package de.unileipzig.irpact.io.spec.impl.distribution;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irpact.io.spec.*;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionSpec
        implements ToSpecConverter<InBooleanDistribution>, ToParamConverter<InBooleanDistribution> {

    public static final BooleanDistributionSpec INSTANCE = new BooleanDistributionSpec();
    public static final String TYPE = "BooleanDistribution";

    @Override
    public Class<InBooleanDistribution> getParamType() {
        return InBooleanDistribution.class;
    }

    @Override
    public void toSpec(InBooleanDistribution input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        if(manager.getDistributions().hasNot(input.getName())) {
            create(input, manager.getDistributions().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InBooleanDistribution input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
    }

    @Override
    public InBooleanDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        return new InBooleanDistribution(spec.getName());
    }
}
