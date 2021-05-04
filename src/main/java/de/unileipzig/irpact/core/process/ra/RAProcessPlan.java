package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.attribute.Attribute;
import de.unileipzig.irpact.commons.attribute.AttributeUtil;
import de.unileipzig.irpact.commons.log.MultiLoggingMessage;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttributeUtil;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentProductRelatedAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.log.IRPSection;
import de.unileipzig.irpact.core.log.InfoTag;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessPlan;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.attributes.UncertaintyAttribute;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
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
    protected NodeFilter networkFilter;
    protected boolean underRenovation = false;
    protected boolean underConstruction = false;

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
        init();
    }

    @Override
    public int getChecksum() {
        return Objects.hash(
                getNeed().getChecksum(),
                getProduct().getChecksum(),
                getAgent().getName(), //loop sonst
                getRnd().getChecksum(),
                getModel().getName(),
                getCurrentStage().getChecksum(),
                isUnderConstruction(),
                isUnderRenovation()
        );
    }

    public void init() {
        networkFilter = model.getNodeFilterScheme()
                .createFilter(this);
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public SimulationEnvironment getEnvironment() {
        return environment;
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

    public NodeFilter getNetworkFilter() {
        return networkFilter;
    }

    public void setNetworkFilter(NodeFilter networkFilter) {
        this.networkFilter = networkFilter;
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
    public boolean isModel(ProcessModel model) {
        return this.model == model;
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
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "reset process stage '{}' to '{}' for agent '{}'", RAStage.IMPEDED, RAStage.DECISION_MAKING, agent.getName());
        }
    }

    protected void updateConstructionAndRenovation() {
        double renovationRate = getRenovationRate(agent);
        double renovationDraw = rnd.nextDouble();
        boolean doRenovation = renovationDraw < renovationRate;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "agent '{}' now under renovation? {} ({} < {})", agent.getName(), doRenovation, renovationDraw, renovationRate);
        setUnderRenovation(doRenovation);

        double constructionRate = getConstructionRate(agent);
        double constructionDraw = rnd.nextDouble();
        boolean doConstruction = constructionDraw < constructionRate;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "agent '{}' now under construction? {} ({} < {})", agent.getName(), doConstruction, constructionDraw, constructionRate);
        setUnderConstruction(doConstruction);

        if(doConstruction) {
            applyUnderConstruction(agent);
        }
    }

    protected ProcessPlanResult initPlan() {
        if(agent.hasAdopted(product)) {
            currentStage = RAStage.ADOPTED;
        } else {
            currentStage = RAStage.AWARENESS;
        }
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "initial stage for '{}': {}", agent.getName(), currentStage);
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
            if(underConstruction || underRenovation) {
                if(underConstruction) {
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] under construction, interest for '{}' set to maximum", agent.getName(), product.getName());
                }
                if(underRenovation) {
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] under renovation, interest for '{}' set to maximum", agent.getName(), product.getName());
                }
                makeInterested(agent);
                doSelfActionAndAllowAttention();
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
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle decision making", agent.getName());

        MultiLoggingMessage mlm = new MultiLoggingMessage();
        mlm.log("{} [{}] calculate U", InfoTag.ADOPTION_THRESHOLD, agent.getName());



        double a = modelData().a();
        double b = modelData().b();
        double c = modelData().c();
        double d = modelData().d();

        double B = 0.0;

        if(a != 0.0) {
            double financial = getFinancialComponent();
            double financialThreshold = getFinancialThreshold(agent, product);
            boolean noFinancial = financial < financialThreshold;
            //check D3 reached
            if(noFinancial) {
                mlm.log("financial component < financial threshold ({} < {}) = {}", financial, financialThreshold, true);
                logCalculateAdoption(mlm);

                updateStage(RAStage.IMPEDED);
                return ProcessPlanResult.IMPEDED;
            }
            double temp = a * financial;
            mlm.log("a * financial component = {} * {} = {}", a, financial, temp);
            B += temp;
        } else {
            mlm.log("a = 0");
        }

        if(b != 0.0) {
            double env = getEnvironmentalComponent();
            double benv = b * env;
            mlm.log("b * environmental component = {} * {} = {}", b, env, benv);
            B += benv;
        } else {
            mlm.log("b = 0");
        }

        if(c != 0.0) {
            double nov = getNoveltyCompoenent();
            double cnov = c * nov;
            mlm.log("c * novelty component = {} * {} = {}", c, nov, cnov);
            B += cnov;
        } else {
            mlm.log("c = 0");
        }

        if(d != 0.0) {
            double soc = getSocialComponent();
            double dsoc = d * soc;
            mlm.log("d * social component = {} * {} = {}", d, soc, dsoc);
            B += dsoc;
        } else {
            mlm.log("d = 0");
        }

        double adoptionThreshold = getAdoptionThreshold(agent, product);
        boolean noAdoption = B < adoptionThreshold;

        mlm.log("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);
        logCalculateAdoption(mlm);

        if(noAdoption) {
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
        return AttributeUtil.getDoubleValue(attr, attrName);
    }

    protected RAModelData modelData() {
        return model.getModelData();
    }

    protected double getFinancialComponent() {
        double npvAvg = getAverageNPV();
        double npvThis = getNPV(agent);

        return getFinancialComponent(npvAvg, npvThis);
    }

    protected double getFinancialComponent(double npvAvg, double npvThis) {
        double ftAvg = getAverageFinancialThresholdAgent();
        double ftThis = getFinancialThresholdAgent(agent);

        double ft = getLogisticFactor() * (ftThis - ftAvg);
        double npv = getLogisticFactor() * (npvThis - npvAvg);

        double logisticFt = MathUtil.logistic(ft);
        double logisticNpv = MathUtil.logistic(npv);

        double comp = (logisticFt + logisticNpv) / 2.0;

        logFinancialComponent(ftAvg, ftThis, npvAvg, npvThis, getLogisticFactor(), ft, npv, logisticFt, logisticNpv, comp);

        return comp;
    }

    public double getFinancialComponent(int year) {
        double npvAvg = getAverageNPV(year);
        double npvThis = getNPV(agent, year);

        return getFinancialComponent(npvAvg, npvThis);
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
        getShareOfAdopterInSocialNetworkAndLocalArea(shareOfAdopterInSocialNetwork, shareOfAdopterInLocalArea);
        double djm = getDependentJudgmentMaking(agent);
        double comp = djm * (shareOfAdopterInSocialNetwork.get() + shareOfAdopterInLocalArea.get()) / 2.0;
        LOGGER.trace(
                IRPSection.SIMULATION_PROCESS,
                "[{}] dependent judgment making = {}, share of adopter in social network = {}, share of adopter in local area = {}, social component = {} = {} * ({} + {}) / 2.0",
                agent.getName(), djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get(), comp, djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get()
        );
        return comp;
    }

    protected static double getFinancialThresholdAgent(ConsumerAgent agent) {
//        double pp = getPurchasePower(agent);
//        double ns = getNoveltySeeking(agent);
//        return pp;
        return getPurchasePower(agent);
    }

    protected double getAverageFinancialThresholdAgent() {
        return modelData().getAverageFinancialThresholdAgent(environment.getAgents().streamConsumerAgents());
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(agent, environment.getTimeModel().getCurrentYear());
    }

    protected double getAverageNPV() {
        double sum = environment.getAgents()
                .streamConsumerAgents()
                .mapToDouble(this::getNPV)
                .sum();
        return sum / environment.getAgents().getTotalNumberOfConsumerAgents();
    }

    protected double getNPV(ConsumerAgent agent, int year) {
        return modelData().NPV(agent, year);
    }

    protected double getAverageNPV(int year) {
        return modelData().avgNPV(environment.getAgents().streamConsumerAgents(), year);
    }

    protected static double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return ConsumerAgentAttributeUtil.getRelatedDoubleValue(agent, product, RAConstants.FINANCIAL_THRESHOLD);
    }

    protected static double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return ConsumerAgentAttributeUtil.getRelatedDoubleValue(agent, product, RAConstants.ADOPTION_THRESHOLD);
    }

    protected static double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return ConsumerAgentAttributeUtil.getRelatedDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_AWARENESS);
    }

    protected static double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return ConsumerAgentAttributeUtil.getRelatedDoubleValue(agent, product, RAConstants.INITIAL_PRODUCT_INTEREST);
    }

    protected static double getInitialAdopter(ConsumerAgent agent, Product product) {
        return ConsumerAgentAttributeUtil.getRelatedDoubleValue(agent, product, RAConstants.INITIAL_ADOPTER);
    }

    protected static double getCommunicationFrequencySN(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.COMMUNICATION_FREQUENCY_SN);
        return AttributeUtil.getDoubleValue(attr, RAConstants.COMMUNICATION_FREQUENCY_SN);
    }

    protected static double getRewiringRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.REWIRING_RATE);
        return AttributeUtil.getDoubleValue(attr, RAConstants.REWIRING_RATE);
    }

    protected static int getId(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.ID);
        return AttributeUtil.getIntValue(attr, RAConstants.ID);
    }

    protected static double getPurchasePower(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.PURCHASE_POWER);
        return AttributeUtil.getDoubleValue(attr, RAConstants.PURCHASE_POWER);
    }

    protected static double getNoveltySeeking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.NOVELTY_SEEKING);
        return AttributeUtil.getDoubleValue(attr, RAConstants.NOVELTY_SEEKING);
    }

    protected static double getDependentJudgmentMaking(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.DEPENDENT_JUDGMENT_MAKING);
        return AttributeUtil.getDoubleValue(attr, RAConstants.DEPENDENT_JUDGMENT_MAKING);
    }

    protected static double getEnvironmentalConcern(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.ENVIRONMENTAL_CONCERN);
        return AttributeUtil.getDoubleValue(attr, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    protected static boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        return AttributeUtil.getBooleanValue(attr, RAConstants.SHARE_1_2_HOUSE);
    }

    @SuppressWarnings("SameParameterValue")
    protected static void setShareOf1Or2FamilyHouse(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.findAttribute(RAConstants.SHARE_1_2_HOUSE);
        AttributeUtil.setBoolean(attr, value, RAConstants.SHARE_1_2_HOUSE);
    }

    protected static boolean isHouseOwner(ConsumerAgent agent) {
        Attribute attr = agent.findAttribute(RAConstants.HOUSE_OWNER);
        return AttributeUtil.getBooleanValue(attr, RAConstants.HOUSE_OWNER);
    }

    @SuppressWarnings("SameParameterValue")
    protected static void setHouseOwner(ConsumerAgent agent, boolean value) {
        Attribute attr = agent.findAttribute(RAConstants.HOUSE_OWNER);
        AttributeUtil.setBoolean(attr, value, RAConstants.HOUSE_OWNER);
    }

    protected static double getConstructionRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.CONSTRUCTION_RATE);
        return AttributeUtil.getDoubleValue(attr, RAConstants.CONSTRUCTION_RATE);
    }

    public boolean isUnderConstruction() {
        return underConstruction;
    }

    public void setUnderConstruction(boolean value) {
        this.underConstruction = value;
    }

    protected static void applyUnderConstruction(ConsumerAgent agent) {
        boolean isShare = isShareOf1Or2FamilyHouse(agent);
        boolean isOwner = isHouseOwner(agent);
        setShareOf1Or2FamilyHouse(agent, true);
        setHouseOwner(agent, true);

        if(!isShare) {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "agent '{}' not share of 1 or 2 family house", agent.getName());
        }
        if(!isOwner) {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "agent '{}' house owner", agent.getName());
        }
    }

    protected static double getRenovationRate(ConsumerAgent agent) {
        ConsumerAgentAttribute attr = agent.getAttribute(RAConstants.RENOVATION_RATE);
        return AttributeUtil.getDoubleValue(attr, RAConstants.RENOVATION_RATE);
    }

    public boolean isUnderRenovation() {
        return underRenovation;
    }

    public void setUnderRenovation(boolean value) {
        this.underRenovation = value;
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
            MutableDouble local) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        environment.getNetwork().getGraph()
                .streamTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(IS_CONSUMER)
                .forEach(globalNode -> {
                    totalGlobal.inc();
                    if(globalNode.getAgent(ConsumerAgent.class).hasAdopted(getProduct())) {
                        adopterGlobal.inc();
                    }
                });

        //TODO vllt ergebnisse cachen
        //-> selbiges dann auch gleich bei obrigen
        environment.getNetwork().getGraph()
                .streamNodes()
                .filter(IS_CONSUMER)
                .filter(networkFilter)
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

        logShareOfAdopterInSocialNetworkAndLocalArea(totalGlobal, adopterGlobal, global, totalLocal, adopterLocal, local);
    }

    protected void applyRelativeAgreement(ConsumerAgent target) {
        applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

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
        double o1 = AttributeUtil.getDoubleValue(o1Attr);
        double u1 = u1Attr.getUncertainty();
        double m1 = u1Attr.getConvergence();
        double o2 = AttributeUtil.getDoubleValue(o2Attr);
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
            AttributeUtil.setDoubleValue(ojAttr, newOj);
            AttributeUtil.setDoubleValue(ujAttr, newUj);
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
                ? IRPLogging.getResultLogger()
                : LOGGER;
    }

    protected static IRPSection getSection(boolean logData) {
        return IRPSection.SIMULATION_PROCESS.orGeneral(logData);
    }

    protected static Level getLevel(boolean logData) {
        return logData
                ? Level.INFO
                : Level.TRACE;
    }

    protected Settings getSettings() {
        return environment.getSettings();
    }

    protected void logInterestUpdate(
            int myOldPoints, int myNewPoints,
            Agent target, int targetOldPoints, int targetNewPoints) {
        boolean logData = getSettings().isLogInterestUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
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
        boolean logData = getSettings().isLogGraphUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(
                section, level,
                "{} [{}] graph update, edge added: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected void logGraphUpdateEdgeRemoved(Agent target) {
        boolean logData = getSettings().isLogGraphUpdate();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
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
        boolean logData = getSettings().isLogRelativeAgreement();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
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
        boolean logData = getSettings().isLogRelativeAgreement();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] relative agreement between i='{}' and j='{}' for '{}' failed (hij={} <= ui={})) | xi={}, ui={}, xj={}, uj={} | hij = {} = Math.min({} + {}, {} + {}) - Math.max({} - {}, {} - {})",
                InfoTag.RELATIVE_AGREEMENT, ai, ai, aj, attribute, hij, ui,
                xi, ui, xj, uj,
                hij, xi, ui, xj, uj, xi, ui, xj, uj
        );
    }

    protected void logShareOfAdopterInSocialNetworkAndLocalArea(
            MutableDouble totalGlobal,
            MutableDouble adopterGlobal,
            MutableDouble shareGlobal,
            MutableDouble totalLocal,
            MutableDouble adopterLocal,
            MutableDouble shareLocal) {
        boolean logData = getSettings().isLogShareNetworkLocale();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);

        logger.log(section, level,
                "{} [{}] global share = {} ({} / {}), local share = {} ({} / {})",
                InfoTag.SHARE_NETORK_LOCAL, agent.getName(),
                shareGlobal.doubleValue(), adopterGlobal.doubleValue(), totalGlobal.doubleValue(), shareLocal.doubleValue(), adopterLocal.doubleValue(), totalLocal.doubleValue()
        );
    }

    protected void logFinancialComponent(
            double ftAvg, double ftThis,
            double npvAvg, double npvThis,
            double logisticFactor,
            double ft, double npv,
            double logisticFt, double logisticNpv,
            double comp) {
        boolean logData = getSettings().isLogFinancialComponent();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(section, level,
                "{} [{}] financal component calculation: t_avg = {}, t = {}, NPV_avg = {}, NPV = {}, lf = {} | lf * (t - t_avg) = {}, lf * (NPV - NPV_avg) = {} | 1 / (1 + e^(-(lf * (t - t_avg))) = {}, 1 / (1 + e^(-(lf * (NPV - NPV_avg))) = {} | (logistic(t) + logistic(NPV)) / 2.0 = {}",
                InfoTag.FINANCAL_COMPONENT, agent.getName(),
                ftAvg, ftThis, npvAvg, npvThis, logisticFactor, ft, npv, logisticFt, logisticNpv, comp
        );
    }

    protected void logCalculateAdoption(MultiLoggingMessage mlm) {
        boolean logData = getSettings().isLogCalculateAdoption();
        IRPLogger logger = getLogger(logData);
        IRPSection section = getSection(logData);
        Level level = getLevel(logData);
        logger.log(section, level, "{}", mlm.buildLazyMessage(" | "));
    }
}
