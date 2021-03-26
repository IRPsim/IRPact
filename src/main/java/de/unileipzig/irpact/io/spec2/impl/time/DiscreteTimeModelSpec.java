package de.unileipzig.irpact.io.spec2.impl.time;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec2.SpecificationConstants.*;

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
    public InDiscreteTimeModel[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getTimeModel(), job);
    }

    @Override
    public InDiscreteTimeModel toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
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
    public void toSpec(InDiscreteTimeModel input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getTimeModel(), job);
    }

    @Override
    protected void create(InDiscreteTimeModel input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_timePerTickInMs, input.getTimePerTickInMs());
    }
}
