package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process.ra.npv.PVFileLoader;
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
public class InPVFile implements InFile {

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
        addEntry(res, thisClass(), "placeholderPVFile");
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
    public InPVFile copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVFile newCopy(CopyCache cache) {
        InPVFile copy = new InPVFile();
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
    public NPVXlsxData parse(IRPactInputParser parser) throws ParsingException {
        try {
            String fileName = getFileNameWithoutExtension();
            PVFileLoader pvLoader = new PVFileLoader();
            pvLoader.setLoader(parser.getResourceLoader());
            pvLoader.setInputFileName(fileName);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            pvLoader.initalize();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "loading finished");
            return pvLoader.getData();
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
