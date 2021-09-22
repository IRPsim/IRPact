package de.unileipzig.irpact.experimental.tests.timeModel;

import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI {

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
    protected long delay;

    public TestAgentBDI() {
    }

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    @SuppressWarnings("unchecked")
    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        timeModel = (JadexTimeModel) resultsFeature.getArguments().get("timeModel");
        delay = (long) resultsFeature.getArguments().get("delay");
        log("onInit");
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        if("A_1".equals(name)) {
            timeModel.wait(execFeature, delay, agent, ia -> {
                log("DELAY1 " + timeModel.getClockService().getTick() + " " + timeModel.getClockService().getTime() + " " + timeModel.now());
                return IFuture.DONE;
            });
        }
        if("A_2".equals(name)) {
            JadexTimestamp until = timeModel.plusMillis(delay);
            timeModel.waitUntil(execFeature, until, agent, ia -> {
                log("DELAY2 " + timeModel.getClockService().getTick() + " " + timeModel.getClockService().getTime() + " " + timeModel.now());
                return IFuture.DONE;
            });
        }
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }
}
