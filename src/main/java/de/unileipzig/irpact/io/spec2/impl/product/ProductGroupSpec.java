package de.unileipzig.irpact.io.spec2.impl.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.spec2.BidirectionalConverter2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;

/**
 * @author Daniel Abitz
 */
public class ProductGroupSpec implements BidirectionalConverter2<InProductGroup> {

    public static final ProductGroupSpec INSTANCE = new ProductGroupSpec();

    @Override
    public InProductGroup[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return new InProductGroup[0];
    }

    @Override
    public InProductGroup toParam(ObjectNode root, SpecificationJob2 job) throws ParsingException {
        return null;
    }

    @Override
    public Class<InProductGroup> getParamType() {
        return InProductGroup.class;
    }

    @Override
    public void toSpec(InProductGroup[] inputArray, SpecificationJob2 job) throws ParsingException {

    }

    @Override
    public void toSpec(InProductGroup input, SpecificationJob2 job) throws ParsingException {

    }

    @Override
    public void create(InProductGroup input, ObjectNode root, SpecificationJob2 job) throws ParsingException {

    }
}
