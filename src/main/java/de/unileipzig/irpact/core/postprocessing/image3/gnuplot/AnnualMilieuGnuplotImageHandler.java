package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageDataWithCache;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractAnnualMilieuImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualMilieuImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AnnualMilieuGnuplotImageHandler
        extends AbstractAnnualMilieuImageHandler
        implements GnuplotHelperAPI<InAnnualMilieuImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualMilieuGnuplotImageHandler.class);

    private static final double PRETTY_FACTOR = 1.05;

    public AnnualMilieuGnuplotImageHandler(ImageProcessor2 processor, InAnnualMilieuImage imageConfiguration) {
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
        return "ANNUAL_MILIEUS";
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
    public GnuPlotBuilder getBuilder(InAnnualMilieuImage image, ImageData data) throws Throwable {
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
    public CsvJsonTableImageData createData(InAnnualMilieuImage image) throws Throwable {
        JsonTableData3 adoptionData = createData(
                getLocalizedString("headerYear"),
                image.isShowInitial()
        );

        MutableDouble min = MutableDouble.empty();
        MutableDouble max = MutableDouble.empty();
        findMinMax(adoptionData, min, max);
        min.modifiy(v -> Math.min(v, 0) * PRETTY_FACTOR);
        max.modifiy(v -> v * PRETTY_FACTOR);

        CsvJsonTableImageDataWithCache imageData = new CsvJsonTableImageDataWithCache(adoptionData, getCsvDelimiter());
        imageData.putInCache("min", min.getOrBoxed(null));
        imageData.putInCache("max", max.getOrBoxed(null));
        return imageData;
    }
}
