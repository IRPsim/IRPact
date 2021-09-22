package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.AdoptionResultInfo2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.*;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZipWithGnuPlot extends AbstractGnuPlotDataVisualizer {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZipWithGnuPlot.class);

    public ComparedAnnualZipWithGnuPlot(ImageProcessor imageProcessor) {
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
    protected GnuPlotBuilder getBuilder(InOutputImage image) {
        return GnuPlotFactory.interactionLineChart0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("keytitle0"), getLocalizedString("keytitle1"),
                getLocalizedString("dashtype0lab"), getLocalizedString("dashtype1lab"),
                getLocalizedString("sep"),
                image.getLinewidthInt(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    protected ImageData createData(InOutputImage image) {
        AnnualAdoptionsZip2 input = createAnnualAdoptionZipData();
        RealAdoptionData realData = imageProcessor.getRealAdoptionData(image);

        Set<Integer> years = new TreeSet<>();
        Map<String, Map<Integer, Integer>> zipData = new LinkedHashMap<>();
        for(Object[] entry: input.getData().iterable()) {
            int year = AnnualAdoptionsZip2.getYear(entry);
            String zip = AnnualAdoptionsZip2.getZip(entry);
            AdoptionResultInfo2 data = AnnualAdoptionsZip2.getData(entry);

            years.add(year);
            zipData.computeIfAbsent(zip, _zip -> new TreeMap<>()).put(year, data.getValue());
        }

        List<List<String>> csvData = new ArrayList<>();
        List<String> header = new ArrayList<>();
        header.add(map("years"));
        for(String zip: zipData.keySet()) {
            header.add(map(zip));
            header.add(map(zip + "-R"));
        }
        csvData.add(header);
        for(Integer year: years) {
            List<String> row = new ArrayList<>();
            row.add(map(Integer.toString(year)));
            for(Map.Entry<String, Map<Integer, Integer>> zipEntry: zipData.entrySet()) {
                row.add(map(Integer.toString(zipEntry.getValue().get(year))));
                row.add(map(Integer.toString(realData.getUncumulated(year, zipEntry.getKey()))));
            }
            csvData.add(row);
        }

        return new CsvBasedImageData(getLocalizedString("sep"), csvData);
    }
}
