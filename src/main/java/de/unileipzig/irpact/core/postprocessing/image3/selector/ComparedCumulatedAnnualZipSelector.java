package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.ComparedCumulatedAnnualZipGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedCumulatedAnnualZipImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedCumulatedAnnualZipSelector extends AbstractImageHandlerSelector<InComparedCumulatedAnnualZipImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedCumulatedAnnualZipSelector.class);

    public ComparedCumulatedAnnualZipSelector(ImageProcessor2 processor) {
        super(InComparedCumulatedAnnualZipImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InComparedCumulatedAnnualZipImage image) {
        return new ComparedCumulatedAnnualZipGnuplotImageHandler(processor, image);
    }
}
