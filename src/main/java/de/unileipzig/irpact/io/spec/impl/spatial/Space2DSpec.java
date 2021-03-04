package de.unileipzig.irpact.io.spec.impl.spatial;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_metric;

/**
 * @author Daniel Abitz
 */
public class Space2DSpec implements ToSpecConverter<InSpace2D>, ToParamConverter<InSpace2D> {

    public static final Space2DSpec INSTANCE = new Space2DSpec();
    public static final String TYPE = "Space2D";

    @Override
    public Class<InSpace2D> getParamType() {
        return InSpace2D.class;
    }

    @Override
    public void toSpec(InSpace2D input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        create(input, manager.getSpatialModel().get(), manager, converter, inline);
    }

    @Override
    public void create(InSpace2D input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_metric, input.getMetric().name());
    }

    @Override
    public InSpace2D[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getSpatialModel().get());
        String name = spec.getName();
        String metricStr = spec.getParametersText(TAG_metric);
        Metric2D metric = Metric2D.get(metricStr);
        InSpace2D space2D = new InSpace2D(name, metric);
        return new InSpace2D[]{space2D};
    }
}
