package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InConstantUnivariateDistribution;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDistributionSpec extends AbstractSubSpec<InConstantUnivariateDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConstantUnivariateDistributionSpec.class);

    public static final ConstantUnivariateDistributionSpec INSTANCE = new ConstantUnivariateDistributionSpec();
    public static final String TYPE = "ConstantUnivariateDoubleDistribution";

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
        return input instanceof InConstantUnivariateDistribution;
    }

    @Override
    protected InConstantUnivariateDistribution[] newArray(int len) {
        return new InConstantUnivariateDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InConstantUnivariateDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InConstantUnivariateDistribution toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InConstantUnivariateDistribution distribution = new InConstantUnivariateDistribution(
                name,
                rootSpec.getDouble(TAG_parameters, TAG_value)
        );
        job.cache(name, distribution);
        return distribution;
    }

    @Override
    public Class<InConstantUnivariateDistribution> getParamType() {
        return InConstantUnivariateDistribution.class;
    }

    @Override
    public void toSpec(InConstantUnivariateDistribution input, SpecificationJob job) throws ParsingException {
        toSpecIfNotExists(input.getName(), job.getData().getDistributions(), input, job);
    }

    @Override
    protected void create(InConstantUnivariateDistribution input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_value, input.getConstDistValue());
    }
}
