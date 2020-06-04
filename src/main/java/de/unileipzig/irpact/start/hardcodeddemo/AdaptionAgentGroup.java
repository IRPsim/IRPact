package de.unileipzig.irpact.start.hardcodeddemo;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.AgentGroup;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("FieldMayBeFinal")
public class AdaptionAgentGroup {

    private String name;
    private AgentGroup group;
    private int year;
    private Random random;

    public AdaptionAgentGroup(String name, int year, Random random, AgentGroup group) {
        this.name = name;
        this.year = year;
        this.random = random;
        this.group = group;
    }

    protected int productId = 0;
    protected synchronized String deriveName() {
        String name = this.name + "#" + productId;
        productId++;
        return name;
    }

    public AdaptionAgentData deriveData() {
        AdaptionAgentData data = new AdaptionAgentData();
        data.setYear(year);
        data.setAdaptionRate(group.getAdaptionRate());
        data.setName(deriveName());
        data.setRandom(new Random(random.nextLong()));
        return data;
    }

    public AgentGroup getGroup() {
        return group;
    }

    public int getNumberOfAgents() {
        return group.getNumberOfAgents();
    }
}
