package de.unileipzig.irpact.start.hardcodeddemo;

import de.unileipzig.irpact.commons.concurrent.ConcurrentUtil;
import de.unileipzig.irpact.start.hardcodeddemo.def.in.Product;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;

import java.util.Random;

/**
 * @author Daniel Abitz
 */
@Agent(type = BDIAgentFactory.TYPE)
public class AdaptionAgentBDI {

    //Jadex parameter
    @Agent
    protected IInternalAccess agent;
    @AgentFeature
    protected IArgumentsResultsFeature resultsFeature;
    @AgentFeature
    protected IBDIAgentFeature bdiFeature;
    @AgentFeature
    protected IExecutionFeature execFeature;
    @AgentFeature
    protected IRequiredServicesFeature reqFeature;
    @AgentFeature
    protected IMessageFeature msgFeature;

    protected AdaptionAgentData data;

    //=========================
    //lifecycle
    //=========================

    @OnInit
    protected void onInit() {
        data = (AdaptionAgentData) resultsFeature.getArguments().get("data");
        if(HardCodedAgentDemo.debug) System.out.println("[" + data.getName() + "] onInit");
    }

    @OnStart
    protected void onStart() {
        if(HardCodedAgentDemo.debug) System.out.println("[" + data.getName() + "] onStart");
        Random random = data.getRandom();
        int[] years = data.getYears();
        double[] rates = data.getAdaptionRate();
        AdaptedProducts adaptedProducts = new AdaptedProducts();
        for(int i = 0; i < years.length; i++) {
            int year = years[i];
            double rate = rates[i];
            for(Product product: data.getProducts()) {
                if(!adaptedProducts.isAdapted(product)) {
                    if(random.nextDouble() < rate) {
                        adaptedProducts.adapt(year, product);
                    }
                }
            }
        }
        resultsFeature.getResults().put("adapted", adaptedProducts);
        MasterAgentBDI.updateAccess();
        agent.killComponent();
    }

    @OnEnd
    protected void onEnd() {
        if(HardCodedAgentDemo.debug) System.out.println("[" + data.getName() + "] onEnd");
    }
}
