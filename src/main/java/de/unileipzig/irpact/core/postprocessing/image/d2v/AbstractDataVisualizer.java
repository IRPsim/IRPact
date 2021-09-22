package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.io.param.input.visualisation.result.InOutputImage;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractDataVisualizer implements DataVisualizer, LoggingHelper {

    protected ImageProcessor imageProcessor;

    public AbstractDataVisualizer(ImageProcessor imageProcessor) {
        this.imageProcessor = imageProcessor;
    }

    protected Path getTargetDir() throws IOException {
        return imageProcessor.getTargetDir();
    }

    protected JsonResource getLocalizedData() {
        return imageProcessor.getLocalizedData();
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    protected abstract boolean isEngineNotUsable();

    protected abstract String getEngineName();

    protected abstract SupportedEngine getSupportedEngine();

    protected abstract DataToVisualize getMode();

    protected Object buildKey(String key) {
        return new String[] {getSupportedEngine().name(), getMode().name(), key};
    }

    protected String getLocalizedString(String key) {
        return getLocalizedData().getString(buildKey(key));
    }

    protected String getLocalizedFormattedString(String key, Object... args) {
        return getLocalizedData().getFormattedString(buildKey(key), args);
    }

    public void handleImage(InOutputImage image) throws IOException {
        trace("handle '{}': storeData={}, storeScript={}, storeImage={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage());
        if(isEngineNotUsable()) {
            handleImageWithoutWorkingEngine(image);
        } else {
            handleImageWithWorkingEngine(image);
        }
    }

    protected void handleImageWithoutWorkingEngine(InOutputImage image) throws IOException {
        warn("{} not usable, skip '{}'", getEngineName(), image.getBaseFileName());
    }

    protected abstract void handleImageWithWorkingEngine(InOutputImage image) throws IOException;

    //=========================
    //helper
    //=========================

    protected AnnualAdoptionsZip2 createAnnualAdoptionZipData() {
        AnnualAdoptionsZip2 analyser = new AnnualAdoptionsZip2();
        analyser.setZipKey(RAConstants.ZIP);
        analyser.setYears(imageProcessor.getAllSimulationYears());
        analyser.init(imageProcessor.getAllZips(RAConstants.ZIP));
        analyser.apply(imageProcessor.getEnvironment());
        return analyser;
    }

    protected AnnualAdoptionsPhase2 createAnnualAdoptionPhaseData() {
        return createAnnualAdoptionPhaseData(true);
    }

    protected AnnualAdoptionsPhase2 createAnnualAdoptionPhaseDataWithInitialAdopter() {
        return createAnnualAdoptionPhaseData(false);
    }

    protected AnnualAdoptionsPhase2 createAnnualAdoptionPhaseData(boolean skipInital) {
        AnnualAdoptionsPhase2 analyser = new AnnualAdoptionsPhase2();
        analyser.setSkipInitial(skipInital);
        analyser.setYears(imageProcessor.getAllSimulationYears());
        analyser.init(imageProcessor.getValidAdoptionPhases(skipInital));
        analyser.apply(imageProcessor.getEnvironment());
        return analyser;
    }

    protected String print(AdoptionPhase phase) {
        return phase.name();
    }
}
