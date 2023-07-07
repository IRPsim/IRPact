package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.ComparedAnnualZipGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedAnnualZipImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualZipSelector extends AbstractImageHandlerSelector<InComparedAnnualZipImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualZipSelector.class);

    public ComparedAnnualZipSelector(ImageProcessor2 processor) {
        super(InComparedAnnualZipImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InComparedAnnualZipImage image) {
        return new ComparedAnnualZipGnuplotImageHandler(processor, image);
    }
}
