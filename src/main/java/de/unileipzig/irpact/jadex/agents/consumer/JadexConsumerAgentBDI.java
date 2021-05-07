package de.unileipzig.irpact.jadex.agents.consumer;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeAccess;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.ExceptionUtil;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ProcessFindingScheme;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.product.*;
import de.unileipzig.irpact.core.product.awareness.ProductAwareness;
import de.unileipzig.irpact.core.product.interest.ProductInterest;
import de.unileipzig.irpact.jadex.agents.AbstractJadexAgentBDI;
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
public class JadexConsumerAgentBDI extends AbstractJadexAgentBDI implements ConsumerAgent {

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

    @Belief
    protected Set<Need> needs = new LinkedHashSet<>();
    @Belief
    protected Map<Need, ProcessPlan> plans = new LinkedHashMap<>();

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
                log().trace(IRPSection.INITIALIZATION_PLATFORM, "[{}] SimulationService found", getName());
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
        proxyAgent.unsync(this);
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
        node = proxyAgent.getSocialGraphNode();
        group = (JadexConsumerAgentGroup) proxyAgent.getGroup();
        environment = (JadexSimulationEnvironment) proxyAgent.getEnvironment();
        informationAuthority = proxyAgent.getInformationAuthority();
        maxNumberOfActions = proxyAgent.getMaxNumberOfActions();
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
        addAllPlans(proxyAgent.getPlans()); //!
        externAttributes.addAll(proxyAgent.getExternAttributes());

        proxyAgent.sync(getRealAgent());
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

    protected void addAllPlans(Map<Need, ProcessPlan> plans) {
        for(Map.Entry<Need, ProcessPlan> entry: plans.entrySet()) {
            addPlan(entry.getKey(), entry.getValue());
        }
    }

    protected void addPlan(Need need, ProcessPlan plan) {
        plans.put(need, plan);
    }

    @Override
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
                ChecksumComparable.getMapChecksum(getPlans()),
                ChecksumComparable.getCollChecksum(externAttributes)
        );
    }

    @Override
    public void allowAttention() {
        if(!accessibleForActions) {
            accessibleForActions = true;
            ACCESS_LOCK.lock();
            try {
                WAIT_FOR_ACCESSIBILITY.signalAll();
            } finally {
                ACCESS_LOCK.unlock();
            }
        }
    }

    @Override
    public boolean tryAquireAttention() {
        if(actionsInThisStep < maxNumberOfActions) {
            while(!accessibleForActions) {
                ACCESS_LOCK.lock();
                try {
                    WAIT_FOR_ACCESSIBILITY.await();
                } catch (InterruptedException e) {
                    //ignore
                } finally {
                    ACCESS_LOCK.unlock();
                }
            }
            ACCESS_LOCK.lock();
            if(actionsInThisStep < maxNumberOfActions) {
                return true;
            } else {
                ACCESS_LOCK.unlock();
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public void actionPerformed() {
        actionsInThisStep++;
    }

    @Override
    public void releaseAttention() {
        ACCESS_LOCK.unlock();
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
    public boolean hasAdopted(Product product) {
        return adoptedProducts.containsKey(product);
    }

    protected boolean hasInitialAdopted(Product product) {
        AdoptedProduct adoptedProduct = adoptedProducts.get(product);
        return adoptedProduct != null && adoptedProduct.isInitial();
    }

    @Override
    public void addAdoptedProduct(AdoptedProduct adoptedProduct) {
        adoptedProducts.put(adoptedProduct.getProduct(), adoptedProduct);
    }

    @Override
    public void adoptInitial(Product product) {
        AdoptedProduct adoptedProduct = new BasicAdoptedProduct(product);
        addAdoptedProduct(adoptedProduct);
    }

    @Override
    public void adopt(Need need, Product product, Timestamp stamp) {
        if(needs.contains(need)) {
            AdoptedProduct adoptedProduct = new BasicAdoptedProduct(need, product, stamp);
            needs.remove(need);
            plans.remove(need);
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
    protected void firstAction() {
        //bdiFeature.dispatchTopLevelGoal(new ProcessExecutionGoal(null, null));
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start loop ({})", getName(), now());
        onLoopAction();
        scheduleLoop();
    }

    @Override
    protected void onLoopAction() {
        //vor allen anderen checks
        waitForYearChangeIfRequired();
        log().trace(IRPSection.SIMULATION_AGENT, "[{}] start next action ({})", getName(), now());

        resetOnNewAction();

        waitForSynchronisationIfRequired();

        for(ProcessPlan plan: getPlans().values())  {
            executePlan(plan);
        }
    }
    
    protected void resetOnNewAction() {
        accessibleForActions = false;
        actionsInThisStep = 0;
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
        if(hasInitialAdopted(product)) {
            needs.remove(need);
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] product='{}' initally adoped", getName(), product.getName());
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

        addPlan(need, plan);
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] added plan for need='{}' and product='{}'", getName(), need.getName(), product.getName());
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

    protected void executePlan(ProcessPlan plan) {
        plan.execute();
    }
}
