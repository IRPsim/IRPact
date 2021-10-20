package de.unileipzig.irpact.core.process2.modular.ca.ra.modules.action;

import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.Acting;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.attribute.ConsumerAgentAttribute;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.alg.RelativeAgreementAlgorithm;
import de.unileipzig.irpact.core.process.ra.uncert.*;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAHelperAPI2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.modules.core.AbstractCAVoidModule2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public class CommunicationModule2
        extends AbstractCAVoidModule2
        implements RAHelperAPI2 {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CommunicationModule2.class);

    protected double adopterPoints = 0;
    protected double interestedPoints = 0;
    protected double awarePoints = 0;
    protected double unknownPoints = 0;

    protected RelativeAgreementAlgorithm raAlgorithm;
    protected UncertaintyHandler uncertaintyHandler;

    public CommunicationModule2() {
        uncertaintyHandler = new UncertaintyHandler();
        uncertaintyHandler.setCache(new UncertaintyCache());
        uncertaintyHandler.setManager(new BasicUncertaintyManager());
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

    public void setRelativeAgreementAlgorithm(RelativeAgreementAlgorithm raAlgorithm) {
        this.raAlgorithm = raAlgorithm;
    }

    public RelativeAgreementAlgorithm getRelativeAgreementAlgorithm() {
        return raAlgorithm;
    }

    @Override
    public UncertaintyHandler getUncertaintyHandler() {
        return uncertaintyHandler;
    }

    @Override
    public void validate() throws Throwable {
        if(raAlgorithm == null) {
            throw new NullPointerException("missing relative agreement algorithm");
        }
    }

    @Override
    public void initialize(SimulationEnvironment environment) throws Throwable {
        if(alreadyInitalized()) {
            return;
        }

        if(getSharedData().contains(UNCERTAINTY)) {
            throw new IllegalStateException("uncertainty already exists");
        } else {
            getSharedData().put(UNCERTAINTY, uncertaintyHandler);
        }
        setInitalized();
    }

    @Override
    public void run(ConsumerAgentData2 input, List<PostAction2> actions) throws Throwable {
        traceModuleCall();

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
        Collections.shuffle(targetList, rnd.getRandom());

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

                    //nur hier haben wir datalock durch aquireAttentionAndDataAccess
                    try {
                        LOGGER.trace(IRPSection.SIMULATION_PROCESS, "[{}] start communication ('{}' -> '{}')", agent.getName(), agent.getName(), targetAgent.getName());

                        updateInterest(agent, targetAgent, product);
                        updateCommunicationGraph(graph, targetNode, agent.getSocialGraphNode());
                        applyRelativeAgreement(agent, targetAgent);

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
//        double myInterest = getInterest(agent, product);
//        double targetInterest = getInterest(targetAgent, product);

        double myPointsToAdd = getInterestPoints(agent, product);
        double targetPointsToAdd = getInterestPoints(targetAgent, product);

        agent.updateInterest(product, targetPointsToAdd);
        targetAgent.updateInterest(product, myPointsToAdd);
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

    protected void applyRelativeAgreement(ConsumerAgent source, ConsumerAgent target) {
        applyRelativeAgreement(source, target, RAConstants.NOVELTY_SEEKING);
        applyRelativeAgreement(source, target, RAConstants.DEPENDENT_JUDGMENT_MAKING);
        applyRelativeAgreement(source, target, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    protected void applyRelativeAgreement(ConsumerAgent source, ConsumerAgent target, String attrName) {
        ConsumerAgentAttribute opinionThis = source.getAttribute(attrName);
        Uncertainty uncertaintyThis = getUncertaintyCache().getUncertainty(source);
        ConsumerAgentAttribute opinionTarget = target.getAttribute(attrName);
        Uncertainty uncertaintyTarget = getUncertaintyCache().getUncertainty(target);

        if(uncertaintyThis == null) {
            throw new NullPointerException("missing uncertainty for agent '" + source.getName() + "', attribute=" + attrName);
        }
        if(uncertaintyTarget == null) {
            throw new NullPointerException("missing uncertainty for agent '" + target.getName() + "', attribute=" + attrName);
        }

        raAlgorithm.apply(
                source.getName(),
                opinionThis,
                uncertaintyThis,
                target.getName(),
                opinionTarget,
                uncertaintyTarget
        );
    }
}
