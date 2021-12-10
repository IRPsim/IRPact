package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.InterestOverviewGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InInterestOverviewImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class InterestOverviewSelector extends AbstractImageHandlerSelector<InInterestOverviewImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InterestOverviewSelector.class);

    public InterestOverviewSelector(ImageProcessor2 processor) {
        super(InInterestOverviewImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InInterestOverviewImage image) {
        return new InterestOverviewGnuplotImageHandler(processor, image);
    }
}
