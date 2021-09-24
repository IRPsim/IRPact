package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.checksum.Checksums;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.MathUtil;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.Agent;
import de.unileipzig.irpact.core.agent.consumer.*;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.*;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.PostAction;
import de.unileipzig.irpact.core.process.ProcessPlanResult;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.uncert.Uncertainty;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.Settings;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.develop.PotentialProblem;
import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.util.log.IRPLogger;
import org.slf4j.event.Level;

import java.util.*;
import java.util.function.Predicate;

/**
 * @author Daniel Abitz
 */
/*
 * http://jasss.soc.surrey.ac.uk/5/4/1.html
 */
public class RAProcessPlan extends RAProcessPlanBase {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(RAProcessPlan.class);

    protected final Predicate<SocialGraph.Node> IS_CONSUMER = node -> node.is(ConsumerAgent.class);
//    protected final Function<SocialGraph.Node, ConsumerAgent> TO_CONSUMER = node -> node.getAgent(ConsumerAgent.class);
//    protected final Predicate<ConsumerAgent> IS_ADOPTER = ca -> ca.hasAdopted(getProduct());

    protected NodeFilter networkFilter;

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

    @PotentialProblem("hier fehlen noch ein paar Komponenten")
    @Override
    public int getChecksum() {
        return Checksums.SMART.getChecksum(
                getStage(),
                isUnderConstruction(),
                isUnderRenovation(),

                getNeed(),
                getProduct(),
                ChecksumComparable.getNameChecksum(getAgent()),
                getRnd(),
                ChecksumComparable.getNameChecksum(getModel()),
                getNetworkFilter(),
                getUncertainty()
        );
    }

    public void setModel(RAProcessModel model) {
        this.model = model;
    }

    @Override
    public RAProcessModel getModel() {
        return (RAProcessModel) model;
    }

    public NodeFilter getNetworkFilter() {
        return networkFilter;
    }

    public void setNetworkFilter(NodeFilter networkFilter) {
        this.networkFilter = networkFilter;
    }

