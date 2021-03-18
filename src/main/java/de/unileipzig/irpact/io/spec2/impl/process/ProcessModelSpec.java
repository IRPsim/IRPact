package de.unileipzig.irpact.io.spec2.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ProcessModelSpec extends AbstractSuperSpec<InProcessModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProcessModelSpec.class);

    public static final ProcessModelSpec INSTANCE = new ProcessModelSpec();

    private static final List<AbstractSubSpec<? extends InProcessModel>> MODELS = createModels(
            RAProcessModelSpec.INSTANCE
    );

    @Override
    protected InProcessModel[] newArray(int len) {
        return new InProcessModel[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InProcessModel>> getModels() {
        return MODELS;
    }

    @Override
    public InProcessModel[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InProcessModel> getParamType() {
        return InProcessModel.class;
    }
}
