package de.unileipzig.irpact.experimental.eval;

/**
 * @author Daniel Abitz
 */
public class InvPolFunc extends AbstractBoundedEval {

    protected double[] a;

    public InvPolFunc(int degree) {
        a = new double[degree + 1];
        a[degree - 1] = 1.0;
    }

    public int degree() {
        return a.length - 1;
    }

    public void setValue(int k, double value) {
        a[k] = value;
    }

    public double getValue(int k) {
        return a[k];
    }

    @Override
    public double calculate(double input) {
        double x = calculateIntern(input);
        double y = 0.0;
        for(int i = 0; i < a.length; i++) {
            if(a[i] != 0.0) {
                double temp = Math.pow(x, i);
                if(temp == 0.0) {
                    throw new ArithmeticException("divide by zero");
                }
                y += a[i] * (1.0 / temp);
            }
        }
        return y;
    }
}
