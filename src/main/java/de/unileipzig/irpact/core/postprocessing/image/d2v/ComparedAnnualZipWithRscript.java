package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.*;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.R.builder.Element;
import de.unileipzig.irpact.util.R.builder.RScriptBuilder;
import de.unileipzig.irpact.util.R.builder.RScriptFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZipWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZipWithRscript.class);

    public ComparedAnnualZipWithRscript(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.COMPARED_ANNUAL_ZIP;
    }

    @Override
    protected RScriptBuilder getBuilder(InOutputImage image) {
        return RScriptFactory.interactionLineChart0(
                getSettings(image)
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsZip2 data = createAnnualAdoptionZipData();
        return map(image, data);
    }

    protected ImageData map(InOutputImage image, AnnualAdoptionsZip2 input) {
        RealAdoptionData realData = imageProcessor.getRealAdoptionData();
        BuilderSettings settings = getSettings(image);

        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("zip"),
                map("adoptions"),
                map("real")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsZip2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(result.printValue()),
                    map(settings.getDistinct0Label())
            ));

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(zip),
                    map(Integer.toString(realData.get(year, zip))),
                    map(settings.getDistinct1Label())
            ));
        }

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
                    .setTitle(localized.getTitle(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setXArg(localized.getXArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setXLab(localized.getXLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setYArg(localized.getYArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setYLab(localized.getYLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setGrpArg(localized.getGrpArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setGrpLab(localized.getGrpLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setDistinctArg(localized.getDistinctArg(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setDistinctLab(localized.getDistinctLab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setDistinct0Label(localized.getDistinct0Lab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setDistinct1Label(localized.getDistinct1Lab(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setSep(localized.getSep(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setLineWidth(image == null ? imageProcessor.getDefaultLinewidth() : image.getLinewidth())
                    .setUseArgsFlag(true)
                    .setUsageFlag(BuilderSettings.USAGE_ARG2)
                    .setCenterTitle(true)
                    //R
                    .setEncoding(localized.getEncoding(DataToVisualize.COMPARED_ANNUAL_ZIP))
                    .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC, Element.CHARACTER)
                    .setScaleXContinuousBreaks(imageProcessor.getYearBreaksForPrettyR())
            ;
        }
        return currentSettings;
    }
}
