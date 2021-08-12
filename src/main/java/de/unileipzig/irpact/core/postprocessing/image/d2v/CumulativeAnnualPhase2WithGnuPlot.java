package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.LocalizedImageData;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irpact.util.script.BuilderSettings;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class CumulativeAnnualPhase2WithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulativeAnnualPhase2WithGnuPlot.class);

    private static final double PRETTY_FACTOR = 1.05;

    public CumulativeAnnualPhase2WithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.CUMULATIVE_ANNUAL_PHASE2;
    }

    @Override
    protected void handleImageWithWorkingEngine(InOutputImage image) throws IOException {
        CustomImageData data = createData(image);
        if(data == null) {
            return;
        }

        GnuPlotBuilder builder = getBuilder(image, data);
        if(builder == null) {
            return;
        }

        execute(image, builder, data);
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        throw new UnsupportedOperationException("use getBuilder with " + CustomImageData.class.getSimpleName());
    }

    protected GnuPlotBuilder getBuilder(InOutputImage image, CustomImageData data) {
        return GnuPlotFactory.stackedBarChart1(
                getSettings(image, data.getInitial(), data.getMinY(), data.getMaxY())
        );
    }

    @Override
    protected CustomImageData createData(InOutputImage image) {
        AnnualAdoptionsPhase2 data = createAnnualAdoptionPhaseDataWithInitialAdopter();
        return map(image, data);
    }

    protected CustomImageData map(InOutputImage image, AnnualAdoptionsPhase2 input) {
        Set<Integer> years = new TreeSet<>();
        Map<AdoptionPhase, Map<Integer, Integer>> phaseData = new LinkedHashMap<>();

        int numberOfInitialAgents = 0;
        Integer lastYear = null;

        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsPhase2.getYear(entry);
            AdoptionPhase phase = AnnualAdoptionsPhase2.getPhase(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsPhase2.getData(entry);

            if(phase.isInitial() && result.getCumulativeValue() > numberOfInitialAgents) {
                numberOfInitialAgents = result.getCumulativeValue();
            }
            if(lastYear == null || year > lastYear) {
                lastYear = year;
            }

            years.add(year);
            phaseData.computeIfAbsent(phase, _phase -> new TreeMap<>()).put(year, result.getCumulativeValue());
        }
        LOGGER.trace(IRPSection.RESULT, "number of initial agents: {}", numberOfInitialAgents);

        int totalNumberOfAdoptions = 0;
        if(lastYear != null) {
            for(Map<Integer, Integer> annualData: phaseData.values()) {
                Integer cumAdopts = annualData.get(lastYear);
                totalNumberOfAdoptions += cumAdopts == null ? 0 : cumAdopts;
            }
        }
        int finalTotalNumberOfAdoptions = (int) Math.ceil(totalNumberOfAdoptions * PRETTY_FACTOR);
        LOGGER.trace(IRPSection.RESULT, "total number of adoptions (last year): {} (*{} = {} (ceil))", totalNumberOfAdoptions, PRETTY_FACTOR, finalTotalNumberOfAdoptions);

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(map("years"));
        for(AdoptionPhase phase: phaseData.keySet()) {
            header.add(map(print(phase)));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(map(Integer.toString(year)));
            for(Map<Integer, Integer> zipEntry: phaseData.values()) {
                row.add(map(Integer.toString(zipEntry.get(year))));
            }
            csvData.add(row);
        }

        BuilderSettings settings = getSettings(image, numberOfInitialAgents, numberOfInitialAgents, finalTotalNumberOfAdoptions);
        return new CustomImageData(
                new CsvBasedImageData(settings.getSep(), csvData),
                numberOfInitialAgents,
                numberOfInitialAgents,
                finalTotalNumberOfAdoptions
        );
    }

    protected BuilderSettings currentSettings;
    protected InOutputImage currentImage;

    protected BuilderSettings getSettings(InOutputImage image, int inital, int minY, int maxY) {
        if(image != currentImage || currentSettings == null) {
            LocalizedImageData localized = getLocalizedImageData();
            currentImage = image;
            String titlePattern = localized.getTitle(getMode());
            String formattedTitle = MessageFormat.format(titlePattern, inital);
            currentSettings = new BuilderSettings()
                    //general
                    .setTitle(formattedTitle)
                    .setXArg(localized.getXArg(getMode()))
                    .setXLab(localized.getXLab(getMode()))
                    .setYArg(localized.getYArg(getMode()))
                    .setYLab(localized.getYLab(getMode()))
                    .setYMin(minY)
                    .setYMax(maxY)
                    .setFillArg(localized.getFillArg(getMode()))
                    .setFillLab(localized.getFillLab(getMode()))
                    .setSep(localized.getSep(getMode()))
                    .setBoxWidthAbsolute(0.8)
                    .setUseArgsFlag(true)
                    .setUsageFlag(BuilderSettings.USAGE_ARG2)
                    .setCenterTitle(true)
                    .setPhase0(localized.getPhase0(getMode()))
                    .setPhase1(localized.getPhase1(getMode()))
                    .setPhase2(localized.getPhase2(getMode()))
                    //gnuplot
                    ;
        }
        return currentSettings;
    }

    /**
     * @author Daniel Abitz
     */
    private static final class CustomImageData implements ImageData {

        private final CsvBasedImageData DATA;
        private final int INITIAL;
        private final int MIN_Y;
        private final int MAX_Y;

        public CustomImageData(CsvBasedImageData data, int inital, int minY, int maxY) {
            DATA = data;
            INITIAL = inital;
            MIN_Y = minY;
            MAX_Y = maxY;
        }

        public int getInitial() {
            return INITIAL;
        }

        public int getMinY() {
            return MIN_Y;
        }

        public int getMaxY() {
            return MAX_Y;
        }

        @Override
        public void writeTo(Path target, Charset charset) throws IOException {
            DATA.writeTo(target, charset);
        }
    }
}
