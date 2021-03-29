package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.experimental.tests.TestAgent;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationAgent;
import de.unileipzig.irpact.jadex.simulation.JadexLifeCycleControl;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@Service
@ProvidedServices({
        @ProvidedService(type = TimeModelService.class)
})
public class TimeModelAgent implements TimeModelService, TestAgent, SimulationAgent {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    protected String name;
    protected JadexTimeModel timeModel;
    protected JadexLifeCycleControl simulationControl;

    public TimeModelAgent() {
    }

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        timeModel = (JadexTimeModel) resultsFeature.getArguments().get("timeModel");
        simulationControl = (JadexLifeCycleControl) resultsFeature.getArguments().get("simulationControl");
        simulationControl.registerSimulationAgentAccess(this, agent);
        log("onInit");
        simulationControl.reportAgentCreated(this);
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        log("wait for termination");
        JadexTimestamp ts = timeModel.endTime();;
        timeModel.waitUntil(execFeature, ts, agent, ia -> {
            log("terminate " + timeModel.getClockService().getTick() + " " + timeModel.getClockService().getTime() + " " + timeModel.now());
            simulationControl.terminate();
            return IFuture.DONE;
        });
    }

    @Override
    public SocialGraph.Node getSocialGraphNode() {
        throw new RuntimeException();
    }

    @Override
    public boolean tryAquireAttention() {
        return false;
    }

    @Override
    public int getMaxNumberOfActions() {
        return -1;
    }

    @Override
    public void allowAttention() {
    }

    @Override
    public void actionPerformed() {
    }

    @Override
    public void releaseAttention() {
    }

    @Override
    public void aquireDataAccess() {
    }

    @Override
    public boolean tryAquireDataAccess() {
        return false;
    }

    @Override
    public boolean tryAquireDataAccess(long time, TimeUnit unit) {
        return false;
    }

    @Override
    public void releaseDataAccess() {

    }

    @Override
    public void setSocialGraphNode(SocialGraph.Node node) {
        throw new RuntimeException();
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }

    @Override
    public JadexTimeModel getTimeModel() {
        return timeModel;
    }

    @Override
    public JadexLifeCycleControl getControl() {
        return simulationControl;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return null;
    }
}
