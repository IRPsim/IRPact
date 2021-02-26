package de.unileipzig.irpact.jadex.agents.simulation;

import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public class ProxySimulationAgent implements SimulationAgent, ProxyAgent<SimulationAgent> {

    protected SimulationAgent realAgent;

    protected String name;
    protected SimulationEnvironment environment;

    public ProxySimulationAgent() {
    }

    @Override
    public boolean isSynced() {
        return realAgent != null;
    }

    public boolean isNotSynced() {
        return realAgent == null;
    }

    public void sync(SimulationAgent realAgent) {
        if(isSynced()) {
            throw new IllegalStateException("already synced");
        }
        this.realAgent = realAgent;
    }

    @Override
    public SimulationAgent getRealAgent() {
        return realAgent;
    }

    protected void checkSynced() {
        if(isNotSynced()) {
            throw new IllegalStateException("not synced");
        }
    }

    protected void checkNotSynced() {
        if(isSynced()) {
            throw new IllegalStateException("synced");
        }
    }

    @Override
    public String getName() {
        if(isSynced()) {
            return getRealAgent().getName();
        } else {
            return name;
        }
    }

    public void setName(String name) {
        checkNotSynced();
        this.name = name;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        if(isSynced()) {
            return getRealAgent().getEnvironment();
        } else {
            return environment;
        }
    }

    public void setEnvironment(SimulationEnvironment environment) {
        checkNotSynced();
        this.environment = environment;
    }

    @Override
    public int getHashCode() {
        if(isSynced()) {
            return getRealAgent().getHashCode();
        } else {
            return Objects.hash(getName());
        }
    }
}
