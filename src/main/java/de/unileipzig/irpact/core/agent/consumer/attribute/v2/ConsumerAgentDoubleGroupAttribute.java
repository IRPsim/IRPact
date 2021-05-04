package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

/**
 * @author Daniel Abitz
 */
public interface ConsumerAgentDoubleGroupAttribute extends ConsumerAgentValueGroupAttribute<Number> {

    @Override
    ConsumerAgentDoubleAttribute derive();

    ConsumerAgentDoubleAttribute derive(double value);
}
