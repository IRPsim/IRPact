package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.SpecUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ProductGroupSpec
        implements ToSpecConverter<InProductGroup>, ToParamConverter<InProductGroup> {

    public static final ProductGroupSpec INSTANCE = new ProductGroupSpec();

    @Override
    public Class<InProductGroup> getParamType() {
        return InProductGroup.class;
    }

    @Override
    public void toSpec(InProductGroup input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(manager.getProductGroups().hasNot(input.getName())) {
            create(input, manager.getProductGroups().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InProductGroup input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());

        SpecificationHelper attrsSpec = spec.getAttributesSpec();
        for(InProductGroupAttribute inAttr: input.getAttributes()) {
            SpecificationHelper attrSpec = attrsSpec.addObjectSpec();
            attrSpec.setName(inAttr.getAttrNameString());

            SpecUtil.inlineDistribution(
                    inAttr.getAttrDistribution(),
                    attrSpec,
                    manager,
                    converter,
                    inline
            );
        }
    }

    @Override
    public InProductGroup[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        List<InProductGroup> pgs = new ArrayList<>();
        for(ObjectNode root: manager.getProductGroups().getAll().values()) {
            InProductGroup pg = toParam(root, manager, converter, cache);
            pgs.add(pg);
        }
        return pgs.toArray(new InProductGroup[0]);
    }

    @Override
    public InProductGroup toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String name = spec.getName();

        if(cache.has(name)) {
            return cache.getAs(name);
        }

        List<InProductGroupAttribute> attrs = new ArrayList<>();
        SpecificationHelper attrsSpec = spec.getAttributesSpec();
        for(JsonNode childNode: attrsSpec.iterateElements()) {
            SpecificationHelper child = new SpecificationHelper(childNode);
            String attrName = child.getName();
            InUnivariateDoubleDistribution dist = SpecUtil.parseInlinedDistribution(
                    child,
                    manager,
                    converter,
                    cache
            );
            InProductGroupAttribute inAttr = new InProductGroupAttribute(name + "_" + attrName, cache.getAttrName(attrName), dist);
            attrs.add(inAttr);
        }

        InProductGroup pg = new InProductGroup(name, attrs.toArray(new InProductGroupAttribute[0]));
        cache.securePut(name, pg);
        return pg;
    }
}
