package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.data3.ScaledRealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractComparedAnnualZipImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedAnnualZipImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZipGnuplotImageHandler
        extends AbstractComparedAnnualZipImageHandler
        implements GnuplotHelperAPI<InComparedAnnualZipImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZipGnuplotImageHandler.class);

    public ComparedAnnualZipGnuplotImageHandler(
            ImageProcessor2 processor,
            InComparedAnnualZipImage imageConfiguration) {
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
        return "COMPARED_ANNUAL_ZIP";
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
    public GnuPlotBuilder getBuilder(InComparedAnnualZipImage image) throws Throwable {
        return GnuPlotFactory.interactionLineChart0(
                getLocalizedString("title"),
                getLocalizedString("xlab"), getLocalizedString("ylab"),
                getLocalizedString("keytitle0"), getLocalizedString("keytitle1"),
                getLocalizedString("dashtype0lab"), getLocalizedString("dashtype1lab"),
                getLocalizedString("sep"),
                image.getLinewidth(),
                image.getImageWidth(), image.getImageHeight()
        );
    }

    @Override
    public ImageData createData(InComparedAnnualZipImage image) throws Throwable {
        RealAdoptionData realData = processor.getRealAdoptionData(image.getRealData());
        JsonTableData3 data;
        boolean validZipsOnly = image.isSkipInvalidZips();
        boolean showPreYear = image.isShowPreYear();
        if(hasValidScale()) {
            ScaledRealAdoptionData scaledData = (ScaledRealAdoptionData) processor.getScaledRealAdoptionData(image.getRealData());
            if(image.isShowUnscaled()) {
                data = createScaledAndUnscaledData(
                        realData,
                        scaledData,
                        getLocalizedString("simuSuffix"),
                        getLocalizedString("realScaledSuffix"),
                        getLocalizedString("realUnscaledSuffix"),
                        showPreYear,
                        validZipsOnly
                );
            } else {
                data = createScaledData(
                        scaledData,
                        getLocalizedString("simuSuffix"),
                        getLocalizedString("realSuffix"),
                        showPreYear,
                        validZipsOnly
                );
            }
        } else {
            data = createUnscaledData(
                    realData,
                    getLocalizedString("simuSuffix"),
                    getLocalizedString("realSuffix"),
                    showPreYear,
                    validZipsOnly
            );
        }
        return new CsvJsonTableImageData(data, getCsvDelimiter());
    }
}
