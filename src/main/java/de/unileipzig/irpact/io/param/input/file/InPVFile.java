package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.process.ra.npv.NPVXlsxData;
import de.unileipzig.irpact.core.process.ra.npv.PVFileLoader;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.LocalizedUiResource;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.DefinitionName;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.input.TreeViewStructureEnum.FILES_PV;

/**
 * @author Daniel Abitz
 */
@Definition
@LocalizedUiResource.PutClassPath(FILES_PV)
public class InPVFile implements InFile {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InPVFile.class);

    @DefinitionName
    public String name;

    @FieldDefinition
    @LocalizedUiResource.AddEntry
    public double placeholderPVFile;

    public InPVFile() {
    }

    public InPVFile(String fileNameWithoutExtension) {
        name = fileNameWithoutExtension;
    }

    @Override
    public InPVFile copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InPVFile newCopy(CopyCache cache) {
        InPVFile copy = new InPVFile();
        copy.name = name;
        return copy;
    }

    @Override
    public String getFileNameWithoutExtension() {
        return name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public NPVXlsxData parse(IRPactInputParser parser) throws ParsingException {
        return parse(parser.getResourceLoader());
    }

    public NPVXlsxData parse(ResourceLoader loader) throws ParsingException {
        try {
            String fileName = getFileNameWithoutExtension();
            PVFileLoader pvLoader = new PVFileLoader();
            pvLoader.setLoader(loader);
            pvLoader.setInputFileName(fileName);
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", fileName);
            pvLoader.parse();
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "loading finished");
            return pvLoader.getData();
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
