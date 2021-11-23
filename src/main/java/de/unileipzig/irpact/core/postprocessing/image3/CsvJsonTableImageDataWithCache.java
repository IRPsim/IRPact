package de.unileipzig.irpact.core.postprocessing.image3;

import de.unileipzig.irpact.commons.util.io3.JsonTableData3;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CsvJsonTableImageDataWithCache extends CsvJsonTableImageData {

    protected final Map<Object, Object> cache = new HashMap<>();

    public CsvJsonTableImageDataWithCache(JsonTableData3 data, String delimiter) {
        super(data, delimiter);
    }

    public void putInCache(Object key, Object value) {
        cache.put(key, value);
    }

    public Object getFromCache(Object key) {
        return cache.get(key);
    }

    public <R> R getFromCacheAs(Object key, Class<R> c) {
        return c.cast(getFromCache(key));
    }

    @SuppressWarnings("unchecked")
    public <R> R getFromCacheAuto(Object key) {
        return (R) getFromCache(key);
    }

    public void clearCache() {
        cache.clear();
    }
}
