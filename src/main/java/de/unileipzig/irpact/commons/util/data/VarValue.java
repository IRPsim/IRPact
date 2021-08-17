package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.ChecksumValue;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
public final class VarValue implements ChecksumComparable {

    @ChecksumValue
    protected final Object[] VALUES;

    public VarValue(Object... values) {
        VALUES = Objects.requireNonNull(values);
    }

    public static VarValue wrap(Object... values) {
        return new VarValue(values);
    }

    //=========================
    //helper
    //=========================

    public static <T> Predicate<VarValue> buildFilter(int index, Predicate<? super T> filter) {
        return varValue -> varValue.test(index, filter);
    }

    public static Predicate<VarValue> buildFilter(Predicate<?>... filters) {
        return varValue -> varValue.test(filters);
    }

    public static Predicate<VarValue> buildFilter(Collection<? extends Predicate<?>> filters) {
        return varValue -> varValue.test(filters);
    }

    //=========================
    //general
    //=========================

    public int numberOfValues() {
        return VALUES.length;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(int index) {
        return (T) VALUES[index];
    }

    public void set(int index, Object value) {
        VALUES[index] = value;
    }

    public <R> R getAndSet(int index, Object newValue) {
        R oldValue = get(index);
        set(index, newValue);
        return oldValue;
    }

    public <T> boolean test(int index, Predicate<? super T> filter) {
        T value = get(index);
        return filter.test(value);
    }

    public boolean test(Predicate<?>... filters) {
        return test(Arrays.asList(filters));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean test(Collection<? extends Predicate<?>> filters) {
        int i = 0;
        for(Predicate filter: filters) {
            Object value = get(i);
            if(!filter.test(value)) {
                return false;
            }
            i++;
        }
        return true;
    }

    public Object[] toArray() {
        return Arrays.copyOf(VALUES, VALUES.length);
    }

    @Override
    public String toString() {
        return Arrays.toString(VALUES);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof VarValue)) return false;
        VarValue varValue = (VarValue) o;
        return Arrays.equals(VALUES, varValue.VALUES);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(VALUES);
    }

    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(VALUES);
    }
}
