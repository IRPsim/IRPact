package de.unileipzig.irpact.commons.util.xlsx;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class KeyValueXlsxTable {

    protected Map<String, Double> data;

    public KeyValueXlsxTable() {
        this(new LinkedHashMap<>());
    }

    public KeyValueXlsxTable(Map<String, Double> data) {
        this.data = data;
    }

    public boolean has(String key) {
        return data.containsKey(key);
    }

    public void store(String key, double value) {
        data.put(key, value);
    }

    public double get(String key) {
        return data.get(key);
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, Double> entry: data.entrySet()) {
            if(sb.length() > 0) {
                sb.append('\n');
            }
            sb.append(entry.getKey());
            sb.append(" : ");
            sb.append(entry.getValue());
        }
        return sb.toString();
    }
}
