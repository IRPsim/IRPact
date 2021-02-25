package de.unileipzig.irpact.commons;

import java.util.Comparator;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class WeightedDouble {

    private static final Comparator<WeightedDouble> ASC_WEIGHT_COMPARATOR = Comparator.comparingDouble(WeightedDouble::getWeight);
    public static Comparator<WeightedDouble> getAscendingWeightComparator() {
        return ASC_WEIGHT_COMPARATOR;
    }

    private static final Comparator<WeightedDouble> DESC_WEIGHT_COMPARATOR = ASC_WEIGHT_COMPARATOR.reversed();
    public static Comparator<WeightedDouble> getDescendingWeightComparator() {
        return DESC_WEIGHT_COMPARATOR;
    }

    private final double value;
    private final double weight;

    public WeightedDouble(double value, double weight) {
        this.value = value;
        this.weight = weight;
    }

    public double getValue() {
        return value;
    }

    public double getWeight() {
        return weight;
    }

    public WeightedDouble norm(double totalWeight) {
        return new WeightedDouble(value, weight / totalWeight);
    }

    @Override
    public String toString() {
        return "(" + value + " = " + weight + ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightedDouble)) return false;
        WeightedDouble that = (WeightedDouble) o;
        return Double.compare(that.value, value) == 0 && Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, weight);
    }
}
