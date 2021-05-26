package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class SelectedGroupedSpatialDistribution2DSpec extends AbstractSubSpec<InFileSelectedGroupedSpatialDistribution2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SelectedGroupedSpatialDistribution2DSpec.class);

    public static final SelectedGroupedSpatialDistribution2DSpec INSTANCE = new SelectedGroupedSpatialDistribution2DSpec();
    public static final String TYPE = "SelectedGroupedSpatialDistribution2D";

    @Override
    public boolean isType(String type) {
        return Objects.equals(type, TYPE);
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public boolean isInstance(Object input) {
        return input instanceof InFileSelectedGroupedSpatialDistribution2D;
    }

    @Override
    protected InFileSelectedGroupedSpatialDistribution2D[] newArray(int len) {
        return new InFileSelectedGroupedSpatialDistribution2D[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InFileSelectedGroupedSpatialDistribution2D[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InFileSelectedGroupedSpatialDistribution2D toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InFileSelectedGroupedSpatialDistribution2D dist = new InFileSelectedGroupedSpatialDistribution2D();
        dist.setName(name);

        dist.setXPositionKey(job.getAttributeName(TAG_x));
        dist.setYPositionKey(job.getAttributeName(TAG_y));
        dist.setFile(job.findFile(rootSpec.getText(TAG_file)));
        dist.setSelectKey(job.getAttributeName(TAG_selectBy));
        dist.setGroupKey(job.getAttributeName(TAG_groupBy));

        job.cache(name, dist);
        return dist;
    }

    @Override
    public Class<InFileSelectedGroupedSpatialDistribution2D> getParamType() {
        return InFileSelectedGroupedSpatialDistribution2D.class;
    }

    @Override
    public void toSpec(InFileSelectedGroupedSpatialDistribution2D input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getConsumerAgentGroups().get(input.getName()), job);
    }

    @Override
    protected void create(InFileSelectedGroupedSpatialDistribution2D input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_x, input.getXPositionKey().getName());
        rootSpec.set(TAG_parameters, TAG_y, input.getYPositionKey().getName());
        rootSpec.set(TAG_parameters, TAG_file, input.getFile().getName());
        rootSpec.set(TAG_parameters, TAG_selectBy, input.getSelectKey().getName());
        rootSpec.set(TAG_parameters, TAG_groupBy, input.getGroupKey().getName());

        job.callToSpec(input.getFile());
    }
}
