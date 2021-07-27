package de.unileipzig.irpact.core.postprocessing.image.d2v;

import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsPhase2;
import de.unileipzig.irpact.core.postprocessing.data.adoptions2.impl.AnnualAdoptionsZip2;
import de.unileipzig.irpact.core.postprocessing.image.ImageProcessor;
import de.unileipzig.irpact.core.postprocessing.image.LocalizedImageData;
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

    protected LocalizedImageData getLocalizedImageData() {
        return imageProcessor.getLocalizedImageData();
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.RESULT;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    protected abstract boolean isEngineNotUsable();

    protected abstract String getEngineName();

    protected abstract DataToVisualize getMode();

    public void handleImage(InOutputImage image) throws IOException {
        if(isEngineNotUsable()) {
            warn("{} not usable, skip '{}'", getEngineName(), image.getBaseFileName());
            return;
        }

        trace("handle '{}': storeData={}, storeScript={}, storeImage={}", image.getBaseFileName(), image.isStoreData(), image.isStoreScript(), image.isStoreImage());
        handleImageWithWorkingEngine(image);
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
        AnnualAdoptionsPhase2 analyser = new AnnualAdoptionsPhase2();
        analyser.setYears(imageProcessor.getAllSimulationYears());
        analyser.init(imageProcessor.getValidAdoptionPhases());
        analyser.apply(imageProcessor.getEnvironment());
        return analyser;
    }

    protected String print(AdoptionPhase phase) {
        return phase.name();
    }
}