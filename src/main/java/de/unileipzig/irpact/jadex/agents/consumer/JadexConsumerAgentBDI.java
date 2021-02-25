package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.agent.consumer.ProxyConsumerAgent;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.start.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.runtime.impl.PlanFailureException;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("ALL")
@Agent(type = BDIAgentFactory.TYPE)
@RequiredServices(
        @RequiredService(type = SimulationService.class)
)
public class JadexConsumerAgentBDI extends AbstractJadexAgentBDI implements ConsumerAgent {

    protected static final IRPLogger LOGGER = IRPLogging.getLogger(JadexConsumerAgentBDI.class);

    protected ProxyConsumerAgent proxyAgent;

    protected JadexConsumerAgentGroup group;
    protected SpatialInformation spatialInformation;
    protected SimulationService simulationService;
    protected Map<String, ConsumerAgentAttribute> attributes = new HashMap<>();
    protected double informationAuthority;
    protected SocialGraph.Node node;
    protected ProductAwareness productAwareness;
    protected Set<AdoptedProduct> adoptedProducts = new HashSet<>();
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;
    protected Set<AttributeAccess> externAttributes = new HashSet<>();

    protected final Lock LOCK = new ReentrantLock();
    protected Timestamp currentStamp = null;
    protected int actionsInThisStep = 0;
    protected int maxActions = 1; //!!!

    @Belief
    protected Set<Need> needs = new HashSet<>();
    @Belief
    protected Map<Need, ProcessPlan> plans = new HashMap<>();

    public JadexConsumerAgentBDI() {
    }

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    protected ConsumerAgent getThisAgent() {
        return getProxyAgent();
    }

    @Override
    protected ProxyConsumerAgent getProxyAgent() {
        return proxyAgent;
    }

    @Override
    protected JadexConsumerAgentBDI getRealAgent() {
        return this;
    }

