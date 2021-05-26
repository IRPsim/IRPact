package de.unileipzig.irpact.commons.resource;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Allows to load files from the data directory and internal resources.
 *
 * @author Daniel Abitz
 */
public interface ResourceLoader {

    String INTERN_RESOURCES = "irpacttempdata";
    String EXTERN_RESOURCES = "irpactdata";
    Path EXTERN_RESOURCES_PATH = Paths.get(EXTERN_RESOURCES);

    Path getTempPath(String prefix, String suffix);

    boolean exists(String fileName);

    boolean hasExternal(String fileName);

    Path getExternal(String fileName);

    InputStream getExternalAsStream(String fileName);

    boolean hasInternal(String fileName);

    URL getInternal(String fileName);

    InputStream getInternalAsStream(String fileName);
}
