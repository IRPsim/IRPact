package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.Quantile;
import de.unileipzig.irpact.commons.util.data.map.Map3;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractCustomAverageQuantilRangeImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCustomAverageQuantilRangeImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InQuantileRange;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CustomAverageQuantilRangeGnuplotImageHandler
        extends AbstractCustomAverageQuantilRangeImageHandler
        implements GnuplotHelperAPI<InCustomAverageQuantilRangeImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CustomAverageQuantilRangeGnuplotImageHandler.class);

    public CustomAverageQuantilRangeGnuplotImageHandler(
            ImageProcessor2 processor,
            InCustomAverageQuantilRangeImage imageConfiguration) {
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
        return "CUSTOM_AVERAGE_QUANTILE_RANGE";
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
    public GnuPlotBuilder getBuilder(InCustomAverageQuantilRangeImage image, ImageData data) throws Throwable {
//        return GnuPlotFactory.simpleMultiLinePlot0(
//                getLocalizedString("title"),
//                getLocalizedString("xlab"), getLocalizedString("ylab"),
//                getLocalizedString("sep"),
//                image.getLinewidth(),
//                image.getImageWidth(), image.getImageHeight()
//        );
        return GnuPlotFactory2.simpleMultiLinePlot(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("sep"),
                getHexRGBPaletteOrNull(),
                image.getLinewidth(),
                image.getImageWidth(), image.getImageHeight(),
                null, null,
                null
        );
    }

    @Override
    public ImageData createData(InCustomAverageQuantilRangeImage image) throws Throwable {
        Map<Integer, Quantile<Number>> quantiles = getQuantileData(image.getLoggingModule());
        Map3<InQuantileRange, Integer, Double> avgQuantileValues = Map3.newLinkedHashMap();
        for(InQuantileRange range: image.getQuantileRanges()) {
            Map<Integer, Double> rangeValues = calculateAverage(quantiles, range.getLowerBound(), range.getUpperBound());
            avgQuantileValues.putAll(range, rangeValues);
        }
        List<Integer> years = processor.getAllSimulationYears();
        if(image.isPrintAverage()) {
            Map<Integer, Double> avgValues = calculateAverage(quantiles);
            JsonTableData3 data = mapToCsv(getDefaultLogger(), years, avgQuantileValues, avgValues);
            updateNames(
                    data,
                    getLocalizedString("years"),
                    getLocalizedString("avg")
            );
            return new CsvJsonTableImageData(data, getCsvDelimiter());
        } else {
            JsonTableData3 data = mapToCsv(getDefaultLogger(), years, avgQuantileValues);
            updateNames(
                    data,
                    getLocalizedString("years")
            );
            return new CsvJsonTableImageData(data, getCsvDelimiter());
        }
    }
}
