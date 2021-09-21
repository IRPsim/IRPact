package de.unileipzig.irpact.experimental.tests.serviceAndThreads;

import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.time.LocalTime;

/**
 * @author Daniel Abitz
 */
@Agent
@Service
@ProvidedServices({
        @ProvidedService(type = ReceiverService.class, scope = ServiceScope.PLATFORM)
})
public class ReceiverAgent implements ReceiverService {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private String name;

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    //=========================
    //ReceiverService
    //=========================

    @Override
    public void receive(Sender sender, Data data) {
        log("received: " + data + ", hash=" + data.hashCode() + ", deephash=" + data.deepData.hashCode() + " ,sender=" + sender.hashCode() + ", thread=" + Thread.currentThread().hashCode());
        sender.oh(data);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
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
