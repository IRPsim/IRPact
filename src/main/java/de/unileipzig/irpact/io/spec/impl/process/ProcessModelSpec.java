package de.unileipzig.irpact.io.spec.impl.process;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
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
    public InProcessModel[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InProcessModel> getParamType() {
        return InProcessModel.class;
    }
}
