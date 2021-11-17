package de.unileipzig.irpact.core.postprocessing.image3;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.*;
import de.unileipzig.irpact.core.postprocessing.image3.selector.CustomAverageQuantilRangeSelector;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.visualisation.result2.*;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public class ImageProcessor2 extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ImageProcessor2.class);

    protected static final String IMAGES_BASENAME = "images2";
    protected static final String IMAGES_EXTENSION = "yaml";

    protected final List<ImageHandlerSelector> SELECTORS = new ArrayList<>();

    public ImageProcessor2(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        super(metaData, clOptions, inRoot, environment);
        initDefaultSelectors();
    }

    protected void initDefaultSelectors() {
        SELECTORS.add(new CustomAverageQuantilRangeSelector(this));
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.RESULT;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public Locale getLocale() {
        return getEnvironment().getMetaData().getLocale();
    }

    //=========================
    //gnuplot
    //=========================

    protected GnuPlotEngine gnuPlotEngine;
    public GnuPlotEngine getGnuPlotEngine() {
        if(gnuPlotEngine == null) {
            gnuPlotEngine = new GnuPlotEngine(clOptions.getGnuplotCommand());
            debug("use gnuplot engine '{}'", gnuPlotEngine.printCommand());
            debug("gnuplot version: {}", gnuPlotEngine.printVersion());
        }
        return gnuPlotEngine;
    }

    public boolean isGnuPlotUsable() {
        return getGnuPlotEngine().isUsable();
    }
    public boolean isGnuPlotNotUsable() {
        return !isGnuPlotUsable();
    }

    //=========================
    //R
    //=========================

    protected RscriptEngine rscriptEngine;
    public RscriptEngine getRscriptEngine() {
        if(rscriptEngine == null) {
            rscriptEngine = new RscriptEngine(clOptions.getRscriptCommand());
            debug("use Rscript engine '{}'", rscriptEngine.printCommand());
            debug("R version: {}", rscriptEngine.printVersion());
        }
        return rscriptEngine;
    }

    public boolean isRUsable() {
        return getRscriptEngine().isUsable();
    }

    public boolean isRNotUsable() {
        return !isRUsable();
    }

    @Override
    public void execute() {
        try {
            execute0();
        } catch (Throwable t) {
            error("unexpected error while executing ImageProcessor2", t);
        } finally {
            cleanUp();
        }
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
    }

    protected void execute0() throws IOException, ParsingException {
        if(inRoot.hasImages2()) {
            loadLocalizedData();
            for(InOutputImage2 image: inRoot.getImages2()) {
                try {
                    handle(image);
                } catch (Throwable t) {
                    error("unexpected error while handling image: " + image.getName(), t);
                }
            }
        }
    }

    protected void loadLocalizedData() throws IOException {
        loadLocalizedData(IMAGES_BASENAME, IMAGES_EXTENSION);
    }

    public JsonResource getLocalizedData() throws UncheckedIOException {
        return getLocalizedData(IMAGES_BASENAME, IMAGES_EXTENSION);
    }

    protected void handle2(InOutputImage2 image) throws Throwable {
        if(image == null) {
            warn("skip null image");
            return;
        }

        if(image.isDisabled()) {
            info("skip disabled image '{}'", image.getName());
            return;
        }

        if(image.hasNothingToStore()) {
            info("skip pointless image '{}' (nothing to store)", image.getName());
            return;
        }

        for(ImageHandlerSelector selector: SELECTORS) {
            if(selector.isSupported(image)) {
                selector.handle(image);
                return;
            }
        }

        warn("unsupported image: " + image.getName());
    }

    protected void handle(InOutputImage2 image) throws Throwable {
        if(image == null) {
            warn("skip null image");
            return;
        }

        if(image.isDisabled()) {
            info("skip disabled image '{}'", image.getName());
            return;
        }

        if(image.hasNothingToStore()) {
            info("skip pointless image '{}' (nothing to store)", image.getName());
            return;
        }

        if(image instanceof InCustomAverageQuantilRangeImage) {
            handleCustomQuantilRangeImage((InCustomAverageQuantilRangeImage) image);
        }
        else if(image instanceof InSpecialAverageQuantilRangeImage) {
            handleSpecialQuantilRangeImage((InSpecialAverageQuantilRangeImage) image);
        }
        else if(image instanceof InComparedAnnualImage) {
            handleComparedAnnualImage((InComparedAnnualImage) image);
        }
        else if(image instanceof InComparedAnnualZipImage) {
            handleComparedAnnualZipImage((InComparedAnnualZipImage) image);
        }
        else if(image instanceof InAdoptionPhaseOverviewImage) {
            handleAdoptionPhaseOverviewImage((InAdoptionPhaseOverviewImage) image);
        }
        else if(image instanceof InInterestOverviewImage) {
            handleInterestOverviewImage((InInterestOverviewImage) image);
        }
        else if(image instanceof InProcessPhaseOverviewImage) {
            handleProcessPhaseOverviewImage((InProcessPhaseOverviewImage) image);
        }
        else if(image instanceof InAnnualBucketImage) {
            handleAnnualBucketImage((InAnnualBucketImage) image);
        }
        else {
            warn("unsupported image: " + image.getName());
        }
    }

    protected void handleCustomQuantilRangeImage(InCustomAverageQuantilRangeImage image) throws Throwable {
        trace("handle InCustomAverageQuantilRangeImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new CustomAverageQuantilRangeGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleSpecialQuantilRangeImage(InSpecialAverageQuantilRangeImage image) throws Throwable {
        trace("handle InSpecialAverageQuantilRangeImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new SpecialAverageQuantilRangeGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleComparedAnnualImage(InComparedAnnualImage image) throws Throwable {
        trace("handle InComparedAnnualImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new ComparedAnnualGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleComparedAnnualZipImage(InComparedAnnualZipImage image) throws Throwable {
        trace("handle InComparedAnnualZipImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new ComparedAnnualZipGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleAdoptionPhaseOverviewImage(InAdoptionPhaseOverviewImage image) throws Throwable {
        trace("handle InAdoptionPhaseOverviewImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new AdoptionPhaseOverviewGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleInterestOverviewImage(InInterestOverviewImage image) throws Throwable {
        trace("handle InInterestOverviewImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new InterestOverviewGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleProcessPhaseOverviewImage(InProcessPhaseOverviewImage image) throws Throwable {
        trace("handle InProcessPhaseOverviewImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new ProcessPhaseOverviewGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected void handleAnnualBucketImage(InAnnualBucketImage image) throws Throwable {
        trace("handle InAnnualBucketImage '{}'", image.getName());

        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = new AnnualBucketGnuplotImageHandler(this, image);
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }
}
