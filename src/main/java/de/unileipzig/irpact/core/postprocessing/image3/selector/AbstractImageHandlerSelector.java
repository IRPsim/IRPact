package de.unileipzig.irpact.core.postprocessing.image3.selector;

import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandler;
import de.unileipzig.irpact.core.postprocessing.image3.ImageHandlerSelector;
import de.unileipzig.irpact.core.postprocessing.image3.ImageProcessor2;
import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractImageHandlerSelector<T>
        implements ImageHandlerSelector, LoggingHelper {

    protected final Class<T> c;
    protected ImageProcessor2 processor;

    public AbstractImageHandlerSelector(Class<T> c, ImageProcessor2 processor) {
        this.c = c;
        this.processor = processor;
    }

    @Override
    public abstract IRPLogger getDefaultLogger();

    @Override
    public boolean isSupported(InOutputImage2 image) {
        return c.isInstance(image);
    }

    @Override
    public void handle(InOutputImage2 image) throws Throwable {
        trace("handle {} '{}'", c.getSimpleName(), image.getName());


        ImageHandler handler;
        if(image.getEngine() == SupportedEngine.GNUPLOT) {
            handler = createHandler(c.cast(image));
        } else {
            warn("R not supported, skip");
            return;
        }

        handler.init();
        handler.execute();
    }

    protected abstract ImageHandler createHandler(T image);
}
