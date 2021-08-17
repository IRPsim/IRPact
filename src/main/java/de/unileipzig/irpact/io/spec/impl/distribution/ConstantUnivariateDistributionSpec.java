package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class ConstantUnivariateDistributionSpec extends AbstractSubSpec<InDiracUnivariateDistribution> {

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
        return input instanceof InDiracUnivariateDistribution;
    }

    @Override
    protected InDiracUnivariateDistribution[] newArray(int len) {
        return new InDiracUnivariateDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InDiracUnivariateDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InDiracUnivariateDistribution toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InDiracUnivariateDistribution distribution = new InDiracUnivariateDistribution(
                name,
                rootSpec.getDouble(TAG_parameters, TAG_value)
        );
        job.cache(name, distribution);
        return distribution;
    }

    @Override
    public Class<InDiracUnivariateDistribution> getParamType() {
        return InDiracUnivariateDistribution.class;
    }

    @Override
    public void toSpec(InDiracUnivariateDistribution input, SpecificationJob job) throws ParsingException {
        toSpecIfNotExists(input.getName(), job.getData().getDistributions(), input, job);
    }

    @Override
    protected void create(InDiracUnivariateDistribution input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_value, input.getValue());
    }
}
