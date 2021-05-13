package de.unileipzig.irpact.commons.util;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class MapResourceBundle extends ResourceBundle {

    protected Map<String, String> properties;

    public MapResourceBundle() {
        this(new HashMap<>());
    }

    public MapResourceBundle(Map<String, String> properties) {
        this.properties = properties;
    }

    public Map<String, String> getMap() {
        return properties;
    }

    public String put(String key, String value) {
        return properties.put(key, value);
    }

    @Override
    protected Object handleGetObject(String key) {
        if(key == null) {
            throw new NullPointerException();
        }
        return properties.get(key);
    }

    @Override
    public Enumeration<String> getKeys() {
        Set<String> keys = properties.keySet();
        return Collections.enumeration(keys);
    }
}
