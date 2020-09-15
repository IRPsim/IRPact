package de.unileipzig.irpact.temp.jadex;

import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.temp.TTimerAgentData;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IExternalAccess;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.types.clock.IClockService;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
@Service
@ProvidedServices({
        @ProvidedService(type = TimerService.class)
})
public class TTimerAgentBDI implements TimerService {

    private static final Logger logger = LoggerFactory.getLogger(TTimerAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    protected IClockService clock;
    protected TTimerAgentData data;
    protected long realStart;
    protected ZonedDateTime simuStart;

    private String getName() {
        return data.getName();
    }

    private void log(String msg) {
        logger.trace("[{}] " + msg, getName());
    }

    private void logTime(String info) {
        long msInSimu = clock.getTime() - clock.getStarttime();
        long msInReal = System.currentTimeMillis() - realStart;
        ZonedDateTime simuNow = ZonedDateTime.ofInstant(Instant.ofEpochMilli(clock.getTime()), ZoneId.systemDefault());
        String msg = "---"
                + "\n" + info
                + "\n" + msInReal
                + "\n" + msInSimu
                + "\n" + simuStart
                + "\n" + simuNow;
        log(msg);
    }

    private void killPlatform(IInternalAccess ia) {
        IComponentIdentifier platformId = ia.getId().getRoot();
        IExternalAccess platform = ia.getExternalAccess(platformId);
        platform.killComponent();
    }

    protected long startTime;
    protected long endTime;
    private void waitForKillDelay() {
        startTime = clock.getStarttime();
        long delay = TimeUnit.DAYS.toMillis(365);
        endTime = startTime + delay;
        data.getContinuousConverter().setStart(startTime);
        execFeature.waitForDelay(delay, ia -> {
            log("KILL IT");
            killPlatform(ia);
            return IFuture.DONE;
        });

    }

    protected double startTick;
    protected double endTick;
    private void waitForKillTick() {
        startTick = clock.getTick();
        long delay = TimeUnit.DAYS.toMillis(365);
        endTick = startTick + data.getTickConverter().delayToTick(delay);
        data.getTickConverter().setStart(startTick);
        waitForKillTick0();
    }

    private void waitForKillTick0() {
        execFeature.waitForTick(ia -> {
            if(isValid2().get()) {
                waitForKillTick0();
            } else {
                log("KILL IT");
                killPlatform(ia);
            }
            return IFuture.DONE;
        });
    }

    //=========================
    //service
    //=========================

    @Override
    public IFuture<Boolean> isValid2() {
        if(data.getContinuousConverter() == null) {
            double currentTick = clock.getTick();
            return currentTick < endTick ? IFuture.TRUE : IFuture.FALSE;
        } else {
            long currentTime = clock.getTime();
            return currentTime < endTime ? IFuture.TRUE : IFuture.FALSE;
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        data = (TTimerAgentData) resultsFeature.getArguments().get("data");
        clock = JadexUtil.getClockService(agent);
        if(data.getContinuousConverter() == null) {
            waitForKillTick();
        } else {
            waitForKillDelay();
        }
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
