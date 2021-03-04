package de.unileipzig.irpact.io.spec.impl.process;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.process.InProcessModel;
import de.unileipzig.irpact.io.param.input.process.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.InSpatialModel;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.spatial.Space2DSpec;

/**
 * @author Daniel Abitz
 */
public class ProcessModelSpec implements ToSpecConverter<InProcessModel>, ToParamConverter<InProcessModel> {

    public static final ProcessModelSpec INSTANCE = new ProcessModelSpec();

    @Override
    public InProcessModel[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getProcessModel().get());
        String type = spec.getType();

        if(RAProcessModelSpec.TYPE.equals(type)) {
            return RAProcessModelSpec.INSTANCE.toParam(manager, converter, cache);
        }

        throw new IllegalArgumentException("unknown type: " + type);
    }

    @Override
    public Class<InProcessModel> getParamType() {
        return InProcessModel.class;
    }

    @Override
    public void toSpec(InProcessModel input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InRAProcessModel) {
            RAProcessModelSpec.INSTANCE.toSpec((InRAProcessModel) input, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }

    @Override
    public void create(InProcessModel input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(input instanceof InRAProcessModel) {
            RAProcessModelSpec.INSTANCE.create((InRAProcessModel) input, root, manager, converter, inline);
        }
        else {
            throw new ParsingException("unknown class: " + input.getClass());
        }
    }
}
