package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.temp.TTestAgentData;
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
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices(
        @RequiredService(type = TimerService.class)
)
public class TTestAgentBDI {

    private static final Logger logger = LoggerFactory.getLogger(TTestAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    protected IClockService clock;
    protected TTestAgentData data;
    protected TimerService timerService;
    protected int count = 0;

    public TTestAgentBDI() {
    }

    private String getName() {
        return data.getName();
    }

    private void log(String msg) {
        logger.debug("[{}] " + msg, getName());
    }

    private void searchTimerService() {
        reqFeature.searchServices(new ServiceQuery<>(TimerService.class, ServiceScope.PLATFORM))
                .addResultListener(new IntermediateDefaultResultListener<TimerService>() {
                    @Override
                    public void intermediateResultAvailable(TimerService result) {
                        timerService = result;
                        if(data.getContinuousConverter() == null) {
                            waitTick();
                        } else {
                            waitDelay();
                        }
                    }
                });
    }

    private void waitDelay() {
        //long delay = 1000L * 60L * 60L * 24L * 31L; //1 Simu-Monat = 1 Real-Sekunde
        long delay = 1000L * 60L * 60L * 24L * 7L; //1 Simu-Wocht = 1 Real-Sekunde
        execFeature.waitForDelay(delay, ia -> {
            if(timerService.isValid2().get()) {
                long nowMs = clock.getTime();
                ZonedDateTime nowZdt = data.getContinuousConverter().toTime(nowMs);
                log("Hello #" + count + " @ "+ nowZdt);
                count++;

                waitDelay();
            }
            return IFuture.DONE;
        });
    }

    private void waitTick() {
        //long delay = 1000L * 60L * 60L * 24L * 31L; //1 Simu-Monat = 1 Real-Sekunde
        long delay = 1000L * 60L * 60L * 24L * 7L; //1 Simu-Wocht = 1 Real-Sekunde
        double ticks = data.getTickConverter().delayToTick(delay);
        if(ticks > 1.0) {
            for(double i = 0.0; i < ticks - 1.0; i++) {
                execFeature.waitForTick().get();
            }
        }
        execFeature.waitForTick(ia -> {
            if(timerService.isValid2().get()) {
                double nowTicks = clock.getTick();
                ZonedDateTime nowZdt = data.getTickConverter().tickToTime(nowTicks);
                log("Hello #" + count + " @ "+ nowZdt);
                count++;

                waitTick();
            }
            return IFuture.DONE;
        });
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        data = (TTestAgentData) resultsFeature.getArguments().get("data");
        clock = JadexUtil.getClockService(agent);
        execFeature.waitForDelay(0, ia -> {
            searchTimerService();
            return IFuture.DONE;
        });
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
}
