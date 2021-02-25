package de.unileipzig.irpact.commons.res;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;

/**
 * Allows to load files from the data directory and internal resources.
 *
 * @author Daniel Abitz
 */
public interface ResourceLoader {

    boolean exists(String fileName);

    boolean hasPath(String fileName);

    Path get(String fileName);

    boolean hasResource(String fileName);

    URL getResource(String fileName);

    InputStream getResourceAsStream(String fileName);
}
