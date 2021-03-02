package de.unileipzig.irpact.io.spec.impl.interest;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_distribution;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplySchemeSpec extends SpecBase<InProductThresholdInterestSupplyScheme, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InProductThresholdInterestSupplyScheme> getParamType() {
        return InProductThresholdInterestSupplyScheme.class;
    }

    @Override
    public void toSpec(InProductThresholdInterestSupplyScheme instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        if(!manager.hasProductInterestSupplyScheme(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getProductInterestSupplyScheme(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("ProductThresholdInterestSupplyScheme");
            spec.set(TAG_distribution, instance.getAwarenessDistribution().getName());
        }
    }
}
