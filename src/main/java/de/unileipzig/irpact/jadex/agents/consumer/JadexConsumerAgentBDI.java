package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.*;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
import de.unileipzig.irpact.core.simulation.IRPactAgentAPI;
import de.unileipzig.irpact.jadex.agents.simulation.SimulationService;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.util.JadexUtil;
import de.unileipzig.irpact.core.spatial.SpatialInformation;
import de.unileipzig.irpact.start.irpact.IRPact;
import de.unileipzig.irptools.util.log.IRPLogger;
import jadex.bdiv3.BDIAgentFactory;
import jadex.bdiv3.annotation.Belief;
import jadex.micro.annotation.Agent;
import jadex.micro.annotation.RequiredService;
import jadex.micro.annotation.RequiredServices;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
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
public class JadexConsumerAgentBDI extends AbstractJadexAgentBDI implements ConsumerAgent, IRPactAgentAPI {

    protected static final IRPLogger LOGGER = IRPLogging.getLogger(JadexConsumerAgentBDI.class);

    protected ProxyConsumerAgent proxyAgent;

    protected JadexConsumerAgentGroup group;
    protected SpatialInformation spatialInformation;
    protected SimulationService simulationService;
    protected Map<String, ConsumerAgentAttribute> attributes = new LinkedHashMap<>();
    protected Map<String, ConsumerAgentProductRelatedAttribute> productRelatedAttributes = new LinkedHashMap<>();
    protected double informationAuthority;
    protected SocialGraph.Node node;
    protected ProductAwareness productAwareness;
    protected ProductInterest productInterest;
    protected Map<Product, AdoptedProduct> adoptedProducts = new LinkedHashMap<>();
    protected ProductFindingScheme productFindingScheme;
    protected ProcessFindingScheme processFindingScheme;
    protected Set<AttributeAccess> externAttributes = new LinkedHashSet<>();

    protected final Lock DATA_LOCK = new ReentrantLock();
    protected final Lock ACCESS_LOCK = new ReentrantLock();
    protected final Condition WAIT_FOR_ACCESSIBILITY = ACCESS_LOCK.newCondition();
    protected boolean accessibleForActions = false;
    protected int actionsInThisStep = 0;
    protected int maxNumberOfActions;
    protected long actingOrder;

