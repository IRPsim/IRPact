package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.experimental.tests.TestAgent;
import de.unileipzig.irpact.jadex.simulation.JadexLiveCycleControl;
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

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@Service
@ProvidedServices({
        @ProvidedService(type = TimeModelService.class)
})
public class TimeModelAgent implements TimeModelService, TestAgent {

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
    protected JadexLiveCycleControl simulationControl;

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
        simulationControl = (JadexLiveCycleControl) resultsFeature.getArguments().get("simulationControl");
        simulationControl.registerSimulationAgentAccess(agent);
        log("onInit");
        simulationControl.reportAgentCreated(this);
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        log("wait for termination");
        JadexTimestamp ts = timeModel.endTime();;
        timeModel.waitUntil(execFeature, ts, ia -> {
            log("terminate " + timeModel.getClockService().getTick() + " " + timeModel.getClockService().getTime() + " " + timeModel.now());
            simulationControl.terminate();
            return IFuture.DONE;
        });
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
    public JadexLiveCycleControl getControl() {
        return simulationControl;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean hasName(String input) {
        return false;
    }

    @Override
    public SimulationEnvironment getEnvironment() {
        return null;
    }
}
