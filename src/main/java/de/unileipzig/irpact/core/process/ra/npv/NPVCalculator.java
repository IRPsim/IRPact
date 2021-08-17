package de.unileipzig.irpact.core.process.ra.npv;

/**
 * @author Daniel Abitz
 */
public class NPVCalculator {

    protected int t_FIT = 20;
    protected NPVData data;

    public NPVCalculator() {
    }

    public void setData(NPVData data) {
        this.data = data;
    }

    public NPVData getData() {
        return data;
    }

    public double NPV(int t0, double N, double A) {
        double I = data.getNettosystempreis(t0);
        double einnahmen0 = calcEinnahmen(t0, N, A, 0);
        double sum = calcNPVSum(t0, N, A);
        return -I + einnahmen0 + sum;
    }

    public double E(int t0, double N, double A, int t) {
        double H = data.getGlobalstrahlung(N);
        double V = data.getAusrichtungsfaktor(A);
        double eta = data.getModulwirkungsgradProzent(t0);
        double D = data.getDegradation();
        double PR = data.getPerformanceRatioProzent(t0);
        return H * V * eta * Math.pow(1.0 - D, t) * PR;
    }

    public double calcNPVSum(int t0, double N, double A) {
        double FIT0 = data.getEinspeiseverguetung(t0);
        double SC = data.getEigenverbrauch();
        double RP0 = data.getStrompreis(t0);
        double p = data.getStromkostenanstieg();
        double r = data.getZinssatzProzent(t0);
        double sum = 0.0;
        for(int t = 1; t <= t_FIT; t++) {
            double einnahmen = calcEinnahmen(t0, N, A, FIT0, SC, RP0, p, t);
            double zins = Math.pow(1 + r, t);
            sum += einnahmen / zins;
        }
        return sum;
    }

    public double calcEinnahmen(int t0, double N, double A, int t) {
        double FIT0 = data.getEinspeiseverguetung(t0);
        double SC = data.getEigenverbrauch();
        double RP0 = data.getStrompreis(t0);
        double p = data.getStromkostenanstieg();
        return calcEinnahmen(t0, N, A, FIT0, SC, RP0, p, t);
    }

    public double calcEinnahmen(int t0, double N, double A, double FIT0, double SC, double RP0, double p, int t) {
        double Et = E(t0, N, A, t);
        return calcEinnahmen(FIT0, SC, RP0, p, Et, t);
    }

    public double calcEinnahmen(double FIT0, double SC, double RP0, double p, double Et, int t) {
        return (FIT0 * (1.0 - SC) + RP0 * Math.pow(1 + p, t) * SC) * Et;
    }
}
