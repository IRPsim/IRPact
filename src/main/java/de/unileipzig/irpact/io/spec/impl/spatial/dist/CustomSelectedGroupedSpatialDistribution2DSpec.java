package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class CustomSelectedGroupedSpatialDistribution2DSpec extends SpecBase<InCustomSelectedGroupedSpatialDistribution2D, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InCustomSelectedGroupedSpatialDistribution2D> getParamType() {
        return InCustomSelectedGroupedSpatialDistribution2D.class;
    }

    @Override
    public void toSpec(InCustomSelectedGroupedSpatialDistribution2D instance, SpecificationManager manager, SpecificationConverter converter) throws ParsingException {
        if(!manager.hasSpatialDistribution(instance.getName())) {
            SpecificationHelper spec = new SpecificationHelper(manager.getSpatialDistribution(instance.getName()));
            spec.setName(instance.getName());
            spec.setType("CustomSelectedGroupedSpatialDistribution2D");
            spec.set(TAG_x, instance.getXPosSupplier().getName());
            spec.set(TAG_y, instance.getYPosSupplier().getName());
            spec.set(TAG_file, instance.getFile().getName());
            spec.set(TAG_selectBy, instance.getSelectKey().getName());
            spec.set(TAG_groupBy, instance.getGroupKey().getName());
        }
    }
}
