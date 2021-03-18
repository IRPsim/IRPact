package de.unileipzig.irpact.io.spec2.impl.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.time.InTimeModel;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class TimeModelSpec extends AbstractSuperSpec<InTimeModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(TimeModelSpec.class);

    public static final TimeModelSpec INSTANCE = new TimeModelSpec();

    private static final List<AbstractSubSpec<? extends InTimeModel>> MODELS = createModels(
            DiscreteTimeModelSpec.INSTANCE
    );

    @Override
    protected InTimeModel[] newArray(int len) {
        return new InTimeModel[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InTimeModel[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getTimeModel(), job);
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InTimeModel>> getModels() {
        return MODELS;
    }

    @Override
    public Class<InTimeModel> getParamType() {
        return InTimeModel.class;
    }
}
