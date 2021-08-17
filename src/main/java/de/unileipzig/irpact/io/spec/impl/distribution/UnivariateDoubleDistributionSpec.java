package de.unileipzig.irpact.io.spec.impl.distribution;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UnivariateDoubleDistributionSpec extends AbstractSuperSpec<InUnivariateDoubleDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnivariateDoubleDistributionSpec.class);

    public static final UnivariateDoubleDistributionSpec INSTANCE = new UnivariateDoubleDistributionSpec();

    private static final List<AbstractSubSpec<? extends InUnivariateDoubleDistribution>> MODELS = createModels(
            BooleanDistributionSpec.INSTANCE,
            ConstantUnivariateDistributionSpec.INSTANCE,
            FiniteMassPointsDiscreteDistributionSpec.INSTANCE,
            RandomBoundedIntegerDistributionSpec.INSTANCE
    );

    @Override
    protected InUnivariateDoubleDistribution[] newArray(int len) {
        return new InUnivariateDoubleDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InUnivariateDoubleDistribution>> getModels() {
        return MODELS;
    }

    @Override
    public InUnivariateDoubleDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getDistributions(), job);
    }

    @Override
    public Class<InUnivariateDoubleDistribution> getParamType() {
        return InUnivariateDoubleDistribution.class;
    }
}
