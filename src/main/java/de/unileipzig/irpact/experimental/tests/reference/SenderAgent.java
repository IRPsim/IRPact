package de.unileipzig.irpact.experimental.tests.reference;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
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
        @ProvidedService(type = SenderService.class, scope = ServiceScope.NETWORK)
})
public class SenderAgent implements SenderService {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private String name;

    public SenderAgent() {
    }

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    //=========================
    //ClientService
    //=========================

    private NonRefData<X> nonRefX = new NonRefData<>(new X("a"));
    @Override
    public NonRefData<X> getNonRefX() {
        log("THREAD: " + Thread.currentThread().getName());
        log("nonref-0: " + nonRefX.get().data + " | " + nonRefX.hashCode());
        execFeature.waitForDelay(2000, ia -> {
            log("nonref-2000: " + Thread.currentThread().getName() + " | " + ia.getId().getName());
            log("nonref-data: " + nonRefX.get().data + " | " + nonRefX.hashCode());
            return IFuture.DONE;
        });
        return nonRefX;
    }

    private RefData<X> refX = new RefData<>(new X("a"));
    @Override
    public RefData<X> getRefX() {
        log("THREAD: " + Thread.currentThread().getName());
        log("ref-0: " + refX.get().data + " | " + refX.hashCode());
        execFeature.waitForDelay(2000, ia -> {
            log("ref-2000: " + Thread.currentThread().getName() + " | " + ia.getId().getName());
            log("ref-data: " + refX.get().data + " | " + refX.hashCode());
            return IFuture.DONE;
        });
        return refX;
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
