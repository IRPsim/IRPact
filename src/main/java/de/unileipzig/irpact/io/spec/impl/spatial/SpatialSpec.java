package de.unileipzig.irpact.io.spec.impl.spatial;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.spec.*;

/**
 * @author Daniel Abitz
 */
public class SpatialSpec implements ToSpecConverter<InSpatialModel>, ToParamConverter<InSpatialModel> {

    public static final SpatialSpec INSTANCE = new SpatialSpec();

    @Override
    public InSpatialModel[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getSpatialModel().get());
        String type = spec.getType();

        if(Space2DSpec.TYPE.equals(type)) {
            return Space2DSpec.INSTANCE.toParam(manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    @Override
    public Class<InSpatialModel> getParamType() {
        return InSpatialModel.class;
    }

    @Override
    public void toSpec(InSpatialModel input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InSpace2D) {
            Space2DSpec.INSTANCE.toSpec((InSpace2D) input, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public void create(InSpatialModel input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InSpace2D) {
            Space2DSpec.INSTANCE.create((InSpace2D) input, root, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }
}
