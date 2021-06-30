package de.unileipzig.irpact.util.sensitivities2;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public abstract class RangeSensitifity<I> implements Sensitifity<I> {

    protected double initialValue;
    protected double startValue;
    protected double endValue;
    protected BiConsumer<I, Double> consumer;

    public RangeSensitifity() {
    }

    public void setInitialValue(double initialValue) {
        this.initialValue = initialValue;
    }

    public double getInitialValue() {
        return initialValue;
    }

    public void setStartValue(double startValue) {
        this.startValue = startValue;
    }

    public double getStartValue() {
        return startValue;
    }

    public void setEndValue(double endValue) {
        this.endValue = endValue;
    }

    public double getEndValue() {
        return endValue;
    }

    public void setConsumer(BiConsumer<I, Double> consumer) {
        this.consumer = consumer;
    }

    public BiConsumer<I, Double> getConsumer() {
        return consumer;
    }
}