    protected void searchSimulationService() {
        JadexUtil2.searchPlatformServices(reqFeature, SimulationService.class, result -> {
            if(simulationService == null) {
                log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] SimulationService found", getName());
                simulationService = result;
                setupAgent();
            }
        });
    }

    protected void setupAgent() {
        //HIER KOMMEN AUFGABEN HIN
        //node setzen
        simulationService.reportAgentCreated(getThisAgent());
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] init", getName());
        searchSimulationService();
    }

    @Override
    protected void onStart() {
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] start", getName());
        scheduleFirstAction();
    }

    @Override
    protected void onEnd() {
        log().trace(IRPSection.INITIALIZATION_AGENT, "[{}] end", getName());
    }

    //=========================
    //ConsumerAgent
    //=========================

    protected ProxyConsumerAgent getProxy() {
        Object obj = resultsFeature.getArguments().get(IRPact.PROXY);
        if(obj instanceof ProxyConsumerAgent) {
            return (ProxyConsumerAgent) obj;
        } else {
            throw new IllegalStateException("ProxyConsumerAgent not found");
        }
    }

    protected void initData() {
        proxyAgent = getProxy();
        name = proxyAgent.getName();
        group = (JadexConsumerAgentGroup) proxyAgent.getGroup();
        environment = (JadexSimulationEnvironment) proxyAgent.getEnvironment();
        informationAuthority = proxyAgent.getInformationAuthority();
        spatialInformation = proxyAgent.getSpatialInformation();
        for(ConsumerAgentAttribute attr: proxyAgent.getAttributes()) {
            attributes.put(attr.getName(), attr);
        }
        productAwareness = proxyAgent.getProductAwareness();
        adoptedProducts.addAll(proxyAgent.getAdoptedProducts());
        processFindingScheme = proxyAgent.getProcessFindingScheme();
        productFindingScheme = proxyAgent.getProductFindingScheme();
        needs.addAll(proxyAgent.getNeeds()); //TODO plan etc aufrufen?
        plans.putAll(proxyAgent.getPlans()); //TODO plan etc aufrufen?
        externAttributes.addAll(proxyAgent.getExternAttributes());

        proxyAgent.sync(getRealAgent());
    }

    public ProductFindingScheme getProductFindingScheme() {
        return productFindingScheme;
    }

    public ProcessFindingScheme getProcessFindingScheme() {
        return processFindingScheme;
    }

    public Set<Need> getNeeds() {
        return needs;
    }

    public Map<Need, ProcessPlan> getPlans() {
        return plans;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public ConsumerAgentGroup getGroup() {
        return group;
    }

    @Override
    public Collection<ConsumerAgentAttribute> getAttributes() {
        return attributes.values();
    }

    @Override
    public ConsumerAgentAttribute getAttribute(String name) {
        return attributes.get(name);
    }

    @Override
    public void addAttribute(ConsumerAgentAttribute attribute) {
        if(attributes.containsKey(attribute.getName())) {
            throw new IllegalArgumentException("attribute '" + attribute.getName() + "' already exists");
        } else {
            attributes.put(attribute.getName(), attribute);
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public ProductAwareness getProductAwareness() {
        return productAwareness;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public JadexSimulationEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setSocialGraphNode(SocialGraph.Node node) {
        this.node = node;
    }

    @Override
    public SocialGraph.Node getSocialGraphNode() {
        return node;
    }

    @Override
    public void lockAction() {
        LOCK.lock();
    }

    @Override
    public void actionPerformed() {
        actionsInThisStep++;
    }

    @Override
    public void releaseAction() {
        LOCK.unlock();
    }

    @Override
    public boolean aquireAction() {
        LOCK.lock();
        try {
            Timestamp now = getEnvironment().getTimeModel().now();
            if(Objects.equals(currentStamp, now)) {
                if(actionsInThisStep >= maxActions) {
                    return false;
                }
                actionPerformed();
                return true;
            } else {
                currentStamp = now;
                actionsInThisStep = 1;
                return true;
            }
        } finally {
            LOCK.unlock();
        }
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }

    @Override
    public Set<AdoptedProduct> getAdoptedProducts() {
        return adoptedProducts;
    }

    @Override
    public boolean hasAdopted(Product product) {
        for(AdoptedProduct ap: adoptedProducts) {
            if(ap.getProduct() == product) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdoptedProduct(AdoptedProduct adoptedProduct) {
        adoptedProducts.add(adoptedProduct);
    }

    @Override
    public void adopt(Need need, Product product) {
        if(needs.contains(need)) {
            JadexTimestamp now = environment.getTimeModel().now();
            AdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, now);
            needs.remove(need);
            plans.remove(need);
            addAdoptedProduct(adoptedProduct);
        } else {
            throw new IllegalStateException("need '" + need.getName() + "' not exists");
        }
    }

    @Override
    public boolean link(AttributeAccess attributeAccess) {
        return externAttributes.add(attributeAccess);
    }

    @Override
    public boolean unlink(AttributeAccess attributeAccess) {
        return externAttributes.remove(attributeAccess);
    }

    @Override
    public Attribute<?> findAttribute(String name) {
        ConsumerAgentAttribute attr = getAttribute(name);
        if(attr != null) {
            return attr;
        }
        for(AttributeAccess attributeAccess: externAttributes) {
            if(attributeAccess.hasAttribute(name)) {
                return attributeAccess.getAttribute(name);
            }
        }
        return null;
    }

    @Override
    protected void firstAction() {
        bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(null, null));
        log().debug("[{}] start loop", getName());
        scheduleLoop();
    }

    @Override
    protected void loopAction() {
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] loop @ {}", getName(), now());
    }

    //=========================
    //new need
    //=========================

    @Goal
    public class FindProcessForNeedGoal {

        protected Need need;

        public FindProcessForNeedGoal(Need need) {
            this.need = need;
        }

        public Need getNeed() {
            return need;
        }
    }

    @Plan(trigger = @Trigger(goals = FindProcessForNeedGoal.class))
    protected void handleNewNeed(FindProcessForNeedGoal goal) throws PlanFailureException {
        Need need = goal.getNeed();
        Product product = productFindingScheme.findProduct(getThisAgent(), need);
        if(product == null) {
            throw new PlanFailureException();
        }
        ProcessModel model = processFindingScheme.findModel(product);
        if(model == null) {
            throw new PlanFailureException();
        }
        ProcessPlan plan = model.newPlan(getThisAgent(), need, product);
        plans.put(need, plan);
        bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(need, plan));
    }

    //=========================
    //new plan
    //=========================

    //@Goal(excludemode = MProcessableElement.ExcludeMode.WhenSucceeded, retry = true, retrydelay = 1)
    @Goal
    public class ProcessExecutionGoal {

        protected Need need;
        protected ProcessPlan plan;

        public ProcessExecutionGoal(Need need, ProcessPlan plan) {
            this.need = need;
            this.plan = plan;
        }

        public Need getNeed() {
            return need;
        }

        public ProcessPlan getPlan() {
            return plan;
        }
    }

    @Plan(trigger = @Trigger(goals = ProcessExecutionGoal.class))
    protected void handleProcessExecution(ProcessExecutionGoal goal) {
        log().debug("[{}] @ {} ({})", getName(), now(), System.identityHashCode(now()));
        log().debug("[{}] pre sync", getName());
        waitForSynchronisationIfRequired();
        log().debug("[{}] post sync", getName());
//        ProcessPlan plan = goal.getPlan();
//        ProcessPlanResult result = plan.execute();
//        switch(result) {
//            case ADOPTED:
//                break;
//
//            case IN_PROCESS:
//            default:
//                throw new PlanFailureException();
//        }
    }
}
