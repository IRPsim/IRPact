package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.spatial.SpatialTableFileContent;
import de.unileipzig.irpact.core.spatial.SpatialTableFileLoader;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.IOConstants.FILES;
import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InSpatialTableFile implements InFile {

    private static final String DEFAULT_SHEET_NAME = "Datensatz";

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
        addEntryWithDefaultAndDomain(res, thisClass(), "coverage", VALUE_1, DOMAIN_GEQ0);
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InSpatialTableFile.class);

    public String _name;

    @FieldDefinition
    public double coverage = 1.0;
    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }
    public double getCoverage() {
        return coverage;
    }

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
            SpatialTableFileLoader loader = new SpatialTableFileLoader();
            loader.setLoader(parser.getResourceLoader());
            loader.setInputFileName(fileName);
            loader.setCoverage(getCoverage());
            loader.setSheetName(DEFAULT_SHEET_NAME);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            loader.parse();
            SpatialTableFileContent spatialData = loader.getAllAttributes();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "loaded '{}' entries", spatialData.size());
            return spatialData;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
