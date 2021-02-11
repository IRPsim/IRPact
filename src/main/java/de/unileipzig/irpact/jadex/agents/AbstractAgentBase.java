package de.unileipzig.irpact.jadex.agents;

import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractAgentBase {

    protected String name;
    protected JadexSimulationEnvironment environment;

    public AbstractAgentBase() {
    }

    public String getName() {
        return name;
    }

    protected abstract IRPLogger log();

    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    protected JadexTimeModel getTimeModel() {
        return getEnvironment().getTimeModel();
    }

    @OnInit
    protected abstract void onInit();

    @OnStart
    protected abstract void onStart();

    @OnEnd
    protected abstract void onEnd();
}
