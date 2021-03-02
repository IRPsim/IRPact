package de.unileipzig.irpact.io.spec.impl.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_timePerTickInMs;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModelSpec extends SpecBase<InDiscreteTimeModel, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InDiscreteTimeModel> getParamType() {
        return InDiscreteTimeModel.class;
    }

    @Override
    public void toSpec(InDiscreteTimeModel instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getTime());
        spec.setName(instance.getName());
        spec.setType("DiscreteTimeModel");
        spec.set(TAG_timePerTickInMs, instance.getTimePerTickInMs());
    }
}
