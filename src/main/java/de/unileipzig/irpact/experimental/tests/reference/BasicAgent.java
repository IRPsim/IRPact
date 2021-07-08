package de.unileipzig.irpact.experimental.tests.reference;

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
import jadex.commons.future.IFuture;
import jadex.commons.future.IIntermediateResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class BasicAgent {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private String name;
    private Object x;

    public BasicAgent() {
    }

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    private void runIt(SenderService senderService) {
        log(">>> " + x + " | " + Objects.hashCode(x));
        execFeature.waitForDelay(2000, ia -> {
            log("<<< " + x + " | " + Objects.hashCode(x));
            return IFuture.DONE;
        });
        if(name.equals("BASIC1")) {
            runIt1(senderService);
        } else {
            runIt2(senderService);
        }
    }

    private void runIt1(SenderService senderService) {
        log("FOUND client");
        log("THREAD: " + Thread.currentThread().getName());
        RefData<X> refX = senderService.getRefX();
        NonRefData<X> nonRefX = senderService.getNonRefX();
        log("nonref-data: " + nonRefX.get().data + " | " + nonRefX.hashCode());
        log("ref-data: " + refX.get().data + " | " + refX.hashCode());
        nonRefX.get().data = "nonref-a";
        refX.get().data = "ref-a";
        log("nonref-data: " + nonRefX.get().data + " | " + nonRefX.hashCode());
        log("ref-data: " + refX.get().data + " | " + refX.hashCode());
    }

    private void runIt2(SenderService senderService) {
        execFeature.waitForDelay(1000, ia -> {
            log("FOUND client");
            log("THREAD: " + Thread.currentThread().getName());
            RefData<X> refX = senderService.getRefX();
            NonRefData<X> nonRefX = senderService.getNonRefX();
            log("nonref-data: " + nonRefX.get().data + " | " + nonRefX.hashCode());
            log("ref-data: " + refX.get().data + " | " + refX.hashCode());
            nonRefX.get().data = "nonref-a";
            refX.get().data = "ref-a";
            log("nonref-data: " + nonRefX.get().data + " | " + nonRefX.hashCode());
            log("ref-data: " + refX.get().data + " | " + refX.hashCode());
            return IFuture.DONE;
        });
    }

    //=========================
    //SchedulerService
    //=========================

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        x = resultsFeature.getArguments().get("x");
        execFeature.waitForTick(access -> {
            reqFeature.searchServices(new ServiceQuery<>(SenderService.class, ServiceScope.NETWORK))
                    .addResultListener(new IIntermediateResultListener<SenderService>() {
                        @Override
                        public void intermediateResultAvailable(SenderService result) {
                            runIt(result);
                        }

                        @Override
                        public void finished() {
                        }

                        @Override
                        public void maxResultCountAvailable(int max) {
                        }

                        @Override
                        public void exceptionOccurred(Exception exception) {
                        }

                        @Override
                        public void resultAvailable(Collection<SenderService> result) {
                        }
                    });
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
