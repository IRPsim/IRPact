package de.unileipzig.irpact.core.postprocessing.image;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface ImageData {

    void writeTo(Path target, Charset charset) throws IOException;
}
