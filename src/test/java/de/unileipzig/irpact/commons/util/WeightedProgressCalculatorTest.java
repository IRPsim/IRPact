package de.unileipzig.irpact.commons.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class WeightedProgressCalculatorTest {

    @Test
    void testSinglePhase() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator();
        assertEquals(1, calculator.numberOfPhases());
    }

    @Test
    void testSinglePhaseGetProgress() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator();
        calculator.setProgress(1, 0.5);
        assertEquals(0.5, calculator.getProgress());
    }

    @Test
    void testMultiPhase() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator(3);
        assertEquals(3, calculator.numberOfPhases());
    }

    @Test
    void testMultiPhaseGetProgress() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator(4);
        calculator.setProgress(1, 0.5);
        assertEquals(0.125, calculator.getProgress());
    }

    @Test
    void testMultiPhaseMultiProgress() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator(4);
        calculator.setProgress(1, 0.5);
        calculator.setProgress(2, 0.5);
        calculator.setProgress(3, 1);
        calculator.setProgress(4, 1);
        assertEquals(0.75, calculator.getProgress());
    }

    @Test
    void testMultiPhaseMultiProgressWithDifferentWeights() {
        WeightedProgressCalculator calculator = new WeightedProgressCalculator(4);
        calculator.setWeight(1, 1);
        calculator.setWeight(2, 2);
        calculator.setWeight(3, 3);
        calculator.setWeight(4, 4);
        calculator.setProgress(1, 0.5);
        calculator.setProgress(2, 0.5);
        calculator.setProgress(3, 1);
        calculator.setProgress(4, 1);
        assertEquals(0.85, calculator.getProgress());
    }
}