package de.unileipzig.irpact.core.process2.modular.ca.ra;

import de.unileipzig.irpact.commons.time.TimeUtil;
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
import de.unileipzig.irpact.core.network.filter.NodeFilterScheme;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.HelperAPI2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface RAHelperAPI2 extends HelperAPI2 {

    //global keys
    //shared keys
    Object AVG_FIN_KEY = new Object();
    Object NEIGHBOURS_KEY = new Object();
    Object NEIGHBOURS_CHECKED_KEY = new Object();
    //individual keys
    Object NODE_FILTER = new Object();
    Object UTILITY = new Object();

    //=========================
    //general
    //=========================

    default NPVDataSupplier getNPVDataSupplier(SimulationEnvironment environment, NPVData data) {
        DataStore store = environment.getGlobalData();
        NPVDataSupplier dataSupplier = store.getAuto(data);
        if(dataSupplier == null) {
            NPVCalculator calculator = new NPVCalculator();
            calculator.setData(data);

            int firstYear = environment.getTimeModel().getFirstSimulationYear();
            int lastYear = environment.getTimeModel().getLastSimulationYear();
            int preFirstYear = firstYear - 1;

            trace(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' (first={}) to '{}'", preFirstYear, firstYear, lastYear);
            dataSupplier = new NPVDataSupplier();
            dataSupplier.setAttributeHelper(environment.getAttributeHelper());
            for(int y = preFirstYear; y <= lastYear; y++) {
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

    default void adopt(ConsumerAgentData2 input, Timestamp now, double utility) {
        input.getAgent().adopt(input.getNeed(), input.getProduct(), now, input.determinePhase(now), utility);
    }

    default int getCurrentYear(ConsumerAgentData2 input) {
        return input.getEnvironment().getTimeModel().getCurrentYear();
    }

    default int getStartYear(ConsumerAgentData2 input) {
        return input.getEnvironment().getTimeModel().getFirstSimulationYear();
    }

    default int getSimulationYearDelta(ConsumerAgentData2 input) {
        return getCurrentYear(input) - getStartYear(input);
    }

    default boolean isAdopter(ConsumerAgentData2 input) {
        return input.getAgent().hasAdopted(input.getProduct());
    }

    default boolean isInitialAdopter(ConsumerAgentData2 input) {
        return input.getAgent().hasInitialAdopted(input.getProduct());
    }

    default ZonedDateTime getCurrentSimulationTime(ConsumerAgentData2 input) {
        if(input.getEnvironment().getTimeModel().hasStarted()) {
            return input.getEnvironment().getTimeModel().now().getTime();
        } else {
            int firstYear = input.getEnvironment().getTimeModel().getFirstSimulationYear();
            return TimeUtil.lastDayOfYear(firstYear - 1);
        }
    }

    @Override
    default String printInputInfo(Object obj) {
        if(obj instanceof ConsumerAgentData2) {
            return ((ConsumerAgentData2) obj).getAgentName();
        } else {
            return HelperAPI2.super.printInputInfo(obj);
        }
    }

    //=========================
    //Utility
    //=========================

    default void setUtility(ConsumerAgentData2 input, double utility) {
        input.put(UTILITY, utility);
    }

    default double getUtility(ConsumerAgentData2 input) {
        return input.getOr(UTILITY, Double.NaN);
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

    default long getId(ConsumerAgentData2 input) {
        return getLong(input, RAConstants.ID);
    }

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

    default long getLong(ConsumerAgentData2 input, String attributeName) {
        return getLong(input.getEnvironment(), input.getAgent(), input.getProduct(), attributeName);
    }

    default double getDouble(ConsumerAgentData2 input, String attributeName) {
        return getDouble(input.getEnvironment(), input.getAgent(), input.getProduct(), attributeName);
    }

    default void setDouble(ConsumerAgentData2 input, String attributeName, double value) {
        setDouble(input.getEnvironment(), input.getAgent(), input.getProduct(), attributeName, value);
    }

    default boolean getBoolean(ConsumerAgentData2 input, String attributeName) {
        return getBoolean(input.getEnvironment(), input.getAgent(), input.getProduct(), attributeName);
    }

    default void setBoolean(ConsumerAgentData2 input, String attributeName, boolean value) {
        setBoolean(input.getEnvironment(), input.getAgent(), input.getProduct(), attributeName, value);
    }

    default double getRenovationRate(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.RENOVATION_RATE);
    }

    default double getConstructionRate(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.CONSTRUCTION_RATE);
    }

    default boolean isShareOf1Or2FamilyHouse(ConsumerAgentData2 input) {
        return getBoolean(input, RAConstants.SHARE_1_2_HOUSE);
    }

    default boolean isShareOf1Or2FamilyHouse(ConsumerAgent agent) {
        return getBoolean(agent.getEnvironment(), agent, null, RAConstants.SHARE_1_2_HOUSE);
    }

    default void setShareOf1Or2FamilyHouse(ConsumerAgentData2 input) {
        setBoolean(input, RAConstants.SHARE_1_2_HOUSE, true);
    }

    default boolean isHouseOwner(ConsumerAgentData2 input) {
        return getBoolean(input, RAConstants.HOUSE_OWNER);
    }

    default boolean isHouseOwner(ConsumerAgent agent) {
        return getBoolean(agent.getEnvironment(), agent, null, RAConstants.HOUSE_OWNER);
    }

    default void setHouseOwner(ConsumerAgentData2 input) {
        setBoolean(input, RAConstants.HOUSE_OWNER, true);
    }

    default double getPurchasePower(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.PURCHASE_POWER_EUR);
    }
    default double getPurchasePower(ConsumerAgent agent, Product product) {
        return getPurchasePower(agent.getEnvironment(), agent, product);
    }
    default double getPurchasePower(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return getDouble(environment, agent, product, RAConstants.PURCHASE_POWER_EUR);
    }

    default double getNoveltySeeking(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.NOVELTY_SEEKING);
    }
    default double getNoveltySeeking(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return getDouble(environment, agent, product, RAConstants.NOVELTY_SEEKING);
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
        return getDouble(input, RAConstants.FINANCIAL_THRESHOLD);
    }

    default double getFinancialThreshold(ConsumerAgent agent, Product product) {
        return getDouble(agent.getEnvironment(), agent, product, RAConstants.FINANCIAL_THRESHOLD);
    }

    default double getAdoptionThreshold(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.ADOPTION_THRESHOLD);
    }

    default double getEnvironmentalConcern(ConsumerAgentData2 input) {
        return getDouble(input, RAConstants.ENVIRONMENTAL_CONCERN);
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

    default NodeFilter getNodeFilter(ConsumerAgentData2 input, NodeFilterScheme scheme) {
        NodeFilter filter = (NodeFilter) input.get(NODE_FILTER);
        if(filter == null) {
            return createNodeFilter(input, scheme);
        } else {
            return filter;
        }
    }

    default NodeFilter createNodeFilter(ConsumerAgentData2 input, NodeFilterScheme scheme) {
        synchronized (NODE_FILTER) {
            NodeFilter filter = (NodeFilter) input.get(NODE_FILTER);
            if(filter == null) {
                filter = scheme.createFilter(input.getAgent());
                input.put(NODE_FILTER, filter);
            }
            return filter;
        }
    }

    //==========
    //local
    //==========

    default double getShareOfAdopterInLocalNetwork(
            ConsumerAgentData2 input,
            NodeFilter filter,
            int maxToStore) {

        if(maxToStore < 1) {
            return getShareOfAdopterInLocalNetwork(input, filter);
        }

        boolean neighboursChecked = getSharedData().contains(input.getAgent(), NEIGHBOURS_CHECKED_KEY);

        if(neighboursChecked) {
            List<ConsumerAgent> neighbours = getSharedData().getAuto(input.getAgent(), NEIGHBOURS_KEY);
            if(neighbours == null) {
                return getShareOfAdopterInLocalNetwork(input, filter);
            } else {
                return getShareOfAdopterInNeighbourhood(input, neighbours);
            }
        } else {
            getSharedData().put(input.getAgent(), NEIGHBOURS_CHECKED_KEY, true);
            return getShareOfAdopterInLocalNetworkAndTryToCache(input, filter, maxToStore);
        }
    }

    default double getShareOfAdopterInLocalNetworkAndTryToCache(
            ConsumerAgentData2 input,
            NodeFilter filter,
            int maxToStore) {

        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();

        MutableBoolean addNeighbours = MutableBoolean.trueValue();
        List<ConsumerAgent> neighbours = new ArrayList<>(maxToStore);

        streamNeighbours(input.getEnvironment(), input.getAgent(), filter)
                .forEach(target -> {
                    //update list
                    if(addNeighbours.isTrue()) {
                        if(neighbours.size() >= maxToStore) {
                            addNeighbours.setFalse();
                            neighbours.clear();
                        } else {
                            neighbours.add(target);
                        }
                    }
                    //check
                    if(isFeasibleAndFinancialNeighbour(target, input.getProduct())) {
                        totalGlobal.inc();
                        if(target.hasAdopted(input.getProduct())) {
                            adopterGlobal.inc();
                        }
                    }
                });

        if(addNeighbours.isTrue()) {
            getSharedData().put(input.getAgent(), NEIGHBOURS_KEY, neighbours);
        }

        if(totalGlobal.isZero()) {
            return 0.0;
        } else {
            return adopterGlobal.get() / totalGlobal.get();
        }
    }

    default double getShareOfAdopterInNeighbourhood(
            ConsumerAgentData2 input,
            Collection<? extends ConsumerAgent> neighbours) {
        double total = 0.0;
        double adopter = 0.0;
        for(ConsumerAgent agent: neighbours) {
            if(isFeasibleAndFinancialNeighbour(agent, input.getProduct())) {
                total++;
                if(agent.hasAdopted(input.getProduct())) {
                    adopter++;
                }
            }
        }
        return total == 0.0
                ? 0.0
                : adopter / total;
    }


    default double getShareOfAdopterInLocalNetwork(
            ConsumerAgentData2 input,
            NodeFilter filter) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();

        streamFeasibleAndFinancialNeighbours(input.getEnvironment(), input.getAgent(), input.getProduct(), filter)
                .forEach(target -> {
                    totalGlobal.inc();
                    if(target.hasAdopted(input.getProduct())) {
                        adopterGlobal.inc();
                    }
                });

        if(totalGlobal.isZero()) {
            return 0.0;
        } else {
            return adopterGlobal.get() / totalGlobal.get();
        }
    }

    default Stream<ConsumerAgent> streamNeighbours(
            SimulationEnvironment environment,
            ConsumerAgent source,
            NodeFilter filter) {
        return environment.getNetwork().getGraph().streamNodes()
                .filter(node -> node.is(ConsumerAgent.class))
                .filter(node -> node.getAgent() != source)
                .filter(filter)
                .map(node -> node.getAgent(ConsumerAgent.class));
    }

    default Stream<ConsumerAgent> streamFeasibleAndFinancialNeighbours(
            SimulationEnvironment environment,
            ConsumerAgent source,
            Product product,
            NodeFilter filter) {
        return streamNeighbours(environment, source, filter)
                .filter(target -> isFeasibleAndFinancialNeighbour(target, product));
    }

    //==========
    //social
    //==========

    default double getShareOfAdopterInSocialNetwork(ConsumerAgentData2 input) {
        MutableDouble totalGlobal = MutableDouble.zero();
        MutableDouble adopterGlobal = MutableDouble.zero();

        streamFeasibleAndFinancialInSocialNetwork(input.getEnvironment(), input.getAgent(), input.getProduct())
                .forEach(target -> {
                    totalGlobal.inc();
                    if(target.hasAdopted(input.getProduct())) {
                        adopterGlobal.inc();
                    }
                });

        if(totalGlobal.isZero()) {
            return 0.0;
        } else {
            return adopterGlobal.get() / totalGlobal.get();
        }
    }

    default Stream<ConsumerAgent> streamFeasibleAndFinancialInSocialNetwork(
            SimulationEnvironment environment,
            ConsumerAgent agent,
            Product product) {
        return environment.getNetwork().getGraph()
                .streamSourcesAndTargets(agent.getSocialGraphNode(), SocialGraph.Type.COMMUNICATION)
                .filter(node -> node.is(ConsumerAgent.class))
                .filter(node -> node.getAgent() != agent)
                .map(node -> node.getAgent(ConsumerAgent.class))
                .filter(target -> isFeasibleAndFinancialNeighbour(target, product));
    }

    default boolean isFeasibleAndFinancialNeighbour(ConsumerAgent agent, Product product) {
        if(isShareOf1Or2FamilyHouse(agent) && isHouseOwner(agent)) {
            double financial = getPurchasePower(agent, product);
            double threshold = getFinancialThreshold(agent, product);
            return threshold <= financial;
        } else {
            return false;
        }
    }
}
