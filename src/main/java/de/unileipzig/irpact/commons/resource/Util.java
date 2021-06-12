package de.unileipzig.irpact.commons.resource;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
final class Util {

    private Util() {
    }

    static String buildName(String baseName, Locale locale, String extension) {
        if(locale == null || locale == Locale.ROOT) {
            return baseName + "." + extension;
        } else {
            return baseName + "_" + locale.toLanguageTag() + "." + extension;
        }
    }
}
