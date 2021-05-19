package de.unileipzig.irpact.io.spec.impl.network;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InGraphTopologyScheme;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class GraphTopologySpec extends AbstractSuperSpec<InGraphTopologyScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GraphTopologySpec.class);

    public static final GraphTopologySpec INSTANCE = new GraphTopologySpec();

    private static final List<AbstractSubSpec<? extends InGraphTopologyScheme>> MODELS = createModels(
            FreeNetworkTopologySpec.INSTANCE,
            UnlinkedGraphTopologySpec.INSTANCE
    );

    @Override
    protected InGraphTopologyScheme[] newArray(int len) {
        return new InGraphTopologyScheme[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InGraphTopologyScheme>> getModels() {
        return MODELS;
    }

    @Override
    public InGraphTopologyScheme[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InGraphTopologyScheme> getParamType() {
        return InGraphTopologyScheme.class;
    }
}
