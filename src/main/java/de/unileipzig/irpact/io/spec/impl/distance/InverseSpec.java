package de.unileipzig.irpact.io.spec.impl.distance;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InInverse;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_name;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_type;

/**
 * @author Daniel Abitz
 */
public class InverseSpec extends AbstractSubSpec<InInverse> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InverseSpec.class);

    public static final InverseSpec INSTANCE = new InverseSpec();
    public static final String TYPE = "Inverse";

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
        return input instanceof InInverse;
    }

    @Override
    protected InInverse[] newArray(int len) {
        return new InInverse[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InInverse[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InInverse toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InInverse dist = new InInverse();
        dist.setName(name);

        job.cache(name, dist);
        return dist;
    }

    @Override
    public Class<InInverse> getParamType() {
        return InInverse.class;
    }

    @Override
    public void toSpec(InInverse input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InInverse input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
    }
}
