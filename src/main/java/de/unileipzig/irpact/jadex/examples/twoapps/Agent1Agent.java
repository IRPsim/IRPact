package de.unileipzig.irpact.jadex.examples.twoapps;

import jadex.bridge.IInternalAccess;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices(
        @ProvidedService(type = Service1.class)
)
@Agent
public class Agent1Agent implements Service1 {

    @Agent
    protected IInternalAccess agent;

    @OnInit
    public void onInit() {
        System.out.println("[Agent1] onInit");
    }

    @OnStart
    public void onStart() {
        System.out.println("[Agent1] onStart");
        agent.scheduleStep(ia -> {
            int count = ia.getFeature(IRequiredServicesFeature.class)
                    .searchServices(new ServiceQuery<>(Service0.class, ServiceScope.NETWORK))
                    .get()
                    .size();
            System.out.println("[Agent1] " + count);
            return IFuture.DONE;
        });
    }

    @OnEnd
    public void onEnd() {
        System.out.println("[Agent1] onEnd");
    }
}
