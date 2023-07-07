package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.commons.Nameable;

/**
 * @author Daniel Abitz
 */
public interface RelativeAgreementAlgorithm2 extends Nameable {

    int INFLUENCE_LENGTH = 4;
    int INDEX_XI = 0;
    int INDEX_UI = 1;
    int INDEX_XJ = 2;
    int INDEX_UJ = 3;

    default double[] calculateInfluence(double xi, double ui, double xj, double uj) {
        double[] influence = new double[INFLUENCE_LENGTH];
        calculateInfluence(xi, ui, xj, uj, influence);
        return influence;
    }

    boolean calculateInfluence(double xi, double ui, double xj, double uj, double[] influence);
}
