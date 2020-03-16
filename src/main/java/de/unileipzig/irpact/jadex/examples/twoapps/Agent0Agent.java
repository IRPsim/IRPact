package de.unileipzig.irpact.jadex.examples.twoapps;

import jadex.bridge.IInternalAccess;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices(
        @ProvidedService(type = Service0.class)
)
@Agent
public class Agent0Agent implements Service0 {

    @Agent
    protected IInternalAccess agent;

    @OnInit
    public void onInit() {
        System.out.println("[Agent0] onInit");
    }

    @OnStart
    public void onStart() {
        System.out.println("[Agent0] onStart");
    }

    @OnEnd
    public void onEnd() {
        System.out.println("[Agent0] onEnd");
    }

    @Override
    public IFuture<String> hello(String input) {
        return new Future<>("Hallo @ '" + input + "'");
    }
}
