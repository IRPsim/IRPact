package de.unileipzig.irpact.io.spec.impl.spatial.dist;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.spatial.dist.InCustomSelectedGroupedSpatialDistribution2D;
import de.unileipzig.irpact.io.spec.*;
import de.unileipzig.irpact.io.spec.impl.file.FilesSpec;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_groupBy;

/**
 * @author Daniel Abitz
 */
public class CustomSelectedGroupedSpatialDistribution2DSpec
        implements ToSpecConverter<InCustomSelectedGroupedSpatialDistribution2D>, ToParamConverter<InCustomSelectedGroupedSpatialDistribution2D> {

    public static final CustomSelectedGroupedSpatialDistribution2DSpec INSTANCE = new CustomSelectedGroupedSpatialDistribution2DSpec();
    public static final String TYPE = "CustomSelectedGroupedSpatialDistribution2D";

    @Override
    public Class<InCustomSelectedGroupedSpatialDistribution2D> getParamType() {
        return InCustomSelectedGroupedSpatialDistribution2D.class;
    }

    @Override
    public void toSpec(InCustomSelectedGroupedSpatialDistribution2D input, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        if(manager.getSpatialDistributions().hasNot(input.getName())) {
            create(input, manager.getSpatialDistributions().get(input.getName()), manager, converter, inline);
        }
    }

    @Override
    public void create(InCustomSelectedGroupedSpatialDistribution2D input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) throws ParsingException {
        SpecificationHelper spec = new SpecificationHelper(root);
        spec.setName(input.getName());
        spec.setType(TYPE);
        spec.setParameters(TAG_x, input.getXPosSupplier().getName());
        spec.setParameters(TAG_y, input.getYPosSupplier().getName());
        spec.setParameters(TAG_file, input.getFile().getName());
        spec.setParameters(TAG_selectBy, input.getSelectKey().getName());
        spec.setParameters(TAG_groupBy, input.getGroupKey().getName());

        converter.callToSpec(input.getFile(), manager, inline);
    }

    @Override
    public InCustomSelectedGroupedSpatialDistribution2D toParam(ObjectNode root, SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        try {
            SpecificationHelper spec = new SpecificationHelper(root);
            String name = spec.getName();

            if(cache.has(name)) {
                return cache.getAs(name);
            }

            InCustomSelectedGroupedSpatialDistribution2D dist = new InCustomSelectedGroupedSpatialDistribution2D();
            dist.setName(name);
            dist.setXPosSupplier(converter.getDistribution(spec.getParametersText(TAG_x), manager, cache));
            dist.setYPosSupplier(converter.getDistribution(spec.getParametersText(TAG_y), manager, cache));
            dist.setFile((InSpatialTableFile) converter.getFile(spec.getParametersText(TAG_file), manager, cache));
            dist.setSelectKey(converter.getName(spec.getParametersText(TAG_selectBy), cache));
            dist.setGroupKey(converter.getName(spec.getParametersText(TAG_groupBy), cache));

            cache.securePut(name, dist);
            return dist;
        } catch (ParsingException e) {
            throw e.unchecked();
        }
    }
}
