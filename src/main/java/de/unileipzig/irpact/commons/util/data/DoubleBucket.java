package de.unileipzig.irpact.commons.util.data;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author Daniel Abitz
 */
public class DoubleBucket implements Bucket<Number> {

    protected final double FROM;
    protected final double TO;

    public DoubleBucket(double from, double to) {
        this.FROM = from;
        this.TO = to;
    }

    public static boolean isNaN(Bucket<? extends Number> bucket) {
        return Double.isNaN(bucket.getTo().doubleValue())
                || Double.isNaN(bucket.getFrom().doubleValue());
    }

    public boolean isNaN() {
        return Double.isNaN(FROM) || Double.isNaN(TO);
    }

    @Override
    public Double getFrom() {
        return getFromAsDouble();
    }

    public double getFromAsDouble() {
        return FROM;
    }

    @Override
    public Double getTo() {
        return getToAsDouble();
    }

    public double getToAsDouble() {
        return TO;
    }

    @Override
    public boolean isInside(Number value) {
        return getFromAsDouble() <= value.doubleValue() && value.doubleValue() < getToAsDouble();
    }

    @Override
    public String print(Function<? super Number, ? extends String> toStringFunction) {
        if(isNaN()) {
            return "NaN";
        } else {
            if(toStringFunction == null) {
                toStringFunction = Objects::toString;
            }
            return "[" + toStringFunction.apply(getFrom()) + ", " + toStringFunction.apply(getTo()) + ")";
        }
    }

    @Override
    public String toString() {
        return print(null);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        DoubleBucket that = (DoubleBucket) o;
        return Double.compare(that.FROM, FROM) == 0 && Double.compare(that.TO, TO) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FROM, TO);
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Bucket<Number> o) {
        if(o instanceof DoubleBucket) {
            return compareToDoubleBucket((DoubleBucket) o);
        } else {
            return compareToNumberBucket(o);
        }
    }

    protected int compareToDoubleBucket(DoubleBucket o) {
        if(isNaN()) {
            if(o.isNaN()) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if(o.isNaN()) {
                return 1;
            } else {
                return Double.compare(getFromAsDouble(), o.getFromAsDouble());
            }
        }
    }

    protected int compareToNumberBucket(Bucket<Number> o) {
        if(isNaN()) {
            if(isNaN(o)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if(isNaN(o)) {
                return 1;
            } else {
                return Double.compare(getFromAsDouble(), o.getFrom().doubleValue());
            }
        }
    }

    public static int compareNumberBuckets(Bucket<Number> o1, Bucket<Number> o2) {
        if(isNaN(o1)) {
            if(isNaN(o2)) {
                return 0;
            } else {
                return -1;
            }
        } else {
            if(isNaN(o2)) {
                return 1;
            } else {
                return Double.compare(o1.getFrom().doubleValue(), o2.getFrom().doubleValue());
            }
        }
    }
}
