package de.unileipzig.irpact.io.spec.impl.interest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.SpecUtil;

/**
 * @author Daniel Abitz
 */
public class ProductThresholdInterestSupplySchemeSpec
        implements ToSpecConverter<InProductThresholdInterestSupplyScheme>, ToParamConverter<InProductThresholdInterestSupplyScheme> {

    public static final ProductThresholdInterestSupplySchemeSpec INSTANCE = new ProductThresholdInterestSupplySchemeSpec();
    public static final String TYPE = "ProductThresholdInterestSupplyScheme";

    @Override
    public Class<InProductThresholdInterestSupplyScheme> getParamType() {
        return InProductThresholdInterestSupplyScheme.class;
    }

    @Override
    public void toSpec(InProductThresholdInterestSupplyScheme input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getTimeModel().get(), manager, converter, inline);
    }

    @Override
    public void create(InProductThresholdInterestSupplyScheme input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);

        SpecUtil.inlineDistribution(
                input.getAwarenessDistribution(),
                spec,
                manager,
                converter,
                inline
        );
    }

    @Override
    public InProductThresholdInterestSupplyScheme toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String name = spec.getName();

        if(cache.has(name)) {
            return cache.getAs(name);
        }

        InUnivariateDoubleDistribution dist = SpecUtil.parseInlinedDistribution(
                spec,
                manager,
                converter,
                cache
        );

        InProductThresholdInterestSupplyScheme scheme = new InProductThresholdInterestSupplyScheme(name, dist);
        cache.securePut(name, scheme);
        return scheme;
    }
}
