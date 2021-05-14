package de.unileipzig.irpact.core.util;

import de.unileipzig.irpact.commons.util.data.MapBasedTripleMapping;
import de.unileipzig.irpact.commons.util.data.TripleMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class AnnualAdoptionData {

    protected Set<Integer> years = new LinkedHashSet<>();
    protected Set<ConsumerAgentGroup> cags = new LinkedHashSet<>();
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

    public Collection<? extends Integer> getYears() {
        return years;
    }

    public Collection<? extends ConsumerAgentGroup> getConsumerAgentGroups() {
        return cags;
    }

    public void update(int year, ConsumerAgentGroup cag, int adoptions, int totalAgents) {
        years.add(year);
        cags.add(cag);

        adoptionsMap.put(year, cag, adoptions);

        int adoptionsCumulativ = adoptionsCumulativMap.get(year, cag, 0);
        adoptionsCumulativMap.put(year, cag, adoptionsCumulativ + adoptions);

        double share = (double) adoptions / (double) totalAgents;
        adoptionsShareMap.put(year, cag, share);

        double adoptionsShareCumulative = adoptionsShareCumulativeMap.get(year, cag, 0.0);
        adoptionsShareCumulativeMap.put(year, cag, adoptionsShareCumulative + share);
    }

    public void set(
            int year,
            ConsumerAgentGroup cag,
            int adoptions,
            int adoptionsCumulativ,
            double adoptionsShare,
            double adoptionsShareCumulative) {
        years.add(year);
        cags.add(cag);
        adoptionsMap.put(year, cag, adoptions);
        adoptionsCumulativMap.put(year, cag, adoptionsCumulativ);
        adoptionsShareMap.put(year, cag, adoptionsShare);
        adoptionsShareCumulativeMap.put(year, cag, adoptionsShareCumulative);
    }
}
