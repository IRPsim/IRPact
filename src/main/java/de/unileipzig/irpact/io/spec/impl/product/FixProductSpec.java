package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InFixProductAttribute;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class FixProductSpec
        implements ToSpecConverter<InFixProduct>, ToParamConverter<InFixProduct> {

    public static final FixProductSpec INSTANCE = new FixProductSpec();

    @Override
    public Class<InFixProduct> getParamType() {
        return InFixProduct.class;
    }

    @Override
    public void toSpec(InFixProduct input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        String pgName = input.getProductGroup().getName();
        SpecificationHelper pgSpec = new SpecificationHelper(manager.getProductGroups().get(pgName));
        SpecificationHelper fixSpec = pgSpec.getArraySpec(TAG_fixProducts);
        create(input, fixSpec.addObject(), manager, converter, true);
    }

    @Override
    public void create(InFixProduct input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setGroup(input.getProductGroup().getName());

        SpecificationHelper attrsSpec = spec.getAttributesSpec();
        for(InFixProductAttribute inAttr: input.getAttributes()) {
            SpecificationHelper attrSpec = attrsSpec.addObjectSpec();
            attrSpec.setGroup(inAttr.getRefPGA().getAttrNameString());
            attrSpec.setValue(inAttr.getValue());
        }
    }

    @Override
    public InFixProduct[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        List<InFixProduct> products = new ArrayList<>();

        for(ObjectNode root: manager.getProductGroups().getAll().values()) {
            SpecificationHelper pgSpec = new SpecificationHelper(root);
            SpecificationHelper fixArraySpec = pgSpec.getArraySpec(TAG_fixProducts);
            for(JsonNode fixNode: fixArraySpec.iterateElements()) {
                SpecificationHelper fixSpec = new SpecificationHelper(fixNode);
                String name = fixSpec.getName();
                String pgName = fixSpec.getGroup();
                InProductGroup pg = converter.getProductGroup(pgName, manager, cache);

                List<InFixProductAttribute> fixAttrs = new ArrayList<>();
                SpecificationHelper attrsSpec = fixSpec.getAttributesSpec();
                for(JsonNode attrNode: attrsSpec.iterateElements()) {
                    SpecificationHelper attrSpec = new SpecificationHelper(attrNode);
                    String attrGroupName = attrSpec.getGroup();
                    InProductGroupAttribute attrGroup = pg.findAttribute(attrGroupName);
                    double value = attrSpec.getValue();

                    InFixProductAttribute fixAttr = new InFixProductAttribute(name + "_" + attrGroupName, attrGroup, value);
                    fixAttrs.add(fixAttr);
                }

                InFixProduct fixProduct = new InFixProduct(name, pg, fixAttrs.toArray(new InFixProductAttribute[0]));
                products.add(fixProduct);
            }
        }

        return products.toArray(new InFixProduct[0]);
    }
}
