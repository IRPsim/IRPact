package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.io.param.input.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.FILES;
import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpatialTableFile implements InFile {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), FILES, thisName());
        addEntry(res, thisClass(), "placeholderInSpatialFile");
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
    public InSpatialTableFile copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InSpatialTableFile newCopy(CopyCache cache) {
        InSpatialTableFile copy = new InSpatialTableFile();
        copy._name = _name;
        return copy;
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
    public SpatialTableFileContent parse(IRPactInputParser parser) throws ParsingException {
        try {
            String fileName = getFileNameWithoutExtension();
            SpatialTableFileLoader gisLoader = new SpatialTableFileLoader();
            gisLoader.setLoader(parser.getResourceLoader());
            gisLoader.setInputFileName(fileName);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            gisLoader.initalize();
            SpatialTableFileContent spatialData = gisLoader.getAllAttributes();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "loaded '{}' entries", spatialData.size());
            return spatialData;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
