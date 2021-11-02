package de.unileipzig.irpact.commons.util.data;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Daniel Abitz
 */
class DoubleBucketFactoryTest {

    @Test
    void testCreation() {
        DoubleBucketFactory fac = new DoubleBucketFactory(0.1);
        DoubleBucket b0 = fac.createBucket(0.15);
        assertEquals(0.1, b0.getFromAsDouble());
        assertEquals(0.2, b0.getToAsDouble());
    }

    @Test
    void testSameBucketWitchCache() {
        DoubleBucketFactory fac = new DoubleBucketFactory(0.1);
        DoubleBucket b0 = fac.createBucket(0.1);
        DoubleBucket b1 = fac.createBucket(0.15);
        assertSame(b0, b1);
    }

    @Test
    void testSameBucketWithoutCache() {
        DoubleBucketFactory fac = new DoubleBucketFactory(0.1, false);
        DoubleBucket b0 = fac.createBucket(0.1);
        DoubleBucket b1 = fac.createBucket(0.15);
        assertNotSame(b0, b1);
        assertEquals(b0, b1);
    }
}