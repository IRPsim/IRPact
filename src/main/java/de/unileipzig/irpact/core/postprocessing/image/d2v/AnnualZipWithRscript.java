package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.LocalizedImageData;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class AnnualZipWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualZipWithRscript.class);

    public AnnualZipWithRscript(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.ANNUAL_ZIP;
    }

    @Override
    protected RScriptBuilder getBuilder(InOutputImage image) {
        return RScriptFactory.lineChart0(
                getSettings(image)
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsZip2 data = createAnnualAdoptionZipData();
        return map(image, data);
    }

    protected ImageData map(InOutputImage image, AnnualAdoptionsZip2 input) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("zip"),
                map("adoptions")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsZip2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(result.printValue())
            ));
        }

        BuilderSettings settings = getSettings(image);
        return new CsvBasedImageData(settings.getSep(), csvData);
    }

    protected BuilderSettings currentSettings;
    protected InOutputImage currentImage;

    protected BuilderSettings getSettings(InOutputImage image) {
        if(image != currentImage || currentSettings == null) {
            LocalizedImageData localized = getLocalizedImageData();
            currentImage = image;
            currentSettings = new BuilderSettings()
                    //general
                    .setTitle(localized.getTitle(DataToVisualize.ANNUAL_ZIP))
                    .setXArg(localized.getXArg(DataToVisualize.ANNUAL_ZIP))
                    .setXLab(localized.getXLab(DataToVisualize.ANNUAL_ZIP))
                    .setYArg(localized.getYArg(DataToVisualize.ANNUAL_ZIP))
                    .setYLab(localized.getYLab(DataToVisualize.ANNUAL_ZIP))
                    .setGrpArg(localized.getGrpArg(DataToVisualize.ANNUAL_ZIP))
                    .setGrpLab(localized.getGrpLab(DataToVisualize.ANNUAL_ZIP))
                    .setSep(localized.getSep(DataToVisualize.ANNUAL_ZIP))
                    .setLineWidth(image == null ? imageProcessor.getDefaultLinewidth() : image.getLinewidth())
                    .setUseArgsFlag(true)
                    .setUsageFlag(BuilderSettings.USAGE_ARG2)
                    .setCenterTitle(true)
                    //R
                    .setEncoding(localized.getEncoding(DataToVisualize.ANNUAL_ZIP))
                    .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                    .setScaleXContinuousBreaks(imageProcessor.getYearBreaksForPrettyR())
            ;
        }
        return currentSettings;
    }
}
