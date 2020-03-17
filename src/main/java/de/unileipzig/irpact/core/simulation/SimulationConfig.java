package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinitiesMapping;

/**
 * @author Daniel Abitz
 */
public interface SimulationConfig {

    ConsumerAgentGroupAffinitiesMapping getAffinitiesMapping();
}
