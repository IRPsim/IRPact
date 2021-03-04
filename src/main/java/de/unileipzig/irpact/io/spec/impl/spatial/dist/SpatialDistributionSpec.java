package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.spec.*;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class SpatialDistributionSpec
        implements ToParamConverter<InSpatialDistribution> {

    public static final SpatialDistributionSpec INSTANCE = new SpatialDistributionSpec();

    @Override
    public InSpatialDistribution[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        throw new UnsupportedOperationException("currently inline only");
    }

    @Override
    public InSpatialDistribution toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(root);
        String type = spec.getType();

        if(CustomSelectedGroupedSpatialDistribution2DSpec.TYPE.equals(type)) {
            return CustomSelectedGroupedSpatialDistribution2DSpec.INSTANCE.toParam(root, manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    public InSpatialDistribution toParamByName(String name, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        for(InSpatialDistribution scheme: toParam(manager, converter, cache)) {
            if(Objects.equals(scheme.getName(), name)) {
                return scheme;
            }
        }
        throw new ParsingException("not found: " + name);
    }
}
