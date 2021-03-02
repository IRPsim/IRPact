package de.unileipzig.irpact.io.spec.impl.file;

import de.unileipzig.irpact.io.param.input.distribution.InBooleanDistribution;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.spec.SpecificationConverter;
import de.unileipzig.irpact.io.spec.SpecificationHelper;
import de.unileipzig.irpact.io.spec.SpecificationManager;
import de.unileipzig.irpact.io.spec.impl.SpecBase;

import java.util.Map;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_pvFiles;

/**
 * @author Daniel Abitz
 */
public class PVFileSpec extends SpecBase<InPVFile, Void> {

    @Override
    public Void toParam(SpecificationManager manager, Map<String, Object> cache) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Class<InPVFile> getParamType() {
        return InPVFile.class;
    }

    @Override
    public void toSpec(InPVFile instance, SpecificationManager manager, SpecificationConverter converter) {
        SpecificationHelper spec = new SpecificationHelper(manager.getBinaryDataRoot());
        SpecificationHelper pvSpec = spec.getArraySpec(TAG_pvFiles);
        pvSpec.addIfNotExists(instance.getName());
    }
}
