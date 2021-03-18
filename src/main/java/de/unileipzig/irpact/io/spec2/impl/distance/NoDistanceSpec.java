package de.unileipzig.irpact.io.spec2.impl.distance;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class NoDistanceSpec extends AbstractSubSpec<InNoDistance> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(NoDistanceSpec.class);

    public static final NoDistanceSpec INSTANCE = new NoDistanceSpec();
    public static final String TYPE = "NoDistance";

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
        return input instanceof InNoDistance;
    }

    @Override
    protected InNoDistance[] newArray(int len) {
        return new InNoDistance[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InNoDistance[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InNoDistance toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InNoDistance dist = new InNoDistance();
        dist.setName(name);

        job.cache(name, dist);
        return dist;
    }

    @Override
    public Class<InNoDistance> getParamType() {
        return InNoDistance.class;
    }

    @Override
    public void toSpec(InNoDistance input, SpecificationJob2 job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InNoDistance input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
    }
}
