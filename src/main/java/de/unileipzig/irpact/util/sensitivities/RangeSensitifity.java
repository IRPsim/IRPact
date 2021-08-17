package de.unileipzig.irpact.util.sensitivities;

import java.util.function.BiConsumer;

/**
 * @author Daniel Abitz
 */
public class RangeSensitifity<I> implements Sensitifity<I> {

    protected double initialValue;
    protected double startValue;
    protected double endValue;
    protected UpdateMode mode;
    protected BiConsumer<I, Double> consumer;

    public RangeSensitifity(
            double initialValue,
            double startValue,
            double endValue,
            UpdateMode mode,
            BiConsumer<I, Double> consumer) {
        this.initialValue = initialValue;
        this.startValue = startValue;
        this.endValue = endValue;
        this.mode = mode;
        this.consumer = consumer;
    }

    @Override
    public double[] getDeltas(int count) {
        return mode.split(startValue, endValue, count);
    }

    @Override
    public double getDelta(int count, int step) {
        return mode.get(startValue, endValue, count, step);
    }

    @Override
    public void apply(I input, double delta) {
        double newValue = mode.apply(initialValue, delta);
        consumer.accept(input, newValue);
    }
}
