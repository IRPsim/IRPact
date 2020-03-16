package de.unileipzig.irpact.jadex.examples.deprecated.old.adoptProductWithService;

import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
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
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.commons.future.IntermediateDefaultResultListener;
import jadex.micro.annotation.*;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@ProvidedServices(
        @ProvidedService(type = OfferProductService.class)
)
@RequiredServices(
        @RequiredService(type = AdoptProductService.class)
)
@Agent(type = BDIAgentFactory.TYPE)
public class CompanyAgentBDI implements OfferProductService {

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeatore;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;

    @Belief
    protected Set<String> products = new HashSet<>();
    //args
    protected String name;
    protected Logger logger;

    //=========================
    //Adopt
    //=========================

    @Goal
    public class AnnounceProductGoal {

        @GoalParameter
        protected String product;

        @GoalCreationCondition(factadded = "products")
        public AnnounceProductGoal(String product) {
            this.product = product;
        }
    }

    @Plan(trigger = @Trigger(goals = AnnounceProductGoal.class))
    protected void announceProduct(String product) {
        logger.debug("{} offer new Product '{}'", sayMyName(), product);
        reqFeature.searchServices(new ServiceQuery<>(AdoptProductService.class, ServiceScope.PLATFORM))
                .addResultListener(new IntermediateDefaultResultListener<AdoptProductService>() {
                    @Override
                    public void intermediateResultAvailable(AdoptProductService result) {
                        logger.debug("{} Offer '{}' to {}", sayMyName(), product, result.getAdopterName());
                        result.anounced(product);
                    }
                });
    }

    //=========================
    //Agent
    //=========================

    public CompanyAgentBDI() {
    }

    protected void initArgs(Map<String, Object> args) {
        name = (String) args.get("name");
        logger = (Logger) args.get("logger");
    }

    private String sayMyName() {
        return "[" + name + "]";
    }

    @OnInit
    protected void created() {
        initArgs(resultsFeature.getArguments());
        logger.debug("{} created", sayMyName());
    }

    @OnStart
    protected void body() {
        logger.debug("{} body", sayMyName());

        @SuppressWarnings("unchecked")
        Set<String> initialProducts = (Set<String>) resultsFeature.getArguments()
                .get("products");
        for(String initialProduct: initialProducts) {
            //noinspection UseBulkOperation
            products.add(initialProduct);
        }

        //neues product
        execFeature.waitForDelay(7000, ia -> {
            products.add("haus");
            return IFuture.DONE;
        });
        //neues product
        execFeature.waitForDelay(14000, ia -> {
            products.add("baum");
            //manuelles goal -> nicht in productlist -> nicht gut
            //bdiFeatore.dispatchTopLevelGoal(new AnnounceProductGoal("baum")).get();
            return IFuture.DONE;
        });
    }

    @OnEnd
    protected void killed() {
        logger.debug("{} killed", sayMyName());

        resultsFeature.getResults()
                .put("products", products);
    }

    @Override
    public String getOfferName() {
        return name;
    }

    @Override
    public IFuture<Boolean> hasProduct(String product) {
        return new Future<>(products.contains(product));
    }

    @Override
    public IFuture<Boolean> adoptProduct(String product) {
        return new Future<>(products.contains(product));
    }
}
