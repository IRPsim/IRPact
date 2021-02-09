package de.unileipzig.irpact.core.process.ra.npv;

/**
 * @author Daniel Abitz
 */
public interface NPVData {

    //I_(0, t_0)
    double getNettosystempreis(int t0);

    //FIT_(t_0)
    double getEinspeiseverguetung(int t0);

    //SC
    double getEigenverbrauch();

    //RP_(t_0)
    double getStrompreis(int t0);

    //p
    double getStromkostenanstieg();

    //r
    double getZinssatzProzent(int t0);

    //H_solar(N_i)
    double getGlobalstrahlung(double N);

    //V(A_i)
    double getAusrichtungsfaktor(double A);

    //eta_(t_0)
    double getModulwirkungsgradProzent(int t0);

    //D
    double getDegradation();

    //PR_(t_0)
    double getPerformanceRatioProzent(int t0);
}
