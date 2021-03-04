package de.unileipzig.irpact.io.spec;

import de.unileipzig.irpact.commons.MultiCounter;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InRoot;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class SpecificationCache {

    protected final Map<String, Object> CACHE = new HashMap<>();
    protected final MultiCounter counter = new MultiCounter();

    public SpecificationCache() {
    }

    public MultiCounter getCounter() {
        return counter;
    }

    public int getAndInc(String key) {
        return getCounter().getAndInc(key);
    }

    public void setRoot(InRoot root) {
        CACHE.put("_ROOT_", root);
    }

    public InRoot getRoot() {
        return (InRoot) CACHE.get("_ROOT_");
    }

    public InAttributeName getAttrName(String name) {
        if(has(name)) {
            return getAs(name);
        } else {
            InAttributeName attrName = new InAttributeName(name);
            securePut(name, attrName);
            return attrName;
        }
    }

    public boolean has(String key) {
        return CACHE.containsKey(key);
    }

    public void securePut(String key, Object value) {
        if(has(key)) {
            throw new IllegalArgumentException("key '" + key + "' already exists");
        } else {
            CACHE.put(key, value);
        }
    }

    public Object remove(String key) {
        return CACHE.remove(key);
    }

    public Object get(String key) {
        return CACHE.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAs(String key) {
        return (T) get(key);
    }
}
