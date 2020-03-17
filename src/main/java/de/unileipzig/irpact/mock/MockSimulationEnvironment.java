package de.unileipzig.irpact.mock;

import de.unileipzig.irpact.core.message.MessageSystem;
import de.unileipzig.irpact.core.simulation.SimulationEnvironmentBase;

/**
 * @author Daniel Abitz
 */
public class MockSimulationEnvironment extends SimulationEnvironmentBase {

    @Override
    public MessageSystem getMessageSystem() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSimulationStarttime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSimulationTime() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getTick() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getSystemTime() {
        throw new UnsupportedOperationException();
    }
}
