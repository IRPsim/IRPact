package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.data.MutableInt;
import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageDataWithCache;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractComparedAnnualZipImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedCumulatedAnnualZipImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedCumulatedAnnualZipGnuplotImageHandler
        extends AbstractComparedAnnualZipImageHandler<InComparedCumulatedAnnualZipImage>
        implements GnuplotHelperAPI<InComparedCumulatedAnnualZipImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedCumulatedAnnualZipGnuplotImageHandler.class);

    public ComparedCumulatedAnnualZipGnuplotImageHandler(
            ImageProcessor2 processor,
            InComparedCumulatedAnnualZipImage imageConfiguration) {
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
        return "COMPARED_CUMULATED_ANNUAL_ZIP";
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
    public GnuPlotBuilder getBuilder(InComparedCumulatedAnnualZipImage image, ImageData data) throws Throwable {
        CsvJsonTableImageDataWithCache dataWithCache = (CsvJsonTableImageDataWithCache) data;

//        return GnuPlotFactory.interactionLineChart0(
//                getLocalizedString("title"),
//                getLocalizedString("xlab"), getLocalizedString("ylab"),
//                getLocalizedString("keytitle0"), getLocalizedString("keytitle1"),
//                getLocalizedString("dashtype0lab"), getLocalizedString("dashtype1lab"),
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

        return GnuPlotFactory2.multiLinePlotWithTwoDashtypes(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("keytitle0"), getLocalizedString("keytitle1"),
                getLocalizedString("dashtype0lab"), getLocalizedString("dashtype1lab"),
                getHexRGBPaletteOrNull(),
                image.getLinewidth(),
                1, 2,
                getCsvDelimiter(),
                image.getImageWidth(), image.getImageHeight(),
                minY, null,
                ytics,
                dataWithCache.getFromCacheAuto("columnCount")
        );
    }

    @Override
    public ImageData createData(InComparedCumulatedAnnualZipImage image) throws Throwable {
        RealAdoptionData realData = processor.getRealAdoptionData(image.getRealData());
        JsonTableData3 data;
        boolean validZipsOnly = image.isSkipInvalidZips();
        boolean showPreYear = image.isShowPreYear();
        MutableInt individualColumnCounter = MutableInt.empty();
        if(hasValidScale()) {
            ScaledRealAdoptionData scaledData = (ScaledRealAdoptionData) processor.getScaledRealAdoptionData(image.getRealData());
//            if(image.isShowUnscaled()) {
//                data = createScaledAndUnscaledData(
//                        realData,
//                        scaledData,
//                        getLocalizedString("simuSuffix"),
//                        getLocalizedString("realScaledSuffix"), //deprecated
//                        getLocalizedString("realUnscaledSuffix"), //deprecated
//                        showPreYear,
//                        validZipsOnly,
//                        individualColumnCounter
//                );
//            } else {
//            }
            data = createScaledData(
                    scaledData,
                    getLocalizedString("simuSuffix"),
                    getLocalizedString("realSuffix"),
                    showPreYear,
                    validZipsOnly,
                    true,
                    individualColumnCounter
            );
        } else {
            data = createUnscaledData(
                    realData,
                    getLocalizedString("simuSuffix"),
                    getLocalizedString("realSuffix"),
                    showPreYear,
                    validZipsOnly,
                    true,
                    individualColumnCounter
            );
        }

        if(individualColumnCounter.isEmpty()) {
            throw new IllegalArgumentException("missing column count");
        }

        CsvJsonTableImageDataWithCache dataWithCache = new CsvJsonTableImageDataWithCache(data, getCsvDelimiter());
        dataWithCache.putInCache("columnCount", individualColumnCounter.get());
        return dataWithCache;
    }
}
