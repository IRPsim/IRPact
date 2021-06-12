package de.unileipzig.irpact.commons.resource;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public final class LocaleUtil {

    private LocaleUtil() {
    }

    public static String buildName(String baseName, Locale locale, String extension) {
        if(locale == null || locale == Locale.ROOT) {
            return baseName + "." + extension;
        } else {
            return baseName + "_" + locale.toLanguageTag() + "." + extension;
        }
    }
}
