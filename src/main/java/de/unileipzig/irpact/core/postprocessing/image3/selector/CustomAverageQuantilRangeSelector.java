package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.CustomAverageQuantilRangeGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InCustomAverageQuantilRangeImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class CustomAverageQuantilRangeSelector extends AbstractImageHandlerSelector<InCustomAverageQuantilRangeImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CustomAverageQuantilRangeSelector.class);

    public CustomAverageQuantilRangeSelector(ImageProcessor2 processor) {
        super(InCustomAverageQuantilRangeImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createHandler(InCustomAverageQuantilRangeImage image) {
        return new CustomAverageQuantilRangeGnuplotImageHandler(processor, image);
    }
}
