package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialDistributionSpec extends AbstractSuperSpec<InSpatialDistribution> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialDistributionSpec.class);

    public static final SpatialDistributionSpec INSTANCE = new SpatialDistributionSpec();

    private static final List<AbstractSubSpec<? extends InSpatialDistribution>> MODELS = createModels(
            CustomSelectedGroupedSpatialDistribution2DSpec.INSTANCE,
            SelectedGroupedSpatialDistribution2DSpec.INSTANCE
    );

    @Override
    protected InSpatialDistribution[] newArray(int len) {
        return new InSpatialDistribution[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InSpatialDistribution>> getModels() {
        return MODELS;
    }

    @Override
    public InSpatialDistribution[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InSpatialDistribution> getParamType() {
        return InSpatialDistribution.class;
    }
}
