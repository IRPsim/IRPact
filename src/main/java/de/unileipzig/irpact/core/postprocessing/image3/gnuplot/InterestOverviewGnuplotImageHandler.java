package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractInterestOverviewImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InInterestOverviewImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class InterestOverviewGnuplotImageHandler
        extends AbstractInterestOverviewImageHandler
        implements GnuplotHelperAPI<InInterestOverviewImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InterestOverviewGnuplotImageHandler.class);

    public InterestOverviewGnuplotImageHandler(
            ImageProcessor2 processor,
            InInterestOverviewImage imageConfiguration) {
        super(processor, imageConfiguration);
    }

    @Override
    protected void validate() throws Throwable {
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
        return "INTEREST_OVERVIEW";
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
    public GnuPlotBuilder getBuilder(InInterestOverviewImage image) throws Throwable {
        return GnuPlotFactory.stackedBarChart0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("sep"),
                image.getBoxWidth(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    public ImageData createData(InInterestOverviewImage image) throws Throwable {
        JsonTableData3 data = createTableData();
        return new CsvJsonTableImageData(data, getCsvDelimiter());
    }
}
