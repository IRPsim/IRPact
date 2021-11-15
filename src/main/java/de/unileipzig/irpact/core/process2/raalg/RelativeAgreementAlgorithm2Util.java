package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.core.process.ra.RAConstants;

/**
 * @author Daniel Abitz
 */
/*
 * http://jasss.soc.surrey.ac.uk/5/4/1.html
 */
public final class RelativeAgreementAlgorithm2Util {

    RelativeAgreementAlgorithm2Util() {
    }

    static void validate(double[] influence) {
        if(influence == null) {
            throw new NullPointerException("influence array is null");
        }
        if(influence.length < 4) {
            throw new IllegalArgumentException("influence array length < 4: " + influence.length);
        }
    }

    public static double gab(double xi, double xj) {
        return Math.abs(xi - xj);
    }

    public static double hij(double xi, double ui, double xj, double uj) {
        return Math.min(xi + ui, xj + uj) - Math.max(xi - ui, xj - uj);
    }

    public static double xj(boolean positive, double xj, double m, double hij, double xi, double ui) {
        double temp = m * ((hij / ui) - 1.0) * (xi - xj);
        return positive
                ? xj + temp
                : xj - temp;
    }

    public static double uj(boolean positive, double uj, double m, double hij, double ui) {
        double temp = m * ((hij / ui) - 1.0) * (ui - uj);
        return positive
                ? uj + temp
                : uj - temp;
    }

    public static boolean ra(boolean positive, double xi, double ui, double xj, double uj, double m, double[] influence, int off) {
        double hij = hij(xi, ui, xj, uj);
        if(hij > ui) {
            influence[off] = xj(positive, xj, m, hij, xi, ui);
            influence[off + 1] = uj(positive, uj, m, hij, ui);
            return true;
        } else {
            influence[off] = xj;
            influence[off + 1] = uj;
            return false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public static boolean mutualRa(boolean positive, double xi, double ui, double xj, double uj, double m, double[] influence, int off) {
        boolean changed = false;
        changed |= ra(positive, xi, ui, xj, uj, m, influence, off + 2); //i->j, [2]=xj, [3]=uj
        changed |= ra(positive, xj, uj, xi, ui, m, influence, off); //j->i, [0]=xi, [1]=ui
        return changed;
    }
}
