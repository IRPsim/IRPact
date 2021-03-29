package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.InfoTag;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeDistanceFilter;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.develop.TodoException;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
/*
 * http://jasss.soc.surrey.ac.uk/5/4/1.html
 */
public class RAProcessPlan implements ProcessPlan {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlan.class);

    protected final Predicate<SocialGraph.Node> IS_CONSUMER = node -> node.is(ConsumerAgent.class);
    protected final Function<SocialGraph.Node, ConsumerAgent> TO_CONSUMER = node -> node.getAgent(ConsumerAgent.class);
    protected final Predicate<ConsumerAgent> IS_ADOPTER = ca -> ca.hasAdopted(getProduct());

    protected SimulationEnvironment environment;
    protected Need need;
    protected Product product;
    protected ConsumerAgent agent;
    protected Rnd rnd;
    protected RAProcessModel model;
    protected NodeDistanceFilter distanceFilter;

    protected RAStage currentStage = RAStage.PRE_INITIALIZATION;

    public RAProcessPlan() {
    }

    public RAProcessPlan(
            SimulationEnvironment environment,
            RAProcessModel model,
            Rnd rnd,
            ConsumerAgent agent,
            Need need,
            Product product) {
        setEnvironment(environment);
        setModel(model);
        setRnd(rnd);
        setAgent(agent);
        setNeed(need);
        setProduct(product);
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getNeed().getChecksum(),
                getProduct().getChecksum(),
                getAgent().getName(), //loop sonst
                getRnd().getChecksum(),
                getModel().getName(),
                getCurrentStage().getChecksum()
        );
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void setModel(RAProcessModel model) {
        this.model = model;
    }

    public RAProcessModel getModel() {
        return model;
    }

    public void setRnd(Rnd rnd) {
        this.rnd = rnd;
        rnd.enableSync();
    }

    public Rnd getRnd() {
        return rnd;
    }

    public void setAgent(ConsumerAgent agent) {
        this.agent = agent;
    }

    public ConsumerAgent getAgent() {
        return agent;
    }

    public void setNeed(Need need) {
        this.need = need;
    }

    public Need getNeed() {
        return need;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setCurrentStage(RAStage currentStage) {
        this.currentStage = currentStage;
    }

    public RAStage getCurrentStage() {
        return currentStage;
    }

    public double getLogisticFactor() {
        return modelData().getLogisticFactor();
    }

    @Override
    public ProcessPlanResult execute() {
        if(currentStage == RAStage.PRE_INITIALIZATION) {
            return initPlan();
        } else {
            return executePlan();
        }
    }

    //=========================
    //phases
    //=========================

    protected void adjustParametersOnNewYear() {
        if(currentStage == RAStage.IMPEDED) {
            currentStage = RAStage.DECISION_MAKING;
        }
    }

    protected void updateConstructionAndRenovation() {
        double renovationRate = RAProcessPlan.getRenovationRate(agent);
        boolean doRenovation = rnd.nextDouble() < renovationRate;
        RAProcessPlan.setUnderRenovation(agent, doRenovation);

        double constructionRate = RAProcessPlan.getConstructionRate(agent);
        boolean doConstruction = rnd.nextDouble() < constructionRate;
        RAProcessPlan.setUnderConstruction(agent, doConstruction);
    }

    protected ProcessPlanResult initPlan() {
        if(agent.hasAdopted(product)) {
            currentStage = RAStage.ADOPTED;
        } else {
            currentStage = RAStage.AWARENESS;
        }
        LOGGER.debug(IRPSection.INITIALIZATION_AGENT, "initial stage for '{}': {}", agent.getName(), currentStage);
        return executePlan();
    }

    protected ProcessPlanResult executePlan() {
        switch (currentStage) {
            case AWARENESS:
                return handleInterest();

            case FEASIBILITY:
                return handleFeasibility();

            case DECISION_MAKING:
                return handleDecisionMaking();

            case ADOPTED:
            case IMPEDED:
                return doAction();

            default:
                throw new IllegalStateException("unknown phase: " + currentStage);
        }
    }
    
    protected void doSelfActionAndAllowAttention() {
        agent.actionPerformed();
        agent.allowAttention();
    }

    protected ProcessPlanResult handleInterest() {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle interest", agent.getName());

        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] current interest for '{}': {}", agent.getName(), product.getName(), getInterest(agent));

        if(isInterested(agent)) {
            doSelfActionAndAllowAttention();
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] is interested in '{}'", agent.getName(), product.getName());
            updateStage(RAStage.FEASIBILITY);
            return ProcessPlanResult.IN_PROCESS;
        }

        if(isAware(agent)) {
            boolean underConstruction = isUnderConstruction(agent);
            boolean underRenovation = isUnderRenovation(agent);
            if(underConstruction || underRenovation) {
                makeInterested(agent);
                doSelfActionAndAllowAttention();
                if(underConstruction) {
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] under construction, interest for '{}' set to maximum", agent.getName(), product.getName());
                }
                if(underRenovation) {
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] under renovation, interest for '{}' set to maximum", agent.getName(), product.getName());
                }
                return ProcessPlanResult.IN_PROCESS;
            }
        }
        return doAction();
    }

    protected ProcessPlanResult doAction() {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] doAction", agent.getName());

        agent.allowAttention();

        if(doCommunicate()) {
            return communicate();
        }

        if(doRewire()) {
            return rewire();
        }

        return nop();
    }

    protected boolean doCommunicate() {
        double r = rnd.nextDouble();
        double freq = getCommunicationFrequencySN(agent);
        boolean doCommunicate = r < freq;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] do communicate: {} ({} < {})", agent.getName(), doCommunicate, r, freq);
        return doCommunicate;
    }

    protected ProcessPlanResult communicate() {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] start communication", agent.getName());

        SocialGraph graph = environment.getNetwork().getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        //create random target order
        List<SocialGraph.Node> targetList = new ArrayList<>();
        graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
        Collections.shuffle(targetList, rnd.getRandom());

        for(SocialGraph.Node targetNode: targetList) {
            ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);

            Acting.AttentionResult result = Acting.aquireDoubleSidedAttentionAndDataAccess(agent, targetAgent);
            switch (result) {
                case SELF_OCCUPIED:
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] failed communication('{}' -> '{}') , self occupied", agent.getName(), agent.getName(), targetAgent.getName());
                    return ProcessPlanResult.IN_PROCESS;

                case TARGET_OCCUPIED:
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] failed communication ('{}' -> '{}'), target occupied, try next target", agent.getName(), agent.getName(), targetAgent.getName());
                    continue;

                case SUCCESS:
                    //nur hier haben wir datalock durch aquireAttentionAndDataAccess
                    try {
                        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] start communication ('{}' -> '{}')", agent.getName(), agent.getName(), targetAgent.getName());

                        updateInterest(targetAgent);
                        updateCommunicationGraph(graph, targetNode);
                        applyRelativeAgreement(targetAgent);

                        return ProcessPlanResult.IN_PROCESS;
                    } finally {
                        agent.releaseDataAccess();
                        targetAgent.releaseDataAccess();
                    }
            }
        }

        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] no valid communication partner found", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    protected void updateInterest(ConsumerAgent targetAgent) {
        int myPoints = getInterestPoints(agent);
        int targetPoints = getInterestPoints(targetAgent);
        updateInterest(agent, targetPoints);
        updateInterest(targetAgent, myPoints);
        logInterestUpdate(myPoints, getInterestPoints(agent), targetAgent, targetPoints, getInterestPoints(targetAgent));
    }

    protected void updateCommunicationGraph(SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
            logGraphUpdateEdgeAdded(target.getAgent());
        }
    }

    protected boolean doRewire() {
        double r = rnd.nextDouble();
        double freq = getRewiringRate(agent);
        boolean doRewire = r < freq;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] do rewire: {} ({} < {})", agent.getName(), doRewire, r, freq);
        return doRewire;
    }

    protected ProcessPlanResult rewire() {
        if(agent.tryAquireAttention()) {
            agent.actionPerformed();
            agent.aquireDataAccess(); //lock data
            agent.releaseAttention();
        } else {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] rewire canceled, already occupied", agent.getName());
            return ProcessPlanResult.IN_PROCESS;
        }
        
        //data locked
        try {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] rewire", agent.getName());

            SocialGraph graph = environment.getNetwork().getGraph();
            SocialGraph.Node node = agent.getSocialGraphNode();
            SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

            //remove
            SocialGraph.Node rndTarget = graph.getRandomTarget(node, SocialGraph.Type.COMMUNICATION, rnd);
            if(rndTarget != null) {
                ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
                SocialGraph.Edge edge = graph.getEdge(node, rndTarget, SocialGraph.Type.COMMUNICATION);
                graph.removeEdge(edge);
                logGraphUpdateEdgeRemoved(rndTarget.getAgent());
                updateLinkCount(linkInfo, tarCag, -1); //dec
            }

            ConsumerAgentGroupAffinities affinities = environment.getAgents()
                    .getConsumerAgentGroupAffinityMapping()
                    .get(agent.getGroup());

            SocialGraph.Node tarNode = null;
            ConsumerAgentGroup tarCag = null;
            while(tarNode == null) {
                if(affinities.isEmpty()) {
                    tarCag = null;
                    break;
                }

                tarCag = affinities.getWeightedRandom(rnd);

                int currentLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
                if(tarCag.getNumberOfAgents() == currentLinkCount) {
                    affinities = affinities.createWithout(tarCag);
                    continue;
                }
                int unlinkedInCag = tarCag.getNumberOfAgents() - currentLinkCount;

                tarNode = getRandomUnlinked(
                        agent.getSocialGraphNode(),
                        tarCag,
                        unlinkedInCag,
                        SocialGraph.Type.COMMUNICATION
                );
            }

            if(tarNode == null) {
                LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] no valid rewire target found", agent.getName());
            } else {
                graph.addEdge(agent.getSocialGraphNode(), tarNode, SocialGraph.Type.COMMUNICATION, 1.0);
                logGraphUpdateEdgeAdded(tarNode.getAgent());
                updateLinkCount(linkInfo, tarCag, 1); //inc
            }
            
            return ProcessPlanResult.IN_PROCESS;
        } finally {
            agent.releaseDataAccess();
        }
    }

    protected void updateLinkCount(
            SocialGraph.LinkageInformation linkInfo,
            ConsumerAgentGroup tarCag,
            int delta) {
        int oldLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        linkInfo.update(tarCag, SocialGraph.Type.COMMUNICATION, delta);
        int newLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] update link count to cag '{}': {} -> {}", agent.getName(), tarCag.getName(), oldLinkCount, newLinkCount);
    }

    @SuppressWarnings("SameParameterValue")
    protected SocialGraph.Node getRandomUnlinked(
            SocialGraph.Node srcNode,
            ConsumerAgentGroup tarCag,
            int unlinkedInCag,
            SocialGraph.Type type) {
        SocialGraph graph = environment.getNetwork().getGraph();

        int rndId = rnd.nextInt(unlinkedInCag);
        int id = 0;

        for(Agent tar: tarCag.getAgents()) {
            SocialGraph.Node tarNode = tar.getSocialGraphNode();
            if(graph.hasNoEdge(srcNode, tarNode, type)) {
                if(id == rndId) {
                    return tarNode;
                } else {
                    id++;
                }
            }
        }

        return null;
    }

    protected ProcessPlanResult nop() {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] nop", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult handleFeasibility() {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle feasibility", agent.getName());

        boolean isShare = isShareOf1Or2FamilyHouse(agent);
        boolean isOwner = isHouseOwner(agent);
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] share of 1 or 2 family house={}, house owner={}", agent.getName(), isShareOf1Or2FamilyHouse(agent), isHouseOwner(agent));

        if(isShare && isOwner) {
            doSelfActionAndAllowAttention();
            updateStage(RAStage.DECISION_MAKING);
            return ProcessPlanResult.IN_PROCESS;
        }

        return doAction();
    }

    protected ProcessPlanResult handleDecisionMaking() {
        doSelfActionAndAllowAttention();
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handleDecisionMaking", agent.getName());

        double a = modelData().a();
        double b = modelData().b();
        double c = modelData().c();
        double d = modelData().d();

        double B = 0.0;
        if(a != 0.0) {
            double financial = getFinancialComponent();
            double financialThreshold = getFinancialThresholdProduct(agent, product);
            //check D3 reached
            if(financial < financialThreshold) {
                updateStage(RAStage.IMPEDED);
                return ProcessPlanResult.IMPEDED;
            }
            B += a * financial;
        }
        if(b != 0.0) {
            B += b * getNoveltyCompoenent();
        }
        if(c != 0.0) {
            B += c * getEnvironmentalComponent();
        }
        if(d != 0.0) {
            B += d * getSocialComponent();
        }
        double adoptionThreshold = getAdoptionThreshold(agent, product);
        if(B < adoptionThreshold) {
            updateStage(RAStage.IMPEDED);
            return ProcessPlanResult.IMPEDED;
        } else {
            agent.adopt(need, product, now());
            updateStage(RAStage.ADOPTED);
            return ProcessPlanResult.ADOPTED;
        }
    }

    //=========================
    //util
    //=========================

    protected Timestamp now() {
        return environment.getTimeModel().now();
    }

    protected void updateStage(RAStage nextStage) {
        logStageUpdate(nextStage);
        currentStage = nextStage;
    }

    protected static double getDouble(ConsumerAgent agent, String attrName) {
        Attribute attr = agent.findAttribute(attrName);
        return attr.getDoubleValue();
    }

    protected RAModelData modelData() {
        return model.getModelData();
    }

    protected double getFinancialComponent() {
        double ftThis = getFinancialThresholdAgent(agent);
        double ftAvg = getAverageFinancialThresholdAgent();

        double npvThis = modelData().NPV(agent, environment.getTimeModel().getYear());
        double npvAvg = modelData().avgNPV(environment.getTimeModel().getYear());

        double ft = getLogisticFactor() * (ftThis - ftAvg);
        double npv = getLogisticFactor() * (npvThis - npvAvg);
        return (MathUtil.logistic(ft) + MathUtil.logistic(npv)) / 2.0;
    }

    protected double getNoveltyCompoenent() {
        return getNoveltySeeking(agent);
    }

    protected double getEnvironmentalComponent() {
        return getEnvironmentalConcern(agent);
    }

    protected double getSocialComponent() {
        MutableDouble shareOfAdopterInSocialNetwork = MutableDouble.zero();
        MutableDouble shareOfAdopterInLocalArea = MutableDouble.zero();
        getShareOfAdopterInSocialNetworkAndLocalArea(shareOfAdopterInSocialNetwork, shareOfAdopterInLocalArea, distanceFilter);
        return getDependentJudgmentMaking(agent) * (shareOfAdopterInSocialNetwork.get() + shareOfAdopterInLocalArea.get()) / 2.0;
    }

    protected static double getFinancialThresholdAgent(ConsumerAgent agent) {
        double pp = getPurchasePower(agent);
        double ns = getNoveltySeeking(agent);
        return pp * ns;
    }

    protected double getAverageFinancialThresholdAgent() {
        double sum = environment.getAgents()
                .streamConsumerAgents()
                .mapToDouble(RAProcessPlan::getFinancialThresholdAgent)
                .sum();
        return sum / environment.getAgents().getTotalNumberOfConsumerAgents();
    }

    protected static double getFinancialThresholdProduct(ConsumerAgent agent, Product product) {
        ProductRelatedConsumerAgentAttribute prAttr = agent.getProductRelatedAttribute(RAConstants.FINANCIAL_THRESHOLD);
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        return attr.getDoubleValue();
    }

    protected static double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        ProductRelatedConsumerAgentAttribute prAttr = agent.getProductRelatedAttribute(RAConstants.ADOPTION_THRESHOLD);
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        return attr.getDoubleValue();
    }

    protected static double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        ProductRelatedConsumerAgentAttribute prAttr = agent.getProductRelatedAttribute(RAConstants.INITIAL_PRODUCT_AWARENESS);
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        return attr.getDoubleValue();
    }

    protected static double getInitialProductInterest(ConsumerAgent agent, Product product) {
        ProductRelatedConsumerAgentAttribute prAttr = agent.getProductRelatedAttribute(RAConstants.INITIAL_PRODUCT_INTEREST);
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        return attr.getDoubleValue();
    }

    protected static double getInitialAdopter(ConsumerAgent agent, Product product) {
        ProductRelatedConsumerAgentAttribute prAttr = agent.getProductRelatedAttribute(RAConstants.INITIAL_PRODUCT_INTEREST);
        ConsumerAgentAttribute attr = prAttr.getAttribute(product);
        return attr.getDoubleValue();
    }

    protected static double getCommunicationFrequencySN(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.COMMUNICATION_FREQUENCY_SN);
        return attr.getDoubleValue();
    }

    protected static double getRewiringRate(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.REWIRING_RATE);
        return attr.getDoubleValue();
    }

    protected static double getPurchasePower(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.PURCHASE_POWER);
        return attr.getDoubleValue();
    }

    protected static double getNoveltySeeking(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.NOVELTY_SEEKING);
        return attr.getDoubleValue();
    }

    protected static double getDependentJudgmentMaking(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        return attr.getDoubleValue();
    }

    protected static double getEnvironmentalConcern(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.ENVIRONMENTAL_CONCERN);
        return attr.getDoubleValue();
    }

    protected static boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        return attr.getDoubleValueAsBoolean();
    }

    @SuppressWarnings("SameParameterValue")
    protected static void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        attr.setDoubleValue(value);
    }

    protected static boolean isHouseOwner(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.HOUSE_OWNER);
        return attr.getDoubleValueAsBoolean();
    }

    @SuppressWarnings("SameParameterValue")
    protected static void setHouseOwner(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.findAttribute(RAConstants.HOUSE_OWNER);
        attr.setDoubleValue(value);
    }

    protected static double getConstructionRate(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.CONSTRUCTION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderConstruction(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        return attr.getDoubleValue() == 1.0;
    }

    protected static void setUnderConstruction(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        attr.setDoubleValue(value);
        if(value) {
            setShareOf1Or2FamilyHouse(agent, true);
            setHouseOwner(agent, true);
        }
    }

    protected static double getRenovationRate(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.RENOVATION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderRenovation(ConsumerAgent agent) {
        Attribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        return attr.getDoubleValue() == 1.0;
    }

    protected static void setUnderRenovation(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        attr.setDoubleValue(value);
    }

    protected boolean isAdopter(ConsumerAgent agent) {
        return agent.hasAdopted(product);
    }

    protected boolean isInterested(ConsumerAgent agent) {
        return agent.isInterested(product);
    }

    protected void makeInterested(ConsumerAgent agent) {
        agent.makeInterested(product);
    }

    protected double getInterest(ConsumerAgent agent) {
        return agent.getInterest(product);
    }

    protected boolean isAware(ConsumerAgent agent) {
        return agent.isAware(product);
    }

    protected int getInterestPoints(ConsumerAgent agent) {
        if(isAdopter(agent)) {
            return modelData().getAdopterPoints();
        }
        if(isInterested(agent)) {
            return modelData().getInterestedPoints();
        }
        if(isAware(agent)) {
            return modelData().getAwarePoints();
        }
        return modelData().getUnknownPoints();
    }

    protected void updateInterest(ConsumerAgent agent, double points) {
        agent.updateInterest(product, points);
    }

    protected void getShareOfAdopterInSocialNetworkAndLocalArea(
            MutableDouble global,
            MutableDouble local,
            NodeFilter distanceFilter) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();
        environment.getNetwork().getGraph().streamNodes()
                .filter(IS_CONSUMER)
                .peek(globalNode -> {
                    totalGlobal.inc();
                    if(globalNode.getAgent(ConsumerAgent.class).hasAdopted(getProduct())) {
                        adopterGlobal.inc();
                    }
                })
                .filter(distanceFilter)
                .forEach(localNode -> {
                    totalLocal.inc();
                    if(localNode.getAgent(ConsumerAgent.class).hasAdopted(getProduct())) {
                        adopterLocal.inc();
                    }
                });
        if(totalGlobal.isZero()) {
            global.set(0);
        } else {
            global.set(adopterGlobal.get() / totalGlobal.get());
        }
        if(totalLocal.isZero()) {
            local.set(0);
        } else {
            local.set(adopterLocal.get() / totalLocal.get());
        }
    }

    protected void applyRelativeAgreement(ConsumerAgent target) {
        applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

      //Diese Variante nutzt einen datalock
//    protected void applyRelativeAgreement(ConsumerAgent target) {
//        if(!waitForDoubleSidedDataAccess(target)) {
//            LOGGER.warn("[{}] Agent-Thread interrupted, 'applyRelativeAgreement' canceled.", agent.getName());
//            return;
//        }
//        try {
//            applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
//            applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
//            applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
//        } finally {
//            agent.releaseDataAccess();
//            target.releaseDataAccess();
//        }
//    }


    protected void applyRelativeAgreement(ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute o1Attr = agent.getAttribute(attrName);
        UncertaintyAttribute u1Attr = (UncertaintyAttribute) agent.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        ConsumerAgentAttribute o2Attr = target.getAttribute(attrName);
        UncertaintyAttribute u2Attr = (UncertaintyAttribute) target.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        applyRelativeAgreement(agent, target, attrName, o1Attr, u1Attr, o2Attr, u2Attr);
    }

    protected void applyRelativeAgreement(
            Agent a1, Agent a2, String attribute,
            ConsumerAgentAttribute o1Attr,
            UncertaintyAttribute u1Attr,
            ConsumerAgentAttribute o2Attr,
            UncertaintyAttribute u2Attr) {
        //anmerkung: werte extrahieren um keine ueberschreibungen zu erhalten
        double o1 = o1Attr.getDoubleValue();
        double u1 = u1Attr.getUncertainty();
        double m1 = u1Attr.getConvergence();
        double o2 = o2Attr.getDoubleValue();
        double u2 = u2Attr.getUncertainty();
        double m2 = u2Attr.getConvergence();
        //1 beeinflusst 2
        applyRelativeAgreement(a1, a2, attribute, o1, u1, o2, u2, o2Attr, u2Attr, m2);
        //2 beeinflusst 1
        applyRelativeAgreement(a2, a1, attribute, o2, u2, o1, u1, o1Attr, u1Attr, m1);
    }

    /**
     * Calculates the realtive agreement betwenn agent i and j, where i influences j and updates j.
     *
     * @param ai name of agent i
     * @param aj name of agent j
     * @param attribute attribute name
     * @param oi opinion of i
     * @param ui uncertainty of i
     * @param oj opinion of j
     * @param uj uncertainty of j
     * @param ojAttr corresponding parameter to store the opinion of j
     * @param ujAttr corresponding parameter to store the uncertainty of j
     * @param m convergence parameter
     */
    protected void applyRelativeAgreement(
            Agent ai, Agent aj, String attribute,
            double oi,
            double ui,
            double oj,
            double uj,
            ConsumerAgentAttribute ojAttr,
            ConsumerAgentAttribute ujAttr,
            double m) {
        double hij = Math.min(oi + ui, oj + uj) - Math.max(oi - ui, oj - uj);
        if(hij > ui) {
            double ra = hij / ui - 1.0;
            double newOj = oj + m * ra * (oi - oj);
            double newUj = uj + m * ra * (ui - uj);
            ojAttr.setDoubleValue(newOj);
            ujAttr.setDoubleValue(newUj);
            logRelativeAgreementSuccess(ai.getName(), aj.getName(), attribute, oi, ui, oj, uj, m, hij, ra, newOj, newUj);
        } else {
            logRelativeAgreementFailed(ai.getName(), aj.getName(), attribute, oi, ui, oj, uj, hij);
        }
    }

    //=========================
    //data logging
    //=========================

    protected void logStageUpdate(RAStage nextStage)  {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] stage update: {} -> {}", agent.getName(), currentStage, nextStage);
    }

    protected static IRPLogger getLogger(boolean logData) {
        return logData
                ? IRPLogging.getClearLogger()
                : LOGGER;
    }

    protected static IRPSection getSection(boolean logData, IRPSection ifLogData) {
        return logData
                ? ifLogData
                : IRPSection.SIMULATION_PROCESS;
    }

    protected static Level getLevel(boolean logData) {
        return logData
                ? Level.INFO
                : Level.TRACE;
    }

    protected boolean isLogData() {
        throw new TodoException();
    }

    protected void logInterestUpdate(
            int myOldPoints, int myNewPoints,
            Agent target, int targetOldPoints, int targetNewPoints) {
        boolean logData = isLogData();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData, IRPSection.TAG_INTEREST_UPDATE);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] interest update: '{}': {} -> {}, '{}': {} -> {}",
                InfoTag.INTEREST_UPDATE, agent.getName(),
                agent.getName(), myOldPoints, myNewPoints,
                target.getName(), targetOldPoints, targetNewPoints
        );
    }

    protected void logGraphUpdateEdgeAdded(Agent target) {
        boolean logData = isLogData();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData, IRPSection.TAG_GRAPH_UPDATE);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] graph update, edge added: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected void logGraphUpdateEdgeRemoved(Agent target) {
        boolean logData = isLogData();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData, IRPSection.TAG_GRAPH_UPDATE);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] graph update, edge removed: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected void logRelativeAgreementSuccess(
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double m,
            double hij, double ra, double newXj, double newUj) {
        boolean logData = isLogData();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData, IRPSection.TAG_RELATIVE_AGREEMENT);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] relative agreement between i='{}' and j='{}' for '{}' success (hij={} > ui={}) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {}) | ra = {} = {} / {} - 1.0 | newXj = {} = {} + {} * {} * ({} - {}) | newUj = {} = {} + {} * {} * ({} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj,
                ra, hij, ui,
                newXj, xj, m, ra, xi, xj,
                newUj, uj, m, ra, ui, uj
        );
    }

    protected void logRelativeAgreementFailed(
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double hij) {
        boolean logData = isLogData();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData, IRPSection.TAG_RELATIVE_AGREEMENT);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] relative agreement between i='{}' and j='{}' for '{}' failed (hij={} <= ui={})) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj
        );
    }
}
