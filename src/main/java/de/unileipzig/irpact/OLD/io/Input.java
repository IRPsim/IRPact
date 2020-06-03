package de.unileipzig.irpact.OLD.io;

import java.util.*;

/**
 * Beschreibt eine Definitionsklasse.
 *
 * @author Daniel Abitz
 */
public final class Input {

    private Map<String, String> paramMap;
    private String type;
    private String name;
    private boolean global;
    private List<String> interfaceList;
    private List<InputField> fields;

    public Input() {
        this(new LinkedHashMap<>(), new ArrayList<>(), new ArrayList<>());
    }

    public Input(Map<String, String> paramMap, List<String> interfaceList, List<InputField> fields) {
        this.paramMap = paramMap;
        this.interfaceList = interfaceList;
        this.fields = fields;
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
     * Gibt den Typ zurueck. Dabei ist nur {@code class} oder {@code interface} moeglich.
     *
     * @return {@code class} oder {@code interface}
     */
    public String getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt den Namen der Klassse bzw. des Interfaces zurueck.
     *
     * @return entsprechende Name
     */
    public String getName() {
        return name;
    }

    public void setGlobal(boolean global) {
        this.global = global;
    }

    public boolean isGlobal() {
        return global;
    }

    public void addInterfaces(String... arr) {
        Collections.addAll(interfaceList, arr);
    }

    public void addInterfaces(Collection<? extends String> coll) {
        interfaceList.addAll(coll);
    }

    public List<String> getInterfaceList() {
        return interfaceList;
    }

    public void addFields(Collection<? extends InputField> coll) {
        fields.addAll(coll);
    }

    public void addField(InputField field) {
        fields.add(field);
    }

    public List<InputField> getFields() {
        return fields;
    }

    public List<MappedInput> toMappedInput() {
        List<MappedInput> list = new ArrayList<>();
        toMappedInput(list);
        return list;
    }

    public void toMappedInput(Collection<MappedInput> coll) {
        if(!isGlobal()) {
            MappedInput set = new MappedInput();
            set.setPrefix(Constants.PREFIX_SET);
            set.setName(Constants.SET + getName());
            set.putParams(getParamMap());
            coll.add(set);
        }
        for(InputField field: getFields()) {
            if(field.isGlobal()) {
                MappedInput par = new MappedInput();
                par.setPrefix(Constants.PREFIX_SCALAR);
                par.setName(Constants.SCA + field.getName());
                par.putParams(field.getParamMap());
                coll.add(par);
            } else if(field.isArray()) {
                MappedInput par = new MappedInput();
                par.setPrefix(Constants.PREFIX_PARAMETER);
                par.setName(Constants.PAR + getName() + Constants.DELIMITER + field.getType());
                par.addDep(Constants.SET + getName());
                par.addDep(Constants.SET + field.getType());
                par.putParams(field.getParamMap());
                par.putParam(Constants.TYPE, Constants.BOOLEAN);
                coll.add(par);
            } else {
                MappedInput par = new MappedInput();
                par.setPrefix(Constants.PREFIX_PARAMETER);
                par.setName(Constants.PAR + getName() + Constants.DELIMITER + field.getName());
                par.addDep(Constants.SET + getName());
                par.putParams(field.getParamMap());
                coll.add(par);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj == this) return true;
        if(obj instanceof Input) {
            Input other = (Input) obj;
            return Objects.equals(paramMap, other.paramMap)
                    && Objects.equals(type, other.type)
                    && Objects.equals(name, other.name)
                    && global == other.global
                    && Objects.equals(interfaceList, other.interfaceList)
                    && Objects.equals(fields, other.fields);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(paramMap, type, name, global, interfaceList, fields);
    }


    @Override
    public String toString() {
        return "Input{" +
                "paramMap=" + paramMap +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", global=" + global +
                ", interfaceList=" + interfaceList +
                ", fields=" + fields +
                '}';
    }
}
