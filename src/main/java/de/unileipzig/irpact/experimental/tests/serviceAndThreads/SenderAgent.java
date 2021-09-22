package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

import de.unileipzig.irpact.jadex.util.JadexUtil;
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
@Agent
public class SenderAgent implements Sender {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private ReceiverService receiver;
    private String name;

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    private boolean is(int i) {
        return name.endsWith(String.valueOf(i));
    }


    //=========================
    //Sender
    //=========================

    @Override
    public void oh(Data data) {
        execFeature.waitForTick(_ia -> {
            log("oh: " + data + ", hash=" + data.hashCode() + ", thread=" + Thread.currentThread().hashCode());
            return IFuture.DONE;
        });
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        receiver = JadexUtil.getPlatformService(reqFeature, ReceiverService.class);
        log("onInit");
    }

    @OnStart
    protected void onStart() {
        log("onStart");
        if(is(0)) {
            execFeature.waitForDelay(2000, _ia -> {
                Data data = new Data("HALLO", new DeepData("WELT"));
                log("hash=" + data.hashCode() + ", deephash=" + data.deepData.hashCode() + ", this=" + this.hashCode() + ", thread=" + Thread.currentThread().hashCode());
                receiver.receive(this, data);
                return IFuture.DONE;
            });
        }
        if(is(1)) {
            execFeature.waitForDelay(4000, _ia -> {
                Data data = new Data("WELT", new DeepData("HALLO"));
                log("hash=" + data.hashCode() + ", deephash=" + data.deepData.hashCode() + ", this=" + this.hashCode() + ", thread=" + Thread.currentThread().hashCode());
                receiver.receive(this, data);
                return IFuture.DONE;
            });
        }
    }

    @OnEnd
    protected void onEnd() {
        log("onEnd");
    }
}
