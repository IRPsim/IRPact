package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InFixProductFindingScheme;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FixProductFindingSchemeSpec
        implements ToSpecConverter<InFixProductFindingScheme>, ToParamConverter<InFixProductFindingScheme> {

    public static final FixProductFindingSchemeSpec INSTANCE = new FixProductFindingSchemeSpec();
    public static final String TYPE = "FixProductFindingScheme";

    @Override
    public Class<InFixProductFindingScheme> getParamType() {
        return InFixProductFindingScheme.class;
    }

    @Override
    public void toSpec(InFixProductFindingScheme input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getTimeModel().get(), manager, converter, inline);
    }

    @Override
    public void create(InFixProductFindingScheme input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.set(TAG_fixProduct, input.getFixProduct().getName());
    }

    @Override
    public InFixProductFindingScheme toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        try {
            SpecificationHelper spec = new SpecificationHelper(root);
            String name = spec.getName();

            if(cache.has(name)) {
                return cache.getAs(name);
            }

            InFixProduct product = converter.getFixProduct(spec.getText(TAG_fixProduct), manager, cache);
            InFixProductFindingScheme scheme = new InFixProductFindingScheme(name, product);
            cache.securePut(name, scheme);
            return scheme;
        } catch (ParsingException e) {
            throw e.unchecked();
        }
    }
}
