package de.unileipzig.irpact.commons.performance;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractPerformanceMetric implements PerformanceMetric {

    static void validate(double[] x1, double[] x2) {
        if(x1 == null) throw new NullPointerException("x1");
        if(x2 == null) throw new NullPointerException("x2");
        if(x1.length != x2.length) throw new IllegalArgumentException("length mismatch");
        if(x1.length == 0) throw new IllegalArgumentException("empty");
    }

    static void validate(double[] x1, double[]... x2) {
        if(x1 == null) throw new NullPointerException("x1");
        if(x2 == null) throw new NullPointerException("x2");
        if(x2.length == 0) throw new IllegalArgumentException("empty");
        for(double[] x2Inner: x2) {
            if(x1.length != x2Inner.length) throw new IllegalArgumentException("length mismatch");
        }
        if(x1.length == 0) throw new IllegalArgumentException("empty");
    }
}
