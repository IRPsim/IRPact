package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.LoggingPart;
import de.unileipzig.irpact.core.log.LoggingType;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.product.AdoptedProduct;
import de.unileipzig.irpact.core.product.BasicAdoptedProduct;
import de.unileipzig.irpact.core.product.ProductFindingScheme;
import de.unileipzig.irpact.jadex.JadexConstants;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.JadexTimestamp;
import de.unileipzig.irpact.jadex.util.JadexUtil2;
import de.unileipzig.irpact.commons.awareness.Awareness;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.bdiv3.annotation.Goal;
import jadex.bdiv3.annotation.Plan;
import jadex.bdiv3.annotation.Trigger;
import jadex.bdiv3.model.MProcessableElement;
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

    protected JadexConsumerAgentGroup group;
    protected SpatialInformation spatialInformation;
    protected SimulationService simulationService;
    protected Set<ConsumerAgentAttribute> attributes = new HashSet<>();
    protected double informationAuthority;
    protected SocialGraph.Node node;
    protected Awareness<Product> productAwareness;
    protected Set<AdoptedProduct> adoptedProducts = new HashSet<>();
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;

    protected final Lock LOCK = new ReentrantLock();
    protected Timestamp lastStamp = null;
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

    protected void searchSimulationService() {
        JadexUtil2.searchPlatformServices(reqFeature, SimulationService.class, result -> {
            if(simulationService == null) {
                log().trace(LoggingType.INITIALIZATION, LoggingPart.AGENT, "[{}] SimulationService found", getName());
                simulationService = result;
                setupAgent();
            }
        });
    }

    protected void setupAgent() {
        //HIER KOMMEN AUFGABEN HIN
        //node setzen
        simulationService.reportAgentCreated(this);
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        searchSimulationService();
        log().trace(LoggingType.INITIALIZATION, LoggingPart.AGENT, "[{}] init", getName());
    }

    @Override
    protected void onStart() {
        log().trace(LoggingType.INITIALIZATION, LoggingPart.AGENT, "[{}] start", getName());
    }

    @Override
    protected void onEnd() {
        log().trace(LoggingType.INITIALIZATION, LoggingPart.AGENT, "[{}] end", getName());
    }

    //=========================
    //ConsumerAgent
    //=========================

    protected ConsumerAgentInitializationData getData() {
        return (ConsumerAgentInitializationData) resultsFeature.getArguments().get(JadexConstants.DATA);
    }

    protected void initData() {
        ConsumerAgentInitializationData data = getData();
        name = data.getName();
        group = (JadexConsumerAgentGroup) data.getGroup();
        environment = (JadexSimulationEnvironment) data.getEnvironment();
        informationAuthority = data.getInformationAuthority();
        spatialInformation = data.getSpatialInformation();
        attributes.addAll(data.getAttributes());
        productAwareness = data.getProductAwareness();
        adoptedProducts.addAll(data.getAdoptedProducts());
        processFindingScheme = data.getProcessFindingScheme();
        productFindingScheme = data.getProductFindingScheme();

        environment.replace(data.getPlaceholder(), this);
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
    public Set<ConsumerAgentAttribute> getAttributes() {
        return attributes;
    }

    @Override
    public ConsumerAgentAttribute getAttribute(String name) {
        for(ConsumerAgentAttribute attribute: attributes) {
            if(Objects.equals(attribute.getName(), name)) {
                return attribute;
            }
        }
        return null;
    }

    @Override
    public boolean addAttribute(ConsumerAgentAttribute attribute) {
        return attributes.add(attribute);
    }

    @Override
    public Awareness<Product> getProductAwareness() {
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
    public boolean aquireAction() {
        LOCK.lock();
        try {
            Timestamp now = getEnvironment().getTimeModel().now();
            if(Objects.equals(now, lastStamp)) {
                if(actionsInThisStep >= maxActions) {
                    return false;
                }
                actionsInThisStep++;
                return true;
            } else {
                lastStamp = now;
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
    public boolean adopt(Need need, Product product) {
        if(needs.contains(need)) {
            JadexTimestamp now = environment.getTimeModel().now();
            AdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, now);
            adoptedProducts.add(adoptedProduct);
            needs.remove(need);
            ProcessPlan plan = plans.remove(need);
            return true;
        } else {
            return false;
        }
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
        Product product = productFindingScheme.findProduct(this, need);
        if(product == null) {
            throw new PlanFailureException();
        }
        ProcessModel model = processFindingScheme.findModel(product);
        if(model == null) {
            throw new PlanFailureException();
        }
        ProcessPlan plan = model.newPlan(this, need, product);
        plans.put(need, plan);
        bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(need, plan));
    }

    //=========================
    //new plan
    //=========================

    @Goal(excludemode = MProcessableElement.ExcludeMode.WhenSucceeded, retry = true, retrydelay = 1)
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
        ProcessPlan plan = goal.getPlan();
        ProcessPlanResult result = plan.execute();
        switch(result) {
            case ADOPTED:
                break;

            case IN_PROCESS:
            default:
                throw new PlanFailureException();
        }
    }
}
