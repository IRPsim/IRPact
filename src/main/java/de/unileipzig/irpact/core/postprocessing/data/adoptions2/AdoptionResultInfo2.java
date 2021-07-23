package de.unileipzig.irpact.core.postprocessing.data.adoptions2;

import java.util.function.BinaryOperator;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public final class AdoptionResultInfo2 {

    public static final Supplier<AdoptionResultInfo2> ZERO = () -> new AdoptionResultInfo2(0, 0);
    public static final AdoptionResultInfo2 ONE = new AdoptionResultInfo2(1, 1);
    public static BinaryOperator<AdoptionResultInfo2> INC_CUMULATIVE = (a1, a2) -> {
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
    public static BinaryOperator<AdoptionResultInfo2> INC_CURRENT_AND_CUMULATIVE = (a1, a2) -> {
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
    public static BinaryOperator<AdoptionResultInfo2> getAdder(boolean incCurrentAndCumulative) {
        return incCurrentAndCumulative ? INC_CURRENT_AND_CUMULATIVE : INC_CUMULATIVE;
    }

    protected int value;
    protected int cumulativeValue;

    public AdoptionResultInfo2(int value, int cumulativeValue) {
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
