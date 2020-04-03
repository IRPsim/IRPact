package de.unileipzig.irpact.io;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Beschreibt ein Feld in einer Definitionsklasse.
 *
 * @author Daniel Abitz
 */
public final class InputField {

    private Map<String, String> paramMap;
    private String type;
    private String name;
    private boolean array;
    private boolean global;

    public InputField() {
        this(new LinkedHashMap<>());
    }

    public InputField(Map<String, String> paramMap) {
        this.paramMap = paramMap;
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

    /**
     * Gibt den Typ, also den Klassennamen, zurueck.
     *
     * @return Klassenname
     */
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Feldnamen zurueck.
     *
     * @return Name des Feldes
     */
    public String getName() {
        return name;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public boolean isArray() {
        return array;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    boolean isNotName() {
        return !Constants.NAME.equals(name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramMap, type, name, array, global);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof InputField) {
            InputField other = (InputField) obj;
            return array == other.array
                    && global == other.global
                    && Objects.equals(type, other.type)
                    && Objects.equals(name, other.name)
                    && Objects.equals(paramMap, other.paramMap);
        }
        return false;
    }

    @Override
    public String toString() {
        return "InputField{" +
                "paramMap=" + paramMap +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", isArray=" + array +
                ", global=" + global +
                '}';
    }
}
