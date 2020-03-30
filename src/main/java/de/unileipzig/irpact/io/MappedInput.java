package de.unileipzig.irpact.io;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class MappedInput {

    private Map<String, String> param = new LinkedHashMap<>();
    private String prefix;
    private String name;
    private List<String> deps = new ArrayList<>();

    public MappedInput() {
    }

    public void putParams(Map<? extends String, ? extends String> param) {
        this.param.putAll(param);
    }

    public void putParam(String key, String value) {
        param.put(key, value);
    }

    public Map<String, String> getParam() {
        return param;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addDep(String dep) {
        deps.add(dep);
    }

    public List<String> getDeps() {
        return deps;
    }

    public String print() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry: param.entrySet()) {
            sb.append(Constants.GAMS_PARAM).append(entry.getKey()).append(":");
            if(entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(" ").append(entry.getValue());
            }
            sb.append('\n');
        }
        sb.append(prefix).append(" ").append(name);
        sb.append("(");
        if(deps.isEmpty()) {
            sb.append("*");
        } else {
            for(int i = 0; i < deps.size(); i++) {
                if(i > 0) {
                    sb.append(",");
                }
                sb.append(deps.get(i));
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
