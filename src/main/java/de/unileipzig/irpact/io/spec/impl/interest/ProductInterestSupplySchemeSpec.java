package de.unileipzig.irpact.io.spec.impl.interest;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.interest.InProductInterestSupplyScheme;
import de.unileipzig.irpact.io.spec.*;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductInterestSupplySchemeSpec
        implements ToParamConverter<InProductInterestSupplyScheme> {

    public static final ProductInterestSupplySchemeSpec INSTANCE = new ProductInterestSupplySchemeSpec();

    @Override
    public InProductInterestSupplyScheme[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        throw new UnsupportedOperationException("currently inline only");
    }

    @Override
    public InProductInterestSupplyScheme toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String type = spec.getType();
        System.out.println("ZZZ " + spec.root() + " " + System.identityHashCode(root));

        if(ProductThresholdInterestSupplySchemeSpec.TYPE.equals(type)) {
            return ProductThresholdInterestSupplySchemeSpec.INSTANCE.toParam(root, manager, converter, cache);
        }
        else {
            throw new IllegalArgumentException("unknown type: " + type);
        }
    }

    public InProductInterestSupplyScheme toParamByName(String name, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        for(InProductInterestSupplyScheme scheme: toParam(manager, converter, cache)) {
            if(Objects.equals(scheme.getName(), name)) {
                return scheme;
            }
        }
        throw new ParsingException("not found: " + name);
    }
}
