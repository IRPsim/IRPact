package de.unileipzig.irpact.io.spec2.impl.agent.consumer;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ConsumerAgentGroupSpec extends AbstractSuperSpec<InConsumerAgentGroup> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ConsumerAgentGroupSpec.class);

    public static final ConsumerAgentGroupSpec INSTANCE = new ConsumerAgentGroupSpec();

    private static final List<AbstractSubSpec<? extends InConsumerAgentGroup>> MODELS = createModels(
            GeneralConsumerAgentGroupSpec.INSTANCE,
            PVactConsumerAgentGroupSpec.INSTANCE
    );

    @Override
    protected InConsumerAgentGroup[] newArray(int len) {
        return new InConsumerAgentGroup[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InConsumerAgentGroup>> getModels() {
        return MODELS;
    }

    @Override
    public InConsumerAgentGroup[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InConsumerAgentGroup> getParamType() {
        return InConsumerAgentGroup.class;
    }
}
