package de.unileipzig.irpact.util.sensitivities;

/**
 * @author Daniel Abitz
 */
public enum UpdateMode {
    ADDITIVE {

        @Override
        public double apply(double value, double modifier) {
            return value + modifier;
        }
    },
    MULTIPLICATIVE {

        @Override
        public double apply(double value, double modifier) {
            return value * modifier;
        }
    };

    public double[] split(double start, double end, int count) {
        if(count < 2) {
            throw new IllegalArgumentException("count < 2 (" + count + ")");
        }
        double delta = end - start;
        double stepSize = delta / (count - 1); //3: 0,1000 -> 0, 500, 1000 -> 500er schritte
        double[] steps = new double[count];
        for(int i = 0; i < count; i++) {
            steps[i] = start + i * stepSize;
        }
        return steps;
    }

    public double get(double start, double end, int count, int step) {
        if(count < 2) {
            throw new IllegalArgumentException("count < 2 (" + count + ")");
        }
        double delta = end - start;
        double stepSize = delta / (count - 1); //3: 0,1000 -> 0, 500, 1000 -> 500er schritte
        return start + step * stepSize;
    }

    public abstract double apply(double value, double modifier);
}
