package de.unileipzig.irpact.commons.loc;

import java.util.Locale;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public interface LocalizedData {

    Set<Locale> getSupportedLocales();

    default boolean isSupported(Locale locale) {
        Set<Locale> locales = getSupportedLocales();
        return locales != null && locales.contains(locale);
    }
}
