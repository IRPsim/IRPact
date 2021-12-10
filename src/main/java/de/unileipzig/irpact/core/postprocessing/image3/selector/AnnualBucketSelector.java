package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.AnnualBucketGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InAnnualBucketImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AnnualBucketSelector extends AbstractImageHandlerSelector<InAnnualBucketImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AnnualBucketSelector.class);

    public AnnualBucketSelector(ImageProcessor2 processor) {
        super(InAnnualBucketImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InAnnualBucketImage image) {
        return new AnnualBucketGnuplotImageHandler(processor, image);
    }
}
