package de.unileipzig.irpact.commons.locale;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public abstract class LocalizedBase implements LocalizedData {

    protected Set<Locale> supported;

    public LocalizedBase(Locale supported) {
        this(Collections.singleton(supported));
    }

    public LocalizedBase(Set<Locale> supported) {
        this.supported = supported;
    }

    @Override
    public Set<Locale> getSupportedLocales() {
        return supported;
    }
}
