package de.unileipzig.irpact.io.spec.impl.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class DiscreteTimeModelSpec extends AbstractSubSpec<InDiscreteTimeModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DiscreteTimeModelSpec.class);

    public static final DiscreteTimeModelSpec INSTANCE = new DiscreteTimeModelSpec();
    public static final String TYPE = "DiscreteTimeModel";

    @Override
    public boolean isType(String type) {
        return Objects.equals(type, TYPE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isInstance(Object input) {
        return input instanceof InDiscreteTimeModel;
    }

    @Override
    protected InDiscreteTimeModel[] newArray(int len) {
        return new InDiscreteTimeModel[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InDiscreteTimeModel[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getTimeModel(), job);
    }

    @Override
    public InDiscreteTimeModel toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InDiscreteTimeModel timeModel = new InDiscreteTimeModel(
                name,
                rootSpec.getLong(TAG_parameters, TAG_timePerTickInMs)
        );
        job.cache(name, timeModel);
        return timeModel;
    }

    @Override
    public Class<InDiscreteTimeModel> getParamType() {
        return InDiscreteTimeModel.class;
    }

    @Override
    public void toSpec(InDiscreteTimeModel input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getTimeModel(), job);
    }

    @Override
    protected void create(InDiscreteTimeModel input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_timePerTickInMs, input.getTimePerTickInMs());
    }
}
