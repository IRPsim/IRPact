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

import static de.unileipzig.irpact.io.param.ParamUtil.addEntry;
import static de.unileipzig.irpact.io.param.ParamUtil.putClassPath;

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
        try {
            if(loader == null) {
                throw new NullPointerException("loader");
            }

            String fileName = getFileNameWithoutExtension();

            XlsxSheetParser2<Attribute> xlsxParser = StandardCellValueConverter2.newParser();

            Rows<Attribute> rows;
            String xlsxFile = fileName + ".xlsx";
            LOGGER.trace(IRPSection.INITIALIZATION_PARAMETER, "try load '{}'", xlsxFile);
            if(loader.hasExternal(xlsxFile)) {
                Path xlsxPath = loader.getExternal(xlsxFile);
                LOGGER.trace("load xlsx file '{}'", xlsxPath);
                try(InputStream in = Files.newInputStream(xlsxPath)) {
                    rows = xlsxParser.parse(in, RAConstants.REAL_ADOPTION_DATA_SHEET);
                }
            }
            else if(loader.hasInternal(xlsxFile)) {
                LOGGER.trace("load xlsx resource '{}'", xlsxFile);
                try(InputStream in = loader.getInternalAsStream(xlsxFile)) {
                    rows = xlsxParser.parse(in, RAConstants.REAL_ADOPTION_DATA_SHEET);
                }
            }
            else {
                throw new ParsingException("missing data: " + xlsxFile);
            }

            TypedMatrix<String, String, Integer> matrix = rows.toMatrix(
                    m -> m.asValueAttribute().getStringValue(),
                    n -> n.asValueAttribute().getStringValue(),
                    v -> v.asValueAttribute().getIntValue()
            );

            return new XlsxRealAdoptionData(matrix);
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}
