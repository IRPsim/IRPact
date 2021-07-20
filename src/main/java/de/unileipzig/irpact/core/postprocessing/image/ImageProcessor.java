package de.unileipzig.irpact.core.postprocessing.image;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
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
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Daniel Abitz
 */
public class ImageProcessor extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ImageProcessor.class);

    protected static final BasicRealAdoptionData PLACEHOLDER_REAL_DATA = new BasicRealAdoptionData(0);

    protected static final String IMAGES_BASENAME = "images";
    protected static final String IMAGES_EXTENSION = "yaml";
    protected static final String RPLOTS_PDF = "Rplots.pdf";

    protected double defaultLinewidth = 1.0;

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

    public double getDefaultLinewidth() {
        return defaultLinewidth;
    }

    public void setDefaultLinewidth(double defaultLinewidth) {
        this.defaultLinewidth = defaultLinewidth;
    }

    public RealAdoptionData getRealAdoptionData() {
        return PLACEHOLDER_REAL_DATA;
    }

    //=========================
    //image
    //=========================

    @Override
    public void execute() {
        try {
            execute0();
        } catch (Throwable t) {
            error("error while executing ImageProcessor", t);
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

    private static void delete(Path path) {
        try {
            if(Files.exists(path)) {
                Files.deleteIfExists(path);
                LOGGER.trace(IRPSection.RESULT, "deleted: '{}'", path);
            }
        } catch (IOException e) {
            LOGGER.error("deleting '" + path + "' failed", e);
        }
    }

    protected void execute0() throws ParsingException, IOException {
        if(inRoot.hasImages()) {
            for(InOutputImage image: inRoot.getImages()) {
                trace("image '{}': data={}, script={}, image={}, enabled={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage(), image.isEnabled());
                if(image.isDisabled()) {
                    trace("skip disabled image '{}'", image.getBaseFileName());
                } else {
                    DataToVisualizeHandler.handleImage(this, image);
                }
            }
        }
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

    public List<AdoptionPhase> getValidAdoptionPhases() {
        return AdoptionPhase.NON_INITIAL;
    }

    protected List<String> zips;
    public List<String> getAllZips(String key) {
        if(zips == null) {
            zips = environment.getAgents().streamConsumerAgents()
                    .filter(agent -> agent.hasAnyAttribute(key))
                    .map(agent -> {
                        Attribute attr = agent.findAttribute(key);
                        return attr.asValueAttribute().getValueAsString();
                    })
                    .collect(Collectors.toList());
        }
        return zips;
    }

    protected List<Integer> years;
    public List<Integer> getAllSimulationYears() {
        if(years == null) {
            int firstYear = metaData.getOldestRunInfo().getActualFirstSimulationYear();
            int lastYear = metaData.getCurrentRunInfo().getLastSimulationYear();
            years = IntStream.rangeClosed(firstYear, lastYear)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return years;
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

    protected LocalizedImageData localizedImageData;
    public LocalizedImageData getLocalizedImageData() {
        if(localizedImageData == null) {
            try {
                if(tryLoadExternal()) {
                    return localizedImageData;
                }
                if(tryLoadInternal()) {
                    return localizedImageData;
                }
                warn("'{}' not found, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            } catch (Exception e) {
                warn("loading '{}' failed, use fallback", LocaleUtil.buildName(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            }
            //fallback
            localizedImageData = DefaultLocalizedImageData.get();
        }
        return localizedImageData;
    }

    protected boolean tryLoadExternal() throws IOException {
        if(metaData.getLoader().hasLocalizedExternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedExternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            InputStream in = metaData.getLoader().getLocalizedExternalAsStream(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION);
            return tryLoad(in);
        } else {
            return false;
        }
    }

    protected boolean tryLoadInternal() throws IOException {
        if(metaData.getLoader().hasLocalizedInternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION)) {
            trace("loading '{}'", metaData.getLoader().getLocalizedInternal(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION));
            InputStream in = metaData.getLoader().getLocalizedInternalAsStream(IMAGES_BASENAME, metaData.getLocale(), IMAGES_EXTENSION);
            return tryLoad(in);
        } else {
            return false;
        }
    }

    protected boolean tryLoad(InputStream in) throws IOException {
        if(in == null) {
            return false;
        }
        try {
            ObjectNode root = JsonUtil.read(in, JsonUtil.YAML);
            LocalizedImageDataYaml localizedImage = new LocalizedImageDataYaml(metaData.getLocale(), root);
            localizedImage.setEscapeSpecialCharacters(true);
            this.localizedImageData = localizedImage;
            return true;
        } finally {
            in.close();
        }
    }
}
