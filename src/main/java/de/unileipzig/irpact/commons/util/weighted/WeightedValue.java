package de.unileipzig.irpact.commons.util.weighted;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class WeightedValue<A> {

    private static final Comparator<WeightedValue<?>> ASC_WEIGHT_COMPARATOR = Comparator.comparingDouble(WeightedValue::getWeight);
    @SuppressWarnings("unchecked")
    public static <T> Comparator<WeightedValue<T>> getAscendingWeightComparator() {
        return (Comparator<WeightedValue<T>>) (Object) ASC_WEIGHT_COMPARATOR;
    }

    private static final Comparator<WeightedValue<?>> DESC_WEIGHT_COMPARATOR = ASC_WEIGHT_COMPARATOR.reversed();
    @SuppressWarnings("unchecked")
    public static <T> Comparator<WeightedValue<T>> getDescendingWeightComparator() {
        return (Comparator<WeightedValue<T>>) (Object) DESC_WEIGHT_COMPARATOR;
    }

    private final A value;
    private final double weight;

    public WeightedValue(A value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public A getValue() {
        return value;
    }

    public double getWeight() {
        return weight;
    }

    public WeightedValue<A> normalize(double total) {
        return new WeightedValue<>(value, weight / total);
    }

    @Override
    public String toString() {
        return "(" + value + " = " + weight + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightedValue)) return false;
        WeightedValue<?> that = (WeightedValue<?>) o;
        return Double.compare(that.weight, weight) == 0 && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, weight);
    }
}