    @Belief
    protected Set<Need> needs = new LinkedHashSet<>();
    @Belief
    protected Map<Need, ProcessPlan> activePlans = new LinkedHashMap<>();
    @Belief
    protected List<ProcessPlan> finishedPlans = new ArrayList<>();
    @Belief
    protected List<ProcessPlan> runningPlans = new ArrayList<>();

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
        JadexUtil.searchPlatformServices(reqFeature, SimulationService.class, result -> {
            if(simulationService == null) {
                setupAgent(result);
            }
        });
    }

    protected void setupAgent(SimulationService result) {
        log().trace(IRPSection.INITIALIZATION_PLATFORM, "[{}] SimulationService found", getName());
        simulationService = result;
        simulationService.reportAgentCreated(getThisAgent());
        if(agent != null) {
            simulationService.registerAgentForFastTermination(agent);
        }
    }

    //=========================
    //livecycle
    //=========================

    @Override
    protected void onInit() {
        initData();
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] init ({})", getName(), now());
        searchSimulationService();
    }

    @Override
    protected void onStart() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] start ({})", getName(), now());
        scheduleFirstAction();
    }

    @Override
    protected void onEnd() {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] end ({})", getName(), now());
        runOnEnd();
    }

    //=========================
    //IRPactAgentAPI
    //=========================

    @Override
    public void initIRPactAgent(
            Map<String, Object> param,
            SimulationService simulationService) throws Throwable {
        initData(param);
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] init ({})", getName(), now());
        setupAgent(simulationService);
    }

    @Override
    public void startIRPactAgent() throws Throwable {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] start ({})", getName(), now());
    }

    @Override
    public void firstIRPactAgentAction(List<PostAction> postActions) throws Throwable {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] first action ({})", getName(), now());
        nextIRPactAgentLoopAction(postActions);
    }

    @Override
    public Map<String, Object> endIRPactAgent() throws Throwable {
        log().trace(IRPSection.SIMULATION_LIFECYCLE, "[{}] end ({})", getName(), now());
        runOnEnd();
        return Collections.emptyMap();
    }

    @Override
    public void nextIRPactAgentLoopAction(List<PostAction> postActions) throws Throwable {
        pulse();

        resetOnNewAction();

        waitForYearChangeIfRequired();

        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start next action ({})", getName(), now());
        waitForSynchronisationAtStartIfRequired();
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] post first sync", getName());

        if(hasPlans()) {
            executePlans(postActions);
        } else {
            allowAttention();
        }

        log().trace(IRPSection.SIMULATION_AGENT, "[{}] end action ({})", getName(), now());
        waitForSynchronisationAtEndIfRequired();
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] post end sync", getName());
    }

    //=========================
    //ConsumerAgent
    //=========================

    protected ProxyConsumerAgent getProxy(Map<String, Object> params) {
        Object obj = params.get(IRPact.PROXY);
        if(obj instanceof ProxyConsumerAgent) {
            return (ProxyConsumerAgent) obj;
        } else {
            throw new IllegalStateException("ProxyConsumerAgent not found");
        }
    }

    protected void initData() {
        initData(resultsFeature.getArguments());
    }

    protected void initData(Map<String, Object> params) {
        proxyAgent = getProxy(params);
        name = proxyAgent.getName();
        node = proxyAgent.getSocialGraphNode();
        group = (JadexConsumerAgentGroup) proxyAgent.getGroup();
        environment = (JadexSimulationEnvironment) proxyAgent.getEnvironment();
        informationAuthority = proxyAgent.getInformationAuthority();
        maxNumberOfActions = proxyAgent.getMaxNumberOfActions();
        actingOrder = proxyAgent.getActingOrder();
        spatialInformation = proxyAgent.getSpatialInformation();
        for(ConsumerAgentAttribute attr: proxyAgent.getAttributes()) {
            attributes.put(attr.getName(), attr);
        }
        for(ConsumerAgentProductRelatedAttribute attr: proxyAgent.getProductRelatedAttributes()) {
            productRelatedAttributes.put(attr.getName(), attr);
        }
        productAwareness = proxyAgent.getProductAwareness();
        productInterest = proxyAgent.getProductInterest();
        for(AdoptedProduct adoptedProduct: proxyAgent.getAdoptedProducts()) {
            adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
        }
        processFindingScheme = proxyAgent.getProcessFindingScheme();
        productFindingScheme = proxyAgent.getProductFindingScheme();
        addAllInitialNeeds(proxyAgent.getNeeds()); //!
        addAllActivePlans(proxyAgent.getActivePlans()); //!
        addAllRunningPlans(proxyAgent.getRunningPlans()); //!
        setupFinishedPlans();
        externAttributes.addAll(proxyAgent.getExternAttributes());

        proxyAgent.sync(getRealAgent());
    }

    protected void runOnEnd() {
        //log().info("[{}] end", getName());
        proxyAgent.unsync(this);
    }

    @Override
    public ProductFindingScheme getProductFindingScheme() {
        return productFindingScheme;
    }

    @Override
    public ProcessFindingScheme getProcessFindingScheme() {
        return processFindingScheme;
    }

    @Override
    public Set<Need> getNeeds() {
        return needs;
    }

    @Override
    public boolean hasNeed(Need need) {
        return needs.contains(need);
    }

    @Override
    public void addNeed(Need need) {
        if(hasNeed(need)) {
            return;
        }

        needs.add(need);
        handleNewNeed(need, false);
    }

    protected void addAllInitialNeeds(Collection<Need> needs) {
        for(Need need: needs) {
            addInitialNeed(need);
        }
    }

    protected void addInitialNeed(Need need) {
        if(hasNeed(need)) {
            return;
        }

        needs.add(need);
        handleNewNeed(need, true);
    }

    protected void addAllActivePlans(Map<Need, ProcessPlan> plans) {
        activePlans.putAll(plans);
    }

    protected void addAllRunningPlans(Collection<ProcessPlan> plans) {
        runningPlans.addAll(plans);
    }

    protected void setupFinishedPlans() {
        for(ProcessPlan plan: runningPlans) {
            if(isFinished(plan)) {
                finishedPlans.add(plan);
            }
        }
    }

    protected boolean isFinished(ProcessPlan plan) {
        return !activePlans.containsValue(plan);
    }

    protected void addFinishedPlan(ProcessPlan plan) {
        finishedPlans.add(plan);
        runningPlans.add(plan);
    }

    protected void addActivePlan(Need need, ProcessPlan plan) {
        activePlans.put(need, plan);
        runningPlans.add(plan);
    }

    @Override
    public Map<Need, ProcessPlan> getActivePlans() {
        return activePlans;
    }

    @Override
    public List<ProcessPlan> getRunningPlans() {
        return runningPlans;
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
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' already exists", attribute.getName());
        } else {
            attributes.put(attribute.getName(), attribute);
        }
    }

    @Override
    public boolean hasAttribute(String name) {
        return attributes.containsKey(name);
    }

    @Override
    public Collection<ConsumerAgentProductRelatedAttribute> getProductRelatedAttributes() {
        return productRelatedAttributes.values();
    }

    @Override
    public boolean hasProductRelatedAttribute(String name) {
        return productRelatedAttributes.containsKey(name);
    }

    @Override
    public ConsumerAgentProductRelatedAttribute getProductRelatedAttribute(String name) {
        return productRelatedAttributes.get(name);
    }

    @Override
    public void addProductRelatedAttribute(ConsumerAgentProductRelatedAttribute attribute) {
        if(hasProductRelatedAttribute(attribute)) {
            throw ExceptionUtil.create(IllegalArgumentException::new, "attribute '{}' already exists", attribute.getName());
        }
        productRelatedAttributes.put(attribute.getName(), attribute);
    }

    @Override
    public void updateProductRelatedAttributes(Product newProduct) {
        ProductGroup productGroup = newProduct.getGroup();
        for(ConsumerAgentProductRelatedAttribute relatedAttribute: getProductRelatedAttributes()) {
            relatedAttribute.getGroup().deriveUpdate(newProduct, relatedAttribute);
        }
    }

    @Override
    public ProductAwareness getProductAwareness() {
        return productAwareness;
    }

    @Override
    public boolean isAware(Product product) {
        return productAwareness.isAware(product);
    }

    @Override
    public void makeAware(Product product) {
        productAwareness.makeAware(product);
    }

    @Override
    public boolean isInterested(Product product) {
        return productInterest.isInterested(product);
    }

    @Override
    public double getInterest(Product product) {
        return productInterest.getValue(product);
    }

    @Override
    public void updateInterest(Product product, double value) {
        productInterest.update(product, value);
    }

    @Override
    public void makeInterested(Product product) {
        productInterest.makeInterested(product);
    }

    @Override
    public ProductInterest getProductInterest() {
        return productInterest;
    }

    @Override
    public double getInformationAuthority() {
        return informationAuthority;
    }

    @Override
    public int getMaxNumberOfActions() {
        return maxNumberOfActions;
    }

    @Override
    public long getActingOrder() {
        return actingOrder;
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
    public int getChecksum() {
        return Objects.hash(
                getName(),
                getGroup().getName(),
                getInformationAuthority(),
                getSpatialInformation().getChecksum(),
                ChecksumComparable.getCollChecksum(getAttributes()),
                getProductInterest().getChecksum(),
                ChecksumComparable.getCollChecksum(getAdoptedProducts()),
                getProductFindingScheme().getChecksum(),
                getProcessFindingScheme().getChecksum(),
                ChecksumComparable.getCollChecksum(getNeeds()),
                ChecksumComparable.getMapChecksum(getActivePlans()),
                ChecksumComparable.getCollChecksum(externAttributes)
        );
    }

    @Override
    public void allowAttention() {
        if(!accessibleForActions) {
            ACCESS_LOCK.lock();
            try {
                accessibleForActions = true;
                if(actionsInThisStep < maxNumberOfActions) {
                    WAIT_FOR_ACCESSIBILITY.signal();
                } else {
                    WAIT_FOR_ACCESSIBILITY.signalAll();
                }
            } finally {
                ACCESS_LOCK.unlock();
            }
        }
    }

    @Override
    public boolean tryAquireAttention() {
        if(actionsInThisStep < maxNumberOfActions) {
            ACCESS_LOCK.lock();
            while(!accessibleForActions) {
                try {
                    WAIT_FOR_ACCESSIBILITY.await();
                } catch (InterruptedException e) {
                    LOGGER.warn(IRPSection.SIMULATION_PROCESS, "InterruptedException @ agent {}: {}", getName(), StringUtil.printStackTrace(e));
                }
            }

            if(actionsInThisStep < maxNumberOfActions) {
                //keep lock -> releaseAttention
                return true;
            } else {
                //release all
                WAIT_FOR_ACCESSIBILITY.signalAll();
                ACCESS_LOCK.unlock();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed() {
        if(actionsInThisStep < maxNumberOfActions) {
            actionsInThisStep++;
        }
    }

    @Override
    public void releaseAttention() {
        try {
            if(actionsInThisStep < maxNumberOfActions) {
                WAIT_FOR_ACCESSIBILITY.signal();
            } else {
                WAIT_FOR_ACCESSIBILITY.signalAll();
            }
        } finally {
            ACCESS_LOCK.unlock();
        }
    }

    @Override
    public void aquireDataAccess() {
        DATA_LOCK.lock();
    }

    @Override
    public boolean tryAquireDataAccess() {
        return DATA_LOCK.tryLock();
    }

    @Override
    public boolean tryAquireDataAccess(long time, TimeUnit unit) throws InterruptedException {
        return DATA_LOCK.tryLock(time, unit);
    }

    @Override
    public void releaseDataAccess() {
        DATA_LOCK.unlock();
    }

    @Override
    public SpatialInformation getSpatialInformation() {
        return spatialInformation;
    }

    @Override
    public Collection<AdoptedProduct> getAdoptedProducts() {
        return adoptedProducts.values();
    }

    @Override
    public AdoptedProduct getAdoptedProduct(Product product) {
        return adoptedProducts.get(product);
    }

    @Override
    public boolean hasAdopted(Product product) {
        return adoptedProducts.containsKey(product);
    }

    @Override
    public boolean hasInitialAdopted(Product product) {
        AdoptedProduct adoptedProduct = adoptedProducts.get(product);
        return adoptedProduct != null && adoptedProduct.isInitial();
    }

    @Override
    public void addAdoptedProduct(AdoptedProduct adoptedProduct) {
        adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
    }

    @Override
    public void adoptInitial(Product product) {
        AdoptedProduct adoptedProduct = new BasicAdoptedProduct(null, product, null, AdoptionPhase.INITIAL);
        addAdoptedProduct(adoptedProduct);
    }

    @Override
    public void adopt(Need need, Product product, Timestamp stamp, AdoptionPhase phase) {
        adopt(need, product, stamp, phase, Double.NaN);
    }

    @Override
    public void adopt(Need need, Product product, Timestamp stamp, AdoptionPhase phase, double utility) {
        if(needs.contains(need)) {
            AdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, stamp, phase, utility);
            needs.remove(need);
            addAdoptedProduct(adoptedProduct);
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] adopt '{}' at {}", getName(), product.getName(), stamp);
        } else {
            LOGGER.warn("need '{}' does not exist", need.getName());
        }
    }

    @Override
    public boolean linkAccess(AttributeAccess attributeAccess) {
        return externAttributes.add(attributeAccess);
    }

    @Override
    public boolean unlinkAccess(AttributeAccess attributeAccess) {
        return externAttributes.remove(attributeAccess);
    }

    @Override
    public boolean hasAnyAttribute(String name) {
        if(hasAttribute(name)) {
            return true;
        }
        for(AttributeAccess attributeAccess: externAttributes) {
            if(attributeAccess.hasAttribute(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Attribute findAttribute(String name) {
        Attribute attr = getAttribute(name);
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
    protected void firstAction() throws Throwable {
        //bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(null, null));
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start loop ({})", getName(), now());
        onLoopAction();
        scheduleLoop();
    }

    protected boolean hasPlans() {
        return !runningPlans.isEmpty();
    }

    @Override
    protected void onLoopAction() throws Throwable {
        //vor allen anderen checks
        resetOnNewAction();

        waitForYearChangeIfRequired();

        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start next action ({})", getName(), now());
        waitForSynchronisationAtStartIfRequired();
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] post first sync", getName());

        if(hasPlans()) {
            executePlans(null);
        } else {
            allowAttention();
        }

        log().trace(IRPSection.SIMULATION_AGENT, "[{}] end action ({})", getName(), now());
        waitForSynchronisationAtEndIfRequired();
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] post end sync", getName());
    }

    protected void executePlans(List<PostAction> postActions) throws Throwable {
        Map<Need, ProcessPlan> adoptedPlans = null;

        for(Map.Entry<Need, ProcessPlan> entry: getActivePlans().entrySet()) {
            if(executePlan(entry.getValue(), postActions) == ProcessPlanResult.ADOPTED) {
                if(adoptedPlans == null) adoptedPlans = new HashMap<>();
                adoptedPlans.put(entry.getKey(), entry.getValue());
            }
        }

        for(ProcessPlan finished: finishedPlans) {
            executePlan(finished, postActions);
        }

        if(adoptedPlans != null) {
            for(Map.Entry<Need, ProcessPlan> entry: adoptedPlans.entrySet()) {
                getActivePlans().remove(entry.getKey());
                if(entry.getValue().keepRunningAfterAdoption()) {
                    log().debug(IRPSection.SIMULATION_PROCESS, "[{}] keep running after adoption: {}", getName(), entry.getValue());
                    finishedPlans.add(entry.getValue());
                } else {
                    runningPlans.remove(entry.getValue());
                }
            }
        }
    }

    protected void resetOnNewAction() {
        accessibleForActions = false;
        actionsInThisStep = 0;
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] reset aktion counter", getName());
    }

    //=========================
    //new need
    //=========================

//    @Goal
//    public class FindProcessForNeedGoal {
//
//        protected Need need;
//
//        public FindProcessForNeedGoal(Need need) {
//            this.need = need;
//        }
//
//        public Need getNeed() {
//            return need;
//        }
//    }
//
//    @Plan(trigger = @Trigger(goals = FindProcessForNeedGoal.class))
//    protected void handleNewNeed(FindProcessForNeedGoal goal) throws PlanFailureException {
//        Need need = goal.getNeed();
//        Product product = productFindingScheme.findProduct(getThisAgent(), need);
//        if(product == null) {
//            throw new PlanFailureException();
//        }
//        ProcessModel model = processFindingScheme.findModel(product);
//        if(model == null) {
//            throw new PlanFailureException();
//        }
//        ProcessPlan plan = model.newPlan(getThisAgent(), need, product);
//        plans.put(need, plan);
//        bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(need, plan));
//    }

    protected void handleNewNeed(Need need, boolean initial) {
        Product product = productFindingScheme.findProduct(getThisAgent(), need);
        if(product == null) {
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] no product found for need '{}'", getName(), need.getName());
            return;
        }

        ProcessModel model = processFindingScheme.findModel(product);
        if(model == null) {
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] no process model found for product '{}'", getName(), product.getName());
            return;
        }

        ProcessPlan plan = model.newPlan(getThisAgent(), need, product);
        if(plan == null) {
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] no process plan found for product '{}' and need '{}'", getName(), product.getName(), need.getName());
            return;
        }

        if(hasInitialAdopted(product)) {
            needs.remove(need);
            if(plan.keepRunningAfterAdoption()) {
                LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] product='{}' initally adoped, keep running", getName(), product.getName());
                addFinishedPlan(plan);
            } else {
                LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] product='{}' initally adoped, finished plan", getName(), product.getName());
            }
        } else {
            addActivePlan(need, plan);
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] added plan for need='{}' and product='{}'", getName(), need.getName(), product.getName());
        }
    }

    //=========================
    //new plan
    //=========================

