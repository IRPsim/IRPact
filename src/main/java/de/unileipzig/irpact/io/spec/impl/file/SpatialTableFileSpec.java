package de.unileipzig.irpact.io.spec.impl.file;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.ToSpecConverter;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileSpec implements ToSpecConverter<InSpatialTableFile> {

    public static final SpatialTableFileSpec INSTANCE = new SpatialTableFileSpec();

    @Override
    public Class<InSpatialTableFile> getParamType() {
        return InSpatialTableFile.class;
    }

    @Override
    public void toSpec(InSpatialTableFile input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        create(input, manager.getFiles().get(), manager, converter, inline);
    }

    @Override
    public void create(InSpatialTableFile input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper rootSpec = new SpecificationHelper(root);
        SpecificationHelper spec = rootSpec.getArraySpec(TAG_spatialTable);
        spec.addIfNotExists(input.getName());
    }
}
