package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
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

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class CumulativeAnnualPhaseWithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulativeAnnualPhaseWithGnuPlot.class);

    public CumulativeAnnualPhaseWithGnuPlot(ImageProcessor imageProcessor) {
        super(imageProcessor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected DataToVisualize getMode() {
        return DataToVisualize.CUMULATIVE_ANNUAL_PHASE;
    }

    @Override
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        return GnuPlotFactory.stackedBarChart0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("sep"),
                imageProcessor.getDefaultBoxWidth(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsPhase2 input = createAnnualAdoptionPhaseData();

        Set<Integer> years = new TreeSet<>();
        Map<AdoptionPhase, Map<Integer, Integer>> phaseData = new LinkedHashMap<>();
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsPhase2.getYear(entry);
            AdoptionPhase phase = AnnualAdoptionsPhase2.getPhase(entry);
            AdoptionResultInfo2 result = AnnualAdoptionsPhase2.getData(entry);

            years.add(year);
            phaseData.computeIfAbsent(phase, _phase -> new TreeMap<>()).put(year, result.getCumulativeValue());
        }

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

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
