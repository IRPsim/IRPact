package de.unileipzig.irpact.io.spec.impl.distribution;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_value;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDistributionSpec
        implements ToSpecConverter<InConstantUnivariateDistribution>, ToParamConverter<InConstantUnivariateDistribution> {

    public static final ConstantUnivariateDistributionSpec INSTANCE = new ConstantUnivariateDistributionSpec();
    public static final String TYPE = "ConstantUnivariateDoubleDistribution";

    @Override
    public Class<InConstantUnivariateDistribution> getParamType() {
        return InConstantUnivariateDistribution.class;
    }

    @Override
    public void toSpec(InConstantUnivariateDistribution input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        if(manager.getDistributions().hasNot(input.getName())) {
            create(input, manager.getDistributions().get(input.getName()), manager, converter ,inline);
        }
    }

    @Override
    public void create(InConstantUnivariateDistribution input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_value, input.getConstDistValue());
    }

    @Override
    public InConstantUnivariateDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        return new InConstantUnivariateDistribution(spec.getName(), spec.getParametersDouble(TAG_value));
    }
}
