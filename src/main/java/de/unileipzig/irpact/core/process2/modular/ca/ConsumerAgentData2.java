package de.unileipzig.irpact.core.process2.modular.ca;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.process2.modular.modules.core.InputData2;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;
import de.unileipzig.irpact.core.util.AttributeHelper;

import java.util.stream.Stream;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentData2 extends InputData2 {

    //=========================
    //generic
    //=========================

    boolean has(int id);

    Object get(int id);

    Object put(int id, Object data);

    //=========================
    //general
    //=========================

    SimulationEnvironment getEnvironment();

    default Stream<? extends ConsumerAgent> streamConsumerAgents() {
        return getEnvironment().getAgents().streamConsumerAgents();
    }

    default int getCurrentYear() {
        return getEnvironment().getTimeModel().getCurrentYear();
    }

    default AttributeHelper getAttributeHelper() {
        return getEnvironment().getAttributeHelper();
    }

    ConsumerAgent getAgent();

    default String getAgentName() {
        return getAgent().getName();
    }

    Product getProduct();

    boolean isUnderConstruction();

    boolean isUnderRenovation();
}
