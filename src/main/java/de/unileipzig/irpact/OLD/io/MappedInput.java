package de.unileipzig.irpact.OLD.io;

import java.util.*;

/**
 * Aufbereitet Definitionsklasse bzw. deren Felder, damit diese in ein entsprechendes
 * Format uebertragen werden koennen.
 *
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

    private boolean isNoScalar() {
        return !Constants.PREFIX_SCALAR.equals(prefix);
    }

    public String printGAMS() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry: param.entrySet()) {
            sb.append(Constants.GAMS_PARAM).append(entry.getKey()).append(":");
            if(entry.getValue() != null && !entry.getValue().isEmpty()) {
                sb.append(" ").append(entry.getValue());
            }
            sb.append('\n');
        }
        sb.append(prefix).append(" ").append(name);
        if(isNoScalar()) {
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
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "MappedInput{" +
                "param=" + param +
                ", prefix='" + prefix + '\'' +
                ", name='" + name + '\'' +
                ", deps=" + deps +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(param, prefix, name, deps);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof MappedInput) {
            MappedInput other = (MappedInput) obj;
            return Objects.equals(param, other.param) &&
                    Objects.equals(prefix, other.prefix) &&
                    Objects.equals(name, other.name) &&
                    Objects.equals(deps, other.deps);
        }
        return false;
    }
}
