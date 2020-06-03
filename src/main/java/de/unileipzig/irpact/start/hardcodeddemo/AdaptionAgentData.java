package de.unileipzig.irpact.start.hardcodeddemo;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
public class AdaptionAgentData {

    public AdaptionAgentData() {
    }

    protected Random random;
    public Random getRandom() {
        return random;
    }
    public void setRandom(Random random) {
        this.random = random;
    }

    protected String name;
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    protected int[] years;
    public int[] getYears() {
        return years;
    }
    public void setYears(int[] years) {
        this.years = years;
    }

    protected double[] adaptionRate;
    public double[] getAdaptionRate() {
        return adaptionRate;
    }
    public void setAdaptionRate(double[] adaptionRate) {
        this.adaptionRate = adaptionRate;
    }

    protected Product[] products;
    public void setProducts(Product[] products) {
        this.products = products;
    }
    public Product[] getProducts() {
        return products;
    }
}
