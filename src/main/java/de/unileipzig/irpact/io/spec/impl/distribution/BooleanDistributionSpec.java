package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionSpec extends SpecBase<InBooleanDistribution, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InBooleanDistribution> getParamType() {
        return InBooleanDistribution.class;
    }

    @Override
    public void toSpec(InBooleanDistribution instance, SpecificationManager manager, SpecificationConverter converter) {
        if(!manager.hasDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("BooleanDistribution");
        }
    }
}
