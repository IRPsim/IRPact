package de.unileipzig.irpact.core.preference;

import de.unileipzig.irpact.commons.annotation.ToDo;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
@ToDo("ueberlegen, ob es besser ist, die values in die productgruppen zu packen")
public class ValueConfiguration<T> {

    private Map<String, Value> values;
    private Set<ValuedObject<T>> valuedObjects;

    public ValueConfiguration(
            Map<String, Value> values,
            Set<ValuedObject<T>> valuedObjects) {
        this.values = values;
        this.valuedObjects = valuedObjects;
    }

    public boolean hasValue(String valueString) {
        return values.containsKey(valueString);
    }

    public Value getValue(String valueString) {
        return values.get(valueString);
    }

    public boolean add(Value value) {
        if(hasValue(value.print())) {
            return false;
        }
        values.put(value.print(), value);
        return true;
    }

    public Collection<? extends Value> getValues() {
        return values.values();
    }

    public Collection<? extends ValuedObject<T>> getValuedObjects() {
        return valuedObjects;
    }

    public Set<? extends ValuedObject<T>> getCorresponingValuedObjects(Value value, Predicate<? super ValuedObject<T>> filter) {
        Set<ValuedObject<T>> set = new HashSet<>();
        for(ValuedObject<T> mapping: valuedObjects) {
            Value mappingValue = mapping.getValue();
            if(value.equals(mappingValue)) {
                if(filter != null && filter.test(mapping)) {
                    set.add(mapping);
                }
            }
        }
        return set;
    }

    public Set<? extends ValuedObject<T>> getCorresponingValuedObjects(Preference preference, Predicate<? super ValuedObject<T>> filter) {
        return getCorresponingValuedObjects(preference.getValue(), filter);
    }

    public Set<T> getCorresponingObjects(Value value, Predicate<? super ValuedObject<T>> filter) {
        Set<T> set = new HashSet<>();
        for(ValuedObject<T> mapping: valuedObjects) {
            Value mappingValue = mapping.getValue();
            if(value.equals(mappingValue)) {
                if(filter != null && filter.test(mapping)) {
                    set.add(mapping.getObject());
                }
            }
        }
        return set;
    }

    public Set<T> getCorresponingObjects(Preference preference, Predicate<? super ValuedObject<T>> filter) {
        return getCorresponingObjects(preference.getValue(), filter);
    }
}
