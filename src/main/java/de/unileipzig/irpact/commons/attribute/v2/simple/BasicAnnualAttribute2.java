package de.unileipzig.irpact.commons.attribute.v2.simple;

import de.unileipzig.irpact.commons.Util;

import java.util.Map;
import java.util.NavigableMap;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * @author Daniel Abitz
 */
public class BasicAnnualAttribute2
        extends AbstractMapContainerAttribute2<Integer>
        implements AnnualAttribute2 {

    /**
     * @author Daniel Abitz
     */
    public enum MissingKeyMode {
        THROW_ERROR,
        USE_PREVIOUS,
        USE_FALLBACK
    }

    protected NavigableMap<Integer, Attribute2> map = new TreeMap<>();

    protected MissingKeyMode mode = MissingKeyMode.THROW_ERROR;
    protected Integer fallbackYear = null;

    public BasicAnnualAttribute2(String name) {
        setName(name);
    }

    public void setMode(MissingKeyMode mode) {
        this.mode = Util.checkNull(mode, MissingKeyMode.THROW_ERROR);
    }

    public MissingKeyMode getMode() {
        return mode;
    }

    public boolean hasFallbackYear() {
        return fallbackYear != null;
    }

    public Integer getFallbackYear() {
        return fallbackYear;
    }

    public void setFallbackYear(int fallbackYear) {
        this.fallbackYear = fallbackYear;
    }

    @Override
    public BasicAnnualAttribute2 copy() {
        BasicAnnualAttribute2 copy = new BasicAnnualAttribute2(getName());
        copy.setMode(getMode());
        for(Map.Entry<Integer, Attribute2> entry: map().entrySet()) {
            copy.set(entry.getKey(), entry.getValue().copy());
        }
        return copy;
    }

    @Override
    protected NavigableMap<Integer, Attribute2> map() {
        return map;
    }

    @Override
    public Attribute2 get(Integer key) {
        return get(key.intValue());
    }

    @Override
    public Attribute2 get(int year) {
        Attribute2 attr = map().get(year);
        if(attr == null) {
            return handleMissing(year);
        } else {
            return attr;
        }
    }

    protected Attribute2 handleMissing(int year) {
        switch (mode) {
            case USE_PREVIOUS:
                return getPrevious(year);

            case USE_FALLBACK:
                return getFallback(year);

            case THROW_ERROR:
                throw new NoSuchElementException("missing value for: " + year);

            default:
                throw new IllegalStateException("unsupported mode: " + mode);
        }
    }

    protected Attribute2 getPrevious(int year) {
        Integer previous = map().lowerKey(year);
        if(previous == null) {
            throw new IllegalArgumentException("no previous year found: " + year);
        } else {
            Attribute2 fallbackValue = map().get(previous);
            if(fallbackValue == null) {
                throw new NoSuchElementException("missing value for previous year: " + year);
            } else {
                return fallbackValue;
            }
        }
    }

    protected Attribute2 getFallback(int year) {
        if(fallbackYear == null) {
            throw new IllegalArgumentException("fallback year not set");
        } else {
            Attribute2 fallbackValue = map().get(fallbackYear);
            if(fallbackValue == null) {
                throw new NoSuchElementException("missing value for fallback year: " + year);
            } else {
                return fallbackValue;
            }
        }
    }

    @Override
    public boolean has(int year) {
        return map().containsKey(year);
    }

    @Override
    public boolean remove(int year) {
        if(has(year)) {
            map.remove(year);
            return true;
        } else {
            return false;
        }
    }
}
