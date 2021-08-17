package de.unileipzig.irpact.io.spec.impl.distance;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InDistanceEvaluator;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class DistanceEvaluatorSpec extends AbstractSuperSpec<InDistanceEvaluator> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DistanceEvaluatorSpec.class);

    public static final DistanceEvaluatorSpec INSTANCE = new DistanceEvaluatorSpec();

    private static final List<AbstractSubSpec<? extends InDistanceEvaluator>> MODELS = createModels(
            InverseSpec.INSTANCE,
            NoDistanceSpec.INSTANCE
    );

    @Override
    protected InDistanceEvaluator[] newArray(int len) {
        return new InDistanceEvaluator[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InDistanceEvaluator>> getModels() {
        return MODELS;
    }

    @Override
    public InDistanceEvaluator[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InDistanceEvaluator> getParamType() {
        return InDistanceEvaluator.class;
    }
}
