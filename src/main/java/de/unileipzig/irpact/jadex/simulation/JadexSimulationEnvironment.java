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

    void replace(ConsumerAgent placeholder, ConsumerAgent real) throws IllegalStateException;
}
