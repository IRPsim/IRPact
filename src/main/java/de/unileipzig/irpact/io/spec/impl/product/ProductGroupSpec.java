package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.node.ArrayNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.param.input.product.InProductGroupAttribute;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ProductGroupSpec extends SpecBase<InProductGroup, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InProductGroup> getParamType() {
        return InProductGroup.class;
    }

    @Override
    public void toSpec(InProductGroup instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        if(!manager.hasProductGroup(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getProductGroup(instance.getName()));
            spec.setName(instance.getName());

            ArrayNode arr = spec.getAttributes();
            for(InProductGroupAttribute inAttr: instance.getAttributes()) {
                SpecificationHelper specAttr = new SpecificationHelper(arr.addObject());
                specAttr.setName(inAttr.getAttrName().getName());
                specAttr.setDistribution(inAttr.getAttrDistribution().getName());

                converter.apply(manager, inAttr.getAttrDistribution());
            }
        }
    }
}
