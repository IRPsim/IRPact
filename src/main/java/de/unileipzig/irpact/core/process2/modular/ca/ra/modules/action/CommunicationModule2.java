package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.action;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAVoidModule2;
import de.unileipzig.irpact.core.process2.raalg.LoggableRelativeAgreementAlgorithm2;
import de.unileipzig.irpact.core.process2.raalg.RelativeAgreementAlgorithm2;
import de.unileipzig.irpact.core.process2.uncert.Uncertainty;
import de.unileipzig.irpact.core.process2.uncert.UncertaintyCache;
import de.unileipzig.irpact.core.process2.uncert.UncertaintySupplier;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Daniel Abitz
 */
public class CommunicationModule2
        extends AbstractCAVoidModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CommunicationModule2.class);

    protected double adopterPoints = RAModelData.DEFAULT_ADOPTER_POINTS;
    protected double interestedPoints = RAModelData.DEFAULT_INTERESTED_POINTS;
    protected double awarePoints = RAModelData.DEFAULT_AWARE_POINTS;
    protected double unknownPoints = RAModelData.DEFAULT_UNKNOWN_POINTS;

    protected boolean raEnabled = true;
    protected RelativeAgreementAlgorithm2 raAlgorithm;
    protected LoggableRelativeAgreementAlgorithm2 logRaAlgorithm;
    protected List<UncertaintySupplier> uncertaintySuppliers = new ArrayList<>();
    protected UncertaintyCache uncertaintyCache = new UncertaintyCache();

    public CommunicationModule2() {
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public void setAdopterPoints(double adopterPoints) {
        this.adopterPoints = adopterPoints;
    }

    public double getAdopterPoints() {
        return adopterPoints;
    }

    public void setInterestedPoints(double interestedPoints) {
        this.interestedPoints = interestedPoints;
    }

    public double getInterestedPoints() {
        return interestedPoints;
    }

    public void setAwarePoints(double awarePoints) {
        this.awarePoints = awarePoints;
    }

    public double getAwarePoints() {
        return awarePoints;
    }

    public void setUnknownPoints(double unknownPoints) {
        this.unknownPoints = unknownPoints;
    }

    public double getUnknownPoints() {
        return unknownPoints;
    }

    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm2 raAlgorithm) {
        this.raAlgorithm = raAlgorithm;
        if(raAlgorithm instanceof LoggableRelativeAgreementAlgorithm2) {
            this.logRaAlgorithm = (LoggableRelativeAgreementAlgorithm2) raAlgorithm;
        }
    }

    public RelativeAgreementAlgorithm2 getRelativeAgreementAlgorithm() {
        return raAlgorithm;
    }

    public void addUncertaintySupplier(UncertaintySupplier supplier) {
        uncertaintySuppliers.add(supplier);
    }

    public void setRaEnabled(boolean raEnabled) {
        this.raEnabled = raEnabled;
    }

    public boolean isRaEnabled() {
        return raEnabled;
    }

    public boolean isRaDisabled() {
        return !raEnabled;
    }

    @Override
    public void validate() throws Throwable {
        if(raAlgorithm == null) {
            throw new NullPointerException("missing relative agreement algorithm");
        }

        if(uncertaintySuppliers.isEmpty()) {
            throw new IllegalArgumentException("missing uncertainty supplier");
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        logRaAlgorithm.initialize(environment);

        setInitalized();
    }

    @Override
    public void initializeNewInput(ConsumerAgentData2 input) throws Throwable {
        traceNewInput(input);

        ConsumerAgent agent = input.getAgent();
        if(!uncertaintyCache.hasUncertainty(agent)) {
            UncertaintySupplier supplier = findSupplier(agent);
            Uncertainty uncertainty = supplier.createFor(agent);
            uncertaintyCache.register(agent, uncertainty);
            trace("[{}] add uncertainty '{}' for agent '{}' (uncertainty supplier={})", getName(), uncertainty.getName(), agent.getName(), supplier.getName());
        }
    }

    @Override
    public void setup(SimulationEnvironment environment) throws Throwable {
        if(alreadySetupCalled()) {
            return;
        }

        setSetupCalled();
    }

    protected UncertaintySupplier findSupplier(ConsumerAgent agent) {
        List<UncertaintySupplier> supported = uncertaintySuppliers.stream()
                .filter(s -> s.isSupported(agent))
                .collect(Collectors.toList());
        if(supported.size() == 1) {
            return supported.get(0);
        } else {
            List<String> names = supported.stream()
                    .map(Nameable::getName)
                    .collect(Collectors.toList());
            throw new IllegalStateException(StringUtil.format("multiple suppliers found for agent '{}': {}", agent.getName(), names));
        }
    }

//    protected void printTargets(ConsumerAgentData2 input, List<SocialGraph.Node> targetList) {
//        StringBuilder sb = new StringBuilder();
//        targetList.forEach(n -> sb.append(n.getAgent().getName()).append(" "));
//        String str = sb.toString();
//        LOGGER.info("[COMMU] {} {} {}", input.getAgentName(), str.hashCode(), str);
//    }

    @Override
    public void run(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall(input);

        trace("[{}] start communication", input.getAgentName());

        SimulationEnvironment environment = input.getEnvironment();
        ConsumerAgent agent = input.getAgent();
        Product product = input.getProduct();
        Rnd rnd = input.rnd();

        SocialGraph graph = environment.getNetwork().getGraph();
        SocialGraph.Node node = agent.getSocialGraphNode();
        //create random target order
        List<SocialGraph.Node> targetList = new ArrayList<>();
        graph.getTargets(node, SocialGraph.Type.COMMUNICATION, targetList);
//        printTargets(input, targetList);
        Collections.shuffle(targetList, rnd.getRandom());
//        printTargets(input, targetList);

        for(SocialGraph.Node targetNode: targetList) {
            ConsumerAgent targetAgent = targetNode.getAgent(ConsumerAgent.class);

            Acting.AttentionResult result = Acting.aquireDoubleSidedAttentionAndDataAccess(agent, targetAgent);
            switch (result) {
                case SELF_OCCUPIED:
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] failed communication('{}' -> '{}') , self occupied", agent.getName(), agent.getName(), targetAgent.getName());
                    return;

                case TARGET_OCCUPIED:
                    LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] failed communication ('{}' -> '{}'), target occupied, try next target", agent.getName(), agent.getName(), targetAgent.getName());
                    continue;

                case SUCCESS:
                    agent.actionPerformed();
                    targetAgent.actionPerformed();
                    agent.releaseAttention();
                    targetAgent.releaseAttention();

                    //datalock
                    try {
                        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] start communication ('{}' -> '{}')", agent.getName(), agent.getName(), targetAgent.getName());

                        updateInterest(agent, targetAgent, product);
                        updateCommunicationGraph(graph, targetNode, agent.getSocialGraphNode());
                        applyRelativeAgreement(agent, targetAgent, input.now());

                        return;
                    } finally {
                        agent.releaseDataAccess();
                        targetAgent.releaseDataAccess();
                    }
            }
        }

        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] no valid communication partner found", agent.getName());
    }

    protected void updateInterest(ConsumerAgent agent, ConsumerAgent targetAgent, Product product) {
        double myPointsToAdd = getInterestPoints(agent, product);
        double targetPointsToAdd = getInterestPoints(targetAgent, product);

        updateInterest(agent, agent, product, targetPointsToAdd);
        updateInterest(agent, targetAgent, product, myPointsToAdd);
    }

    protected void updateInterest(ConsumerAgent thisAgent, ConsumerAgent agent, Product product, double pointsToAdd) {
        if(pointsToAdd > 0) {
            if(!agent.isAware(product)) {
                agent.makeAware(product);
                trace(IRPSection.SIMULATION_PROCESS, "[{}]@[{}] make aware '{}'", getName(), thisAgent.getName(), agent.getName());
            }
            double oldInterest = agent.getInterest(product);
            agent.updateInterest(product, pointsToAdd);
            double newInterest = agent.getInterest(product);
            trace(IRPSection.SIMULATION_PROCESS, "[{}]@[{}] update interest '{}': {} -> {} (delta={}, interested={})", getName(), thisAgent.getName(), agent.getName(), oldInterest, newInterest, pointsToAdd, agent.isInterested(product));
        } else {
            trace(IRPSection.SIMULATION_PROCESS, "[{}]@[{}] 0 points to add ('{}')", getName(), thisAgent.getName(), agent.getName());
        }
    }

    protected double getInterestPoints(ConsumerAgent agent, Product product) {
        if(agent.hasAdopted(product)) {
            return adopterPoints;
        }
        if(agent.isInterested(product)) {
            return interestedPoints;
        }
        if(agent.isAware(product)) {
            return awarePoints;
        }
        return unknownPoints;
    }

    protected void applyRelativeAgreement(ConsumerAgent source, ConsumerAgent target, Timestamp now) {
        if(isRaDisabled()) {
            trace("[{}]@[{}] relative agreement disabled", getName(), source.getName());
            return;
        }

        applyRelativeAgreement(source, target, RAConstants.NOVELTY_SEEKING, now);
        //applyRelativeAgreement(source, target, RAConstants.DEPENDENT_JUDGMENT_MAKING, now);
        applyRelativeAgreement(source, target, RAConstants.ENVIRONMENTAL_CONCERN, now);
    }

    protected void applyRelativeAgreement(ConsumerAgent source, ConsumerAgent target, String attrName, Timestamp now) {

        ConsumerAgentAttribute opinionThis = source.getAttribute(attrName);
        Uncertainty uncertaintyThis = uncertaintyCache.getUncertainty(source);
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = uncertaintyCache.getUncertainty(target);

        if(uncertaintyThis == null) {
            throw new NullPointerException("missing uncertainty for agent '" + source.getName() + "', attribute=" + attrName);
        }
        if(uncertaintyTarget == null) {
            throw new NullPointerException("missing uncertainty for agent '" + target.getName() + "', attribute=" + attrName);
        }

        double xi = opinionThis.asValueAttribute().getDoubleValue();
        double ui = uncertaintyThis.getUncertainty(opinionThis);
        double xj = opinionTarget.asValueAttribute().getDoubleValue();
        double uj = uncertaintyTarget.getUncertainty(opinionTarget);

        boolean changed;
        double[] influence = new double[RelativeAgreementAlgorithm2.INFLUENCE_LENGTH];

        if(logRaAlgorithm == null) {
            changed = raAlgorithm.calculateInfluence(xi, ui, xj, uj, influence);
        } else {
            changed = logRaAlgorithm.calculateInfluence(
                    source.getName(), xi, ui,
                    target.getName(), xj, uj,
                    attrName,
                    influence,
                    now
            );
        }

        if(changed) {
            opinionThis.asValueAttribute().setDoubleValue(influence[RelativeAgreementAlgorithm2.INDEX_XI]);
            uncertaintyThis.setUncertainty(opinionThis, influence[RelativeAgreementAlgorithm2.INDEX_UI]);
            opinionTarget.asValueAttribute().setDoubleValue(influence[RelativeAgreementAlgorithm2.INDEX_XJ]);
            uncertaintyTarget.setUncertainty(opinionTarget, influence[RelativeAgreementAlgorithm2.INDEX_UJ]);

            uncertaintyThis.updateOpinion(opinionThis, xi, influence[RelativeAgreementAlgorithm2.INDEX_XI]);
            uncertaintyTarget.updateOpinion(opinionTarget, xj, influence[RelativeAgreementAlgorithm2.INDEX_XJ]);
        }
    }
}
