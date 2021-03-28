package de.unileipzig.irpact.commons.util;

import de.unileipzig.irpact.commons.util.Rnd;

import java.util.function.*;

/**
 * @author Daniel Abitz
 */
public class WeightedRndElementReducer<T> implements BinaryOperator<T> {

    protected ToDoubleFunction<T> weightFunction;
    protected long currentIndex = 0;
    protected double temp = 0.0;
    protected double tempMean = 0.0;
    protected double tempSum = 0.0;
    protected double drawValue;
    protected boolean finished = false;

    public WeightedRndElementReducer(Rnd rnd, ToDoubleFunction<T> weightFunction) {
        this(rnd.nextDouble(), weightFunction);
    }

    public WeightedRndElementReducer(double drawValue, ToDoubleFunction<T> weightFunction) {
        this.weightFunction = weightFunction;
        this.drawValue = drawValue;
    }

    protected void update(double weight) {
        tempSum += weight;
        tempMean = tempMean + ((weight - tempMean) / currentIndex);
        temp = tempSum / tempMean;
    }

    @Override
    public T apply(T t, T t2) {
        if(finished) {
            return t;
        } else {
            if(currentIndex == 0) {
                currentIndex++;
                update(weightFunction.applyAsDouble(t));
                if(drawValue < temp) {
                    finished = true;
                    return t;
                }
            }
            currentIndex++;
            update(weightFunction.applyAsDouble(t));
            if(drawValue < temp) {
                finished = true;
                return t2;
            }
            return null;
        }
    }
}
