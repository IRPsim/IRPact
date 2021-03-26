package de.unileipzig.irpact.io.spec2.impl.distance;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.network.InDistanceEvaluator;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
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
    public InDistanceEvaluator[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getConsumerAgentGroups(), job);
    }

    @Override
    public Class<InDistanceEvaluator> getParamType() {
        return InDistanceEvaluator.class;
    }
}
