package de.unileipzig.irpact.core.process2.modular.ca.ra;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.data.AgentDataState;
import de.unileipzig.irpact.core.logging.data.AgentDataStateLoggingHelper;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.logging.IRPLoggingMessageCollection;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.filter.ProcessPlanNodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyCache;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyHandler;
import de.unileipzig.irpact.core.process.ra.uncert.UncertaintyManager;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public interface RAHelperAPI2 extends HelperAPI2 {

    //global keys
    //shared keys
    Object AVG_FIN_KEY = new Object();
    Object LOCAL_NEIGHBOURS = new Object();
    Object LOCAL_NEIGHBOURS_TOO_LARGE = new Object();
    Object UNCERTAINTY = new Object();
    //individual keys
    Object STAGE = new Object();
    Object NODE_FILTER = new Object();

    //=========================
    //general
    //=========================

    default void traceModuleInfo(ConsumerAgentData2 input) {
        trace("[{}] call module {}={}", input.getAgentName(), getClass().getSimpleName(), getName());
    }

    default NPVDataSupplier getNPVDataSupplier(SimulationEnvironment environment, NPVData data) {
        DataStore store = environment.getGlobalData();
        NPVDataSupplier dataSupplier = store.getAuto(data);
        if(dataSupplier == null) {
            NPVCalculator calculator = new NPVCalculator();
            calculator.setData(data);

            int firstYear = environment.getTimeModel().getFirstSimulationYear();
            int lastYear = environment.getTimeModel().getLastSimulationYear();

            trace(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
            dataSupplier = new NPVDataSupplier();
            dataSupplier.setAttributeHelper(environment.getAttributeHelper());
            for(int y = firstYear; y <= lastYear; y++) {
                trace("calculate year '{}'", y);
                NPVMatrix matrix = new NPVMatrix();
                matrix.calculate(calculator, y);
                dataSupplier.put(y, matrix);
            }
            trace(IRPSection.INITIALIZATION_PARAMETER, "NPVDataSupplier for NPVDAta '{}' created", data);
            store.put(data, dataSupplier);
        } else {
            trace(IRPSection.INITIALIZATION_PARAMETER, "NPVDataSupplier for NPVDAta '{}' already exists", data);
        }
        return dataSupplier;
    }

    default void adopt(ConsumerAgentData2 input, Timestamp now) {
        input.getAgent().adopt(input.getNeed(), input.getProduct(), now, input.determinePhase(now));
    }

    default int getCurrentYear(ConsumerAgentData2 input) {
        return input.getEnvironment().getTimeModel().getCurrentYear();
    }

    default int getStartYear(ConsumerAgentData2 input) {
        return input.getEnvironment().getTimeModel().getFirstSimulationYear();
    }

    default int getYearDelta(ConsumerAgentData2 input) {
        return getCurrentYear(input) - getStartYear(input);
    }

    default boolean isAdopter(ConsumerAgentData2 input) {
        return input.getAgent().hasAdopted(input.getProduct());
    }

    default boolean isInitialAdopter(ConsumerAgentData2 input) {
        return input.getAgent().hasInitialAdopted(input.getProduct());
    }

    @Override
    default String printName(Object obj) {
        if(obj instanceof ConsumerAgentData2) {
            return ((ConsumerAgentData2) obj).getAgentName();
        } else {
            return HelperAPI2.super.printName(obj);
        }
    }

    //=========================
    //Uncertainty
    //=========================

    default UncertaintyHandler getUncertaintyHandler() {
        UncertaintyHandler handler = getSharedData().getAs(UNCERTAINTY, UncertaintyHandler.class);
        if(handler == null) {
            throw new NoSuchElementException("missing uncertainy handler");
        }
        return handler;
    }

    default UncertaintyCache getUncertaintyCache() {
        return getUncertaintyHandler().getCache();
    }

    default UncertaintyManager getUncertaintyManager() {
        return getUncertaintyHandler().getManager();
    }

    //=========================
    //Graph
    //=========================

    default void updateCommunicationGraph(SocialGraph graph, SocialGraph.Node source, SocialGraph.Node target) {
        if(graph.hasNoEdge(source, target, SocialGraph.Type.COMMUNICATION)) {
            graph.addEdge(source, target, SocialGraph.Type.COMMUNICATION, 1.0);
        }
    }

    //=========================
    //Logging
    //=========================

    default void traceSimulationProcess(IRPLoggingMessageCollection lmc) {
        lmc.setSection(IRPSection.SIMULATION_PROCESS)
                .trace(getDefaultLogger());
    }

    default AgentDataState getAgentDataState(ConsumerAgentData2 input) {
        return input.getEnvironment().getDataAnalyser().getAgentDataState(input.getAgent(), input.getProduct(), input.getCurrentYear());
    }

    default void updateAgentDataState(ConsumerAgentData2 input, AgentDataStateLoggingHelper helper, double value) {
        AgentDataState state = getAgentDataState(input);
        helper.set(state, value);
    }

    default void logEvaluationFailed(ConsumerAgentData2 input, Timestamp now, AgentDataState state) {
        input.getDataLogger().logEvaluationFailed(input.getAgent(), input.getProduct(), now, state);
    }

    default void logEvaluationSuccess(ConsumerAgentData2 input, Timestamp now, AgentDataState state) {
        input.getDataLogger().logEvaluationSuccess(input.getAgent(), input.getProduct(), now, state);
    }

    default void logAgentDataState(ConsumerAgentData2 input) {
        logAgentDataState(input, input.now(), getAgentDataState(input));
    }

    default void logAgentDataState(ConsumerAgentData2 input, Timestamp now, AgentDataState state) {
        input.getDataAnalyser().logAgentDataState(input.getAgent(), input.getProduct(), now.getYear(), state);
    }

    default void logPhaseTransition(ConsumerAgentData2 input, Timestamp now, DataAnalyser.Phase phase) {
        input.getDataAnalyser().logPhaseTransition(input.getAgent(), phase, input.getProduct(), now);
    }

    default void logAnnualInterest(ConsumerAgentData2 input) {
        input.getDataAnalyser().logAnnualInterest(input.getAgent(), input.getProduct(), getInterest(input), input.now());
    }

    //=========================
    //Attribute
    //=========================

    default boolean isInterested(ConsumerAgentData2 input) {
        return input.getAgent().isInterested(input.getProduct());
    }

    default void makeInterested(ConsumerAgentData2 input) {
        input.getAgent().makeInterested(input.getProduct());
    }

    default double getInterest(ConsumerAgentData2 input) {
        return getInterest(input.getAgent(), input.getProduct());
    }

    default double getInterest(ConsumerAgent agent, Product product) {
        return agent.getInterest(product);
    }

    default boolean isAware(ConsumerAgentData2 input) {
        return input.getAgent().isAware(input.getProduct());
    }

    //TODO spezielles action module einbauen, welches die attention automatisch macht + task erstellt
    default void allowAttention(ConsumerAgentData2 input) {
        input.getAgent().allowAttention();
    }

    default void doSelfActionAndAllowAttention(ConsumerAgentData2 input) {
        input.getAgent().actionPerformed();
        input.getAgent().allowAttention();
    }

    default RAStage2 updateStageAndLog(ConsumerAgentData2 input, Timestamp stamp, RAStage2 newStage, DataAnalyser.Phase phase) {
        logPhaseTransition(input, stamp, phase);
        return updateStage(input, newStage);
    }

    default RAStage2 updateStage(ConsumerAgentData2 input, RAStage2 newStage) {
        return (RAStage2) input.put(STAGE, newStage);
    }

    default DataAnalyser.Phase getPhase(RAStage2 stage) {
        switch (stage) {
            case AWARENESS:
                return DataAnalyser.Phase.AWARENESS;
            case FEASIBILITY:
                return DataAnalyser.Phase.FEASIBILITY;
            case DECISION_MAKING:
            case IMPEDED:
                return DataAnalyser.Phase.DECISION_MAKING;
            case ADOPTED:
                return DataAnalyser.Phase.ADOPTED;

            default:
                throw new IllegalArgumentException("unsupported stage: " + stage);
        }
    }

    //=========================
    //Attribute
    //=========================

    default double getDoubleValue(ConsumerAgentData2 input, String key) {
        return tryFindDoubleValue(input.getEnvironment(), input.getAgent(), input.getProduct(), key);
    }

    default double tryFindDoubleValue(ConsumerAgentData2 input, String key) {
        return tryFindDoubleValue(input.getEnvironment(), input.getAgent(), input.getProduct(), key);
    }

    default double getInitialAdopter(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.INITIAL_ADOPTER);
    }

    default boolean getBooleanValue(ConsumerAgentData2 input, String key) {
        return getBooleanValue(input.getEnvironment(), input.getAgent(), key);
    }

    default double getRenovationRate(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.RENOVATION_RATE);
    }

    default double getConstructionRate(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.CONSTRUCTION_RATE);
    }

    default boolean isShareOf1Or2FamilyHouse(ConsumerAgentData2 input) {
        return getBooleanValue(input, RAConstants.SHARE_1_2_HOUSE);
    }

    default void setShareOf1Or2FamilyHouse(ConsumerAgentData2 input) {
        input.getEnvironment().getAttributeHelper().findAndSetBooleanValue(input.getAgent(), RAConstants.SHARE_1_2_HOUSE, true);
    }

    default boolean isHouseOwner(ConsumerAgentData2 input) {
        return getBooleanValue(input, RAConstants.HOUSE_OWNER);
    }

    default void setHouseOwner(ConsumerAgentData2 input) {
        input.getEnvironment().getAttributeHelper().findAndSetBooleanValue(input.getAgent(), RAConstants.HOUSE_OWNER, true);
    }

    default double getPurchasePower(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.PURCHASE_POWER_EUR);
    }
    default double getPurchasePower(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return tryFindDoubleValue(environment, agent, product, RAConstants.PURCHASE_POWER_EUR);
    }

    default double getNoveltySeeking(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.NOVELTY_SEEKING);
    }
    default double getNoveltySeeking(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return getDoubleValue(environment, agent, product, RAConstants.NOVELTY_SEEKING);
    }

    default double getFinancialPurchasePower(ConsumerAgentData2 input) {
//        return getPurchasePower(input) * getNoveltySeeking(input);
        return getPurchasePower(input);
    }
    default double getFinancialPurchasePower(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return getPurchasePower(environment, agent, product);
    }

    default double getAverageFinancialPurchasePower(ConsumerAgentData2 input) {
        MutableDouble avgFin = getSharedData().getAuto(AVG_FIN_KEY);
        if(avgFin == null) {
            return calculateAverageFinancialPurchasePower(input);
        } else {
            return avgFin.get();
        }
    }

    default double calculateAverageFinancialPurchasePower(ConsumerAgentData2 input) {
        synchronized (AVG_FIN_KEY) {
            MutableDouble avgFin = getSharedData().getAuto(AVG_FIN_KEY);
            if(avgFin == null) {
                MutableDouble total = MutableDouble.zero();
                double sum = input.streamConsumerAgents()
                        .mapToDouble(ca -> {
                            total.inc();
                            return getFinancialPurchasePower(input.getEnvironment(), ca, input.getProduct());
                        })
                        .sum();
                double result = sum / total.get();
                MutableDouble cachedResult = MutableDouble.wrap(result);
                getSharedData().put(AVG_FIN_KEY, cachedResult);
                return cachedResult.get();
            } else {
                return avgFin.get();
            }
        }
    }

    default double getFinancialThreshold(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.FINANCIAL_THRESHOLD);
    }

    default double getAdoptionThreshold(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.ADOPTION_THRESHOLD);
    }

    default double getEnvironmentalConcern(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.ENVIRONMENTAL_CONCERN);
    }

    //=========================
    //util
    //=========================

    default void applyUnderConstruction(ConsumerAgentData2 input) {
        if(!isShareOf1Or2FamilyHouse(input)) {
            setShareOf1Or2FamilyHouse(input);
            trace("[{}] agent '{}' is now share of 1 or 2 family house", getName());
        }

        if(!isHouseOwner(input)) {
            setHouseOwner(input);
            trace("[{}] agent '{}' is now house owner", getName());
        }
    }

    default NodeFilter getNodeFilter(ConsumerAgentData2 input, ProcessPlanNodeFilterScheme scheme) {
        NodeFilter filter = (NodeFilter) input.get(NODE_FILTER);
        if(filter == null) {
            return createNodeFilter(input, scheme);
        } else {
            return filter;
        }
    }

    default NodeFilter createNodeFilter(ConsumerAgentData2 input, ProcessPlanNodeFilterScheme scheme) {
        synchronized (NODE_FILTER) {
            NodeFilter filter = (NodeFilter) input.get(NODE_FILTER);
            if(filter == null) {
                filter = scheme.createFilter(input.getPlan());
                input.put(NODE_FILTER, filter);
            }
            return filter;
        }
    }

    default double getShareOfAdopterInLocalNetwork(ConsumerAgentData2 input, NodeFilter filter, int maxToStore) {
        List<ConsumerAgent> neighbours = getSharedData().getAuto(LOCAL_NEIGHBOURS, input.getAgent());
        if(maxToStore > 0 && neighbours != null) {
            double adopter = 0;
            for(ConsumerAgent agent: neighbours) {
                if(agent.hasAdopted(input.getProduct())) {
                    adopter++;
                }
            }
            return adopter / (double) neighbours.size();
        } else {
            boolean tooLarge = getSharedData().getOr(LOCAL_NEIGHBOURS_TOO_LARGE, input.getAgent(), false);
            if(maxToStore < 1 || tooLarge) {
                return getShareOfAdopterInLocalNetwork(input, filter);
            } else {
                return getShareOfAdopterInLocalNetworkAndTryToStore(input, filter, maxToStore);
            }
        }
    }

    default double getShareOfAdopterInLocalNetwork(ConsumerAgentData2 input, NodeFilter filter) {
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        input.getEnvironment().getNetwork().getGraph()
                .streamNodes()
                .filter(node -> node.is(ConsumerAgent.class))
                .filter(filter)
                .map(node -> node.getAgent(ConsumerAgent.class))
                .forEach(agent -> {
                    totalLocal.inc();
                    if(agent.hasAdopted(input.getProduct())) {
                        adopterLocal.inc();
                    }
                });

        if(totalLocal.isZero()) {
            return 0;
        } else {
            return adopterLocal.get() / totalLocal.get();
        }
    }

    default double getShareOfAdopterInLocalNetworkAndTryToStore(ConsumerAgentData2 input, NodeFilter filter, int maxToStore) {
        MutableBoolean valid = MutableBoolean.trueValue();
        MutableDouble totalLocal = MutableDouble.zero();
        MutableDouble adopterLocal = MutableDouble.zero();

        List<ConsumerAgent> neighbours = new ArrayList<>();
        input.getEnvironment().getNetwork().getGraph()
                .streamNodes()
                .filter(node -> node.is(ConsumerAgent.class))
                .filter(filter)
                .map(node -> node.getAgent(ConsumerAgent.class))
                .forEach(agent -> {
                    totalLocal.inc();
                    if(valid.isTrue()) {
                        neighbours.add(agent);
                        if(neighbours.size() > maxToStore) {
                            valid.setFalse();
                            neighbours.clear();
                        }
                    }
                    if(agent.hasAdopted(input.getProduct())) {
                        adopterLocal.inc();
                    }
                });

        trace("[{}] getShareOfAdopterInLocalNetworkAndTryToStore: valid={}, neighbours={}, maxToStore={}", input.getAgentName(), valid.get(), neighbours.size(), maxToStore);
        if(valid.isTrue()) {
            getSharedData().put(LOCAL_NEIGHBOURS, input.getAgent(), neighbours);
        } else {
            getSharedData().put(LOCAL_NEIGHBOURS_TOO_LARGE, input.getAgent(), true);
        }

        if(totalLocal.isZero()) {
            return 0;
        } else {
            return adopterLocal.get() / totalLocal.get();
        }
    }

    default double getShareOfAdopterInSocialNetwork(ConsumerAgentData2 input) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();

        input.getEnvironment().getNetwork().getGraph()
                .streamSourcesAndTargets(input.getAgent().getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(node -> node.is(ConsumerAgent.class))
                .distinct()
                .forEach(globalNode -> {
                    totalGlobal.inc();
                    if(globalNode.getAgent(ConsumerAgent.class).hasAdopted(input.getProduct())) {
                        adopterGlobal.inc();
                    }
                });

        if(totalGlobal.isZero()) {
            return 0.0;
        } else {
            return adopterGlobal.get() / totalGlobal.get();
        }
    }
}
