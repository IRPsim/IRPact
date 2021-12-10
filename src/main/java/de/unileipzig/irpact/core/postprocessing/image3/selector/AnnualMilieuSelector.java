package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.AnnualMilieuGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualMilieuImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AnnualMilieuSelector extends AbstractImageHandlerSelector<InAnnualMilieuImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualMilieuSelector.class);

    public AnnualMilieuSelector(ImageProcessor2 processor) {
        super(InAnnualMilieuImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InAnnualMilieuImage image) {
        return new AnnualMilieuGnuplotImageHandler(processor, image);
    }
}
