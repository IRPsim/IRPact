package de.unileipzig.irpact.core.process2.raalg;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class LoggableAttitudeGapRelativeAgreementAlgorithm2Test {

    @Test
    void testRA() {
        BasicRelativeAgreementAlgorithm2 alg = new BasicRelativeAgreementAlgorithm2();
        alg.setName("RA");
        alg.setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);

        double[] values = new double[4];
        boolean changed = alg.calculateInfluence(
                1.1, 0.7,
                1.5, 0.8,
                values
        );

        assertTrue(changed);
        assertEquals(1.175, values[RelativeAgreementAlgorithm2.INDEX_XI], 0.0001);
        assertEquals(0.7187, values[RelativeAgreementAlgorithm2.INDEX_UI], 0.0001);
        assertEquals(1.3857, values[RelativeAgreementAlgorithm2.INDEX_XJ], 0.0001);
        assertEquals(0.7714, values[RelativeAgreementAlgorithm2.INDEX_UJ], 0.0001);
    }

    @Test
    void testRA2() {
        BasicRelativeAgreementAlgorithm2 alg = new BasicRelativeAgreementAlgorithm2();
        alg.setName("RA");
        alg.setSpeedOfConvergence(0.4);

        double[] values = new double[4];
        boolean changed = alg.calculateInfluence(
                1.1, 0.75,
                2.5, 1.2,
                values
        );

        assertFalse(changed);
        assertEquals(1.1, values[RelativeAgreementAlgorithm2.INDEX_XI], 0.0001);
        assertEquals(0.75, values[RelativeAgreementAlgorithm2.INDEX_UI], 0.0001);
        assertEquals(2.5, values[RelativeAgreementAlgorithm2.INDEX_XJ], 0.0001);
        assertEquals(1.2, values[RelativeAgreementAlgorithm2.INDEX_UJ], 0.0001);
    }
}