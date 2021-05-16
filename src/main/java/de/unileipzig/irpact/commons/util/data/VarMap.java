package de.unileipzig.irpact.commons.util.data;

import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.ChecksumValue;
import de.unileipzig.irpact.commons.util.MapSupplier;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public final class VarMap implements ChecksumComparable {

    protected static final Object[] COUNT_DUMMY = new Object[0];

    @ChecksumValue
    protected final Class<?>[] PARAMETERS;
    @ChecksumValue
    protected final SubMap FIRST;
    @ChecksumValue
    protected boolean allowNull = true;

    public VarMap(int length) {
        this(createObjectArray(length));
    }

    public VarMap(Class<?>... parameters) {
        this(MapSupplier.LINKED, parameters);
    }

    public VarMap(MapSupplier mapSupplier, Class<?>... parameters) {
        if(parameters == null || parameters.length < 1) {
            throw new IllegalArgumentException("min 1 type");
        }
        PARAMETERS = parameters;
        FIRST = new SubMap(0, PARAMETERS.length == 1, mapSupplier);
    }

    public VarMap(VarMap other) {
        PARAMETERS = other.getParameters();
        FIRST = new SubMap(other.FIRST);
        allowNull = other.allowNull;
    }

    //=========================
    //validation
    //=========================

    private void validate(Object[] values) {
//        if(values.length != PARAMETERS.length) {
//            throw new IllegalArgumentException("length != " + numberOfParameters());
//        }
//        for(int i = 0; i < values.length; i++) {
//            Class<?> type = PARAMETERS[i];
//            Object value = values[i];
//            if(isAllowNull() && value == null) {
//                continue;
//            }
//            if(!type.isInstance(value)) {
//                throw new IllegalArgumentException(value + " != " + type);
//            }
//        }
        validate(Arrays.asList(values));
    }

    private void validate(Collection<?> values) {
        if(values.size() != PARAMETERS.length) {
            throw new IllegalArgumentException("length != " + numberOfParameters());
        }
        int i = 0;
        for(Object value: values) {
            Class<?> type = PARAMETERS[i];
            if(isAllowNull() && value == null) {
                continue;
            }
            if(!type.isInstance(value)) {
                throw new IllegalArgumentException(value + " != " + type);
            }
            i++;
        }
    }

    private void validateRemove(Object[] values) {
        if(values.length > PARAMETERS.length) {
            throw new IllegalArgumentException("length >= " + numberOfParameters());
        }
    }

    private void validateLength(Object[] values) {
        if(values.length != PARAMETERS.length) {
            throw new IllegalArgumentException("length != " + numberOfParameters());
        }
    }

    private void validateIndex(int index) {
        if(index < 0) {
            throw new IndexOutOfBoundsException("negative index: " + index);
        }
        if(index >= numberOfParameters()) {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    //=========================
    //general
    //=========================

    private static Class<?>[] createObjectArray(int length) {
        if(length < 1) {
            throw new IllegalArgumentException("min 1 type");
        }
        Class<?>[] arr = new Class<?>[length];
        for(int i = 0; i < length; i++) {
            arr[i] = Object.class;
        }
        return arr;
    }

    public VarMap copy() {
        return new VarMap(this);
    }

    public Class<?> getParameter(int index) {
        return PARAMETERS[index];
    }

    public Class<?>[] getParameters() {
        return Arrays.copyOf(PARAMETERS, PARAMETERS.length);
    }

    public void setAllowNull(boolean allowNull) {
        this.allowNull = allowNull;
    }

    public boolean isAllowNull() {
        return allowNull;
    }

    @Override
    public int getChecksum() throws UnsupportedOperationException {
        return Checksums.SMART.getChecksum(
                PARAMETERS,
                FIRST,
                allowNull
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VarMap)) return false;
        VarMap varMap = (VarMap) o;
        return allowNull == varMap.allowNull
                && Arrays.equals(PARAMETERS, varMap.PARAMETERS)
                && Objects.equals(FIRST, varMap.FIRST);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(FIRST, allowNull);
        result = 31 * result + Arrays.hashCode(PARAMETERS);
        return result;
    }

    @Override
    public String toString() {
        return FIRST.toString();
    }

    public int numberOfParameters() {
        return PARAMETERS.length;
    }

    @SuppressWarnings("unchecked")
    public <T> Class<T> getType(int index) {
        return (Class<T>) PARAMETERS[index];
    }

    public boolean contains(Object... values) {
        validate(values);
        SubMap next = FIRST;
        for(int i = 0; i < values.length; i++) {
            Object value = values[i];
            if(i == values.length - 1) {
                //last
                return next.contains(value);
            } else {
                next = next.getNext(value);
                if(next == null) {
                    return false;
                }
            }
        }
        //unreachable
        throw new IllegalStateException();
    }

    public void putArray(Object... values) {
//        validate(values);
//        SubMap next = FIRST;
//        for(int i = 0; i < values.length; i++) {
//            Object value = values[i];
//            if(i == values.length - 1) {
//                //last
//                next.putLast(value);
//            } else {
//                boolean secondToLast = i == values.length - 2;
//                next = next.putNext(value, secondToLast);
//            }
//        }
        putCollection(Arrays.asList(values));
    }

    public void putCollection(Collection<?> values) {
        validate(values);
        SubMap next = FIRST;
        int i = 0;
        for(Object value: values) {
            if(i == values.size() - 1) {
                //last
                next.putLast(value);
            } else {
                boolean secondToLast = i == values.size() - 2;
                next = next.putNext(value, secondToLast);
            }
            i++;
        }
    }

    public boolean remove(Object... values) {
        validateRemove(values);
        SubMap next = FIRST;
        for(int i = 0; i < values.length; i++) {
            Object value = values[i];
            if(i == values.length - 1) {
                //last
                return next.remove(value);
            } else {
                next = next.getNext(value);
                if(next == null) {
                    return false;
                }
            }
        }
        //unreachable
        throw new IllegalStateException();
    }

    public Object[] removeIndex(int index) {
        Object[] value = get(index);
        remove(value);
        return value;
    }

    public int count() {
        return (int) FIRST.streamValues(numberOfParameters(), null, true).count();
    }

    public void clear() {
        FIRST.clear();
    }

    public Object[] get(int index) {
        if(index < 0) {
            throw new IndexOutOfBoundsException("negativ index: " + index);
        }

        Optional<Object[]> result = stream().skip(index).findFirst();
        if(result.isPresent()) {
            return result.get();
        } else {
            throw new IndexOutOfBoundsException("index: " + index);
        }
    }

    public Object get(int index, int arrIndex) {
        Object[] arr = get(index);
        return arr[arrIndex];
    }

    @SuppressWarnings("unchecked")
    public <R> R getAs(int index, int arrIndex) {
        return (R) get(index, arrIndex);
    }

    public <R> R getAs(int index, int arrIndex, Class<R> type) {
        Object value = get(index, arrIndex);
        return type.cast(value);
    }

    //=========================
    //stream
    //=========================

    public Stream<Object[]> stream() {
        return FIRST.streamValues(numberOfParameters(), null, false);
    }

    public Stream<Object[]> filteredStream(Predicate<?>... predicates) {
        validateLength(predicates);
        return FIRST.streamValues(numberOfParameters(), predicates, false);
    }

    public Stream<Object[]> filteredStream(int index, Predicate<?> predicate) {
        Predicate<?>[] predicates = new Predicate<?>[numberOfParameters()];
        predicates[index] = predicate;
        return filteredStream(predicates);
    }

    public Iterator<Object[]> iterator() {
        return stream().iterator();
    }

    @SuppressWarnings("NullableProblems")
    public Iterable<Object[]> iterable() {
        return this::iterator;
    }

    //=========================
    //stream index
    //=========================

    public Stream<Object> indexStream(int index) {
        validateIndex(index);
        return FIRST.streamIndex(index, null);
    }

    public Stream<Object> filteredIndexStream(int index, Predicate<?>... predicates) {
        validateIndex(index);
        validateLength(predicates);
        return FIRST.streamIndex(index, predicates);
    }

    public Stream<Object> filteredIndexStream(int index, Predicate<?> predicate) {
        Predicate<?>[] predicates = new Predicate<?>[numberOfParameters()];
        predicates[index] = predicate;
        return filteredIndexStream(index, predicates);
    }

    public Iterator<Object> iterator(int index) {
        return indexStream(index).iterator();
    }

    public Iterable<Object> iterable(int index) {
        return () -> iterator(index);
    }

    //=========================
    //helper
    //=========================

    /**
     * @author Daniel Abitz
     */
    private static final class SubMap implements ChecksumComparable {

        private final MapSupplier SUPPLIER;
        @ChecksumValue
        private final Map<Object, SubMap> MAP;
        @ChecksumValue
        private final int POS;
        @ChecksumValue
        private final boolean LAST;

        private SubMap(int position, boolean isLast, MapSupplier supplier) {
            SUPPLIER = supplier;
            LAST = isLast;
            POS = position;
            MAP = supplier.newMap();
        }

        private SubMap(SubMap other) {
            SUPPLIER = other.SUPPLIER;
            MAP = SUPPLIER.newMap();
            POS = other.POS;
            LAST = other.LAST;
            for(Map.Entry<Object, SubMap> otherEntry: other.MAP.entrySet()) {
                MAP.put(
                        otherEntry.getKey(),
                        LAST ? null : new SubMap(otherEntry.getValue())
                );
            }
        }

        private boolean isLast() {
            return LAST;
        }

        private void requiresNext() {
            if(LAST) {
                throw new IllegalStateException("last");
            }
        }

        private void requiresLast() {
            if(!LAST) {
                throw new IllegalStateException("not last");
            }
        }

        private void clear() {
            MAP.clear();
        }

        private boolean contains(Object value) {
            return MAP.containsKey(value);
        }

        private SubMap getNext(Object value) {
            return MAP.get(value);
        }

        private SubMap putNext(Object value, boolean secondToLast) {
            requiresNext();
            return MAP.computeIfAbsent(value, _value -> new SubMap(POS + 1, secondToLast, SUPPLIER));
        }

        private void putLast(Object value) {
            requiresLast();
            MAP.put(value, null);
        }

        private boolean remove(Object value) {
            boolean removable = MAP.containsKey(value);
            MAP.remove(value);
            return removable;
        }

        private Stream<Object> streamIndex(
                int index,
                Predicate<?>[] predicates) {
            return flatStream(index, predicates);
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Stream<Object> flatStream(
                int index,
                Predicate[] predicates) {
            if(index == POS) {
                return MAP.keySet().stream()
                        .filter(key -> {
                            if(predicates == null || predicates[POS] == null) {
                                return true;
                            }
                            return predicates[POS].test(key);
                        });
            } else {
                return MAP.entrySet().stream()
                        .filter(entry -> {
                            if(predicates == null || predicates[POS] == null) {
                                return true;
                            }
                            return predicates[POS].test(entry.getKey());
                        })
                        .flatMap(entry -> entry.getValue().flatStream(index, predicates));
            }
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Stream<Object[]> streamValues(
                int length,
                Predicate[] predicates,
                boolean forCount) {
            return MAP.entrySet().stream()
                    .filter(entry -> {
                        if(predicates == null || predicates[POS] == null) {
                            return true;
                        }
                        return predicates[POS].test(entry.getKey());
                    })
                    .flatMap(entry -> updateAndNext(
                            forCount ? COUNT_DUMMY : new Object[length],
                            entry,
                            predicates,
                            forCount)
                    );
        }

        private Stream<Object[]> updateAndNext(
                Object[] result,
                Map.Entry<Object, SubMap> entry,
                Predicate<?>[] predicates,
                boolean forCount) {
            if(!forCount) {
                result[POS] = entry.getKey();
            }
            if(isLast()) {
                Object copy = result;
                if(!forCount) {
                    copy = Arrays.copyOf(result, result.length);
                }
                //shitty varargs
                return Stream.of(copy)
                        .map(obj -> (Object[]) obj);
            } else {
                return entry.getValue().flatStream(result, predicates, forCount);
            }
        }

        @SuppressWarnings({"unchecked", "rawtypes"})
        private Stream<Object[]> flatStream(
                Object[] result,
                Predicate[] predicates,
                boolean forCount) {
            return MAP.entrySet().stream()
                    .filter(entry -> {
                        if(predicates == null || predicates[POS] == null) {
                            return true;
                        }
                        return predicates[POS].test(entry.getKey());
                    })
                    .flatMap(entry -> updateAndNext(result, entry, predicates, forCount));
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SubMap)) return false;
            SubMap subMap = (SubMap) o;
            return POS == subMap.POS
                    && LAST == subMap.LAST
                    && SUPPLIER == subMap.SUPPLIER
                    && Objects.equals(MAP, subMap.MAP);
        }

        @Override
        public int hashCode() {
            return Objects.hash(SUPPLIER, MAP, POS, LAST);
        }

        @Override
        public String toString() {
            return isLast()
                    ? MAP.keySet().toString()
                    : MAP.toString();
        }

        @Override
        public int getChecksum() throws UnsupportedOperationException {
            return Checksums.SMART.getChecksum(
                    MAP,
                    POS,
                    LAST
            );
        }
    }
}
