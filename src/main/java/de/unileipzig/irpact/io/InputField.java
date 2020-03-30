package de.unileipzig.irpact.io;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class InputField {

    private Map<String, String> paramMap = new LinkedHashMap<>();
    private String type;
    private String name;
    private boolean isArray;

    public InputField() {
    }

    public void putParam(String key, String value) {
        paramMap.put(key, value);
    }

    public Map<String, String> getParamMap() {
        return paramMap;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public boolean isArray() {
        return isArray;
    }

    @Override
    public String toString() {
        return getParamMap() + "|" + getType() + "|" + getName() + "|" + isArray();
    }
}
