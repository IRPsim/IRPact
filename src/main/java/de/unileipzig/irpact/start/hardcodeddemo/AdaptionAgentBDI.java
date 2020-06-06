package de.unileipzig.irpact.start.hardcodeddemo;

import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class AdaptionAgentBDI {

    private static final Logger logger = LoggerFactory.getLogger(MasterAgentBDI.class);

    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;

    protected AdaptionAgentData data;

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        data = (AdaptionAgentData) resultsFeature.getArguments().get("data");
        logger.debug("[{}] onInit", data.getName());
        MasterAgentBDI.incCreated();
    }

    @OnStart
    protected void onStart() {
        logger.debug("[{}] onStart", data.getName());
        Random random = data.getRandom();
        AdaptedProducts adaptedProducts = new AdaptedProducts();
        int year = data.getYear();
        double rate = data.getAdaptionRate();
        for(Product product: data.getProducts()) {
            if(!adaptedProducts.isAdapted(product)) {
                if(random.nextDouble() < rate) {
                    adaptedProducts.adapt(year, product);
                }
            }
        }
        resultsFeature.getResults().put("adapted", adaptedProducts);
        MasterAgentBDI.incKilled();
    }

    @OnEnd
    protected void onEnd() {
        logger.debug("[{}] onEnd", data.getName());
    }
}
