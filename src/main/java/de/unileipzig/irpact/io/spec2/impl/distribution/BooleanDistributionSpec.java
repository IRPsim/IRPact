package de.unileipzig.irpact.io.spec2.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irpact.io.spec2.SpecificationHelper2;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class BooleanDistributionSpec extends AbstractSubSpec<InBooleanDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BooleanDistributionSpec.class);

    public static final BooleanDistributionSpec INSTANCE = new BooleanDistributionSpec();
    public static final String TYPE = "BooleanDistribution";

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
        return input instanceof InBooleanDistribution;
    }

    @Override
    protected InBooleanDistribution[] newArray(int len) {
        return new InBooleanDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InBooleanDistribution[] toParamArray(SpecificationJob2 job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InBooleanDistribution toParam(SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InBooleanDistribution distribution = new InBooleanDistribution(name);
        job.cache(name, distribution);
        return distribution;
    }

    @Override
    public Class<InBooleanDistribution> getParamType() {
        return InBooleanDistribution.class;
    }

    @Override
    public void toSpec(InBooleanDistribution input, SpecificationJob2 job) throws ParsingException {
        toSpecIfNotExists(input.getName(), job.getData().getDistributions(), input, job);
    }

    @Override
    protected void create(InBooleanDistribution input, SpecificationHelper2 rootSpec, SpecificationJob2 job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
    }
}
