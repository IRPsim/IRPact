package de.unileipzig.irpact.core.agent.consumer.attribute;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAnnualGroupAttribute
        extends ConsumerAgentRelatedGroupAttribute<Number, Number> {

    @Override
    default boolean isAnnualGroupAttribute() {
        return true;
    }

    @Override
    default ConsumerAgentAnnualGroupAttribute asAnnualGroupAttribute() {
        return this;
    }

    @Override
    ConsumerAgentAnnualAttribute derive(Number input);

    ConsumerAgentAnnualAttribute derive(int year);

    boolean hasYear(int year);
}
