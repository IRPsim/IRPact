package de.unileipzig.irpact.jadex.agent.company;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.company.CompanyAgentBase;
import de.unileipzig.irpact.core.agent.company.advertisement.AdvertisementScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bridge.IComponentIdentifier;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
import jadex.bridge.component.IMessageFeature;
import jadex.bridge.service.annotation.OnEnd;
import jadex.bridge.service.annotation.OnInit;
import jadex.bridge.service.annotation.OnStart;
import jadex.bridge.service.annotation.Service;
import jadex.bridge.service.component.IRequiredServicesFeature;
import jadex.commons.future.Future;
import jadex.commons.future.IFuture;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.AgentFeature;
import jadex.micro.annotation.ProvidedService;
import jadex.micro.annotation.ProvidedServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices({
        @ProvidedService(type = CompanyAgentService.class),
        @ProvidedService(type = JadexAgentService.class)
})
@Agent(type = BDIAgentFactory.TYPE)
public class JadexCompanyAgentBDI extends JadexAgentBase
        implements CompanyAgent, CompanyAgentService, JadexAgentService {

    //Argument names
    public static final String AGENT_BASE = StartSimulation.AGENT_BASE;

    //general
    private static final Logger logger = LoggerFactory.getLogger(JadexCompanyAgentBDI.class);

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

    //ConsumerAgent parameter
    protected CompanyAgentBase agentBase;

    //Beliefs
    @Belief
    protected Set<Product> productPortfolio = new HashSet<>();

    //=========================
    //Constructer
    //=========================

    public JadexCompanyAgentBDI() {
    }

    //=========================
    //CompanyAgent
    //=========================

    @Override
    public double getInformationAuthority() {
        return agentBase.getInformationAuthority();
    }

    @Override
    public String getName() {
        return agentBase.getName();
    }

    @Override
    public Set<Product> getProductPortfolio() {
        return productPortfolio;
    }

    @Override
    public AdvertisementScheme getAdvertisementScheme() {
        return agentBase.getAdvertisementScheme();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) agentBase.getEnvironment();
    }

    @Override
    public boolean is(EntityType type) {
        switch (type) {
            case AGENT:
            case INFORMATION_AGENT:
            case COMPANY_AGENT:
                return true;

            default:
                return false;
        }
    }

    //=========================
    //JadexAgentBase
    //=========================

    @Override
    protected Logger logger() {
        return logger;
    }

    @Override
    protected IComponentIdentifier getCompnentIdentifier() {
        return agent.getId();
    }

    @Override
    protected IMessageFeature getMessageFeature() {
        return msgFeature;
    }

    @Override
    protected void initArgs(Map<String, Object> args) {
        try {
            agentBase = get(args, AGENT_BASE);
        } catch (Throwable t) {
            String _name = agentBase == null
                    ? getClass().getSimpleName()
                    : getName();
            logger.error("[" + _name + "] initArgs error", t);
            throw t;
        }
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        getEnvironment().getConfiguration() .register(agent.getExternalAccess(), this);
        initMessageHandler();
        logger.trace("[{}] onInit", getName());
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.trace("[{}] onStart", getName());

    }

    @OnEnd
    @Override
    protected void onEnd() {
        logger.trace("[{}] onEnd", getName());
    }

    //=========================
    //CompanyAgentService
    //=========================

    @Override
    public JadexCompanyAgentBDI getCompanyAgentSyn() {
        return this;
    }

    @Override
    public IFuture<JadexCompanyAgentBDI> getCompanyAgentAsyn() {
        return new Future<>(this);
    }

    //=========================
    //JadexAgentService
    //=========================

    @Override
    public JadexCompanyAgentBDI getAgentSyn() {
        return getCompanyAgentSyn();
    }

    @Override
    public IFuture<JadexCompanyAgentBDI> getAgentAsyn() {
        return getCompanyAgentAsyn();
    }
}
