package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractComparedAnnualImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedCumulatedAnnualImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedCumulatedAnnualGnuplotImageHandler
        extends AbstractComparedAnnualImageHandler<InComparedCumulatedAnnualImage>
        implements GnuplotHelperAPI<InComparedCumulatedAnnualImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedCumulatedAnnualGnuplotImageHandler.class);

    public ComparedCumulatedAnnualGnuplotImageHandler(
            ImageProcessor2 processor,
            InComparedCumulatedAnnualImage imageConfiguration) {
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
        return "COMPARED_CUMULATED_ANNUAL";
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
    public GnuPlotBuilder getBuilder(InComparedCumulatedAnnualImage image, ImageData data) throws Throwable {
//        int n;
//        if(hasValidScale()) {
//            if(image.isShowUnscaled()) {
//                n = 3;
//            } else {
//                n = 2;
//            }
//        } else {
//            n = 2;
//        }
//        return GnuPlotFactory.interactionLineChartForNValues0(
//                n,
//                getLocalizedString("title"),
//                getLocalizedString("xlab"), getLocalizedString("ylab"),
//                getLocalizedString("sep"),
//                image.getLinewidth(),
//                image.getImageWidth(), image.getImageHeight()
//        );

        Double ytics = null;
        if(image.isAutoTickY()) {
            CsvJsonTableImageData imageData = (CsvJsonTableImageData) data;
            if(imageData.getData().getNumberOfRows() < MIN_ROW_COUNT) {
                ytics = 1.0;
            }
        }

        Double minY = image.isStartAtMinValue()
                ? null
                : 0.0;

        return GnuPlotFactory2.simpleMultiLinePlot(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("sep"),
                getHexRGBPaletteOrNull(),
                image.getLinewidth(),
                image.getImageWidth(), image.getImageHeight(),
                minY, null,
                ytics
        );
    }

    @Override
    public ImageData createData(InComparedCumulatedAnnualImage image) throws Throwable {
        RealAdoptionData realData = processor.getRealAdoptionData(image.getRealData());
        JsonTableData3 data;
        boolean validZipsOnly = image.isSkipInvalidZips();
        boolean showPreYear = image.isShowPreYear();
        if(hasValidScale()) {
            ScaledRealAdoptionData scaledData = (ScaledRealAdoptionData) processor.getScaledRealAdoptionData(image.getRealData());
//            if(image.isShowUnscaled()) {
//                data = createScaledAndUnscaledData(
//                        realData,
//                        scaledData,
//                        getLocalizedString("simu"),
//                        getLocalizedString("realScaled"), //deprecated
//                        getLocalizedString("realUnscaled"), //deprecated
//                        showPreYear,
//                        validZipsOnly
//                );
//            } else {
//            }
            data = createScaledData(
                    scaledData,
                    getLocalizedString("simu"),
                    getLocalizedString("real"),
                    showPreYear,
                    validZipsOnly,
                    true
            );
        } else {
            data = createUnscaledData(
                    realData,
                    getLocalizedString("simu"),
                    getLocalizedString("real"),
                    showPreYear,
                    validZipsOnly,
                    true
            );
        }
        return new CsvJsonTableImageData(data, getCsvDelimiter());
    }
}
