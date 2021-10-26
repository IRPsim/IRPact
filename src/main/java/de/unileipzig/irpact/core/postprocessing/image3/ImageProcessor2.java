package de.unileipzig.irpact.core.postprocessing.image3;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.base.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.CustomAverageQuantilRangeGnuplotImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.SpecialAverageQuantilRangeGnuplotImageHandler;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCustomAverageQuantilRangeImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InSpecialAverageQuantilRangeImage;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irpact.util.R.RscriptEngine;
import de.unileipzig.irpact.util.gnuplot.GnuPlotEngine;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Daniel Abitz
 */
public class ImageProcessor2 extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ImageProcessor2.class);

    protected static final String IMAGES_BASENAME = "images2";
    protected static final String IMAGES_EXTENSION = "yaml";
    protected static final String RPLOTS_PDF = "Rplots.pdf";

    protected JsonResource localizedData;

    public ImageProcessor2(
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

    @Override
    public IRPSection getDefaultResultSection() {
        return IRPSection.RESULT;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
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

    protected void cleanUp() {
        deleteRplotsPdf();
    }

    protected void deleteRplotsPdf() {
        delete(Paths.get(RPLOTS_PDF));
        delete(clOptions.getOutputDir().resolve(RPLOTS_PDF));
        delete(clOptions.getDownloadDir().resolve(RPLOTS_PDF));
    }

    protected void delete(Path path) {
        try {
            if(Files.exists(path)) {
                Files.deleteIfExists(path);
                trace("deleted: '{}'", path);
            }
        } catch (IOException e) {
            error("deleting '" + path + "' failed", e);
        }
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
        ObjectNode root = tryLoadYaml(IMAGES_BASENAME, IMAGES_EXTENSION);
        if(root == null) {
            throw new IOException("missing resource: " + LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
        }
        localizedData = new JsonResource(root);
    }

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

    protected void handle(InOutputImage2 image) throws Throwable {
        if(image instanceof InCustomAverageQuantilRangeImage) {
            handleCustomQuantilRangeImage((InCustomAverageQuantilRangeImage) image);
        }
        else if(image instanceof InSpecialAverageQuantilRangeImage) {
            handleSpecialQuantilRangeImage((InSpecialAverageQuantilRangeImage) image);
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
}
