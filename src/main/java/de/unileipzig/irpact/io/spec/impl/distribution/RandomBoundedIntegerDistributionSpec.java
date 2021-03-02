package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.io.param.input.distribution.InRandomBoundedIntegerDistribution;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_lowerBound;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_upperBound;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistributionSpec extends SpecBase<InRandomBoundedIntegerDistribution, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InRandomBoundedIntegerDistribution> getParamType() {
        return InRandomBoundedIntegerDistribution.class;
    }

    @Override
    public void toSpec(InRandomBoundedIntegerDistribution instance, SpecificationManager manager, SpecificationConverter converter) {
        if(!manager.hasDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("RandomBoundedIntegerDistribution");
            spec.setParameters(TAG_lowerBound, instance.getLowerBound());
            spec.setParameters(TAG_upperBound, instance.getUpperBound());
        }
    }
}
