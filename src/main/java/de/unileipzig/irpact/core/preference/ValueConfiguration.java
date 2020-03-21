package de.unileipzig.irpact.core.preference;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public class ValueConfiguration<T, M extends ValueMapping<T>> {

    private Map<String, Value> values;
    private Set<M> valueMappings;

    public ValueConfiguration(
            Map<String, Value> values,
            Set<M> valueMappings) {
        this.values = values;
        this.valueMappings = valueMappings;
    }

    public boolean hasValue(String key) {
        return values.containsKey(key);
    }

    public Value getValue(String key) {
        return values.get(key);
    }

    public boolean add(String key, Value value) {
        if(hasValue(key)) {
            return false;
        }
        values.put(key, value);
        return true;
    }

    public Collection<? extends Value> getValues() {
        return values.values();
    }

    public Collection<? extends M> getValueMappings() {
        return valueMappings;
    }

    public Set<? extends M> getCorresponingMappings(Value value, Predicate<? super M> filter) {
        Set<M> set = new HashSet<>();
        for(M mapping: valueMappings) {
            Value mappingValue = mapping.getValue();
            if(value.equals(mappingValue)) {
                if(filter != null && filter.test(mapping)) {
                    set.add(mapping);
                }
            }
        }
        return set;
    }

    public Set<? extends M> getCorresponingMappings(Preference preference, Predicate<? super M> filter) {
        return getCorresponingMappings(preference.getValue(), filter);
    }

    public Set<T> getCorresponingObjects(Value value, Predicate<? super M> filter) {
        Set<T> set = new HashSet<>();
        for(M mapping: valueMappings) {
            Value mappingValue = mapping.getValue();
            if(value.equals(mappingValue)) {
                if(filter != null && filter.test(mapping)) {
                    set.add(mapping.getObject());
                }
            }
        }
        return set;
    }

    public Set<T> getCorresponingObjects(Preference preference, Predicate<? super M> filter) {
        return getCorresponingObjects(preference.getValue(), filter);
    }
}
