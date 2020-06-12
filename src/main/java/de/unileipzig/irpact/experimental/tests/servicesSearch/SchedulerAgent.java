package de.unileipzig.irpact.experimental.tests.servicesSearch;

import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.ServiceScope;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.bridge.service.search.ServiceQuery;
import jadex.commons.future.IFuture;
import jadex.commons.future.IIntermediateResultListener;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;

import java.time.LocalTime;
import java.util.Collection;

/**
 * @author Daniel Abitz
 */
@Agent
@Service
@ProvidedServices({
        @ProvidedService(type = SchedulerService.class, scope = ServiceScope.NETWORK)
})
public class SchedulerAgent implements SchedulerService {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    private String name;
    private ClientService clientService;

    public SchedulerAgent() {
    }

    //=========================
    //help
    //=========================

    private void log(String msg) {
        System.out.println("[" + LocalTime.now() + "]"
                + " [" + name + "]"
                + " " + msg);
    }

    private void runIt(ClientService clientService) {
        log("FOUND client");
    }

    //=========================
    //SchedulerService
    //=========================

    @Override
    public void runTask(ClientService client, Task task) {
        log("access-hash: " + client.hashCode());
        log("task-hash: " + task.hashCode());
        client.runTaskSelf(task);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        name = (String) resultsFeature.getArguments().get("name");
        execFeature.waitForTick(access -> {
            reqFeature.searchServices(new ServiceQuery<>(ClientService.class, ServiceScope.NETWORK))
                    .addResultListener(new IIntermediateResultListener<ClientService>() {
                        @Override
                        public void intermediateResultAvailable(ClientService result) {
                            runIt(result);
                        }

                        @Override
                        public void finished() {
                        }

                        @Override
                        public void exceptionOccurred(Exception exception) {
                        }

                        @Override
                        public void resultAvailable(Collection<ClientService> result) {
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

    @Override
    public String toString() {
        return "CLOCKAGENT";
    }
}
