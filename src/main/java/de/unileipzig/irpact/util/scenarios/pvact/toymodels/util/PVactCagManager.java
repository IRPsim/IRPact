package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.pvact.Milieu;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class PVactCagManager {

    protected Map<String, InPVactConsumerAgentGroup> cags = new LinkedHashMap<>();
    protected List<PVactCagModifier> globalModifiers = new ArrayList<>();
    protected Map<String, List<PVactCagModifier>> cagModifiers = new LinkedHashMap<>();
    protected final Map<String, Integer> cagEdgeCount = new LinkedHashMap<>();
    protected final Map<String, double[]> cagAffinities = new LinkedHashMap<>();

    protected InUnivariateDoubleDistribution defaultDist;

    public PVactCagManager(InUnivariateDoubleDistribution defaultDist) {
        this.defaultDist = defaultDist;
    }

    public void add(String name) {
        if(!cags.containsKey(name)) {
            InPVactConsumerAgentGroup cag = new InPVactConsumerAgentGroup();
            cag.setName(name);
            cag.setForAll(defaultDist);
            cags.put(name, cag);
        }
    }

    public void addAll(String... names) {
        for(String name: names) {
            add(name);
        }
    }

    public InPVactConsumerAgentGroup get(String name) {
        InPVactConsumerAgentGroup cag = cags.get(name);
        if(cag == null) {
            throw new NoSuchElementException("missing: " + name);
        }
        return cag;
    }

    public void registerForAll(PVactCagModifier modifier) {
        globalModifiers.add(modifier);
    }

    public void useDefaultMilieus() {
        Milieu.WITHOUT_G.forEach(m -> add(m.print()));
    }

    public void register(
            String name,
            int edgeCount,
            double[] affinities,
            PVactCagModifier modifier) {
        setEdgeCount(name, edgeCount);
        setAffinities(name, affinities);
        register(name, modifier);
    }

    public void register(String name, PVactCagModifier modifier) {
        add(name);
        cagModifiers.computeIfAbsent(name, _name -> new ArrayList<>()).add(modifier);
    }

    public void apply() {
        for(String cagName: cags.keySet()) {
            InPVactConsumerAgentGroup cag = cags.get(cagName);

            for(PVactCagModifier globalModifier: globalModifiers) {
                globalModifier.modify(cag);
            }

            List<PVactCagModifier> customModifiers = cagModifiers.getOrDefault(cagName, Collections.emptyList());
            for(PVactCagModifier customModifier: customModifiers) {
                customModifier.modify(cag);
            }
        }
    }

    public void setEdgeCount(String cagGroup, int count) {
        add(cagGroup);
        cagEdgeCount.put(cagGroup, count);
    }

    public int getEdgeCount(String grpName) {
        return cagEdgeCount.get(grpName);
    }

    public int getEdgeCount(InPVactConsumerAgentGroup grp) {
        return cagEdgeCount.get(grp.getName());
    }

    public int[] getEdgeCountArray(String... names) {
        return Arrays.stream(names)
                .mapToInt(this::getEdgeCount)
                .toArray();
    }

    public int[] getEdgeCountArray() {
        return cags.keySet().stream()
                .mapToInt(this::getEdgeCount)
                .toArray();
    }

    public void setAffinities(String cagGroup, double[] affinites) {
        add(cagGroup);
        cagAffinities.put(cagGroup, affinites);
    }

    public Collection<InPVactConsumerAgentGroup> getCags() {
        return cags.values();
    }

    public InPVactConsumerAgentGroup[] getCagsArray() {
        return getCags().toArray(new InPVactConsumerAgentGroup[0]);
    }

    public InPVactConsumerAgentGroup[] getCagsArray(String... names) {
        return Arrays.stream(names)
                .map(this::get)
                .toArray(InPVactConsumerAgentGroup[]::new);
    }

    public InAffinities createAffinities(String name) {
        try {
            return createAffinities0(name);
        } catch (ParsingException e) {
            throw new RuntimeException(e);
        }
    }

    private InAffinities createAffinities0(String name) throws ParsingException {
        List<InAffinityEntry> entries = new ArrayList<>();
        for(String srcCagName: cags.keySet()) {
            InPVactConsumerAgentGroup srcCag = cags.get(srcCagName);
            double[] affinityValues = cagAffinities.get(srcCagName);
            int i = 0;
            for(InPVactConsumerAgentGroup tarCag: cags.values()) {
                double affinityValue = affinityValues[i++];
                InComplexAffinityEntry entry = new InComplexAffinityEntry();
                entry.setSrcCag(srcCag);
                entry.setTarCag(tarCag);
                entry.setAffinityValue(affinityValue);
                entry.setName("Affinity_" + srcCag.getName() + "_" + tarCag.getName());
                entries.add(entry);
            }
        }
        InAffinities affinities = new InAffinities();
        affinities.setName(name);
        affinities.setEntries(entries);
        return affinities;
    }
}
