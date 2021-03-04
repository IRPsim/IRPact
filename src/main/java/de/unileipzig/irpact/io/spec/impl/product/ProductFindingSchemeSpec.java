package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InProductFindingScheme;
import de.unileipzig.irpact.io.spec.*;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProductFindingSchemeSpec
        implements ToParamConverter<InProductFindingScheme> {

    public static final ProductFindingSchemeSpec INSTANCE = new ProductFindingSchemeSpec();

    @Override
    public InProductFindingScheme[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        throw new UnsupportedOperationException("currently inline only");
    }

    @Override
    public InProductFindingScheme toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String type = spec.getType();

        if(FixProductFindingSchemeSpec.TYPE.equals(type)) {
            return FixProductFindingSchemeSpec.INSTANCE.toParam(root, manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    public InProductFindingScheme toParamByName(String name, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        for(InProductFindingScheme scheme: toParam(manager, converter, cache)) {
            if(Objects.equals(scheme.getName(), name)) {
                return scheme;
            }
        }
        throw new ParsingException("not found: " + name);
    }
}
