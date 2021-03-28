package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.util.CollectionUtil;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.DoubleAttribute;
import de.unileipzig.irpact.commons.attribute.StringAttribute;
import de.unileipzig.irpact.commons.interest.Interest;
import de.unileipzig.irpact.commons.util.data.DataType;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeDistanceFilter;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductAttribute;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.util.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;

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

    protected ProcessPlanResult finishOwnAction(ProcessPlanResult result) {
        agent.actionPerformed();
        agent.allowAquire();
        return result;
    }

    protected ProcessPlanResult handleInterest() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] handleAwareness", agent.getName());

        Interest<Product> productInterest = agent.getProductInterest();
        if(productInterest.isInterested(product)) {
            currentStage = RAStage.FEASIBILITY;
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
            return finishOwnAction(ProcessPlanResult.IN_PROCESS);
        }
        if(isAware(agent)) {
            if(isUnderConstruction(agent) || isUnderRenovation(agent)) {
                productInterest.makeInterested(product);
                currentStage = RAStage.FEASIBILITY;
                LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
                return finishOwnAction(ProcessPlanResult.IN_PROCESS);
            }
        }
        return doAction();
    }

    protected ProcessPlanResult doAction() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] doAction", agent.getName());

        agent.allowAquire();

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
        return r < freq;
    }

    protected ProcessPlanResult communicate() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] communicate", agent.getName());

        SocialGraph graph = environment.getNetwork().getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        LinkedList<SocialGraph.Node> targetList = new LinkedList<>();
        graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
        Collections.shuffle(targetList, rnd.getRandom());

        while(targetList.size() > 0) {
            SocialGraph.Node targetNode = targetList.removeFirst();
            ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);

            if(targetAgent.tryAquireAction()) {
                if(agent.tryAquireSelf()) {
                    LOGGER.trace(IRPSection.SIMULATION_AGENT_COMMUNICATION, "communication: '{}' -> '{}'", agent.getName(), targetAgent.getName());

                    targetAgent.actionPerformed();
                    agent.actionPerformed();
                    targetAgent.releaseAquire();
                    agent.releaseAquire();

                    int myPoints = getInterestPoints(agent);
                    int targetPoints = getInterestPoints(targetAgent);
                    updateAwareness(agent, targetPoints);
                    updateAwareness(targetAgent, myPoints);
                    updateCommunicationGraph(graph, targetNode);
                    applyRelativeAgreement(targetAgent);
                    return ProcessPlanResult.IN_PROCESS;
                } else {
                    targetAgent.aquireFailed();
                    LOGGER.trace(IRPSection.SIMULATION_AGENT_COMMUNICATION, "failed communication: '{}' -> '{}'", agent.getName(), targetAgent.getName());
                }
            }
        }
        return ProcessPlanResult.IN_PROCESS;
    }

    protected void updateCommunicationGraph(SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
        }
    }

    protected boolean doRewire() {
        double r = rnd.nextDouble();
        double freq = getRewiringRate(agent);
        return r < freq;
    }

    @Todo("LOGGING")
    protected ProcessPlanResult rewire() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] rewire", agent.getName());

        if(agent.tryAquireSelf()) {
            agent.actionPerformed();
            agent.releaseAquire();
        } else {
            return ProcessPlanResult.IN_PROCESS;
        }

        SocialGraph graph = environment.getNetwork().getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        SocialGraph.LinkageInformation linkInfo = graph.getLinkageInformation(node);

        //remove
        List<? extends SocialGraph.Node> targets = graph.listTargets(node, SocialGraph.Type.COMMUNICATION);
        if(!targets.isEmpty()) {
            SocialGraph.Node rndTarget = rnd.getRandom(targets);
            SocialGraph.Edge edge = graph.getEdge(node, rndTarget, SocialGraph.Type.COMMUNICATION);
            graph.removeEdge(edge);
            ConsumerAgentGroup tarCag = rndTarget.getAgent(ConsumerAgent.class).getGroup();
            linkInfo.dec(tarCag, SocialGraph.Type.COMMUNICATION);
            //TODO add Logging mit entsprechender IRPSection
        }

        ConsumerAgentGroupAffinities affinities = environment.getAgents()
                .getConsumerAgentGroupAffinityMapping()
                .get(agent.getGroup());

        SocialGraph.Node tarNode = null;
        while(tarNode == null) {
            ConsumerAgentGroup tarCag = affinities.getWeightedRandom(rnd);

            int currentLinkCount = linkInfo.get(tarCag, SocialGraph.Type.COMMUNICATION);
            if(tarCag.getNumberOfAgents() == currentLinkCount) {
                affinities = affinities.createWithout(tarCag);
                continue;
            }

            tarNode = getRandomUnlinked(
                    agent.getSocialGraphNode(),
                    tarCag,
                    SocialGraph.Type.COMMUNICATION
            );
        }

        LOGGER.debug("rewire-add edge '{} -> {}", agent.getName(), tarNode.getLabel());
        environment.getNetwork()
                .getGraph()
                .addEdge(agent.getSocialGraphNode(), tarNode, SocialGraph.Type.COMMUNICATION, 1.0);

        return ProcessPlanResult.IN_PROCESS;
    }

    protected SocialGraph.Node getRandomUnlinked(SocialGraph.Node srcNode, ConsumerAgentGroup cag, SocialGraph.Type type) {
        SocialGraph graph = environment.getNetwork().getGraph();
        List<SocialGraph.Node> tars = new ArrayList<>();
        for(Agent tar: cag.getAgents()) {
            SocialGraph.Node tarNode = tar.getSocialGraphNode();
            if(graph.hasNoEdge(srcNode, tarNode, type)) {
                tars.add(tarNode);
            }
        }
        if(tars.isEmpty()) {
            return null;
        } else {
            return CollectionUtil.getRandom(tars, rnd);
        }
    }

    @Todo
    protected ProcessPlanResult nop() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] nop", agent.getName());
        return ProcessPlanResult.IN_PROCESS;
    }

    protected ProcessPlanResult handleFeasibility() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] handleFeasibility", agent.getName());

        LOGGER.trace(">>>>> {} {} {}", agent.getName(), isShareOf1Or2FamilyHouse(agent), isHouseOwner(agent));
        if(isShareOf1Or2FamilyHouse(agent) && isHouseOwner(agent)) {
            currentStage = RAStage.DECISION_MAKING;
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
            return finishOwnAction(ProcessPlanResult.IN_PROCESS);
        }

        return doAction();
    }

    protected ProcessPlanResult handleDecisionMaking() {
        LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] handleDecisionMaking", agent.getName());

        double a = modelData().a();
        double b = modelData().b();
        double c = modelData().c();
        double d = modelData().d();

        double B = 0.0;
        if(a != 0.0) {
            double financial = getFinancialComponent();
            double financialThreshold = getFinancialThresholdProduct(product);
            //check D3 reached
            if(financial < financialThreshold) {
                currentStage = RAStage.IMPEDED;
                LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
                return finishOwnAction(ProcessPlanResult.IMPEDED);
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
        double adoptionThreshold = getAdoptionThreshold(product);
        if(B < adoptionThreshold) {
            currentStage = RAStage.IMPEDED;
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
            return finishOwnAction(ProcessPlanResult.IMPEDED);
        } else {
            agent.adopt(need, product);
            currentStage = RAStage.ADOPTED;
            LOGGER.trace(IRPSection.SIMULATION_AGENT, "[{}] new stage: {}", agent.getName(), currentStage);
            return finishOwnAction(ProcessPlanResult.ADOPTED);
        }
    }

    //=========================
    //util
    //=========================

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
        return getDependentJudgmentMaking() * (shareOfAdopterInSocialNetwork.get() + shareOfAdopterInLocalArea.get()) / 2.0;
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

    protected static double getFinancialThresholdProduct(Product product) {
        ProductAttribute attr = product.getAttribute(RAConstants.FINANCIAL_THRESHOLD);
        return attr.getDoubleValue();
    }

    protected static double getAdoptionThreshold(Product product) {
        ProductAttribute attr = product.getAttribute(RAConstants.ADOPTION_THRESHOLD);
        return attr.getDoubleValue();
    }

    protected static double getInitialProductInterest(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.INITIAL_PRODUCT_INTEREST);
        return attr.getDoubleValue();
    }

    protected static double getCommunicationFrequencySN(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.COMMUNICATION_FREQUENCY_SN);
        return attr.getDoubleValue();
    }

    protected static double getRewiringRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.REWIRING_RATE);
        return attr.getDoubleValue();
    }

    protected static double getPurchasePower(ConsumerAgent agent) {
        DoubleAttribute attr = (DoubleAttribute) agent.findAttribute(RAConstants.PURCHASE_POWER);
        return attr.getDoubleValue();
    }

    protected static double getNoveltySeeking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.NOVELTY_SEEKING);
        return attr.getDoubleValue();
    }

    protected double getDependentJudgmentMaking() {
        return getDependentJudgmentMaking(agent);
    }
    protected static double getDependentJudgmentMaking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        return attr.getDoubleValue();
    }

    protected static double getEnvironmentalConcern(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.ENVIRONMENTAL_CONCERN);
        return attr.getDoubleValue();
    }

    protected static boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        DoubleAttribute attr = (DoubleAttribute) agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        double value = attr.getDoubleValue();
        return value == 1.0 || value == 2.0;
    }

    protected static void setShareOf1Or2FamilyHouse(ConsumerAgent agent, double n) {
        DoubleAttribute attr = (DoubleAttribute) agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        attr.setDoubleValue(n);
    }

    protected static boolean isHouseOwner(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.HOUSE_OWNER);
        return attr.getDoubleValueAsBoolean();
    }

    protected static void setHouseOwner(ConsumerAgent agent, String value) {
        StringAttribute attr = (StringAttribute) agent.findAttribute(RAConstants.HOUSE_OWNER);
        attr.setValue(value);
    }

    protected static double getConstructionRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.CONSTRUCTION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderConstruction(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        return attr.getDoubleValue() == 1.0;
    }

    protected static void setUnderConstruction(ConsumerAgent agent, boolean value) {
        double dvalue = value ? 1.0 : 0.0;
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_CONSTRUCTION);
        attr.setDoubleValue(dvalue);
        if(value) {
            setShareOf1Or2FamilyHouse(agent, 1);
            setHouseOwner(agent, RAConstants.PRIVATE);
        }
    }

    protected static double getRenovationRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.RENOVATION_RATE);
        return attr.getDoubleValue();
    }

    protected static boolean isUnderRenovation(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        return attr.getDoubleValue() == 1.0;
    }

    protected static void setUnderRenovation(ConsumerAgent agent, boolean value) {
        double dvalue = value ? 1.0 : 0.0;
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.UNDER_RENOVATION);
        attr.setDoubleValue(dvalue);
    }

    protected static boolean isInitialAdopter(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.INITIAL_ADOPTER);
        return attr.getDoubleValue() == 1.0;
    }

    protected boolean isAdopter(ConsumerAgent agent) {
        return agent.hasAdopted(product);
    }

    protected boolean isInterested(ConsumerAgent agent) {
        Interest<Product> interest = agent.getProductInterest();
        return interest.isInterested(product);
    }

    protected boolean isAware(ConsumerAgent agent) {
        Interest<Product> interest = agent.getProductInterest();
        return interest.isAware(product);
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

    protected void updateAwareness(ConsumerAgent agent, double points) {
        Interest<Product> interest = agent.getProductInterest();
        interest.update(product, points);
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
        //A2
        applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
        //A3
        applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        //A4
        applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    protected void applyRelativeAgreement(ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute o1Attr = agent.getAttribute(attrName);
        UncertaintyAttribute u1Attr = (UncertaintyAttribute) agent.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        ConsumerAgentAttribute o2Attr = target.getAttribute(attrName);
        UncertaintyAttribute u2Attr = (UncertaintyAttribute) target.getAttribute(RAConstants.getUncertaintyAttributeName(attrName));
        applyRelativeAgreement(o1Attr, u1Attr, o2Attr, u2Attr);
    }

    protected static void applyRelativeAgreement(
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
        applyRelativeAgreement(o1, u1, o2, u2, o2Attr, u2Attr, m2);
        //2 beeinflusst 1
        applyRelativeAgreement(o2, u2, o1, u1, o1Attr, u1Attr, m1);
    }

    /**
     * Calculates the realtive agreement betwenn agent i and j, where i influences j and updates j.
     *
     * @param oi opinion of i
     * @param ui uncertainty of i
     * @param oj opinion of j
     * @param uj uncertainty of j
     * @param ojAttr corresponding parameter to store the opinion of j
     * @param ujAttr corresponding parameter to store the uncertainty of j
     * @param m convergence parameter
     */
    protected static void applyRelativeAgreement(
            double oi,
            double ui,
            double oj,
            double uj,
            ConsumerAgentAttribute ojAttr,
            ConsumerAgentAttribute ujAttr,
            double m) {
        double hij = Math.min(oi + ui, oj + uj) - Math.max(oi - ui, oj -uj);
        if(hij > ui) {
            double ra = hij / ui - 1.0;
            double newOj = oj + m * ra * (oi - oj);
            double newUj = uj + m * ra * (ui - uj);
            ojAttr.setDoubleValue(newOj);
            ujAttr.setDoubleValue(newUj);
        }
    }
}
