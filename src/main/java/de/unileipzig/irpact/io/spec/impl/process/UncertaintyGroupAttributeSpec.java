package de.unileipzig.irpact.io.spec.impl.process;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.process.InCustomUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.param.input.process.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.spec.*;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributeSpec implements ToSpecConverter<InUncertaintyGroupAttribute>, ToParamConverter<InUncertaintyGroupAttribute> {

    public static final UncertaintyGroupAttributeSpec INSTANCE = new UncertaintyGroupAttributeSpec();

    @Override
    public InUncertaintyGroupAttribute[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        return new InUncertaintyGroupAttribute[0];
    }

    @Override
    public InUncertaintyGroupAttribute toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String type = spec.getType();

        if(CustomUncertaintyGroupAttributeSpec.TYPE.equals(type)) {
            return CustomUncertaintyGroupAttributeSpec.INSTANCE.toParam(root, manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    public InUncertaintyGroupAttribute[] toParam(ArrayNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        InUncertaintyGroupAttribute[] arr = new InUncertaintyGroupAttribute[root.size()];
        for(int i = 0; i < root.size(); i++) {
            ObjectNode node = (ObjectNode) root.get(i);
            arr[i] = toParam(node, manager, converter, cache);
        }
        return arr;
    }

    @Override
    public Class<InUncertaintyGroupAttribute> getParamType() {
        return InUncertaintyGroupAttribute.class;
    }

    @Override
    public void toSpec(InUncertaintyGroupAttribute input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InCustomUncertaintyGroupAttribute) {
            CustomUncertaintyGroupAttributeSpec.INSTANCE.toSpec((InCustomUncertaintyGroupAttribute) input, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public void create(InUncertaintyGroupAttribute input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InCustomUncertaintyGroupAttribute) {
            CustomUncertaintyGroupAttributeSpec.INSTANCE.create((InCustomUncertaintyGroupAttribute) input, root, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }
}
