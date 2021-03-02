package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.network.InInverse;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class InverseSpec extends SpecBase<InInverse, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InInverse> getParamType() {
        return InInverse.class;
    }

    @Override
    public void toSpec(InInverse instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        if(!manager.hasEvaluator(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getEvaluator(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("Inverse");
        }
    }
}
