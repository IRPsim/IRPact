package de.unileipzig.irpact.io.spec.impl;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
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
    public InGeneral[] toParamArray(SpecificationJob job) throws ParsingException {
        return new InGeneral[]{
                toParam(job.getData().getGeneral().get(), job)
        };
    }

    @Override
    public InGeneral toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        InGeneral general = new InGeneral();
        general.seed = rootSpec.getLong(TAG_seed);
        general.timeout = rootSpec.getLong(TAG_timeout);
        general.setFirstSimulationYear(rootSpec.getInt(TAG_startYear));
        general.lastSimulationYear = rootSpec.getInt(TAG_endYear);
        general.logLevel = rootSpec.getInt(TAG_logLevel);
        general.logAll = rootSpec.getIntAsBoolean(TAG_logAll);
        return general;
    }

    @Override
    public Class<InGeneral> getParamType() {
        return InGeneral.class;
    }

    @Override
    public void toSpec(InGeneral input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getGeneral(), job);
    }

    @Override
    protected void create(InGeneral input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_seed, input.seed);
        rootSpec.set(TAG_timeout, input.timeout);
        rootSpec.set(TAG_logLevel, input.logLevel);
        rootSpec.setAsInt(TAG_logAll, input.logAll);
        rootSpec.set(TAG_startYear, input.getFirstSimulationYear());
        rootSpec.set(TAG_endYear, input.lastSimulationYear);
    }
}
