package de.unileipzig.irpact.core.process.ra.npv;

/**
 * @author Daniel Abitz
 */
public class NPVMatrix {

    protected int Nmax;
    protected int Amax;
    protected double[][] matrix;

    public NPVMatrix() {
        this(91, 91);
    }

    public NPVMatrix(int Nmax, int Amax) {
        this.Nmax = Nmax;
        this.Amax = Amax;
        matrix = new double[Nmax][Amax];
    }

    public void calculate(NPVCalculator calculator, int t0) {
        for(int N = 0; N < Nmax; N++) {
            for(int A = 0; A < Amax; A++) {
                double npv = calculator.NPV(t0, N, A);
                matrix[N][A] = npv;
            }
        }
    }

    public int getNmax() {
        return Nmax;
    }

    public int getAmax() {
        return Amax;
    }

    public double getValue(int N, int A) {
        return matrix[N][A];
    }

    public double sumValues() {
        double sum = 0.0;
        for(int N = 0; N < Nmax; N++) {
            for(int A = 0; A < Amax; A++) {
                sum += getValue(N, A);
            }
        }
        return sum;
    }

    public double averageValue() {
        double sum = sumValues();
        return sum / (Nmax * Amax);
    }
}
