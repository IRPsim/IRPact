package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process.ra.npv.PVFileLoader;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
@Definition
public class InPVFile implements InFile {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InPVFile.class,
                res.getCachedElement("Dateien"),
                res.getCachedElement("PV Daten")
        );
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InPVFile.class);

    public String _name;

    @FieldDefinition
    public double placeholderPVFile;

    public InPVFile() {
    }

    public InPVFile(String fileNameWithoutExtension) {
        _name = fileNameWithoutExtension;
    }

    @Override
    public String getFileNameWithoutExtension() {
        return _name;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public NPVXlsxData parse(InputParser parser) throws ParsingException {
        try {
            String fileName = getFileNameWithoutExtension();
            PVFileLoader pvLoader = new PVFileLoader();
            pvLoader.setLoader(parser.getResourceLoader());
            pvLoader.setInputFileName(fileName);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            pvLoader.initalize();
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "loading finished");
            return pvLoader.getData();
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