//    //@Goal(excludemode = MProcessableElement.ExcludeMode.WhenSucceeded, retry = true, retrydelay = 1)
//    @Goal
//    public class ProcessExecutionGoal {
//
//        protected Need need;
//        protected ProcessPlan plan;
//
//        public ProcessExecutionGoal(Need need, ProcessPlan plan) {
//            this.need = need;
//            this.plan = plan;
//        }
//
//        public Need getNeed() {
//            return need;
//        }
//
//        public ProcessPlan getPlan() {
//            return plan;
//        }
//    }
//
//    @Plan(trigger = @Trigger(goals = ProcessExecutionGoal.class))
//    protected void handleProcessExecution(ProcessExecutionGoal goal) {
//        log().trace("[{}] @ {} ({})", getName(), now(), System.identityHashCode(now()));
//        log().trace("[{}] pre sync", getName());
//        waitForSynchronisationIfRequired();
//        log().trace("[{}] post sync", getName());
////        ProcessPlan plan = goal.getPlan();
////        ProcessPlanResult result = plan.execute();
////        switch(result) {
////            case ADOPTED:
////                break;
////
////            case IN_PROCESS:
////            default:
////                throw new PlanFailureException();
////        }
//    }

    protected ProcessPlanResult executePlan(ProcessPlan plan) throws Throwable {
        return plan.execute();
    }

    protected ProcessPlanResult executePlan(ProcessPlan plan, List<PostAction> postActions) throws Throwable {
        return plan.execute(postActions);
    }
}
