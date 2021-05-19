package de.unileipzig.irpact.core.agent.consumer;

import de.unileipzig.irpact.commons.affinity.Affinities;
import de.unileipzig.irpact.commons.affinity.BasicAffinityMapping;
import de.unileipzig.irpact.commons.checksum.Checksums;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicConsumerAgentGroupAffinityMapping
        extends BasicAffinityMapping<ConsumerAgentGroup, ConsumerAgentGroup>
        implements ConsumerAgentGroupAffinityMapping {

    public BasicConsumerAgentGroupAffinityMapping() {
        this(new LinkedHashMap<>());
    }

    public BasicConsumerAgentGroupAffinityMapping(Map<ConsumerAgentGroup, Affinities<ConsumerAgentGroup>> mapping) {
        this.mapping = mapping;
    }

    public void putForAll(Collection<? extends ConsumerAgentGroup> cags, double value) {
        for(ConsumerAgentGroup src: cags) {
            for(ConsumerAgentGroup tar: cags) {
                put(src, tar, value);
            }
        }
    }

    @Override
    protected ConsumerAgentGroupAffinities newAffinitiesInstance() {
        return new BasicBasicConsumerAgentGroupAffinities();
    }

    @Override
    public ConsumerAgentGroupAffinities get(ConsumerAgentGroup source) {
        return (ConsumerAgentGroupAffinities) super.get(source);
    }

    @Override
    public int getChecksum() {
        Map<String, Map<String, Double>> helper = new LinkedHashMap<>();
        for(ConsumerAgentGroup src: sources()) {
            ConsumerAgentGroupAffinities srcAffi = get(src);
            Map<String, Double> srcMap = helper.computeIfAbsent(src.getName(), _name -> new LinkedHashMap<>());
            for(ConsumerAgentGroup tar: srcAffi.targets()) {
                srcMap.put(tar.getName(), srcAffi.getValue(tar));
            }
        }
        return Checksums.SMART.getChecksum(getName(), helper);
    }
}
