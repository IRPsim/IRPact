package de.unileipzig.irpact.core.process.ra.npv;

import java.util.Arrays;

/**
 * @author Daniel Abitz
 */
public class NPVMatrix0 implements NPVData0 {

    protected int Nmax;
    protected int Amax;
    protected double[][] matrix;

    public NPVMatrix0() {
        this(91, 91);
    }

    public NPVMatrix0(int Nmax, int Amax) {
        this.Nmax = Nmax;
        this.Amax = Amax;
        matrix = new double[Nmax][Amax];
    }

    public void fill(double value) {
        for(double[] arr: matrix) {
            Arrays.fill(arr, value);
        }
    }

    public int getNmax() {
        return Nmax;
    }

    public int getAmax() {
        return Amax;
    }

    @Override
    public double NPV(int N, int A) {
        return matrix[N][A];
    }

    @Override
    public double totalNPV() {
        double sum = 0.0;
        for(int N = 0; N < Nmax; N++) {
            for(int A = 0; A < Amax; A++) {
                sum += NPV(N, A);
            }
        }
        return sum;
    }

    @Override
    public double averageNPV() {
        double sum = totalNPV();
        return sum / (Nmax * Amax);
    }
}
