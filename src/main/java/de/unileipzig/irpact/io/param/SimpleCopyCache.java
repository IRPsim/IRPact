package de.unileipzig.irpact.io.param;

import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.Copyable;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public final class SimpleCopyCache implements CopyCache {

    protected final Map<Object, Object> CACHE = new HashMap<>();

    public SimpleCopyCache() {
    }

    @Override
    public void clear() {
        CACHE.clear();
    }

    @Override
    public boolean has(Object input) {
        return CACHE.containsKey(input);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(T input) {
        return (T) CACHE.get(input);
    }

    @Override
    public <T> void store(T original, T copy) {
        CACHE.put(original, copy);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends Copyable> T copy(T input) {
        if(input == null) {
            return null;
        }

        Object copy = CACHE.get(input);
        if(copy == null) {
            T copy0 = (T) input.copy(this);
            CACHE.put(input, copy0);
            copy = copy0;
        }
        return (T) copy;
    }
}
