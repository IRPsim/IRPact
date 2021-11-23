package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.Quantile;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractSpecialAverageQuantilRangeImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InQuantileRange;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InSpecialAverageQuantilRangeImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class SpecialAverageQuantilRangeGnuplotImageHandler
        extends AbstractSpecialAverageQuantilRangeImageHandler
        implements GnuplotHelperAPI<InSpecialAverageQuantilRangeImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpecialAverageQuantilRangeGnuplotImageHandler.class);

    public SpecialAverageQuantilRangeGnuplotImageHandler(
            ImageProcessor2 processor,
            InSpecialAverageQuantilRangeImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    protected SupportedEngine getSupportedEngine() {
        return SupportedEngine.GNUPLOT;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void execute() throws Throwable {
        handleImage(imageConfiguration, processor.isGnuPlotUsable());
    }

    @Override
    protected String getResourceKey() {
        return "SPECIAL_AVERAGE_QUANTILE_RANGE";
    }

    @Override
    public String getCsvDelimiter() {
        return getLocalizedString("sep");
    }

    @Override
    public GnuPlotEngine getEngine() {
        return processor.getGnuPlotEngine();
    }

    @Override
    public GnuPlotBuilder getBuilder(InSpecialAverageQuantilRangeImage image, ImageData data) throws Throwable {
        return GnuPlotFactory.simpleMultiLinePlot0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("sep"),
                image.getLinewidth(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    public ImageData createData(InSpecialAverageQuantilRangeImage image) throws Throwable {
        Map<Integer, Quantile<Number>> quantiles = getQuantileData(image.getLoggingModule());
        Map3<InQuantileRange, Integer, Double> avgQuantileValues = Map3.newLinkedHashMap();
        for(InQuantileRange range: getDefaultRanges()) {
            Map<Integer, Double> rangeValues = calculateAverage(quantiles, range.getLowerBound(), range.getUpperBound());
            avgQuantileValues.putAll(range, rangeValues);
        }
        if(image.isPrintAverage()) {
            Map<Integer, Double> avgValues = calculateAverage(quantiles);
            List<Integer> years = processor.getAllSimulationYears();
            JsonTableData3 data = mapToCsv(getDefaultLogger(), years, avgQuantileValues, avgValues);
            updateNames(
                    data,
                    getLocalizedString("years"),
                    getLocalizedString("avg"),
                    getLocalizedString("q0"),
                    getLocalizedString("q1"),
                    getLocalizedString("q2"),
                    getLocalizedString("q3"),
                    getLocalizedString("q4"),
                    getLocalizedString("q5")
            );
            return new CsvJsonTableImageData(data, getCsvDelimiter());
        } else {
            List<Integer> years = processor.getAllSimulationYears();
            JsonTableData3 data = mapToCsv(getDefaultLogger(), years, avgQuantileValues);
            updateNames(
                    data,
                    getLocalizedString("years"),
                    getLocalizedString("q0"),
                    getLocalizedString("q1"),
                    getLocalizedString("q2"),
                    getLocalizedString("q3"),
                    getLocalizedString("q4"),
                    getLocalizedString("q5")
            );
            return new CsvJsonTableImageData(data, getCsvDelimiter());
        }
    }
}
