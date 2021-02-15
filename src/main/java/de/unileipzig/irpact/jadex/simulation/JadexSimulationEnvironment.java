package de.unileipzig.irpact.jadex.simulation;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import jadex.bridge.service.annotation.Reference;

/**
 * @author Daniel Abitz
 */
@Reference(local = true, remote = true)
public interface JadexSimulationEnvironment extends SimulationEnvironment {

    @Override
    JadexTimeModel getTimeModel();

    @Override
    JadexLifeCycleControl getLiveCycleControl();

    /**
     * Replaces the placeholder agent with the real agent. This method is thread safe.
     *
     * @param placeholder placeholder agent
     * @param real real agent
     * @throws IllegalStateException If the real agent is invalid
     */
    void replace(ConsumerAgent placeholder, ConsumerAgent real) throws IllegalStateException;
}
