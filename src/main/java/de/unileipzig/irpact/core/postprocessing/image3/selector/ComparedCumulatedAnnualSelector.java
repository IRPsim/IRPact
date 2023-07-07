package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.ComparedCumulatedAnnualGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedCumulatedAnnualImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedCumulatedAnnualSelector extends AbstractImageHandlerSelector<InComparedCumulatedAnnualImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedCumulatedAnnualSelector.class);

    public ComparedCumulatedAnnualSelector(ImageProcessor2 processor) {
        super(InComparedCumulatedAnnualImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InComparedCumulatedAnnualImage image) {
        return new ComparedCumulatedAnnualGnuplotImageHandler(processor, image);
    }
}
