package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InFixProduct;
import de.unileipzig.irpact.io.param.input.product.InFixProductAttribute;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_group;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_value;

/**
 * @author Daniel Abitz
 */
public class FixProductSpec extends SpecBase<InFixProduct, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InFixProduct> getParamType() {
        return InFixProduct.class;
    }

    @Override
    public void toSpec(InFixProduct instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        if(!manager.hasFixProduct(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getFixProduct(instance.getName()));
            spec.setName(instance.getName());
            spec.set(TAG_group, instance.getProductGroup().getName());

            ArrayNode arr = spec.getAttributes();
            for(InFixProductAttribute inAttr: instance.getAttributes()) {
                SpecificationHelper specAttr = new SpecificationHelper(arr.addObject());
                specAttr.setName(inAttr.getName());
                specAttr.set(TAG_group, inAttr.getRefPGA().getName());
                specAttr.set(TAG_value, inAttr.getValue());
            }
        }
    }
}
