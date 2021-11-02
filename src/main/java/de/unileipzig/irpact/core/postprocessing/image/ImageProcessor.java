package de.unileipzig.irpact.core.postprocessing.image;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.postprocessing.data3.RealAdoptionData;
import de.unileipzig.irpact.core.postprocessing.image.d2v.DataToVisualizeHandler;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class ImageProcessor extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ImageProcessor.class);

    protected static final String IMAGES_BASENAME = "images";
    protected static final String IMAGES_EXTENSION = "yaml";
    protected static final String RPLOTS_PDF = "Rplots.pdf";

    protected double defaultLinewidth = 1.0;
    protected double defaultBoxWidth = 0.8;
    protected int defaultWidth = 1280;
    protected int defaultHeight = 720;

    public ImageProcessor(
            MetaData metaData,
            MainCommandLineOptions clOptions,
            InRoot inRoot,
            SimulationEnvironment environment) {
        super(metaData, clOptions, inRoot, environment);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
    }

    public double getDefaultBoxWidth() {
        return defaultBoxWidth;
    }

    public void setDefaultBoxWidth(double defaultBoxWidth) {
        this.defaultBoxWidth = defaultBoxWidth;
    }

    public double getDefaultLinewidth() {
        return defaultLinewidth;
    }

    public void setDefaultLinewidth(double defaultLinewidth) {
        this.defaultLinewidth = defaultLinewidth;
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public int getDefaultWidth() {
        return defaultWidth;
    }

    public RealAdoptionData getRealAdoptionData(InOutputImage image) {
        if(image.hasRealAdoptionDataFile()) {
            try {
                return getScaledRealAdoptionData(image.getRealAdoptionDataFile());
            } catch (Throwable t) {
                LOGGER.warn("loading adoption data for '{}' failed: {}", image.getBaseFileName(), t.getMessage());
                return getFallbackAdoptionData();
            }
        } else {
            return getFallbackAdoptionData();
        }
    }

    //=========================
    //image
    //=========================

    @Override
    public void execute() {
        try {
            if(inRoot.hasImages()) {
                loadLocalizedData();
                for(InOutputImage image : inRoot.getImages()) {
                    execute(image);
                }
            }
        } catch (Throwable t) {
            error("unexpected error while executing ImageProcessor", t);
        } finally {
            cleanUp();
        }
    }

    protected void init() throws IOException {
        trace("load resource: " + LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
        loadLocalizedData();
    }

    protected void execute(InOutputImage image) {
        trace("image '{}': data={}, script={}, image={}, enabled={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage(), image.isEnabled());
        if(image.isDisabled()) {
            trace("skip disabled image '{}'", image.getBaseFileName());
        } else {
            try {
                DataToVisualizeHandler.handleImage(this, image);
            } catch (Throwable t) {
                error("executing image '" + image.getName() + "' failed, skip", t);
            }
        }
    }

    protected void cleanUp() {
        deleteRplotsPdf();
    }

    protected void deleteRplotsPdf() {
        delete(Paths.get(RPLOTS_PDF));
        delete(clOptions.getOutputDir().resolve(RPLOTS_PDF));
        delete(clOptions.getDownloadDir().resolve(RPLOTS_PDF));
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

    //=========================
    //util
    //=========================

    public List<AdoptionPhase> getValidAdoptionPhases(boolean skipInitial) {
        return skipInitial
                ? AdoptionPhase.NON_INITIAL
                : AdoptionPhase.VALID_PHASES;
    }

    protected String[] yearBreaks;
    public String[] getYearBreaksForPrettyR() {
        if(yearBreaks == null) {
            yearBreaks = getAllSimulationYears().stream()
                    .map(Object::toString)
                    .toArray(String[]::new);
        }
        return yearBreaks;
    }

    protected void loadLocalizedData() throws IOException {
        ObjectNode root = tryLoadYaml(IMAGES_BASENAME, IMAGES_EXTENSION);
        if(root == null) {
            throw new IOException("missing resource: " + LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
        }
        localizedData = new JsonResource(root);
    }

    protected JsonResource localizedData;
    public JsonResource getLocalizedData() throws UncheckedIOException {
        if(localizedData == null) {
            try {
                loadLocalizedData();
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return localizedData;
    }
}
