package de.unileipzig.irpact.experimental.tests.timeModelWithController;

import de.unileipzig.irpact.experimental.tests.TestAgent;
import de.unileipzig.irpact.jadex.time.JadexTimeModel;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
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
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI implements TestAgent {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    protected String name;
    protected TimeModelService timeModelSerivce;
    protected long delay;

    public TestAgentBDI() {
    }

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        delay = (long) resultsFeature.getArguments().get("delay");
        searchTimerService();
        log("onInit");
    }

    @OnStart
    protected void onStart() {
        log("onStart");
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }

    protected void searchTimerService() {
        JadexUtil2.searchPlatformServices(reqFeature, TimeModelService.class, result -> {
            if(timeModelSerivce == null) {
                log("timeModelSerivce found");
                timeModelSerivce = result;
                timeModelSerivce.getControl().reportAgentCreated(this);
                doOnStart();
            }
        });
    }

    protected JadexTimeModel timeModel() {
        return timeModelSerivce.getTimeModel();
    }

    protected void doOnStart() {
        JadexTimeModel timeModel = timeModel();
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
        if("A_3".equals(name)) {
            JadexTimestamp now = timeModel.now();
            JadexTimestamp x364 = timeModel.plusDays(now, 364);
            JadexTimestamp x366 = timeModel.plusDays(now, 366);
            log("valid: " + x364 + " -> " + timeModel.isValid(x364));
            log("valid: " + x366 + " -> " + timeModel.isValid(x366));
        }
    }

    @Override
    public void aquireDataAccess() {

    }

    @Override
    public boolean tryAquireDataAccess() {
        return false;
    }

    @Override
    public boolean tryAquireDataAccess(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void releaseDataAccess() {

    }
}
