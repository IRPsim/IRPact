package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.MapBasedTripleMapping;
import de.unileipzig.irpact.commons.util.data.TripleMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

/**
 * @author Daniel Abitz
 */
public class AnnualAdoptionData {

    protected TripleMapping<Integer, ConsumerAgentGroup, Integer> adoptionsMap = new MapBasedTripleMapping<>();
    protected TripleMapping<Integer, ConsumerAgentGroup, Integer> adoptionsCumulativMap = new MapBasedTripleMapping<>();
    protected TripleMapping<Integer, ConsumerAgentGroup, Double> adoptionsShareMap = new MapBasedTripleMapping<>();
    protected TripleMapping<Integer, ConsumerAgentGroup, Double> adoptionsShareCumulativeMap = new MapBasedTripleMapping<>();

    public AnnualAdoptionData() {
    }

    public TripleMapping<Integer, ConsumerAgentGroup, Integer> getAdoptionsMap() {
        return adoptionsMap;
    }

    public TripleMapping<Integer, ConsumerAgentGroup, Integer> getAdoptionsCumulativMap() {
        return adoptionsCumulativMap;
    }

    public TripleMapping<Integer, ConsumerAgentGroup, Double> getAdoptionsShareMap() {
        return adoptionsShareMap;
    }

    public TripleMapping<Integer, ConsumerAgentGroup, Double> getAdoptionsShareCumulativeMap() {
        return adoptionsShareCumulativeMap;
    }

    public void update(int year, ConsumerAgentGroup cag, int adoptions, int totalAgents) {
        adoptionsMap.put(year, cag, adoptions);

        int adoptionsCumulativ = adoptionsCumulativMap.get(year, cag, 0);
        adoptionsCumulativMap.put(year, cag, adoptionsCumulativ + adoptions);

        double share = (double) adoptions / (double) totalAgents;
        adoptionsShareMap.put(year, cag, share);

        double adoptionsShareCumulative = adoptionsShareCumulativeMap.get(year, cag, 0.0);
        adoptionsShareCumulativeMap.put(year, cag, adoptionsShareCumulative + share);
    }
}
