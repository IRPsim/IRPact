package de.unileipzig.irpact.jadex.agents;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.ProxyAgent;
import de.unileipzig.irpact.core.simulation.LifeCycleControl;
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

    protected abstract Agent getThisAgent();

    protected abstract ProxyAgent<?> getProxyAgent();

    protected abstract Agent getRealAgent();

    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    protected JadexTimeModel getTimeModel() {
        return getEnvironment().getTimeModel();
    }

    protected LifeCycleControl getLifeCycleControl() {
        return getEnvironment().getLifeCycleControl();
    }

    protected void pulse() {
        getEnvironment().getLifeCycleControl().pulse();
    }

    protected Timestamp now() {
        return getTimeModel().now();
    }

    protected void waitForYearChangeIfRequired() {
        getEnvironment().getLifeCycleControl().waitForYearChangeIfRequired(getThisAgent());
    }

    protected void waitForSynchronisationAtStartIfRequired() {
        getEnvironment().getLifeCycleControl().waitForSynchronisationAtStartIfRequired(getThisAgent());
    }

    protected void waitForSynchronisationAtEndIfRequired() {
        getEnvironment().getLifeCycleControl().waitForSynchronisationAtEndIfRequired(getThisAgent());
    }

    protected abstract void scheduleFirstAction();

    protected abstract void firstAction() throws Throwable;

    protected abstract void scheduleLoop();

    protected abstract void onLoopAction() throws Throwable;

    @OnInit
    protected abstract void onInit();

    @OnStart
    protected abstract void onStart();

    @OnEnd
    protected abstract void onEnd();
}
