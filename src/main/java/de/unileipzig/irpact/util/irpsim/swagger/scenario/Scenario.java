package de.unileipzig.irpact.util.irpsim.swagger.scenario;

/**
 * @author Daniel Abitz
 */
public class Scenario {

    protected ScenarioMetaData metaData;
    protected ScenarioData data;

    public Scenario(ScenarioMetaData metaData, ScenarioData data) {
        this.metaData = metaData;
        this.data = data;
    }

    public ScenarioMetaData getMetaData() {
        return metaData;
    }

    public ScenarioData getData() {
        return data;
    }
}
