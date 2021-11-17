package de.unileipzig.irpact.core.postprocessing.image3;

import de.unileipzig.irpact.io.param.input.visualisation.result2.InOutputImage2;

/**
 * @author Daniel Abitz
 */
public interface ImageHandlerSelector {

    boolean isSupported(InOutputImage2 image);

    void handle(InOutputImage2 image) throws Throwable;
}
