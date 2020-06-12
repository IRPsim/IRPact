package de.unileipzig.irpact.experimental.tests.clockStepsTicks;

import de.unileipzig.irpact.experimental.ExperimentalUtil;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices({
        @RequiredService(type = ClockAgentService.class)
})
public class TestAgentBDI {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    protected IClockService clock;

    protected ClockAgentService clockAgent;

    private String name;

    public TestAgentBDI() {
    }

    //=========================
    //help
    //=========================

    private boolean is(int i) {
        return name.endsWith(String.valueOf(i));
    }

    private void log(String msg) {
        if(clock == null) {
            System.out.println("[" + LocalTime.now() + "]"
                    + " [" + name + "]"
                    + " " + msg);
        } else {
            System.out.println("[" + LocalTime.now() + "]"
                    + " [" + clock.getTime() + "]"
                    + " [" + clock.getTick() + "]"
                    + " [" + name + "]"
                    + " " + msg);
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        clock = ExperimentalUtil.getClockService(agent);
        clockAgent = reqFeature.searchServices(new ServiceQuery<>(ClockAgentService.class, ServiceScope.NETWORK))
                .get()
                .toArray(new ClockAgentService[0])[0];
        log("onInit: " + clockAgent);
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        /*
        if(is(0)) {
            testScheduleTicks0();
        }
        if(is(1)) {
            testScheduleTicks1();
        }
        if(is(2)) {
            testScheduleTicks2();
        }
        */
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }

    //=========================
    //stuff
    //=========================

    private ClockAgentService clockAgent() {
        return ClockAgent.AGENT;
    }

    /*
    private void testScheduleTicks1() {
        clockAgent().waitForTicks(10, execFeature, _access -> {
            log("10 tick");
            return IFuture.DONE;
        });
        clockAgent().waitForTicks(20, execFeature, _access -> {
            log("20 tick");
            return IFuture.DONE;
        });
        clockAgent().waitForTicks(30, execFeature, _access -> {
            log("30 tick");
            return IFuture.DONE;
        });
        clockAgent().waitForTicks(40, execFeature, _access -> {
            log("40 tick");
            return IFuture.DONE;
        });
        clockAgent().waitForTicks(90, execFeature, _access -> {
            log("90 tick");
            return IFuture.DONE;
        });
    }

    private void testScheduleTicks2() {
        clockAgent().waitForTicks(20, execFeature, _access -> {
            log("20 tick");
            return IFuture.DONE;
        });
        clockAgent().waitForTicks(40, execFeature, _access -> {
            log("40 tick");
            return IFuture.DONE;
        });
    }

    private void testScheduleTicks0() {
        clockAgent().waitForTicks(40, execFeature, _access -> {
            log("40 tick");
            return IFuture.DONE;
        });
    }
    */

    private void testWaitForTicks() {
        ExperimentalUtil.waitForTicks(execFeature, 1, _access -> {
            log("1 tick");
            return IFuture.DONE;
        });
        ExperimentalUtil.waitForTicks(execFeature, 2, _access -> {
            log("2 tick");
            return IFuture.DONE;
        });
        ExperimentalUtil.waitForTicks(execFeature, 3, _access -> {
            log("3 tick");
            return IFuture.DONE;
        });
        ExperimentalUtil.waitForTicks(execFeature, 10, _access -> {
            log("10 tick");
            return IFuture.DONE;
        });
    }

    private void testWaitForDelay() {
        execFeature.waitForDelay(1000, _access -> {
            log("1 sek");
            return IFuture.DONE;
        });
        execFeature.waitForDelay(2000, _access -> {
            log("2 sek");
            return IFuture.DONE;
        });
        execFeature.waitForDelay(3000, _access -> {
            log("3 sek");
            return IFuture.DONE;
        });
    }
}
