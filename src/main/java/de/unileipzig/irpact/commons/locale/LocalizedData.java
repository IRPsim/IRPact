package de.unileipzig.irpact.commons.locale;

import java.util.Locale;
import java.util.MissingResourceException;
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

    String getString(Object[] keys) throws MissingResourceException;

    String getFormattedString(Object[] keys, Object[] args);

    String[] getStringArray(Object[] keys) throws MissingResourceException;
}
