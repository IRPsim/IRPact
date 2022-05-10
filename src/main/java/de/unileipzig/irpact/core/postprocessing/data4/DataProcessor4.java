package de.unileipzig.irpact.core.postprocessing.data4;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.PostProcessor;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irpact.io.param.input.postdata.InNeighbourhoodOverview;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.start.MainCommandLineOptions;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * @author Daniel Abitz
 */
public class DataProcessor4 extends PostProcessor {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DataProcessor4.class);

    protected static final String RESULT_RES_BASENAME = "result2";
    protected static final String RESULT_RES_EXTENSION = "yaml";

    protected static final String POPULATION_OVERVIEW_BASENAME = "Populationsuebersicht";
    protected static final String ADOPTION_OVERVIEW_BASENAME = "Adoptionsuebersicht";
    protected static final String ADOPTION_ANALYSIS_BASENAME = "AdoptionAnalysis";

    protected ObjectNode performanceNode;

    public DataProcessor4(
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

    public ObjectNode getPerformanceNode() {
        return performanceNode;
    }

    protected void loadLocalizedData() throws IOException {
        loadLocalizedData(RESULT_RES_BASENAME, RESULT_RES_EXTENSION);
    }

    public JsonResource getLocalizedData() throws UncheckedIOException {
        return getLocalizedData(RESULT_RES_BASENAME, RESULT_RES_EXTENSION);
    }

    @Override
    public void execute() {
        try {
            handlePopulationOverview();
            handleAdoptionOverview();
            handleAdoptionAnalysis();
            handlePerformanceEvaluator();
            executeDataHandler();
        } catch (Throwable t) {
            error("error while executing DataProcessor", t);
        } finally {
            cleanUp();
        }
    }

    protected void handlePopulationOverview() {
        try {
            trace("handle PopulationOverview '{}'", POPULATION_OVERVIEW_BASENAME);
            PopulationOverview overview = new PopulationOverview(this, POPULATION_OVERVIEW_BASENAME);
            overview.init();
            overview.execute();
        } catch (Throwable t) {
            error("unexpected error while handling 'population overview'", t);
        }
    }

    protected void handleAdoptionOverview() {
        if(!getSettings().isLogResultAdoptionsAll()) {
            return;
        }

        try {
            trace("handle AdoptionOverview '{}'", ADOPTION_OVERVIEW_BASENAME);
            AdoptionOverview overview = new AdoptionOverview(this, ADOPTION_OVERVIEW_BASENAME);
            overview.init();
            overview.execute();
        } catch (Throwable t) {
            error("unexpected error while handling 'adoption overview'", t);
        }
    }

    protected void handleAdoptionAnalysis() {
        if(!getSettings().isLogAdoptionAnalysis()) {
            return;
        }

        try {
            trace("handle AdoptionAnalysis '{}'", ADOPTION_ANALYSIS_BASENAME);
            AdoptionAnalysis analysis = new AdoptionAnalysis(this, ADOPTION_ANALYSIS_BASENAME);
            analysis.init();
            analysis.execute();
        } catch (Throwable t) {
            error("unexpected error while handling 'adoption analysis'", t);
        }
    }

    protected void handlePerformanceEvaluator() {
        if(!getSettings().isLogPerformance()) {
            return;
        }

        try {
            trace("handle AdoptionOverview '{}'", ADOPTION_OVERVIEW_BASENAME);
            PerformanceEvaluator performance = new PerformanceEvaluator(this);
            performance.init();
            performance.execute();
            performanceNode = performance.getPerfomance(clOptions.getCalculatePerformanceArray());
        } catch (Throwable t) {
            error("unexpected error while handling 'PerformanceEvaluator'", t);
        }
    }

    protected void executeDataHandler() throws Throwable {
        if(inRoot.hasPostData()) {
            loadLocalizedData();
            for(InPostDataAnalysis postData: inRoot.getPostData()) {
                try {
                    handle(postData);
                } catch (Throwable t) {
                    error("unexpected error while handling post data: " + postData.getName(), t);
                }
            }
        }
    }

    @Override
    protected void cleanUp() {
        super.cleanUp();
    }

    protected void handle(InPostDataAnalysis postData) throws Throwable {
        if(postData == null) {
            warn("skip null analysis");
            return;
        }

        if(postData.isDisabled()) {
            info("skip disabled analysis '{}'", postData.getName());
            return;
        }

        if(postData.hasNothingToStore()) {
            info("skip pointless analysis '{}' (nothing to store)", postData.getName());
            return;
        }

        if(postData instanceof InBucketAnalyser) {
            handleBucketAnalyser((InBucketAnalyser) postData);
        }
        else if(postData instanceof InNeighbourhoodOverview) {
            handleNeighborhoodOverview((InNeighbourhoodOverview) postData);
        }
        else {
            warn("unsupported analysis: {} ({})", postData.getName(), postData.getClass());
        }
    }

    protected void handleBucketAnalyser(InBucketAnalyser postData) throws Throwable {
        trace("handle InBucketAnalyser '{}'", postData.getName());
        BucketAnalyser analyser = new BucketAnalyser(this, postData);
        analyser.init();
        analyser.execute();
    }

    protected void handleNeighborhoodOverview(InNeighbourhoodOverview postData) throws Throwable {
        trace("handle InBucketAnalyser '{}'", postData.getName());
        NeighbourhoodOverview overview = new NeighbourhoodOverview(this, postData);
        overview.init();
        overview.execute();
    }
}
