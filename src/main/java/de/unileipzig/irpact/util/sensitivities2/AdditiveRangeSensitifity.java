package de.unileipzig.irpact.util.sensitivities2;

/**
 * @author Daniel Abitz
 */
public class AdditiveRangeSensitifity<I> extends RangeSensitifity<I> {

    @Override
    public void apply(I instance, int numberOfSteps, int step) {
        if(numberOfSteps < 2) {
            throw new IllegalArgumentException("number of steps < 2 (" + numberOfSteps + ")");
        }
        double delta = getEndValue() - getStartValue();
        double stepSize = delta / (numberOfSteps - 1);
        double value = getInitialValue() + step * stepSize;
        consumer.accept(instance, value);
    }
}
