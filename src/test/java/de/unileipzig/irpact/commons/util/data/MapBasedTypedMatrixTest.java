package de.unileipzig.irpact.commons.util.data;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class MapBasedTypedMatrixTest {

    @Test
    void testGetM() {
        MapBasedTypedMatrix<String, String, Double> matrix = new MapBasedTypedMatrix<>();
        matrix.init(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("x", "y", "z"),
                0.0
        );
        assertEquals(
                Arrays.asList("a", "b", "c"),
                new ArrayList<>(matrix.getM())
        );
    }

    @Test
    void testGetN() {
        MapBasedTypedMatrix<String, String, Double> matrix = new MapBasedTypedMatrix<>();
        matrix.init(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("x", "y", "z"),
                0.0
        );
        assertEquals(
                Arrays.asList("x", "y", "z"),
                new ArrayList<>(matrix.getN())
        );
    }

    @Test
    void testSetGet() {
        MapBasedTypedMatrix<String, String, Double> matrix = new MapBasedTypedMatrix<>();
        matrix.init(
                Arrays.asList("a", "b", "c"),
                Arrays.asList("x", "y", "z"),
                0.0
        );
        assertEquals(0.0, matrix.get("a", "x"));
        assertEquals(0.0, matrix.set("a", "x", 1.0));
        assertEquals(1.0, matrix.get("a", "x"));
    }
}