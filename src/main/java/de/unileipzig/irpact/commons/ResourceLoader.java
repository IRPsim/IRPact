package de.unileipzig.irpact.commons;

import java.io.InputStream;
import java.net.URL;

/**
 * @author Daniel Abitz
 */
public final class ResourceLoader {

    private ResourceLoader() {
    }

    public static URL load(String path) {
        return ResourceLoader.class.getClassLoader().getResource(path);
    }

    public static InputStream open(String path) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(path);
    }
}
