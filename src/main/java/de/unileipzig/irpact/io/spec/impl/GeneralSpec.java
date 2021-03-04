package de.unileipzig.irpact.io.spec.impl;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_endYear;

/**
 * @author Daniel Abitz
 */
public class GeneralSpec implements ToSpecConverter<InGeneral>, ToParamConverter<InGeneral> {

    public static final GeneralSpec INSTANCE = new GeneralSpec();

    @Override
    public Class<InGeneral> getParamType() {
        return InGeneral.class;
    }

    @Override
    public void toSpec(InGeneral input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getGeneral().get(), manager, converter, inline);
    }

    @Override
    public void create(InGeneral input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.set(TAG_seed, input.seed);
        spec.set(TAG_timeout, input.timeout);
        spec.set(TAG_startYear, input.startYear);
        spec.set(TAG_endYear, input.endYear);
    }

    @Override
    public InGeneral[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        ObjectNode root = manager.getGeneral().get();
        return new InGeneral[] {
                toParam(root, manager, converter, cache)
        };
    }

    @Override
    public InGeneral toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        InGeneral general = new InGeneral();
        SpecificationHelper spec = new SpecificationHelper(root);
        general.seed = spec.getLong(TAG_seed);
        general.timeout = spec.getLong(TAG_timeout);
        general.startYear = spec.getInt(TAG_startYear);
        general.endYear = spec.getInt(TAG_endYear);
        return general;
    }
}
