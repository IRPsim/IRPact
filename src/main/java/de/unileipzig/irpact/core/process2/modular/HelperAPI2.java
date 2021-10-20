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

    //=========================
    //Attribute
    //=========================

    default int getFirstSimulationYear(SimulationEnvironment environment) {
        return environment.getSettings().getFirstSimulationYear();
    }

    default double getDoubleValue(SimulationEnvironment environment, ConsumerAgent agent, String key) {
        return environment.getAttributeHelper().findDoubleValue(agent, key);
    }

    default double getDoubleValue(SimulationEnvironment environment, ConsumerAgent agent, String key, int year) {
        return environment.getAttributeHelper().getDoubleValue(agent, key, year);
    }

    default double getDoubleValue(SimulationEnvironment environment, ConsumerAgent agent, Product product, String key) {
        return environment.getAttributeHelper().getDoubleValue(agent, product, key);
    }

    default double tryGetDoubleValue(SimulationEnvironment environment, ConsumerAgent agent, Product product, String key) {
        return environment.getAttributeHelper().tryGetDoubleValue(agent, product, key);
    }

    default boolean getBooleanValue(SimulationEnvironment environment, ConsumerAgent agent, String key) {
        return environment.getAttributeHelper().findBooleanValue(agent, key);
    }
}
