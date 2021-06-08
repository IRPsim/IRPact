package de.unileipzig.irpact.core.util.result.adoptions;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public final class AdoptionResultInfo {

    static final Supplier<AdoptionResultInfo> ZERO = () -> new AdoptionResultInfo(0, 0);
    static final AdoptionResultInfo ONE = new AdoptionResultInfo(1, 1);
    static BinaryOperator<AdoptionResultInfo> ADD_CUMULATIVE = (a1, a2) -> {
        if(a1 == ONE) {
            a2.incCumulative();
            return a2;
        }
        if(a2 == ONE) {
            a1.incCumulative();
            return a1;
        }
        throw new IllegalStateException("requires ONE instance");
    };
    static BinaryOperator<AdoptionResultInfo> ADD_BOTH = (a1, a2) -> {
        if(a1 == ONE) {
            a2.inc();
            a2.incCumulative();
            return a2;
        }
        if(a2 == ONE) {
            a1.inc();
            a1.incCumulative();
            return a1;
        }
        throw new IllegalStateException("requires ONE instance");
    };

    protected int value;
    protected int cumulativeValue;

    public AdoptionResultInfo(int value, int cumulativeValue) {
        this.value = value;
        this.cumulativeValue = cumulativeValue;
    }

    private void validate() {
        if(this == ONE) {
            throw new IllegalStateException("dummy instance");
        }
    }

    private void inc() {
        validate();
        value++;
    }

    private void incCumulative() {
        validate();
        cumulativeValue++;
    }

    public int getValue() {
        validate();
        return value;
    }

    public String printValue() {
        return Integer.toString(getValue());
    }

    public int getCumulativeValue() {
        validate();
        return cumulativeValue;
    }

    public String printCumulativeValue() {
        return Integer.toString(getCumulativeValue());
    }

    @Override
    public String toString() {
        return "AdoptionResultInfo{" +
                "value=" + value +
                ", cumulativeValue=" + cumulativeValue +
                '}';
    }
}
