package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageDataWithCache;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractAdoptionPhaseOverviewImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAdoptionPhaseOverviewImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AdoptionPhaseOverviewGnuplotImageHandler
        extends AbstractAdoptionPhaseOverviewImageHandler
        implements GnuplotHelperAPI<InAdoptionPhaseOverviewImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionPhaseOverviewGnuplotImageHandler.class);

    private static final double PRETTY_FACTOR = 1.05;

    public AdoptionPhaseOverviewGnuplotImageHandler(
            ImageProcessor2 processor,
            InAdoptionPhaseOverviewImage imageConfiguration) {
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
        return "ADOPTION_PHASE_OVERVIEW";
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
    public GnuPlotBuilder getBuilder(InAdoptionPhaseOverviewImage image, ImageData data) {
        CsvJsonTableImageDataWithCache cData = (CsvJsonTableImageDataWithCache) data;
        return GnuPlotFactory2.stackedBarChartForThreeValuesAndAHiddenValue(
                getLocalizedFormattedString("title", cData.getFromCache("initial")),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("keylab"),
                getLocalizedString("sep"),
                getHexRGBPaletteOrNull(),
                image.getBoxWidth(),
                cData.getFromCacheAuto("initial"), cData.getFromCacheAuto("total"),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    public ImageData createData(InAdoptionPhaseOverviewImage image) throws Throwable {
        int initialAdopter = getInitialAdopterCount();
        trace("number of initial agents: {}", initialAdopter);

        int totalAdopter = getTotalAdopterCount();
        int finalTotalNumberOfAdoptions = (int) Math.ceil(totalAdopter * PRETTY_FACTOR);
        trace("total number of adoptions (last year): {} (*{} = {} (ceil))", totalAdopter, PRETTY_FACTOR, finalTotalNumberOfAdoptions);

        JsonTableData3 data = getTableData();

        CsvJsonTableImageDataWithCache imageData = new CsvJsonTableImageDataWithCache(data, getCsvDelimiter());
        imageData.putInCache("initial", initialAdopter);
        imageData.putInCache("total", finalTotalNumberOfAdoptions);
        return imageData;
    }
}
