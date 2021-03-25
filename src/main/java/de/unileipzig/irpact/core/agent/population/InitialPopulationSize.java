package de.unileipzig.irpact.core.agent.population;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * Manages the initial population size.
 *
 * @author Daniel Abitz
 */
public interface InitialPopulationSize {

    void setFixConsumerAgentGroupSize(ConsumerAgentGroup cag, int size);

    boolean hasFixConsumerAgentGroupSize(ConsumerAgentGroup cag);

    int getFixConsumerAgentGroupSize(ConsumerAgentGroup cag);

    void setConsumerAgentGroupSizeShare(ConsumerAgentGroup cag, double share);

    boolean hasConsumerAgentGroupSizeShare(ConsumerAgentGroup cag);

    double getConsumerAgentGroupSizeShare(ConsumerAgentGroup cag);

    void updateTotalSizeForShares(int delta);

    void calculate();

    int getConsumerAgentGroupSize(ConsumerAgentGroup cag) throws IllegalStateException;
}
