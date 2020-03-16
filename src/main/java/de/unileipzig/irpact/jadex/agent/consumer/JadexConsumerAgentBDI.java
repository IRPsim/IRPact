package de.unileipzig.irpact.jadex.agent.consumer;

import de.unileipzig.irpact.commons.annotation.Idea;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.product.AdoptedProductInfo;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.jadex.agent.JadexAgentBase;
import de.unileipzig.irpact.jadex.agent.JadexAgentService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.start.StartSimulation;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.*;
import jadex.bdiv3.features.IBDIAgentFeature;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.bridge.IInternalAccess;
import jadex.bridge.component.IArgumentsResultsFeature;
import jadex.bridge.component.IExecutionFeature;
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
@Service
@ProvidedServices({
        @ProvidedService(type = ConsumerAgentService.class),
        @ProvidedService(type = JadexAgentService.class)
})
@Agent(type = BDIAgentFactory.TYPE)
public class JadexConsumerAgentBDI extends JadexAgentBase
        implements ConsumerAgent, ConsumerAgentService, JadexAgentService {

    //Argument names
    public static final String AGENT_BASE = StartSimulation.AGENT_BASE;

    //general
    private static final Logger logger = LoggerFactory.getLogger(JadexConsumerAgentBDI.class);
    private final JadexConsumerAgentIdentifier IDENTIFIER = new JadexConsumerAgentIdentifier();

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

    //ConsumerAgent parameter
    protected ConsumerAgentBase agentBase;

    //Beliefs
    @Belief
    protected Set<Need> needs = new HashSet<>();
    @Belief
    protected Set<Product> knownProducts = new HashSet<>();
    @Belief
    protected Set<ProductGroup> knownProductGroups = new HashSet<>();
    @Belief
    protected Set<AdoptedProductInfo> adoptedProducts = new HashSet<>();

    //=========================
    //Constructer
    //=========================

    public JadexConsumerAgentBDI() {
    }

    //=========================
    //ConsumerAgent
    //=========================

    @Override
    public ConsumerAgentGroup getGroup() {
        return agentBase.getGroup();
    }

    @Override
    public double getInformationAuthority() {
        return agentBase.getInformationAuthority();
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return agentBase.getSpatialInformation();
    }

    @Override
    public String getName() {
        return agentBase.getName();
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return (JadexSimulationEnvironment) agentBase.getEnvironment();
    }

    @Override
    public Set<ConsumerAgentAttribute> getAttributes() {
        return agentBase.getAttributes();
    }

    @Override
    public Set<Need> getNeeds() {
        return needs;
    }

    @Override
    public Set<Product> getKnownProducts() {
        return knownProducts;
    }

    @Override
    public Set<ProductGroup> getKnownProductGroups() {
        return knownProductGroups;
    }

    @Override
    public ConsumerAgentIdentifier getIdentifier() {
        return IDENTIFIER;
    }

    //=========================
    //JadexAgentBase
    //=========================

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
    public JadexConsumerAgentBDI getConsumerAgentSyn() {
        return this;
    }

    @Override
    public IFuture<JadexConsumerAgentBDI> getConsumerAgentAsyn() {
        return new Future<>(this);
    }

    //=========================
    //JadexAgentService
    //=========================

    @Override
    public JadexConsumerAgentBDI getAgentSyn() {
        return getConsumerAgentSyn();
    }

    @Override
    public IFuture<JadexConsumerAgentBDI> getAgentAsyn() {
        return getConsumerAgentAsyn();
    }

    //=========================
    //handle product need goal
    //=========================

    @Belief
    protected int handleNeedRecurTrigger = 0;

    @Goal(recur = true)
    public class HandleNeedGoal {

        @GoalParameter
        protected Need need;

        @GoalCreationCondition(factadded = "needs")
        public HandleNeedGoal(Need need) {
            this.need = need;
        }

        @GoalRecurCondition(beliefs = "handleNeedRecurTrigger")
        public boolean checkRecur() {
            return true;
        }
    }

    @Plan(trigger = @Trigger(goals = HandleNeedGoal.class))
    protected void handleProductNeed(Need need) {
        Collection<? extends Product> potentialProducts = getGroup().getFindingScheme()
                .searchPotentialProducts(getEnvironment(), this, need);
        if(potentialProducts.isEmpty()) {
            throw new PlanFailureException();
        } else {
            Product potentialProduct = getGroup().getAdoptionDecisionScheme()
                    .decide(getEnvironment(), this, potentialProducts);
            AdoptedProductInfo adoptedProduct = new AdoptedProductInfo(need, potentialProduct);
            adoptedProducts.add(adoptedProduct);
            bdiFeature.dispatchTopLevelGoal(new NeedSatisfyGoal(need));
        }
    }

    @Idea("caller mit schedule notwenig? bzw bessere art der implementierung?")
    public void fireHandleProductNeedRecur(Object caller) {
        if(caller == this) {
            handleNeedRecurTrigger = handleNeedRecurTrigger + 1;
        } else {
            agent.scheduleStep(cs -> {
                fireHandleProductNeedRecur(JadexConsumerAgentBDI.this);
                return IFuture.DONE;
            });
        }
    }

    //=========================
    //handle new need
    //=========================

    public void developNewNeeds() {
        Collection<? extends Need> newNeeds = getGroup().getNeedDevelopmentScheme()
                .developNeeds(this);
        needs.addAll(newNeeds);
    }

    //=========================
    //handle need expiration
    //=========================

    public void expireNeeds() {
        Collection<? extends Need> expiredNeeds = getGroup().getNeedExpirationScheme()
                .expiredNeeds(this);
        needs.removeAll(expiredNeeds);
    }

    //=========================
    //handle need satisfy
    //=========================

    @Goal
    public class NeedSatisfyGoal {

        @GoalParameter
        protected Need satisfiedNeed;

        @GoalCreationCondition(factremoved = "needs")
        public NeedSatisfyGoal(Need satisfiedNeed) {
            this.satisfiedNeed = satisfiedNeed;
        }
    }

    @Plan(trigger = @Trigger(goals = NeedSatisfyGoal.class))
    protected void handleNeedSatisfy(Need satisfiedNeed) {
        getGroup().getNeedSatisfyScheme()
                .handle(this, satisfiedNeed);
    }
}
