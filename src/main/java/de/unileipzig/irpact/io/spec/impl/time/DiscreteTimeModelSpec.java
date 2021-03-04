package de.unileipzig.irpact.io.spec.impl.time;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_timePerTickInMs;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModelSpec
        implements ToSpecConverter<InDiscreteTimeModel>, ToParamConverter<InDiscreteTimeModel> {

    public static final DiscreteTimeModelSpec INSTANCE = new DiscreteTimeModelSpec();
    public static final String TYPE = "DiscreteTimeModel";

    @Override
    public InDiscreteTimeModel[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        InDiscreteTimeModel model = toParam(manager.getTimeModel().get(), manager, converter, cache);
        return new InDiscreteTimeModel[]{model};
    }

    @Override
    public InDiscreteTimeModel toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        return new InDiscreteTimeModel(spec.getName(), spec.getParametersLong(TAG_timePerTickInMs));
    }

    @Override
    public Class<InDiscreteTimeModel> getParamType() {
        return InDiscreteTimeModel.class;
    }

    @Override
    public void toSpec(InDiscreteTimeModel input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        create(input, manager.getTimeModel().get(), manager, converter, inline);
    }

    @Override
    public void create(InDiscreteTimeModel input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_timePerTickInMs, input.getTimePerTickInMs());
    }
}
