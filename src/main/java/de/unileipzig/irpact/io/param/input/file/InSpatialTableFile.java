package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.spatial.attribute.SpatialAttribute;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpatialTableFile implements InFile {

    //damit ich bei copy&paste nie mehr vergesse die Klasse anzupassen :)
    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InSpatialTableFile.class,
                res.getCachedElement("Dateien"),
                res.getCachedElement("Tabellen")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("-")
                .setGamsDescription("Platzhalter")
                .store(InSpatialTableFile.class, "placeholderInSpatialFile");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InSpatialTableFile.class);

    public String _name;

    @FieldDefinition
    public double placeholderInSpatialFile;

    public InSpatialTableFile() {
    }

    public InSpatialTableFile(String fileNameWithoutExtension) {
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
    public  List<List<SpatialAttribute<?>>> parse(InputParser parser) throws ParsingException {
        try {
            String fileName = getFileNameWithoutExtension();
            SpatialTableFileLoader gisLoader = new SpatialTableFileLoader();
            gisLoader.setLoader(parser.getResourceLoader());
            gisLoader.setInputFileName(fileName);
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            gisLoader.initalize();
            List<List<SpatialAttribute<?>>> spatialData = gisLoader.getAllAttributes();
            LOGGER.debug(IRPSection.INITIALIZATION_PARAMETER, "loaded '{}' entries", spatialData.size());
            return spatialData;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}