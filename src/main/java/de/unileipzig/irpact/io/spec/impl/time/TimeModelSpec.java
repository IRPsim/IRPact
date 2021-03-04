package de.unileipzig.irpact.io.spec.impl.time;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.spec.*;

/**
 * @author Daniel Abitz
 */
public class TimeModelSpec
        implements ToSpecConverter<InTimeModel>, ToParamConverter<InTimeModel> {

    public static final TimeModelSpec INSTANCE = new TimeModelSpec();

    @Override
    public InTimeModel[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getTimeModel().get());
        String type = spec.getType();

        if(DiscreteTimeModelSpec.TYPE.equals(type)) {
            return DiscreteTimeModelSpec.INSTANCE.toParam(manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    @Override
    public Class<InTimeModel> getParamType() {
        return InTimeModel.class;
    }

    @Override
    public void toSpec(InTimeModel input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InDiscreteTimeModel) {
            DiscreteTimeModelSpec.INSTANCE.toSpec((InDiscreteTimeModel) input, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public void create(InTimeModel input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InDiscreteTimeModel) {
            DiscreteTimeModelSpec.INSTANCE.create((InDiscreteTimeModel) input, root, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }
}
