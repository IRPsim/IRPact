package de.unileipzig.irpact.core.message;

import de.unileipzig.irpact.commons.Check;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

/**
 * @author Daniel Abitz
 */
public class BasicMessageSystem implements MessageSystem {

    private SimulationEnvironment env;

    public BasicMessageSystem(SimulationEnvironment environment) {
        this.env = Check.requireNonNull(environment, "environment");
    }

    protected SimulationEnvironment env() {
        return env;
    }

    @Override
    public void send(Agent from, MessageContent content, Agent to) {
        if(to.isHandling(from, content)) {
            to.handleMessage(from, content);
        }
    }

    @Override
    public void send(Agent from, MessageContent content, Agent... to) {
        for(Agent target: to) {
            send(from, content, target);
        }
    }
}
