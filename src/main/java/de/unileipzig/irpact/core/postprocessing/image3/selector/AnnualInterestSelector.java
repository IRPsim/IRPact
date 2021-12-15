package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.AnnualInterestGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualInterestImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AnnualInterestSelector extends AbstractImageHandlerSelector<InAnnualInterestImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualInterestSelector.class);

    public AnnualInterestSelector(ImageProcessor2 processor) {
        super(InAnnualInterestImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InAnnualInterestImage image) {
        return new AnnualInterestGnuplotImageHandler(processor, image);
    }
}
