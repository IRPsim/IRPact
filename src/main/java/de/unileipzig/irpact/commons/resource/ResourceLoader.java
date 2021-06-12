package de.unileipzig.irpact.commons.resource;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Allows to load files from the data directory and internal resources.
 *
 * @author Daniel Abitz
 */
public interface ResourceLoader {

    String INTERN_RESOURCES = "irpactdata";
    String EXTERN_RESOURCES = "irpactdata";
    Path EXTERN_RESOURCES_PATH = Paths.get(EXTERN_RESOURCES);

    //=========================
    //util
    //=========================

    Path getTempPath(String prefix, String suffix);

    //=========================
    //general
    //=========================

    boolean exists(String fileName);

    boolean hasExternal(String fileName);

    Path getExternal(String fileName);

    InputStream getExternalAsStream(String fileName);

    boolean hasInternal(String fileName);

    URL getInternal(String fileName);

    InputStream getInternalAsStream(String fileName);

    //=========================
    //localized support
    //=========================

    default boolean existsLocalized(String baseName, Locale locale, String extension) {
        return exists(LocaleUtil.buildName(baseName, locale, extension));
    }

    default boolean hasLocalizedExternal(String baseName, Locale locale, String extension) {
        return hasExternal(LocaleUtil.buildName(baseName, locale, extension));
    }

    default Path getLocalizedExternal(String baseName, Locale locale, String extension) {
        return getExternal(LocaleUtil.buildName(baseName, locale, extension));
    }

    default InputStream getLocalizedExternalAsStream(String baseName, Locale locale, String extension) {
        return getExternalAsStream(LocaleUtil.buildName(baseName, locale, extension));
    }

    default boolean hasLocalizedInternal(String baseName, Locale locale, String extension) {
        return hasInternal(LocaleUtil.buildName(baseName, locale, extension));
    }

    default URL getLocalizedInternal(String baseName, Locale locale, String extension) {
        return getInternal(LocaleUtil.buildName(baseName, locale, extension));
    }

    default InputStream getLocalizedInternalAsStream(String baseName, Locale locale, String extension) {
        return getInternalAsStream(LocaleUtil.buildName(baseName, locale, extension));
    }
}
