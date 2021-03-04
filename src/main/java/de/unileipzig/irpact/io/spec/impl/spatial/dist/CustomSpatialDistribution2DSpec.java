package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSpatialDistribution2D;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class CustomSpatialDistribution2DSpec
        implements ToSpecConverter<InCustomSpatialDistribution2D>, ToParamConverter<InCustomSpatialDistribution2D> {

    public static final CustomSpatialDistribution2DSpec INSTANCE = new CustomSpatialDistribution2DSpec();
    public static final String TYPE = "CustomSpatialDistribution2D";

    @Override
    public Class<InCustomSpatialDistribution2D> getParamType() {
        return InCustomSpatialDistribution2D.class;
    }

    @Override
    public void toSpec(InCustomSpatialDistribution2D input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(manager.getSpatialDistributions().hasNot(input.getName())) {
            create(input, manager.getSpatialDistributions().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InCustomSpatialDistribution2D input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_x, input.getXPosSupplier().getName());
        spec.setParameters(TAG_y, input.getYPosSupplier().getName());
        spec.setParameters(TAG_file, input.getAttributeFile().getName());

        converter.callToSpec(input.getAttributeFile(), manager, inline);
    }

    @Override
    public InCustomSpatialDistribution2D toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        try {
            SpecificationHelper spec = new SpecificationHelper(root);
            String name = spec.getName();

            if(cache.has(name)) {
                return cache.getAs(name);
            }

            InCustomSpatialDistribution2D dist = new InCustomSpatialDistribution2D();
            dist.setName(name);
            dist.setXPosSupplier(converter.getDistribution(spec.getParametersText(TAG_x), manager, cache));
            dist.setYPosSupplier(converter.getDistribution(spec.getParametersText(TAG_y), manager, cache));
            dist.setAttributeFile((InSpatialTableFile) converter.getFile(spec.getParametersText(TAG_file), manager, cache));

            cache.securePut(name, dist);
            return dist;
        } catch (ParsingException e) {
            throw e.unchecked();
        }
    }
}
