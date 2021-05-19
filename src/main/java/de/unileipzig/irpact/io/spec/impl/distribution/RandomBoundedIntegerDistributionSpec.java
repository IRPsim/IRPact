package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InBoundedUniformIntegerDistribution;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class RandomBoundedIntegerDistributionSpec extends AbstractSubSpec<InBoundedUniformIntegerDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RandomBoundedIntegerDistributionSpec.class);

    public static final RandomBoundedIntegerDistributionSpec INSTANCE = new RandomBoundedIntegerDistributionSpec();
    public static final String TYPE = "RandomBoundedIntegerDistribution";

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
        return input instanceof InBoundedUniformIntegerDistribution;
    }

    @Override
    protected InBoundedUniformIntegerDistribution[] newArray(int len) {
        return new InBoundedUniformIntegerDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InBoundedUniformIntegerDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InBoundedUniformIntegerDistribution toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InBoundedUniformIntegerDistribution distribution = new InBoundedUniformIntegerDistribution(
                name,
                rootSpec.getInt(TAG_parameters, TAG_lowerBound),
                rootSpec.getInt(TAG_parameters, TAG_upperBound)
        );
        job.cache(name, distribution);
        return distribution;
    }

    @Override
    public Class<InBoundedUniformIntegerDistribution> getParamType() {
        return InBoundedUniformIntegerDistribution.class;
    }

    @Override
    public void toSpec(InBoundedUniformIntegerDistribution input, SpecificationJob job) throws ParsingException {
        toSpecIfNotExists(input.getName(), job.getData().getDistributions(), input, job);
    }

    @Override
    protected void create(InBoundedUniformIntegerDistribution input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_lowerBound, input.getLowerBound());
        rootSpec.set(TAG_parameters, TAG_upperBound, input.getUpperBound());
    }
}
