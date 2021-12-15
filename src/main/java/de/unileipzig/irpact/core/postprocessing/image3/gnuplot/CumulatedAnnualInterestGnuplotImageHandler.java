package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractCumulatedAnnualInterestImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCumulatedAnnualInterestImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class CumulatedAnnualInterestGnuplotImageHandler
        extends AbstractCumulatedAnnualInterestImageHandler
        implements GnuplotHelperAPI<InCumulatedAnnualInterestImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulatedAnnualInterestGnuplotImageHandler.class);

    public CumulatedAnnualInterestGnuplotImageHandler(
            ImageProcessor2 processor,
            InCumulatedAnnualInterestImage imageConfiguration) {
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
        return "CUMULATED_ANNUAL_INTEREST";
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
    public GnuPlotBuilder getBuilder(InCumulatedAnnualInterestImage image, ImageData data) throws Throwable {
        return GnuPlotFactory2.simpleMultiLinePlot(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("sep"),
                getHexRGBPaletteOrNull(),
                image.getLinewidth(),
                image.getImageWidth(), image.getImageHeight(),
                getMinYOrNull(), getMaxYOrNull(),
                false
        );
    }

    protected Double getMinYOrNull() {
        return imageConfiguration.isUseCustomYRangeMin()
                ? imageConfiguration.getMinY()
                : null;
    }

    protected Double getMaxYOrNull() {
        return imageConfiguration.isUseCustomYRangeMax()
                ? imageConfiguration.getMaxY()
                : null;
    }

    @Override
    public ImageData createData(InCumulatedAnnualInterestImage image) throws Throwable {
        JsonTableData3 data = createTableData(
                getLocalizedString("yearLabel"),
                getLocalizedString("dataLabel")
        );
        return new CsvJsonTableImageData(data, getCsvDelimiter());
    }
}
