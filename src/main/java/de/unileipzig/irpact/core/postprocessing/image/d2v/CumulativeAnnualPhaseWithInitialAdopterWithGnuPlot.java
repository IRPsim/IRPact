package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.image.CsvBasedImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class CumulativeAnnualPhaseWithInitialAdopterWithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulativeAnnualPhaseWithInitialAdopterWithGnuPlot.class);

    private static final double PRETTY_FACTOR = 1.05;

    public CumulativeAnnualPhaseWithInitialAdopterWithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.CUMULATIVE_ANNUAL_PHASE_WITH_INITIAL;
    }

    @Override
    protected void handleImage(InOutputImage image, boolean engineUsable) throws IOException {
        CustomImageData data = createData(image);
        if(data == null) {
            return;
        }

        GnuPlotBuilder builder = getBuilder(image, data);
        if(builder == null) {
            return;
        }

        execute(image, builder, data, engineUsable);
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        throw new UnsupportedOperationException("use getBuilder with " + CustomImageData.class.getSimpleName());
    }

    protected GnuPlotBuilder getBuilder(InOutputImage image, CustomImageData data) {
        return GnuPlotFactory.stackedBarChart1(
                getLocalizedFormattedString("title", data.getInitial()),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("phase0"), getLocalizedString("phase1"), getLocalizedString("phase2"),
                getLocalizedString("sep"),
                imageProcessor.getDefaultBoxWidth(),
                image.getImageWidth(), image.getImageHeight(),
                data.getMinY(), data.getMaxY()
        );
    }

    @Override
    protected CustomImageData createData(InOutputImage image) {
        AnnualAdoptionsPhase2 input = createAnnualAdoptionPhaseDataWithInitialAdopter();

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

        return new CustomImageData(
                new CsvBasedImageData(getLocalizedString("sep"), csvData),
                numberOfInitialAgents,
                numberOfInitialAgents,
                finalTotalNumberOfAdoptions
        );
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
