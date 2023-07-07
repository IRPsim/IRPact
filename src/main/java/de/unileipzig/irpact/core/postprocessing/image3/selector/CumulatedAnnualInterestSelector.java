package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.CumulatedAnnualInterestGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCumulatedAnnualInterestImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class CumulatedAnnualInterestSelector extends AbstractImageHandlerSelector<InCumulatedAnnualInterestImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CumulatedAnnualInterestSelector.class);

    public CumulatedAnnualInterestSelector(ImageProcessor2 processor) {
        super(InCumulatedAnnualInterestImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InCumulatedAnnualInterestImage image) {
        return new CumulatedAnnualInterestGnuplotImageHandler(processor, image);
    }
}
