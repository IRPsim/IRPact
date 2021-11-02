package de.unileipzig.irpact.core.postprocessing.image3.gnuplot;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image.ImageData;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.CsvJsonTableImageData;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.base.AbstractAdoptionPhaseOverviewImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAdoptionPhaseOverviewImage;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotBuilder;
import de.unileipzig.irpact.util.gnuplot.builder.GnuPlotFactory;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

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
    public void handleImage(InAdoptionPhaseOverviewImage image, boolean engineUsable) throws Throwable {
        CustomImageData data = createData(image);
        if(data == null) {
            return;
        }

        GnuPlotBuilder builder = getBuilder(image, data);
        if(builder == null) {
            return;
        }

        execute(image, builder, data, engineUsable);
    }

    public GnuPlotBuilder getBuilder(InAdoptionPhaseOverviewImage image, CustomImageData data) {
        return GnuPlotFactory.stackedBarChart1(
                getLocalizedFormattedString("title", data.getInitial()),
                getLocalizedString("xlab"), getLocalizedString("ylab"), getLocalizedString("filllab"),
                getLocalizedString("startMid"), getLocalizedString("midEnd"), getLocalizedString("endStart"),
                getLocalizedString("sep"),
                image.getBoxWidth(),
                image.getImageWidth(), image.getImageHeight(),
                data.getMinY(), data.getMaxY()
        );
    }

    @Override
    public GnuPlotBuilder getBuilder(InAdoptionPhaseOverviewImage image) throws Throwable {
        throw new UnsupportedOperationException("use getBuilder with " + CustomImageData.class.getSimpleName());
    }

    @Override
    public CustomImageData createData(InAdoptionPhaseOverviewImage image) throws Throwable {
        int initialAdopter = getInitialAdopterCount();
        trace("number of initial agents: {}", initialAdopter);

        int totalAdopter = getTotalAdopterCount();
        int finalTotalNumberOfAdoptions = (int) Math.ceil(totalAdopter * PRETTY_FACTOR);
        trace("total number of adoptions (last year): {} (*{} = {} (ceil))", totalAdopter, PRETTY_FACTOR, finalTotalNumberOfAdoptions);

        JsonTableData3 data = getTableData();

        return new CustomImageData(
                new CsvJsonTableImageData(data),
                initialAdopter,
                initialAdopter,
                finalTotalNumberOfAdoptions
        );
    }

    /**
     * @author Daniel Abitz
     */
    private static final class CustomImageData implements ImageData {

        private final CsvJsonTableImageData DATA;
        private final int INITIAL;
        private final int MIN_Y;
        private final int MAX_Y;

        public CustomImageData(CsvJsonTableImageData data, int inital, int minY, int maxY) {
            DATA = data;
            INITIAL = inital;
            MIN_Y = minY;
            MAX_Y = maxY;
        }

        public int getInitial() {
            return INITIAL;
        }

        public int getMinY() {
            return MIN_Y;
        }

        public int getMaxY() {
            return MAX_Y;
        }

        @Override
        public void writeTo(Path target, Charset charset) throws IOException {
            DATA.writeTo(target, charset);
        }
    }
}
