package de.unileipzig.irpact.start.hardcodeddemo;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class AdaptionAgentGroup {

    private String name;
    private int[] years;
    private double[] adaptionRate;
    private Random random;

    public AdaptionAgentGroup(String name, int[] years, double[] adaptionRate, Random random) {
        this.name = name;
        this.years = years;
        this.adaptionRate = adaptionRate;
        this.random = random;
    }

    protected int productId = 0;
    protected synchronized String deriveName() {
        String name = this.name + "#" + productId;
        productId++;
        return name;
    }

    public AdaptionAgentData deriveData() {
        AdaptionAgentData data = new AdaptionAgentData();
        data.setYears(years);
        data.setAdaptionRate(adaptionRate);
        data.setName(deriveName());
        data.setRandom(new Random(random.nextLong()));
        return data;
    }
}
