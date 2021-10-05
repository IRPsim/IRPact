package de.unileipzig.irpact.core.process2.modular.ca.ra.modules;

import de.unileipzig.irpact.commons.util.data.DataStore;
import de.unileipzig.irpact.commons.util.data.MutableBoolean;
import de.unileipzig.irpact.commons.util.data.MutableDouble;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.network.SocialGraph;
import de.unileipzig.irpact.core.network.filter.NodeFilter;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.npv.NPVCalculator;
import de.unileipzig.irpact.core.process.ra.npv.NPVData;
import de.unileipzig.irpact.core.process.ra.npv.NPVDataSupplier;
import de.unileipzig.irpact.core.process.ra.npv.NPVMatrix;
import de.unileipzig.irpact.core.process2.PostAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ConsumerAgentData2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAAction2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.modules.HelperAPI2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public interface RAHelperAPI2 extends HelperAPI2 {

    int ID = 123456789;
    Object AVG_FIN_KEY = new Object();
    Object LOCAL_NEIGHBOURS = new Object();
    Object LOCAL_NEIGHBOURS_TOO_LARGE = new Object();
    Object SOCIAL_NEIGHBOURS = new Object();
    Object SOCIAL_NEIGHBOURS_TOO_LARGE = new Object();

    //=========================
    //general
    //=========================

    default void traceModuleInfo(ConsumerAgentData2 input) {
        trace("[{}] call module {}={}", input.getAgentName(), getClass().getSimpleName(), getName());
    }

    default NPVDataSupplier getNPVDataSupplier(SimulationEnvironment environment, NPVData data) {
        DataStore store = environment.getGlobalData();
        if(store.contains(data)) {
            trace(IRPSection.INITIALIZATION_PARAMETER, "NPVDataSupplier for NPVDAta '{}' already exists", data);
            return store.getAs(data, NPVDataSupplier.class);
        } else {
            NPVCalculator calculator = new NPVCalculator();
            calculator.setData(data);

            int firstYear = environment.getTimeModel().getFirstSimulationYear();
            int lastYear = environment.getTimeModel().getLastSimulationYear();

            trace(IRPSection.INITIALIZATION_PARAMETER, "calculating npv matrix from '{}' to '{}'", firstYear, lastYear);
            NPVDataSupplier dataSupplier = new NPVDataSupplier();
            for(int y = firstYear; y <= lastYear; y++) {
                trace("calculate year '{}'", y);
                NPVMatrix matrix = new NPVMatrix();
                matrix.calculate(calculator, y);
                dataSupplier.put(y, matrix);
            }
            trace(IRPSection.INITIALIZATION_PARAMETER, "NPVDataSupplier for NPVDAta '{}' created", data);
            store.put(data, dataSupplier);
            return dataSupplier;
        }
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

    default boolean isAware(ConsumerAgentData2 input) {
        return input.getAgent().isAware(input.getProduct());
    }

    default void doSelfActionAndAllowAttention(ConsumerAgentData2 input) {
        input.getAgent().actionPerformed();
        input.getAgent().allowAttention();
    }

    default RAStage2 updateStage(ConsumerAgentData2 input, RAStage2 newStage) {
        return (RAStage2) input.put(ID, newStage);
    }

    default void doAction(ConsumerAgentData2 input, List<PostAction2> actions) {
        RAAction2.execute(input, actions);
    }

    //=========================
    //Attribute
    //=========================

    default double getDoubleValue(SimulationEnvironment environment, ConsumerAgent agent, Product product, String key) {
        return environment.getAttributeHelper().getDoubleValue(agent, product, key);
    }

    default double getDoubleValue(ConsumerAgentData2 input, String key) {
        return input.getAttributeHelper().getDoubleValue(input.getAgent(), input.getProduct(), key);
    }

    default double getInitialAdopter(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.INITIAL_ADOPTER);
    }

    default boolean getBooleanValue(ConsumerAgentData2 input, String key) {
        return input.getAttributeHelper().getBooleanValue(input.getAgent(), key);
    }

    default boolean isShareOf1Or2FamilyHouse(ConsumerAgentData2 input) {
        return getBooleanValue(input, RAConstants.SHARE_1_2_HOUSE);
    }

    default boolean isHouseOwner(ConsumerAgentData2 input) {
        return getBooleanValue(input, RAConstants.HOUSE_OWNER);
    }

    default double getPurchasePower(ConsumerAgentData2 input) {
        return getDoubleValue(input, RAConstants.PURCHASE_POWER_EUR);
    }
    default double getPurchasePower(SimulationEnvironment environment, ConsumerAgent agent, Product product) {
        return getDoubleValue(environment, agent, product, RAConstants.PURCHASE_POWER_EUR);
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
        DataStore store = input.getEnvironment().getGlobalData();
        if(store.contains(AVG_FIN_KEY)) {
            MutableDouble result = store.getAs(AVG_FIN_KEY, MutableDouble.class);
            return result.get();
        } else {
            return calculateAverageFinancialPurchasePower(input);
        }
    }

    default double calculateAverageFinancialPurchasePower(ConsumerAgentData2 input) {
        synchronized (AVG_FIN_KEY) {
            DataStore store = input.getEnvironment().getGlobalData();
            if(store.contains(AVG_FIN_KEY)) {
                MutableDouble result = store.getAs(AVG_FIN_KEY, MutableDouble.class);
                return result.get();
            } else {
                MutableDouble total = MutableDouble.zero();
                double sum = input.streamConsumerAgents()
                        .mapToDouble(ca -> {
                            total.inc();
                            return getFinancialPurchasePower(input.getEnvironment(), ca, input.getProduct());
                        })
                        .sum();
                double result = sum / total.get();
                MutableDouble cachedResult = MutableDouble.wrap(result);
                store.put(AVG_FIN_KEY, cachedResult);
                return cachedResult.get();
            }
        }
    }

    default double getShareOfAdopterInLocalNetwork(ConsumerAgentData2 input, NodeFilter filter, int maxToStore) {
        DataStore store = input.getEnvironment().getGlobalData();
        if(maxToStore > 0 && store.contains(LOCAL_NEIGHBOURS, input.getAgent())) {
            List<ConsumerAgent> neighbours = store.getAuto(LOCAL_NEIGHBOURS, input.getAgent());
            double adopter = 0;
            for(ConsumerAgent agent: neighbours) {
                if(agent.hasAdopted(input.getProduct())) {
                    adopter++;
                }
            }
            return adopter / (double) neighbours.size();
        } else {
            boolean tooLarge = store.getOr(LOCAL_NEIGHBOURS_TOO_LARGE, input.getAgent(), false);
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
        DataStore store = input.getEnvironment().getGlobalData();

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
            store.put(LOCAL_NEIGHBOURS, input.getAgent(), neighbours);
        } else {
            store.put(LOCAL_NEIGHBOURS_TOO_LARGE, input.getAgent(), true);
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
