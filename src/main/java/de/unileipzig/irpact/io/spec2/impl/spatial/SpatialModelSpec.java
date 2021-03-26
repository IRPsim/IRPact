package de.unileipzig.irpact.io.spec2.impl.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.spec2.SpecificationJob2;
import de.unileipzig.irpact.io.spec2.impl.AbstractSubSpec;
import de.unileipzig.irpact.io.spec2.impl.AbstractSuperSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class SpatialModelSpec extends AbstractSuperSpec<InSpatialModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialModelSpec.class);

    public static final SpatialModelSpec INSTANCE = new SpatialModelSpec();

    private static final List<AbstractSubSpec<? extends InSpatialModel>> MODELS = createModels(
            Space2DSpec.INSTANCE
    );

    @Override
    protected InSpatialModel[] newArray(int len) {
        return new InSpatialModel[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    protected Collection<? extends AbstractSubSpec<? extends InSpatialModel>> getModels() {
        return MODELS;
    }

    @Override
    public InSpatialModel[] toParamArray(SpecificationJob2 job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public Class<InSpatialModel> getParamType() {
        return InSpatialModel.class;
    }
}