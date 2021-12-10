package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.core.postprocessing.image3.gnuplot.ProcessPhaseOverviewGnuplotImageHandler;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InInterestOverviewImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InProcessPhaseOverviewImage;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class ProcessPhaseOverviewSelector extends AbstractImageHandlerSelector<InProcessPhaseOverviewImage> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(ProcessPhaseOverviewSelector.class);

    public ProcessPhaseOverviewSelector(ImageProcessor2 processor) {
        super(InProcessPhaseOverviewImage.class, processor);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    protected ImageHandler createGnuplotHandler(InProcessPhaseOverviewImage image) {
        return new ProcessPhaseOverviewGnuplotImageHandler(processor, image);
    }
}
