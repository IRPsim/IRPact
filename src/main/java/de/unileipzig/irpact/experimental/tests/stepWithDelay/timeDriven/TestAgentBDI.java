package de.unileipzig.irpact.experimental.tests.stepWithDelay.timeDriven;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class TestAgentBDI {

    protected static final Logger LOGGER = LoggerFactory.getLogger(TestAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;

    protected IClockService clock;
    protected String name;
    protected CountDownLatch latch;
    protected int counter;
    protected double startTick;

    //=========================
    //util
    //=========================

    protected void initData(Map<String, Object> input) {
        name = (String) input.get("NAME");
        latch = (CountDownLatch) input.get("LATCH");
        clock = JadexUtil2.getClockService(agent);
    }

    //=========================
    //live
    //=========================

    public TestAgentBDI() {
    }

    @OnInit
    public void onInit() {
        initData(resultsFeature.getArguments());
        LOGGER.trace("[{}] init: {}", name, clock.getTick());
    }

    @OnStart
    public void onStart() {
        LOGGER.trace("[{}] start: {}", name, clock.getTick());
        startTick = clock.getTick();
        doAction(0);
        //doAction2();
        latch.countDown();
    }

    @OnEnd
    public void onEnd() {
        LOGGER.trace("[{}] counter: {} | end: {}", name, counter, clock.getTick());
    }

    protected void doAction(int i) {
        if(i == 3) {
            return;
        }
        if("Agent#0".equals(name)) {
            execFeature.waitForTick(ia -> {
                LOGGER.trace("[{},{}] a - {}", name, clock.getTick(), Thread.currentThread().getName());
                doAction(i + 1);
                return IFuture.DONE;
            });
        }
        else if("Agent#1".equals(name)) {
            execFeature.waitForTick(ia -> {
                ConcurrentUtil.sleepSilently(2000);
                LOGGER.trace("[{},{}] b - {}", name, clock.getTick(), Thread.currentThread().getName());
                doAction(i + 1);
                return IFuture.DONE;
            });
        }
        else if("Agent#2".equals(name)) {
            execFeature.waitForTick(ia -> {
                ConcurrentUtil.sleepSilently(1000);
                LOGGER.trace("[{},{}] c - {}", name, clock.getTick(), Thread.currentThread().getName());
                doAction(i + 1);
                return IFuture.DONE;
            });
        }
    }

    /*
    waitForDelay -> synchronisiert Aufrufe
    waitForTick -> asynchron
     */

    /*
    TODO
    scheduleStep(int ticks)
    -> waitForDelay(ticks-1) + waitForTick()

     */

    protected void doAction2() {
        if("Agent#0".equals(name)) {
            //MASTER
            execFeature.waitForDelay(10001L, ia -> {
                LOGGER.trace("[{},{}] ENDE", name, clock.getTick());
//                IComponentIdentifier platformId = agent.getId().getRoot();
//                IExternalAccess platform = agent.getExternalAccess(platformId);
//                ISimulationService simulationService = JadexUtil2.getSimulationService(platform);
//                simulationService.pause().get();
//                LOGGER.trace("[{},{}] PAUSIERT", name, clock.getTick());
                return IFuture.DONE;
            });
        }
        else if("Agent#1".equals(name)) {
            doRec(2000, "1");
        }
        else if("Agent#2".equals(name)) {
            doRec(1000, "2");
        }
    }

    protected void doRec(long delay, String x) {
        double thisTick = clock.getTick();
        if(thisTick + delay > startTick + 10000) {
            return;
        }
        execFeature.waitForDelay(delay, ia -> {
            LOGGER.trace("[{},{}] {} -> {}", name, clock.getTick(), x, Thread.currentThread().getName());
            ConcurrentUtil.sleepSilently(delay);
            doRec(delay, x);
            return IFuture.DONE;
        });
    }
}
