package de.unileipzig.irpact.io.param.input.file;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.data.TypedMatrix;
import de.unileipzig.irpact.commons.util.fio2.Rows;
import de.unileipzig.irpact.commons.util.fio2.xlsx2.StandardCellValueConverter2;
import de.unileipzig.irpact.commons.util.fio2.xlsx2.XlsxSheetParser2;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.image.XlsxRealAdoptionData;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.file.Files;
import java.nio.file.Path;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@Definition
public class InRealAdoptionDataFile implements InFile {

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
        putClassPath(res, thisClass(), InRootUI.FILES_REALADOPTION);
        addEntry(res, thisClass(), "placeholder");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InRealAdoptionDataFile.class);

    public String _name;

    @FieldDefinition
    public double placeholder;

    public InRealAdoptionDataFile() {
    }

    public InRealAdoptionDataFile(String fileNameWithoutExtension) {
        _name = fileNameWithoutExtension;
    }

    @Override
    public InRealAdoptionDataFile copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InRealAdoptionDataFile newCopy(CopyCache cache) {
        InRealAdoptionDataFile copy = new InRealAdoptionDataFile();
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
    public XlsxRealAdoptionData parse(IRPactInputParser parser) throws ParsingException {
        return parse(parser.getResourceLoader());
    }

    public XlsxRealAdoptionData parse(ResourceLoader loader) throws ParsingException {
        String fileName = getFileNameWithoutExtension();

        Rows<Attribute> rows = parseXlsx(loader, fileName);
        TypedMatrix<String, String, Integer> matrix = toIntMatrix(rows);

        return new XlsxRealAdoptionData(matrix);
    }
}
