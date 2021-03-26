package de.unileipzig.irpact.io.spec.impl.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationJob;
import de.unileipzig.irpact.io.spec.impl.AbstractSubSpec;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class Space2DSpec extends AbstractSubSpec<InSpace2D> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(Space2DSpec.class);

    public static final Space2DSpec INSTANCE = new Space2DSpec();
    public static final String TYPE = "Space2D";

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
        return input instanceof InSpace2D;
    }

    @Override
    protected InSpace2D[] newArray(int len) {
        return new InSpace2D[len];
    }

    @Override
    protected IRPLogger logger() {
        return LOGGER;
    }

    @Override
    public InSpace2D[] toParamArray(SpecificationJob job) throws ParsingException {
        return toParamArray(job.getData().getSpatialModel(), job);
    }

    @Override
    public InSpace2D toParam(SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        String name = rootSpec.getText(TAG_name);
        if(job.isCached(name)) {
            return job.getCached(name);
        }

        Metric2D metric = Metric2D.get(rootSpec.getText(TAG_parameters, TAG_metric));
        InSpace2D space2D = new InSpace2D(name, metric);
        job.cache(name, space2D);
        return space2D;
    }

    @Override
    public Class<InSpace2D> getParamType() {
        return InSpace2D.class;
    }

    @Override
    public void toSpec(InSpace2D input, SpecificationJob job) throws ParsingException {
        create(input, job.getData().getSpatialModel(), job);
    }

    @Override
    protected void create(InSpace2D input, SpecificationHelper rootSpec, SpecificationJob job) throws ParsingException {
        rootSpec.set(TAG_name, input.getName());
        rootSpec.set(TAG_type, TYPE);
        rootSpec.set(TAG_parameters, TAG_metric, input.getMetric().name());
    }
}
