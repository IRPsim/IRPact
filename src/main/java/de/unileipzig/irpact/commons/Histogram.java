package de.unileipzig.irpact.commons;

import de.unileipzig.irpact.dev.Experimental;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@Experimental("...")
public class Histogram {

    private List<Bin> bins = new ArrayList<>();

    public Histogram() {
    }

    static void isSorted(double[] data) {
        for(int i = 0; i < data.length - 1; i++) {
            double d0 = data[i];
            double d1 = data[i + 1];
            if(d0 >= d1) {
                throw new IllegalStateException("data not sorted, " + d0 + " >= " + d1 + ", index: " + i);
            }
        }
    }

    public void setBins(double[] limits) {
        isSorted(limits);
        bins.clear();
        double min = limits[0];
        double max;
        for(int i = 1; i < limits.length; i++) {
            max = limits[i];
            Bin bin = new Bin(min, max, 0.0);
            bins.add(bin);
            min = max;
        }
    }

    public int getNumberOfBins() {
        return bins.size();
    }

    public double getMin() {
        return bins.get(0)
                .getMin();
    }

    public double getMax() {
        return bins.get(bins.size() - 1)
                .getMax();
    }

    public Bin getBin(int index) {
        return bins.get(index);
    }

    public Bin getBin(double value) {
        for(Bin bin: bins) {
            if(bin.getMin() <= value && value < bin.getMax()) {
                return bin;
            }
        }
        return null;
    }

    public Bin findBin(double value) {
        Bin bin = getBin(value);
        if(bin == null) {
            throw new NoSuchElementException();
        }
        return bin;
    }

    public void setHeight(double value, double height) {
        findBin(value).setHeight(height);
    }

    public void updateHeight(double value, double delta) {
        Bin bin = findBin(value);
        bin.setHeight(bin.getHeight() + delta);
    }

    /**
     * @author Daniel Abitz
     */
    public static class Bin {

        private double min;
        private double max;
        private double height;

        private Bin(double min, double max, double height) {
            this.min = min;
            this.max = max;
            this.height = height;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }
    }
}
