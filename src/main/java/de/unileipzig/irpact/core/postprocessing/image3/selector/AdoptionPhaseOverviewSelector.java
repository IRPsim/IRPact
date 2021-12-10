package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.AdoptionPhaseOverviewGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAdoptionPhaseOverviewImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AdoptionPhaseOverviewSelector extends AbstractImageHandlerSelector<InAdoptionPhaseOverviewImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AdoptionPhaseOverviewSelector.class);

    public AdoptionPhaseOverviewSelector(ImageProcessor2 processor) {
        super(InAdoptionPhaseOverviewImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InAdoptionPhaseOverviewImage image) {
        return new AdoptionPhaseOverviewGnuplotImageHandler(processor, image);
    }
}
