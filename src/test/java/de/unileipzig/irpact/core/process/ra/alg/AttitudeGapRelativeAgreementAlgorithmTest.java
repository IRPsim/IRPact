package de.unileipzig.irpact.core.process.ra.alg;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.attribute.BasicConsumerAgentDoubleAttribute;
import de.unileipzig.irpact.core.process.ra.uncert.BasicUncertainty;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class AttitudeGapRelativeAgreementAlgorithmTest {

    @Test
    void testWithSmallGab() {
        BasicRelativeAgreementAlgorithm alg = new BasicRelativeAgreementAlgorithm();
        alg.setDisableLogging(true);
        alg.setCurrentYearFallback(2015);


        BasicConsumerAgentDoubleAttribute o1 = new BasicConsumerAgentDoubleAttribute("o1", 1);
        BasicUncertainty u1 = new BasicUncertainty("u1", 0.7, 0.5);
        BasicConsumerAgentDoubleAttribute o2 = new BasicConsumerAgentDoubleAttribute("o2", 1.5);
        BasicUncertainty u2 = new BasicUncertainty("u2", 0.8, 0.5);

        alg.apply(
                "a1", o1, u1,
                "a2", o2, u2
        );

        assertEquals(1.0625, o1.getDoubleValue());
        assertEquals(0.712, u1.getUncertainty(), 0.001);
        assertEquals(1.392, o2.getDoubleValue(), 0.001);
        assertEquals(0.778, u2.getUncertainty(), 0.001);
    }
}