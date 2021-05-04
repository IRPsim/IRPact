package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentAnnualGroupAttribute
        extends ConsumerAgentRelatedGroupAttribute<Number, Number> {

    @Override
    ConsumerAgentAnnualAttribute derive(Number input);

    ConsumerAgentAnnualAttribute derive(int year);
}
