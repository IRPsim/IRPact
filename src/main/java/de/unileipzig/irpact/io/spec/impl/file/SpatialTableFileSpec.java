package de.unileipzig.irpact.io.spec.impl.file;

import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_spatialTableFiles;

/**
 * @author Daniel Abitz
 */
public class SpatialTableFileSpec extends SpecBase<InSpatialTableFile, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InSpatialTableFile> getParamType() {
        return InSpatialTableFile.class;
    }

    @Override
    public void toSpec(InSpatialTableFile instance, SpecificationManager manager, SpecificationConverter converter) {
        SpecificationHelper spec = new SpecificationHelper(manager.getBinaryDataRoot());
        SpecificationHelper pvSpec = spec.getArraySpec(TAG_spatialTableFiles);
        pvSpec.addIfNotExists(instance.getName());
    }
}
