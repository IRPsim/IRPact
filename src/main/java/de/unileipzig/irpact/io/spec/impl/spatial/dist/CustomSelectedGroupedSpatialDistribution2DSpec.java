package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomFileSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class CustomSelectedGroupedSpatialDistribution2DSpec extends AbstractSubSpec<InCustomFileSelectedGroupedSpatialDistribution2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CustomSelectedGroupedSpatialDistribution2DSpec.class);

    public static final CustomSelectedGroupedSpatialDistribution2DSpec INSTANCE = new CustomSelectedGroupedSpatialDistribution2DSpec();
    public static final String TYPE = "CustomSelectedGroupedSpatialDistribution2D";

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
        return input instanceof InCustomFileSelectedGroupedSpatialDistribution2D;
    }

    @Override
    protected InCustomFileSelectedGroupedSpatialDistribution2D[] newArray(int len) {
        return new InCustomFileSelectedGroupedSpatialDistribution2D[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InCustomFileSelectedGroupedSpatialDistribution2D[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InCustomFileSelectedGroupedSpatialDistribution2D toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        InCustomFileSelectedGroupedSpatialDistribution2D dist = new InCustomFileSelectedGroupedSpatialDistribution2D();
        dist.setName(name);

        dist.setXPosSupplier(job.parseInlinedDistribution(rootSpec.getNode(TAG_x)));
        dist.setYPosSupplier(job.parseInlinedDistribution(rootSpec.getNode(TAG_y)));
        dist.setFile(job.findFile(rootSpec.getText(TAG_file)));
        dist.setSelectKey(job.getAttributeName(TAG_selectBy));
        dist.setGroupKey(job.getAttributeName(TAG_groupBy));

        job.cache(name, dist);
        return dist;
    }

    @Override
    public Class<InCustomFileSelectedGroupedSpatialDistribution2D> getParamType() {
        return InCustomFileSelectedGroupedSpatialDistribution2D.class;
    }

    @Override
    public void toSpec(InCustomFileSelectedGroupedSpatialDistribution2D input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getConsumerAgentGroups().get(input.getName()), job);
    }

    @Override
    protected void create(InCustomFileSelectedGroupedSpatialDistribution2D input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);

        rootSpec.set(TAG_parameters, TAG_x, job.inlineEntity(input.getXPosSupplier(), false));
        rootSpec.set(TAG_parameters, TAG_y, job.inlineEntity(input.getYPosSupplier(), false));
        rootSpec.set(TAG_parameters, TAG_file, input.getFile().getName());
        rootSpec.set(TAG_parameters, TAG_selectBy, input.getSelectKey().getName());
        rootSpec.set(TAG_parameters, TAG_groupBy, input.getGroupKey().getName());

        job.callToSpec(input.getFile());
    }
}
