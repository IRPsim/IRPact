package de.unileipzig.irpact.core.process2.modular;

import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.JsonResource;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public interface LoggingResourceAccess extends LoggingHelper {

    String FILE_NAME = "logger";
    String FILE_EXTENSION = "yaml";

    default String getFileName() {
        return FILE_NAME;
    }

    default String getFileExtension() {
        return FILE_EXTENSION;
    }

    JsonResource getLocalizedData();

    default JsonResource load(SimulationEnvironment environment) throws IOException {
        JsonResource res = loadInternal(environment);
        if(res == null) {
            return loadExternal(environment);
        } else {
            return res;
        }
    }

    default JsonResource loadInternal(SimulationEnvironment environment) throws IOException {
        ResourceLoader loader = environment.getMetaData().getLoader();
        Locale locale = environment.getMetaData().getLocale();
        String fileName = getFileName();
        String fileExtension = getFileExtension();

        URL internalUrl = loader.getLocalizedInternal(fileName, locale, fileExtension);
        trace("try (internal) loading '{}'", internalUrl);
        if(loader.hasLocalizedInternal(fileName, locale, fileExtension)) {
            JsonResource cached = getIfExists(environment, internalUrl);
            if(cached != null) {
                trace("get cached '{}'", internalUrl);
                return cached;
            } else {
                trace("loading '{}'", internalUrl);
                InputStream in = loader.getLocalizedExternalAsStream(fileName, locale, fileExtension);
                JsonResource loaded = load(in);
                if(loaded != null) {
                    trace("cache '{}'", internalUrl);
                }
                return loaded;
            }
        }

        return null;
    }

    default JsonResource loadExternal(SimulationEnvironment environment) throws IOException {
        ResourceLoader loader = environment.getMetaData().getLoader();
        Locale locale = environment.getMetaData().getLocale();
        String fileName = getFileName();
        String fileExtension = getFileExtension();

        Path externalPath = loader.getLocalizedExternal(fileName, locale, fileExtension);
        trace("try (external) loading '{}'", externalPath);
        if(loader.hasLocalizedInternal(fileName, locale, fileExtension)) {
            JsonResource cached = getIfExists(environment, externalPath);
            if(cached != null) {
                trace("get cached '{}'", externalPath);
                return cached;
            } else {
                trace("loading '{}'", externalPath);
                InputStream in = loader.getLocalizedExternalAsStream(fileName, locale, fileExtension);
                JsonResource loaded = load(in);
                if(loaded != null) {
                    trace("cache '{}'", externalPath);
                }
                return loaded;
            }
        }

        return null;
    }

    default JsonResource getIfExists(SimulationEnvironment environment, Object key) {
        DataStore store = environment.getGlobalData();
        if(store.contains(key)) {
            return store.getAuto(key);
        } else {
            return null;
        }
    }

    default void store(SimulationEnvironment environment, Object key, JsonResource resource) {
        DataStore store = environment.getGlobalData();
        store.put(key, resource);
    }

    default JsonResource load(InputStream in) throws IOException {
        if(in == null) {
            return null;
        }

        try {
            ObjectNode node = JsonUtil.read(in, JsonUtil.YAML);
            return new JsonResource(node);
        } finally {
            in.close();
        }
    }

    String getResourceType();

    default String[] buildKey(String key) {
        return new String[] {getResourceType(), key};
    }

    default String getLocalizedString(String key) {
        return getLocalizedData().getString(buildKey(key));
    }

    default String getLocalizedFormattedString(String key, Object... args) {
        return getLocalizedData().getFormattedString(buildKey(key), args);
    }
}
