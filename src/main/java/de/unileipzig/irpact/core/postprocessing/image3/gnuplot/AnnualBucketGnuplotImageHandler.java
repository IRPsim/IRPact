package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.data.Bucket;
import de.unileipzig.irpact.commons.util.data.BucketMap;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageDataWithCache;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractAnnualBucketImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualBucketImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class AnnualBucketGnuplotImageHandler
        extends AbstractAnnualBucketImageHandler
        implements GnuplotHelperAPI<InAnnualBucketImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualBucketGnuplotImageHandler.class);

    private static final double PRETTY_FACTOR = 1.05;

    public AnnualBucketGnuplotImageHandler(ImageProcessor2 processor, InAnnualBucketImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected SupportedEngine getSupportedEngine() {
        return SupportedEngine.GNUPLOT;
    }

    @Override
    protected String getResourceKey() {
        return "ANNUAL_BUCKETS";
    }

    @Override
    public void init() throws Throwable {
        trace("[{}] has color palette: {}", imageConfiguration.getName(), imageConfiguration.hasColorPalette());
    }

    @Override
    public void execute() throws Throwable {
        handleImage(imageConfiguration, processor.isGnuPlotUsable());
    }

    @Override
    public String getCsvDelimiter() {
        return super.getCsvDelimiter();
    }

    @Override
    public GnuPlotEngine getEngine() {
        return processor.getGnuPlotEngine();
    }

    @Override
    public GnuPlotBuilder getBuilder(InAnnualBucketImage image, ImageData data) throws Throwable {
        CsvJsonTableImageDataWithCache imageData = (CsvJsonTableImageDataWithCache) data;
        return GnuPlotFactory2.clusteredBarChart(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("keylab"),
                getCsvDelimiter(),
                getHexRGBPaletteOrNull(),
                image.getBoxWidth(),
                getMinYOrDefault(imageData.getFromCacheAuto("min")), getMaxYOrDefault(imageData.getFromCacheAuto("max")),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    public CsvJsonTableImageData createData(InAnnualBucketImage image) throws Throwable {
        JsonTableData3 loggingData = loadLoggingData();
        List<Integer> years = processor.getAllSimulationYearsPrior();
        Map<Integer, BucketMap<Number, Integer>> annualBuckets = createAnnualBucketData(
                loggingData,
                imageConfiguration.getLoggingModule().isPrintHeader(),
                years,
                year -> newBucketMap(),
                this::getYear,
                this::getValue
        );
        Collection<? extends Bucket<Number>> buckets = getAllBuckets();
        JsonTableData3 gnuplotData = createCsvData(
                annualBuckets,
                years,
                buckets
        );
        MutableDouble min = MutableDouble.empty();
        MutableDouble max = MutableDouble.empty();
        findMinMax(gnuplotData, min, max);
        if(getLoggingModule().isPrintHeader()) {
            addHeader(gnuplotData, buckets);
        }

        min.modifiy(v -> Math.min(v, 0) * PRETTY_FACTOR);
        max.modifiy(v -> v * PRETTY_FACTOR);

        CsvJsonTableImageDataWithCache imageData = new CsvJsonTableImageDataWithCache(gnuplotData, getCsvDelimiter());
        imageData.putInCache("min", min.isEmpty() ? null : min.get());
        imageData.putInCache("max", max.isEmpty() ? null : max.get());
        return imageData;
    }
}
