package de.unileipzig.irpact.io.spec.impl.file;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.spec.*;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_pvFiles;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_spatialTable;

/**
 * @author Daniel Abitz
 */
public class PVFileSpec implements ToSpecConverter<InPVFile> {

    public static final PVFileSpec INSTANCE = new PVFileSpec();

    @Override
    public Class<InPVFile> getParamType() {
        return InPVFile.class;
    }

    @Override
    public void toSpec(InPVFile input, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        create(input, manager.getFiles().get(), manager, converter, inline);
    }

    @Override
    public void create(InPVFile input, ObjectNode root, SpecificationManager manager, SpecificationConverter converter, boolean inline) {
        SpecificationHelper rootSpec = new SpecificationHelper(root);
        SpecificationHelper spec = rootSpec.getArraySpec(TAG_pvFiles);
        spec.addIfNotExists(input.getName());
    }
}
