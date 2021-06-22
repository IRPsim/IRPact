package de.unileipzig.irpact.util.scenarios;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public interface Scenario {

    void storeUploadableTo(Path target, boolean pretty) throws IOException ;

    void storeUploadableTo(Path target, Charset charset, boolean pretty) throws IOException;
}
