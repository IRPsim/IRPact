package de.unileipzig.irpact.io.spec.impl.product;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.product.InProductGroup;
import de.unileipzig.irpact.io.spec.BidirectionalConverter;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.util.Todo;

/**
 * @author Daniel Abitz
 */
@Todo
public class ProductGroupSpec implements BidirectionalConverter<InProductGroup> {

    public static final ProductGroupSpec INSTANCE = new ProductGroupSpec();

    @Override
    public InProductGroup[] toParamArray(SpecificationJob job) throws ParsingException {
        return new InProductGroup[0];
    }

    @Override
    public InProductGroup toParam(ObjectNode root, SpecificationJob job) throws ParsingException {
        return null;
    }

    @Override
    public Class<InProductGroup> getParamType() {
        return InProductGroup.class;
    }

    @Override
    public void toSpec(InProductGroup[] inputArray, SpecificationJob job) throws ParsingException {

    }

    @Override
    public void toSpec(InProductGroup input, SpecificationJob job) throws ParsingException {

    }

    @Override
    public void create(InProductGroup input, ObjectNode root, SpecificationJob job) throws ParsingException {

    }
}
