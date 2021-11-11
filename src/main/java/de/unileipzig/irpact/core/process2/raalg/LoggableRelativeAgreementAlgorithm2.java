package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public interface LoggableRelativeAgreementAlgorithm2 extends RelativeAgreementAlgorithm2 {

    void initialize(SimulationEnvironment environment) throws Throwable;

    @Override
    default boolean calculateInfluence(double xi, double ui, double xj, double uj, double[] influence) {
        return calculateInfluence(
                null, xi, ui,
                null, xj, uj,
                null,
                influence,
                null
        );
    }

    @Override
    default double[] calculateInfluence(double xi, double ui, double xj, double uj) {
        return calculateInfluence(
                null, xi, ui,
                null, xj, uj,
                null,
                null
        );
    }

    default double[] calculateInfluence(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            Timestamp time) {
        double[] influence = new double[INFLUENCE_LENGTH];
        calculateInfluence(ni, xi, ui, nj, xj, uj, attr, influence, time);
        return influence;
    }

    boolean calculateInfluence(
            String ni, double xi, double ui,
            String nj, double xj, double uj,
            String attr,
            double[] influence,
            Timestamp time);
}
