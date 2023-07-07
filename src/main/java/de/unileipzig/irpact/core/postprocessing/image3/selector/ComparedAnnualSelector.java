package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.ComparedAnnualGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualMilieuImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InComparedAnnualImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ComparedAnnualSelector extends AbstractImageHandlerSelector<InComparedAnnualImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ComparedAnnualSelector.class);

    public ComparedAnnualSelector(ImageProcessor2 processor) {
        super(InComparedAnnualImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InComparedAnnualImage image) {
        return new ComparedAnnualGnuplotImageHandler(processor, image);
    }
}
