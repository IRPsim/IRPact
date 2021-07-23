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

    void storeRunnableTo(Path target, boolean pretty) throws IOException;

    void storeRunnableTo(Path target, Charset charset, boolean pretty) throws IOException;

    void storePostableTo(Path target, boolean pretty) throws IOException;

    void storePostableTo(Path target, Charset charset, boolean pretty) throws IOException;

    @SuppressWarnings("unchecked")
    default <R extends Scenario> R getAs() {
        return (R) this;
    }
}
