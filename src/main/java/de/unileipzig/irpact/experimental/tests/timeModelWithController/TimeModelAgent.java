package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.v2.jadex.simulation.JadexSimulationControl;
import de.unileipzig.irpact.v2.jadex.time.JadexTimeModel;
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
public class TimeModelAgent implements TimeModelService {

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
    protected JadexSimulationControl simulationControl;

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
        simulationControl = (JadexSimulationControl) resultsFeature.getArguments().get("simulationControl");
        simulationControl.registerControlAgent(agent);
        log("onInit");
        simulationControl.reportAgentCreation();
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        log("wait for termination");
        timeModel.waitUntilEnd(execFeature, ia -> {
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
    public JadexSimulationControl getControl() {
        return simulationControl;
    }
}
