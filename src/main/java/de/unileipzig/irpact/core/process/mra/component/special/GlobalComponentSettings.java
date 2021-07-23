package de.unileipzig.irpact.core.process.mra.component.special;

import de.unileipzig.irpact.core.agent.AgentManager;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.process.ra.RAProcessModelBase;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public final class GlobalComponentSettings implements LoggingHelper {

    //=========================
    //access
    //=========================

    private static GlobalComponentSettings instance;

    public static GlobalComponentSettings get() {
        if(instance == null) {
            return syncGet();
        } else {
            return instance;
        }
    }

    private static synchronized GlobalComponentSettings syncGet() {
        if(instance == null) {
            instance = new GlobalComponentSettings();
        }
        return instance;
    }

    //=========================
    //instance
    //=========================

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GlobalComponentSettings.class);

    protected GlobalComponentSettings() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.INITIALIZATION_PARAMETER;
    }

    protected Set<Product> handledProducts = new HashSet<>();
    public synchronized void handleNewProduct(Product product, RAProcessModelBase processModelBase) {
        if(handledProducts.contains(product)) {
            return;
        }

        handledProducts.add(product);

        AgentManager agentManager = processModelBase.getEnvironment().getAgents();

        for(ConsumerAgentGroup cag: agentManager.getConsumerAgentGroups()) {
            for(ConsumerAgent ca : cag.getAgents()) {
                initalizeInitialProductAwareness(processModelBase, ca, product);
                initalizeInitialProductInterest(processModelBase, ca, product);
                initalizeInitialAdopter(processModelBase, ca, product);
            }
        }
    }

    protected void initalizeInitialProductAwareness(RAProcessModelBase processModelBase, ConsumerAgent ca, Product p) {
        if(ca.isAware(p)) {
            trace("consumer agent '{}' already aware of '{}'", ca.getName(), p.getName());
            return;
        }

        double chance = processModelBase.getInitialProductAwareness(ca, p);
        double draw = processModelBase.getRnd().nextDouble();
        boolean isAware = draw < chance;
        trace("is consumer agent '{}' initial aware of product '{}'? {} ({} < {})", ca.getName(), p.getName(), isAware, draw, chance);
        if(isAware) {
            ca.makeAware(p);
        }
    }

    protected void initalizeInitialProductInterest(RAProcessModelBase processModelBase, ConsumerAgent ca, Product p) {
        double interest = processModelBase.getInitialProductInterest(ca, p);
        if(interest > 0) {
            trace("set awareness for consumer agent '{}' because initial interest value {} for product '{}'", ca.getName(), interest, p.getName());
            ca.makeAware(p);
        }
        ca.updateInterest(p, interest);
        trace("consumer agent '{}' has initial interest value {} for product '{}'", ca.getName(), interest, p.getName());
    }

    protected void initalizeInitialAdopter(RAProcessModelBase processModelBase, ConsumerAgent ca, Product p) {
        double chance = processModelBase.getInitialAdopter(ca, p);
        double draw = processModelBase.getRnd().nextDouble();
        boolean isAdopter = draw < chance;
        trace("Is consumer agent '{}' initial adopter of product '{}'? {} ({} < {})", ca.getName(), p.getName(), isAdopter, draw, chance);
        if(isAdopter) {
            ca.adoptInitial(p);
        }
    }
}
