package de.unileipzig.irpact.core.process2.modular.ca;

import de.unileipzig.irpact.commons.time.Timestamp;
import de.unileipzig.irpact.commons.util.Rnd;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.logging.data.DataAnalyser;
import de.unileipzig.irpact.core.logging.data.DataLogger;
import de.unileipzig.irpact.core.need.Need;
import de.unileipzig.irpact.core.process2.ProcessPlan2;
import de.unileipzig.irpact.core.process2.modular.ca.ra.RAStage2;
import de.unileipzig.irpact.core.process2.modular.modules.core.InputData2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.product.ProductGroup;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AdoptionPhase;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentData2 extends InputData2 {

    //=========================
    //generic data store
    //=========================

    boolean has(Object id);

    Object get(Object id);

    @SuppressWarnings("unchecked")
    default <R> R getAuto(Object id) {
        return (R) get(id);
    }

    default <R> R getOr(Object id, R ifMissing) {
        R value = getAuto(id);
        return value == null ? ifMissing : value;
    }

    Object put(Object id, Object data);

    //=========================
    //run
    //=========================

    long currentRun();

    boolean has(long run, Object key);

    Object get(long run, Object key);

    Object put(long run, Object key, Object data);

    //=========================
    //general
    //=========================

    RAStage2 getStage();

    void setStage(RAStage2 stage);

    SimulationEnvironment getEnvironment();

    ProcessPlan2 getPlan();

    default Stream<? extends ConsumerAgent> streamConsumerAgents() {
        return getEnvironment().getAgents().streamConsumerAgents();
    }

    default Timestamp now() {
        return getEnvironment().getTimeModel().now();
    }

    default int getCurrentYear() {
        return getEnvironment().getTimeModel().getCurrentYear();
    }

    default AttributeHelper getAttributeHelper() {
        return getEnvironment().getAttributeHelper();
    }

    default DataLogger getDataLogger() {
        return getEnvironment().getDataLogger();
    }

    default DataAnalyser getDataAnalyser() {
        return getEnvironment().getDataAnalyser();
    }

    ConsumerAgent getAgent();

    default String getAgentName() {
        return getAgent().getName();
    }

    default ConsumerAgentGroup getAgentGroup() {
        return getAgent().getGroup();
    }

    default String getAgentGroupName() {
        return getAgentGroup().getName();
    }

    Need getNeed();

    Product getProduct();

    default String getProductName() {
        return getProduct().getName();
    }

    default ProductGroup getProductGroup() {
        return getProduct().getGroup();
    }

    default String getProductGroupName() {
        return getProductGroup().getName();
    }

    boolean isUnderConstruction();

    void setUnderConstruction(boolean value);

    boolean isUnderRenovation();

    void setUnderRenovation(boolean value);

    CAModularProcessModel2 getModel();

    default AdoptionPhase determinePhase(Timestamp ts) {
        return getModel().determinePhase(ts);
    }

    Rnd rnd();
}
