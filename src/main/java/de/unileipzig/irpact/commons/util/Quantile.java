package de.unileipzig.irpact.commons.util;

import java.util.*;
import java.util.function.ToDoubleFunction;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public class Quantile<T> {

    protected final Comparator<T> FUNC_COMP = this::doCompare;
    protected ToDoubleFunction<T> func;
    protected List<T> sorted;

    public Quantile(ToDoubleFunction<T> func) {
        this.func = func;
    }

    protected final int doCompare(T t1, T t2) {
        return Double.compare(func.applyAsDouble(t1), func.applyAsDouble(t2));
    }

    protected final double toDouble(T t) {
        return func.applyAsDouble(t);
    }

    public void clear() {
        sorted.clear();
    }

    public int size() {
        return sorted.size();
    }

    @SafeVarargs
    public final void set(T... data) {
        set(Arrays.asList(data));
    }

    public void set(Collection<? extends T> data) {
        if(sorted == null) {
            sorted = new ArrayList<>(data);
        } else {
            sorted.clear();
            sorted.addAll(data);
        }
        sorted.sort(FUNC_COMP);
    }

    protected void checkNotEmpty() {
        if(sorted == null || sorted.isEmpty()) {
            throw new IllegalStateException("no data");
        }
    }

    public double calculate(double p) {
        return calculate(sorted, func, p);
    }

    public static <T> double calculate(
            List<T> sortedList,
            ToDoubleFunction<T> func,
            double p) {
        if(sortedList.isEmpty()) {
            throw new IllegalArgumentException("list is empty");
        }

        if(p <= 0 || p >= 1) {
            throw new IllegalArgumentException("p not in (0,1): " + p);
        }

        double np = sortedList.size() * p;
        if(Math.floor(np) == np) {
            int i = (int) np - 1;
            T v1 = sortedList.get(i);
            T v2 = sortedList.get(i + 1);
            return 0.5 * (func.applyAsDouble(v1) + func.applyAsDouble(v2));
        } else {
            int i = (int) Math.floor(np + 1) - 1;
            T v = sortedList.get(i);
            return func.applyAsDouble(v);
        }
    }

    public double average(double ifMissing) {
        OptionalDouble avg = stream()
                .mapToDouble(this::toDouble)
                .average();
        if(avg.isPresent()) {
            return avg.getAsDouble();
        } else {
            return ifMissing;
        }
    }

    public double average(double lower, double upper, double ifMissing) {
        OptionalDouble avg = streamRange(lower, upper)
                .mapToDouble(this::toDouble)
                .average();
        if(avg.isPresent()) {
            return avg.getAsDouble();
        } else {
            return ifMissing;
        }
    }

    public Stream<T> stream() {
        return sorted.stream();
    }

    public Stream<T> streamLess(double p) {
        final double q = calculate(p);
        return stream()
                .filter(t -> {
                    double v = toDouble(t);
                    return v < q;
                });
    }

    public Stream<T> streamGreaterEquals(double p) {
        final double q = calculate(p);
        return stream()
                .filter(t -> {
                    double v = toDouble(t);
                    return v >= q;
                });
    }

    public Stream<T> streamRange(double lower, double upper) {
        if(lower == 0) {
            if(upper == 1) {
                return stream();
            } else {
                return streamLess(upper);
            }
        } else {
            if(upper == 1) {
                return streamGreaterEquals(lower);
            } else {
                final double lowerQ = calculate(lower);
                final double upperQ = calculate(upper);
                return stream()
                        .filter(t -> {
                            double v = toDouble(t);
                            return v >= lowerQ && v < upperQ;
                        });
            }
        }
    }

    public long count(double lower, double upper) {
        return streamRange(lower, upper).count();
    }
}
