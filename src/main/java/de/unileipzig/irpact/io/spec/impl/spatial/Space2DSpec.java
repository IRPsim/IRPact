package de.unileipzig.irpact.io.spec.impl.spatial;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_metric;

/**
 * @author Daniel Abitz
 */
public class Space2DSpec extends SpecBase<InSpace2D, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InSpace2D> getParamType() {
        return InSpace2D.class;
    }

    @Override
    public void toSpec(InSpace2D instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(manager.getSpatial());
        spec.setName(instance.getName());
        spec.setType("Space2D");
        spec.set(TAG_metric, instance.getMetric().name());
    }
}
