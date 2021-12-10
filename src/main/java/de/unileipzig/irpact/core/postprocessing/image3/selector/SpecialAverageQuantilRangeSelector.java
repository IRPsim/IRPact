package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.SpecialAverageQuantilRangeGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InSpecialAverageQuantilRangeImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpecialAverageQuantilRangeSelector extends AbstractImageHandlerSelector<InSpecialAverageQuantilRangeImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpecialAverageQuantilRangeSelector.class);

    public SpecialAverageQuantilRangeSelector(ImageProcessor2 processor) {
        super(InSpecialAverageQuantilRangeImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InSpecialAverageQuantilRangeImage image) {
        return new SpecialAverageQuantilRangeGnuplotImageHandler(processor, image);
    }
}
