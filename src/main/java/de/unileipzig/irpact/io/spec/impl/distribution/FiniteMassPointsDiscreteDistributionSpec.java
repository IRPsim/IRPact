package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.io.param.input.distribution.InFiniteMassPointsDiscreteDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InMassPoint;
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
public class FiniteMassPointsDiscreteDistributionSpec extends SpecBase<InFiniteMassPointsDiscreteDistribution, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InFiniteMassPointsDiscreteDistribution> getParamType() {
        return InFiniteMassPointsDiscreteDistribution.class;
    }

    @Override
    public void toSpec(InFiniteMassPointsDiscreteDistribution instance, SpecificationManager manager, SpecificationConverter converter) {
        if(!manager.hasDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("FiniteMassPointsDiscreteDistribution");
            for(InMassPoint mp: instance.getMassPoints()) {
                spec.setParameters(Double.toString(mp.getWeight()), mp.getValue());
            }
        }
    }
}
