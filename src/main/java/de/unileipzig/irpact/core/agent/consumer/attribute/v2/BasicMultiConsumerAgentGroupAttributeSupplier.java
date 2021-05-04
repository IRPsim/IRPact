package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicMultiConsumerAgentGroupAttributeSupplier extends NameableBase implements MultiConsumerAgentGroupAttributeSupplier {

    protected Map<ConsumerAgentGroup, List<ConsumerAgentGroupAttributeSupplier>> mapping;

    public BasicMultiConsumerAgentGroupAttributeSupplier() {
        this(new HashMap<>());
    }

    public BasicMultiConsumerAgentGroupAttributeSupplier(Map<ConsumerAgentGroup, List<ConsumerAgentGroupAttributeSupplier>> mapping) {
        this.mapping = mapping;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getMapCollChecksumWithMappedKey(
                getMapping(),
                Nameable::getName
        );
    }

    public Map<ConsumerAgentGroup, List<ConsumerAgentGroupAttributeSupplier>> getMapping() {
        return mapping;
    }

    @Override
    public void add(ConsumerAgentGroup cag, ConsumerAgentGroupAttributeSupplier supplier) {
        List<ConsumerAgentGroupAttributeSupplier> list = mapping.computeIfAbsent(cag, _cag -> new ArrayList<>());
        list.add(supplier);
    }

    @Override
    public void addAllGroupAttributesTo(ConsumerAgentGroup cag) {
        List<ConsumerAgentGroupAttributeSupplier> list = mapping.get(cag);
        if(list != null) {
            for(ConsumerAgentGroupAttributeSupplier supplier: list) {
                supplier.addGroupAttributeTo(cag);
            }
        }
    }
}
