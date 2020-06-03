package de.unileipzig.irpact.jadex.agent.company;

import de.unileipzig.irpact.core.agent.company.CompanyAgent;
import de.unileipzig.irpact.core.agent.company.CompanyAgentData;
import de.unileipzig.irpact.core.agent.company.ProductIntroductionScheme;
import de.unileipzig.irpact.core.agent.company.advertisement.AdvertisementScheme;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.EntityType;
import de.unileipzig.irpact.jadex.agent.Identifier;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
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

    //non-Beliefs
    protected CompanyAgentData data;

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
        return data.getInformationAuthority();
    }

    @Override
    public Set<Product> getProductPortfolio() {
        return productPortfolio;
    }

    @Override
    public AdvertisementScheme getAdvertisementScheme() {
        return data.getAdvertisementScheme();
    }

    @Override
    public ProductIntroductionScheme getProductIntroductionScheme() {
        return data.getProductIntroductionScheme();
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

    @Override
    public void addProductToPortfolio(Product product) {
        productPortfolio.add(product);
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
    protected void initArgsThis(Map<String, Object> args) {
        name = get(args, Identifier.NAME);
        environment = get(args, Identifier.ENVIRONMENT);
        data = get(args, Identifier.DATA);
    }

    //=========================
    //lifecycle
    //=========================

    @OnInit
    @Override
    protected void onInit() {
        initArgs(resultsFeature.getArguments());
        getEnvironment().getConfiguration().register(agent.getExternalAccess(), this);
        initMessageHandler();
        logger.trace("[{}] onInit", getName());
    }

    @OnStart
    @Override
    protected void onStart() {
        logger.trace("[{}] onStart", getName());
        execFeature.waitForTick(ia -> {
            for(Product product: data.getInitialProducts()) {
                addProductToPortfolio(product);
            }
            return IFuture.DONE;
        });
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

    //=========================
    //NewProduct
    //=========================

    @Goal
    public class NewProductGoal {

        protected Product product;

        @GoalCreationCondition(beliefs = "productPortfolio")
        public NewProductGoal(Product product) {
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }
    }

    @Plan(trigger = @Trigger(goals = NewProductGoal.class))
    protected void handleNewProduct(NewProductGoal goal) {
        getProductIntroductionScheme().handle(
                this,
                goal.getProduct()
        );
    }
}
