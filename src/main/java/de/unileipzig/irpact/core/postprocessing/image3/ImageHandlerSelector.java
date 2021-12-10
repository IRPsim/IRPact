package de.unileipzig.irpact.core.postprocessing.image3;

import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;

/**
 * @author Daniel Abitz
 */
public interface ImageHandlerSelector {

    boolean isSupported(InOutputImage2 image);

    ImageHandler getHandler(InOutputImage2 image) throws Throwable;
}