    public double getLogisticFactor() {
        return modelData().getLogisticFactor();
    }

    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return getModel().getRelativeAgreementAlgorithm();
    }

    @Override
    public ProcessPlanResult execute() throws Throwable {
        return execute(null);
    }

    @Override
    public ProcessPlanResult execute(List<PostAction<?>> postActions) throws Throwable {
        if(currentStage == RAStage.PRE_INITIALIZATION) {
            return initPlan(postActions);
        } else {
            return executePlan(postActions);
        }
    }

    //=========================
    //phases
    //=========================

    protected ProcessPlanResult initPlan(List<PostAction<?>> postActions) throws Throwable {
        if(agent.hasAdopted(product)) {
            if(agent.hasInitialAdopted(product)) {
                logPhaseTransition(DataAnalyser.Phase.INITIAL_ADOPTED, now());
            } else {
                logPhaseTransition(DataAnalyser.Phase.ADOPTED, now());
            }
            currentStage = RAStage.ADOPTED;
        } else {
            logPhaseTransition(DataAnalyser.Phase.AWARENESS, now());
            currentStage = RAStage.AWARENESS;
        }
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "initial stage for '{}': {}", agent.getName(), currentStage);
        return executePlan(postActions);
    }

    protected ProcessPlanResult executePlan(List<PostAction<?>> postActions) throws Throwable {
        switch (currentStage) {
            case AWARENESS:
                return handleInterest(postActions);

            case FEASIBILITY:
                return handleFeasibility(postActions);

            case DECISION_MAKING:
                return handleDecisionMaking(postActions);

            case ADOPTED:
            case IMPEDED:
                return doAction(postActions);

            default:
                throw new IllegalStateException("unknown phase: " + currentStage);
        }
    }
    
    protected void doSelfActionAndAllowAttention() {
        agent.actionPerformed();
        agent.allowAttention();
    }

    protected ProcessPlanResult handleInterest(List<PostAction<?>> postActions) throws Throwable {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle interest", agent.getName());

        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] current interest for '{}': {}", agent.getName(), product.getName(), getInterest(agent));

        if(isInterested(agent)) {
            doSelfActionAndAllowAttention();
            LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] is interested in '{}'", agent.getName(), product.getName());
            logPhaseTransition(DataAnalyser.Phase.FEASIBILITY, now());
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
        return doAction(postActions);
    }

    protected ProcessPlanResult doAction(List<PostAction<?>> postActions) throws Throwable {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] doAction", agent.getName());

        agent.allowAttention();

        if(postActions == null) {
            return doAction0();
        } else {
            postActions.add(new PostAction<RAProcessPlan>() {

                private final RAProcessPlan plan = RAProcessPlan.this;

                @Override
                public boolean isSupported(Class<?> type) {
                    return type.isInstance(plan);
                }

                @Override
                public RAProcessPlan getInput() {
                    return plan;
                }

                @Override
                public String getInputName() {
                    return plan.getAgent().getName();
                }

                @Override
                public void execute() throws Throwable {
                    doAction0();
                }
            });
            return ProcessPlanResult.IN_PROCESS;
        }
    }

    protected ProcessPlanResult doAction0() throws Throwable {
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
        double factor = modelData().getCommunicationFactor();
        double chance = r * factor;
        double freq = getCommunicationFrequencySN(agent);
        boolean doCommunicate = chance < freq;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] do communicate: {} ({} * {} = {} < {})", agent.getName(), doCommunicate, r, factor, chance, freq);
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
                    agent.actionPerformed();
                    targetAgent.actionPerformed();
                    agent.releaseAttention();
                    targetAgent.releaseAttention();

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
        double myInterest = getInterest(agent);
        double targetInterest = getInterest(targetAgent);

        double myPointsToAdd = getInterestPoints(agent);
        double targetPointsToAdd = getInterestPoints(targetAgent);

        updateInterest(agent, targetPointsToAdd);
        updateInterest(targetAgent, myPointsToAdd);

        logInterestUpdate(myInterest, getInterest(agent), targetAgent, targetInterest, getInterest(targetAgent));
    }

    protected void updateCommunicationGraph(SocialGraph graph, SocialGraph.Node target) {
        if(!graph.hasEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(target, agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION, 1.0);
            logGraphUpdateEdgeAdded(target.getAgent());
        }
    }

    protected boolean doRewire() {
        double r = rnd.nextDouble();
        double factor = modelData().getRewireFactor();
        double chance = r * factor;
        double freq = getRewiringRate(agent);
        boolean doRewire = chance < freq;
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] do rewire: {} ({} * {} = {} < {})", agent.getName(), doRewire, r, factor, chance, freq);
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
            if(rndTarget == null) {
                logGraphUpdateEdgeRemoved(null);
            } else {
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
                int maxTargetAgents = tarCag.getNumberOfAgents();
                if(tarCag == agent.getGroup()) {
                    maxTargetAgents -= 1;
                }

                LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] tar: {}, currentLinkCount: {}, max: {} (self: {})", agent.getName(), tarCag.getName(), currentLinkCount, maxTargetAgents, getAgent().getGroup() == tarCag);

                if(maxTargetAgents == currentLinkCount) {
                    affinities = affinities.createWithout(tarCag);
                    continue;
                }

                int unlinkedInCag = maxTargetAgents - currentLinkCount;

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
            if(tar == agent) {
                continue;
            }
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

    protected ProcessPlanResult handleFeasibility(List<PostAction<?>> postActions) throws Throwable {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle feasibility", agent.getName());

        boolean isShare = isShareOf1Or2FamilyHouse(agent);
        boolean isOwner = isHouseOwner(agent);
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] share of 1 or 2 family house={}, house owner={}", agent.getName(), isShareOf1Or2FamilyHouse(agent), isHouseOwner(agent));

        if(isShare && isOwner) {
            doSelfActionAndAllowAttention();
            logPhaseTransition(DataAnalyser.Phase.DECISION_MAKING, now());
            updateStage(RAStage.DECISION_MAKING);
            return ProcessPlanResult.IN_PROCESS;
        }

        return doAction(postActions);
    }

    protected ProcessPlanResult handleDecisionMaking(List<PostAction<?>> postActions) {
        doSelfActionAndAllowAttention();
        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] handle decision making", agent.getName());

        DataLogger dataLogger = environment.getDataLogger();
        DataAnalyser dataAnalyser = environment.getDataAnalyser();
        IRPLoggingMessageCollection alm = new IRPLoggingMessageCollection()
                .setLazy(true)
                .setAutoDispose(true);
        alm.append("{} [{}] calculate U", InfoTag.DECISION_MAKING, agent.getName());

        Timestamp now = now();
        double ft = getFinancialThresholdAgent(agent);
        double financialThreshold = getFinancialThreshold(agent, product);
        if(ft < financialThreshold) {
            dataLogger.logEvaluationFailed(
                    agent, product, now,
                    financialThreshold, ft
            );
            alm.append("financial component < financial threshold ({} < {}) = {}", ft, financialThreshold, true);
            logCalculateDecisionMaking(alm);
            updateStage(RAStage.IMPEDED);
            return ProcessPlanResult.IMPEDED;
        }

        double a = modelData().a();
        double b = modelData().b();
        double c = modelData().c();
        double d = modelData().d();

        double aWeight = modelData().getAWeight();
        double bWeight = modelData().getBWeight();
        double cWeight = modelData().getCWeight();
        double dWeight = modelData().getDWeight();

        double fin;
        double env;
        double nov;
        double soc;

        double B = 0.0;

        //a
        fin = getFinancialComponent();
        double afin = a * fin;
        double wafin = aWeight * afin;
        alm.append("aWeight * a * financial component = {} * {} * {} = {}", aWeight, a, fin, wafin);
        B += wafin;

        //b
        env = getEnvironmentalComponent();
        double benv = b * env;
        double wbenv = bWeight * benv;
        alm.append("bWeight * b * environmental component = {} * {} * {} = {}", bWeight, b, env, wbenv);
        B += wbenv;

        //c
        nov = getNoveltyCompoenent();
        double cnov = c * nov;
        double wcnov = cWeight * cnov;
        alm.append("cWeight * c * novelty component = {} * {} * {} = {}", cWeight, c, nov, wcnov);
        B += wcnov;

        //d
        soc = getSocialComponent();
        double dsoc = d * soc;
        double wdsoc = dWeight * dsoc;
        alm.append("dWeight * d * social component = {} * {} * {} = {}", dWeight, d, soc, wdsoc);
        B += wdsoc;

        double adoptionThreshold = getAdoptionThreshold(agent, product);
        boolean noAdoption = B < adoptionThreshold;

        dataAnalyser.logEvaluationData(
                product, now,
                fin, env, nov, soc,
                afin, benv, cnov, dsoc,
                wafin, wbenv, wcnov, wdsoc,
                B
        );

        dataLogger.logEvaluationSuccess(
                agent, product, now,
                aWeight, bWeight, cWeight, dWeight,
                a, b, c, d,
                fin, env, nov, soc,
                afin, benv, cnov, dsoc,
                wafin, wbenv, wcnov, wdsoc,
                financialThreshold, ft,
                adoptionThreshold, B
        );

        alm.append("U < adoption threshold ({} < {}): {}", B, adoptionThreshold, noAdoption);
        logCalculateDecisionMaking(alm);

        if(noAdoption) {
            updateStage(RAStage.IMPEDED);
            return ProcessPlanResult.IMPEDED;
        } else {
            agent.adopt(need, product, now, determinePhase(now));
            logPhaseTransition(DataAnalyser.Phase.ADOPTED, now);
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

    protected void logPhaseTransition(DataAnalyser.Phase phaseId, Timestamp now) {
        environment.getDataAnalyser().logPhaseTransition(agent, phaseId, product, now);
    }

    protected void updateStage(RAStage nextStage) {
        logStageUpdate(nextStage);
        currentStage = nextStage;
    }

    protected AdoptionPhase determinePhase(Timestamp ts) {
        if(model.isYearChange()) {
            return AdoptionPhase.END_START;
        } else {
            if(model.isBeforeWeek27(ts)) {
                return AdoptionPhase.START_MID;
            } else {
                return AdoptionPhase.MID_END;
            }
        }
    }

    protected RAModelData modelData() {
        return getModel().getModelData();
    }

    protected double getFinancialComponent() {
        double npvAvg = getAverageNPV();
        double npvThis = getNPV(agent);

        double ftAvg = getAverageFinancialThresholdAgent();
        double ftThis = getFinancialThresholdAgent(agent);

        double ft = getLogisticFactor() * (ftThis - ftAvg);
        double npv = getLogisticFactor() * (npvThis - npvAvg);

        double logisticFt = MathUtil.logistic(ft);
        double logisticNpv = MathUtil.logistic(npv);

        double fin = (modelData().getWeightFT() * logisticFt) + (modelData().getWeightNPV() * logisticNpv);

        logFinancialComponent(ftAvg, ftThis, npvAvg, npvThis, getLogisticFactor(), ft, npv, logisticFt, logisticNpv, fin);
        environment.getDataLogger().logFinancialComponent(
                agent, product, now(),
                getLogisticFactor(),
                modelData().getWeightFT(), ftAvg, ftThis, ft, logisticFt,
                modelData().getWeightNPV(), npvAvg, npvThis, npv, logisticNpv,
                fin
        );

        return fin;
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
        double comp = djm * ((modelData().getWeightSocial() * shareOfAdopterInSocialNetwork.get()) + (modelData().getWeightLocal() * shareOfAdopterInLocalArea.get()));
        LOGGER.trace(
                IRPSection.SIMULATION_PROCESS,
                "[{}] dependent judgment making = {}, share of adopter in social network = {}, share of adopter in local area = {}, social component = {} = {} * ({} + {}) / 2.0",
                agent.getName(), djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get(), comp, djm, shareOfAdopterInSocialNetwork.get(), shareOfAdopterInLocalArea.get()
        );
        return comp;
    }

    protected double getFinancialThresholdAgent(ConsumerAgent agent) {
        return getModel().getFinancialPurchasePower(agent);
    }

    protected double getAverageFinancialThresholdAgent() {
        return modelData().getAverageFinancialPurchasePower(environment.getAgents().streamConsumerAgents());
    }

    protected double getNPV(ConsumerAgent agent) {
        return getNPV(agent, environment.getTimeModel().getCurrentYear());
    }

    protected double getNPV(ConsumerAgent agent, int year) {
        return modelData().NPV(agent, year);
    }

    protected double getAverageNPV() {
        return getAverageNPV(environment.getTimeModel().getCurrentYear());
    }

    protected double getAverageNPV(int year) {
        return modelData().avgNPV(environment.getAgents().streamConsumerAgents(), year);
    }

    protected double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getModel().getFinancialThreshold(agent, product);
    }

    protected double getAdoptionThreshold(ConsumerAgent agent, Product product) {
        return getModel().getAdoptionThreshold(agent, product);
    }

    protected double getInitialProductAwareness(ConsumerAgent agent, Product product) {
        return getModel().getInitialProductAwareness(agent, product);
    }

    protected double getInitialProductInterest(ConsumerAgent agent, Product product) {
        return getModel().getInitialProductInterest(agent, product);
    }

    protected double getInitialAdopter(ConsumerAgent agent, Product product) {
        return getModel().getInitialAdopter(agent, product);
    }

    protected double getCommunicationFrequencySN(ConsumerAgent agent) {
        return getModel().getCommunicationFrequencySN(agent);
    }

    protected double getRewiringRate(ConsumerAgent agent) {
        return getModel().getRewiringRate(agent);
    }

    protected long getId(ConsumerAgent agent) {
        return getModel().getId(agent);
    }

    protected double getPurchasePower(ConsumerAgent agent) {
        return getModel().getPurchasePower(agent);
    }

    protected double getNoveltySeeking(ConsumerAgent agent) {
        return getModel().getNoveltySeeking(agent);
    }

    protected double getDependentJudgmentMaking(ConsumerAgent agent) {
        return getModel().getDependentJudgmentMaking(agent);
    }

    protected double getEnvironmentalConcern(ConsumerAgent agent) {
        return getModel().getEnvironmentalConcern(agent);
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    @Override
    public void runEvaluationAtEndOfYear() {
        super.runEvaluationAtEndOfYear();
        logInterestValues();
    }

    @Override
    protected void doRunEvaluationAtEndOfYear() {
        handleDecisionMaking(null);
    }

    protected void logInterestValues() {
        environment.getDataAnalyser().logAnnualInterest(agent, product, getInterest(agent), now());
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

    protected double getInterestPoints(ConsumerAgent agent) {
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
                .streamSourcesAndTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(IS_CONSUMER)
                .distinct()
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

    @Todo
    protected void applyRelativeAgreement(ConsumerAgent target) {
        applyRelativeAgreement(target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    @Todo
    protected void applyRelativeAgreement(ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute opinionThis = agent.getAttribute(attrName);
        Uncertainty uncertaintyThis = getUncertainty();
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = getModel().getUncertaintyCache().getUncertainty(target);
        if(uncertaintyTarget == null) {
            LOGGER.warn(IRPSection.SIMULATION_PROCESS, "agent '{}' has no uncertainty - skip", target.getName());
        } else {
            getRelativeAgreementAlgorithm().apply(
                    getAgent().getName(),
                    opinionThis,
                    uncertaintyThis,
                    target.getName(),
                    opinionTarget,
                    uncertaintyTarget
            );
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
            double myOldPoints, double myNewPoints,
            Agent target, double targetOldPoints, double targetNewPoints) {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                "{} [{}] interest update: '{}': {} -> {}, '{}': {} -> {}",
                InfoTag.INTEREST_UPDATE, agent.getName(),
                agent.getName(), myOldPoints, myNewPoints,
                target.getName(), targetOldPoints, targetNewPoints
        );
    }

    protected void logGraphUpdateEdgeAdded(Agent target) {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                "{} [{}] graph update, edge added: '{}' -> '{}'",
                InfoTag.GRAPH_UPDATE, agent.getName(),
                agent.getName(), target.getName()
        );
    }

    protected void logGraphUpdateEdgeRemoved(Agent target) {
        if(target == null) {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                    "{} [{}] graph not updated, no valid target found",
                    InfoTag.GRAPH_UPDATE, agent.getName()
            );
        } else {
            LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                    "{} [{}] graph update, edge removed: '{}' -> '{}'",
                    InfoTag.GRAPH_UPDATE, agent.getName(),
                    agent.getName(), target.getName()
            );
        }
    }

    protected void logRelativeAgreementSuccess(
            String ai, String aj, String attribute,
            double xi, double ui, double xj, double uj, double m,
            double hij, double ra, double newXj, double newUj) {
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
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
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
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
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
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
        LOGGER.trace(IRPSection.SIMULATION_PROCESS,
                "{} [{}] financal component calculation: t_avg = {}, t = {}, NPV_avg = {}, NPV = {}, lf = {} | lf * (t - t_avg) = {}, lf * (NPV - NPV_avg) = {} | 1 / (1 + e^(-(lf * (t - t_avg))) = {}, 1 / (1 + e^(-(lf * (NPV - NPV_avg))) = {} | (logistic(t) + logistic(NPV)) / 2.0 = {}",
                InfoTag.FINANCAL_COMPONENT, agent.getName(),
                ftAvg, ftThis, npvAvg, npvThis, logisticFactor, ft, npv, logisticFt, logisticNpv, comp
        );
    }

    protected void logCalculateDecisionMaking(IRPLoggingMessageCollection mlm) {
        mlm.setSection(IRPSection.SIMULATION_PROCESS)
                .trace(LOGGER);
    }
}
