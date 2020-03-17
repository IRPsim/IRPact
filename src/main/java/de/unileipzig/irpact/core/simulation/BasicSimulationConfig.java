package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;

/**
 * @author Daniel Abitz
 */
public class BasicSimulationConfig implements SimulationConfig {

    protected ConsumerAgentGroupAffinitiesMapping affinitiesMapping;

    public BasicSimulationConfig() {
    }

    public void setAffinitiesMapping(ConsumerAgentGroupAffinitiesMapping affinitiesMapping) {
        this.affinitiesMapping = affinitiesMapping;
    }

    @Override
    public ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping() {
        return affinitiesMapping;
    }
}
