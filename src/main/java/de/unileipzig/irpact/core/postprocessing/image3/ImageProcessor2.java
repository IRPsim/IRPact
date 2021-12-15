package de.unileipzig.irpact.core.postprocessing.image3;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.logging.LazyString;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.postprocessing.image3.selector.*;
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
import java.util.stream.Collectors;

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
        SELECTORS.add(new AdoptionPhaseOverviewSelector(this));
        SELECTORS.add(new AnnualBucketSelector(this));
        SELECTORS.add(new AnnualInterestSelector(this));
        SELECTORS.add(new AnnualMilieuSelector(this));
        SELECTORS.add(new ComparedAnnualSelector(this));
        SELECTORS.add(new ComparedAnnualZipSelector(this));
        SELECTORS.add(new CumulatedAnnualInterestSelector(this));
        SELECTORS.add(new CustomAverageQuantilRangeSelector(this));
        SELECTORS.add(new InterestOverviewSelector(this));
        SELECTORS.add(new ProcessPhaseOverviewSelector(this));
        SELECTORS.add(new SpecialAverageQuantilRangeSelector(this));
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

        List<ImageHandlerSelector> validSelectors = findAllSupportedSelectors(image);

        switch(validSelectors.size()) {
            case 0:
                warn("unsupported image: " + image.getName());
                break;

            case 1:
                ImageHandlerSelector selector = validSelectors.get(0);
                debug("use selector '{}'", selector.getClass().getSimpleName());
                ImageHandler handler = selector.getHandler(image);
                handler.init();
                handler.execute();
                break;

            default:
                warn("multiple selectors found ({}), skip", new LazyString(() -> validSelectors.stream()
                        .map(_selector -> _selector.getClass().getSimpleName())
                        .collect(Collectors.toList()))
                );
                break;
        }
    }

    protected List<ImageHandlerSelector> findAllSupportedSelectors(InOutputImage2 image) {
        List<ImageHandlerSelector> supportedSelectors = new ArrayList<>();
        for(ImageHandlerSelector selector: SELECTORS) {
            if(selector.isSupported(image)) {
                supportedSelectors.add(selector);
            }
        }
        return supportedSelectors;
    }
}
