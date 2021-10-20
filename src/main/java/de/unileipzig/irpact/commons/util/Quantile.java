package de.unileipzig.irpact.commons.util;

import java.util.*;
import java.util.function.ToDoubleFunction;

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
        checkNotEmpty();

        if(p <= 0 || p >= 1) {
            throw new IllegalArgumentException("p=" + p);
        }

        double np = sorted.size() * p;
        if(Math.floor(np) == np) {
            int i = (int) np - 1;
            T v1 = sorted.get(i);
            T v2 = sorted.get(i + 1);
            return 0.5 * (func.applyAsDouble(v1) + func.applyAsDouble(v2));
        } else {
            int i = (int) Math.floor(np + 1) - 1;
            T v = sorted.get(i);
            return func.applyAsDouble(v);
        }
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public double average() {
        checkNotEmpty();
        return sorted.stream()
                .mapToDouble(this::toDouble)
                .average()
                .getAsDouble();
    }
}
