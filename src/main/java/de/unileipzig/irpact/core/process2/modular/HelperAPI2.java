package de.unileipzig.irpact.core.process2.modular;

import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgent;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irpact.core.product.Product;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public interface HelperAPI2 extends Nameable, LoggingHelper {

    Object REEVALUATOR_CALL = new Object();

    //=========================
    //general
    //=========================

    SharedModuleData getSharedData();

    default void traceModuleInitalization() {
        trace("[{}] initalize module", getName());
    }

    default void traceModuleValidation() {
        trace("[{}] validate module", getName());
    }

    default void traceModuleCall() {
        trace("[{}] call module ({})", getName(), getClass().getSimpleName());
    }

    default void traceSetSharedData() {
        trace("[{}] set shared data ({})", getName(), getClass().getSimpleName());
    }

    default String printName(Object obj) {
        if(obj instanceof Nameable) {
            return ((Nameable) obj).getName();
        } else {
            return Objects.toString(obj);
        }
    }

    default void startReevaluatorCall() {
        getSharedData().put(REEVALUATOR_CALL, Boolean.TRUE);
    }

    default void finishReevaluatorCall() {
        getSharedData().remove(REEVALUATOR_CALL);
    }

    default boolean isReevaluatorCall() {
        return getSharedData().contains(REEVALUATOR_CALL);
    }

    //=========================
    //Attribute
    //=========================

    default double getDouble(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName) {
        return environment.getAttributeHelper().getDouble(agent, product, attributeName, true);
    }

    default void setDouble(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName, double value) {
        environment.getAttributeHelper().setDouble(agent, product, attributeName, value, true);
    }

    default boolean getBoolean(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName) {
        return environment.getAttributeHelper().getBoolean(agent, product, attributeName, true);
    }

    default void setBoolean(SimulationEnvironment environment, ConsumerAgent agent, Product product, String attributeName, boolean value) {
        environment.getAttributeHelper().setBoolean(agent, product, attributeName, value, true);
    }
}
