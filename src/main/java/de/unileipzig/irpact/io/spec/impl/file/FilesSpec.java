package de.unileipzig.irpact.io.spec.impl.file;

import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.spec.*;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_pvFiles;
import static de.unileipzig.irpact.io.spec.SpecificationConstants.TAG_spatialTable;

/**
 * @author Daniel Abitz
 */
public class FilesSpec implements ToParamConverter<InFile> {

    public static final FilesSpec INSTANCE = new FilesSpec();

    @Override
    public InFile[] toParam(SpecificationManager manager, SpecificationConverter converter, SpecificationCache cache) {
        SpecificationHelper spec = new SpecificationHelper(manager.getFiles().get());

        List<InFile> files = new ArrayList<>();

        SpecificationHelper spatialTableSpec = spec.getArraySpec(TAG_spatialTable);
        for(JsonNode node: spatialTableSpec.iterateElements()) {
            InSpatialTableFile file = new InSpatialTableFile(node.textValue());
            files.add(file);
        }

        SpecificationHelper pvFilesSpec = spec.getArraySpec(TAG_pvFiles);
        for(JsonNode node: pvFilesSpec.iterateElements()) {
            InPVFile file = new InPVFile(node.textValue());
            files.add(file);
        }

        return files.toArray(new InFile[0]);
    }
}
