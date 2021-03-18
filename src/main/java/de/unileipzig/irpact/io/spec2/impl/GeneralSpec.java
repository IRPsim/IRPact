package de.unileipzig.irpact.io.spec2.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irptools.util.log.IRPLogger;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class GeneralSpec extends AbstractSpec<InGeneral> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GeneralSpec.class);

    public static final GeneralSpec INSTANCE = new GeneralSpec();

    @Override
    protected InGeneral[] newArray(int len) {
        return new InGeneral[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InGeneral[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return new InGeneral[]{
                toParam(job.getData().getGeneral().get(), job)
        };
    }

    @Override
    public InGeneral toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        InGeneral general = new InGeneral();
        general.seed = rootSpec.getLong(TAG_seed);
        general.timeout = rootSpec.getLong(TAG_timeout);
        general.startYear = rootSpec.getInt(TAG_startYear);
        general.endYear = rootSpec.getInt(TAG_endYear);
        general.logLevel = rootSpec.getInt(TAG_logLevel);
        general.logAll = rootSpec.getIntAsBoolean(TAG_logAll);
        return general;
    }

    @Override
    public Class<InGeneral> getParamType() {
        return InGeneral.class;
    }

    @Override
    public void toSpec(InGeneral input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getGeneral(), job);
    }

    @Override
    protected void create(InGeneral input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_seed, input.seed);
        rootSpec.set(TAG_timeout, input.timeout);
        rootSpec.set(TAG_logLevel, input.logLevel);
        rootSpec.setAsInt(TAG_logAll, input.logAll);
        rootSpec.set(TAG_startYear, input.startYear);
        rootSpec.set(TAG_endYear, input.endYear);
    }
}
