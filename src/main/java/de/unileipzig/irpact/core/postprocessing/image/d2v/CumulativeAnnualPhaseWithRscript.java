package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.LocalizedImageData;
import de.unileipzig.irpact.core.util.AdoptionPhase;
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
public class CumulativeAnnualPhaseWithRscript extends AbstractRscriptDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulativeAnnualPhaseWithRscript.class);

    public CumulativeAnnualPhaseWithRscript(ImageProcessor imageProcessor) {
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
        AnnualAdoptionsPhase2 data = createAnnualAdoptionPhaseData();
        return map(image, data);
    }

    protected ImageData map(InOutputImage image, AnnualAdoptionsPhase2 input) {
        List<List<String>> csvData = new ArrayList<>();
        csvData.add(Arrays.asList(
                map("year"),
                map("phase"),
                map("adoptionsCumulative")
        ));
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsPhase2.getYear(entry);
            AdoptionPhase phase = AnnualAdoptionsPhase2.getPhase(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsPhase2.getData(entry);

            csvData.add(Arrays.asList(
                    map(Integer.toString(year)),
                    map(print(phase)),
                    map(result.printCumulativeValue())
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
                    .setTitle(localized.getTitle(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setXArg(localized.getXArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setXLab(localized.getXLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setYArg(localized.getYArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setYLab(localized.getYLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setFillArg(localized.getFillArg(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setFillLab(localized.getFillLab(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setSep(localized.getSep(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setBoxWidthAbsolute(0.8)
                    .setUseArgsFlag(true)
                    .setUsageFlag(BuilderSettings.USAGE_ARG2)
                    .setCenterTitle(true)
                    //R
                    .setEncoding(localized.getEncoding(DataToVisualize.CUMULATIVE_ANNUAL_PHASE))
                    .setColClasses(Element.NUMERIC, Element.CHARACTER, Element.NUMERIC)
                    .setScaleXContinuousBreaks(imageProcessor.getYearBreaksForPrettyR())
            ;
        }
        return currentSettings;
    }
}
