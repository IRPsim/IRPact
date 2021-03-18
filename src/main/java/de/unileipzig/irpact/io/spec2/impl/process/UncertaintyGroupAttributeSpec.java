package de.unileipzig.irpact.io.spec2.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.process.InUncertaintyGroupAttribute;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class UncertaintyGroupAttributeSpec extends AbstractSuperSpec<InUncertaintyGroupAttribute> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UncertaintyGroupAttributeSpec.class);

    public static final UncertaintyGroupAttributeSpec INSTANCE = new UncertaintyGroupAttributeSpec();

    private static final List<AbstractSubSpec<? extends InUncertaintyGroupAttribute>> MODELS = createModels(
            IndividualAttributeBasedUncertaintyGroupAttributeSpec.INSTANCE,
            IndividualAttributeBasedUncertaintyWithConvergenceGroupAttributeSpec.INSTANCE,
            NameBasedUncertaintyGroupAttributeSpec.INSTANCE,
            NameBasedUncertaintyWithConvergenceGroupAttributeSpec.INSTANCE
    );

    @Override
    protected InUncertaintyGroupAttribute[] newArray(int len) {
        return new InUncertaintyGroupAttribute[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InUncertaintyGroupAttribute>> getModels() {
        return MODELS;
    }

    @Override
    public InUncertaintyGroupAttribute[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InUncertaintyGroupAttribute> getParamType() {
        return InUncertaintyGroupAttribute.class;
    }
}
