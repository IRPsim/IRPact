package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.DirectDerivable;
import de.unileipzig.irpact.commons.Nameable;
import de.unileipzig.irpact.commons.attribute.v3.AttributeBase;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicConsumerAgentAnnualAttribute
        extends AbstractConsumerAgentAttribute
        implements ConsumerAgentAnnualAttribute {

    protected MapSupplier mapSupplier;
    protected Map<Integer, ConsumerAgentAttribute> yearMapping;

    public BasicConsumerAgentAnnualAttribute() {
        this(MapSupplier.getDefault());
    }

    public BasicConsumerAgentAnnualAttribute(MapSupplier mapSupplier) {
        this(mapSupplier, mapSupplier.newMap());
    }

    public BasicConsumerAgentAnnualAttribute(MapSupplier mapSupplier, Map<Integer, ConsumerAgentAttribute> yearMapping) {
        this.mapSupplier = mapSupplier;
        this.yearMapping = yearMapping;
    }

    @Override
    public BasicConsumerAgentAnnualAttribute copy() {
        BasicConsumerAgentAnnualAttribute copy = new BasicConsumerAgentAnnualAttribute(
                getMapSupplier(),
                deepCopy(getMapSupplier())
        );
        copy.setGroup(getGroup());
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        return copy;
    }

    protected Map<Integer, ConsumerAgentAttribute> deepCopy(MapSupplier mapSupplier) {
        Map<Integer, ConsumerAgentAttribute> copy = mapSupplier.newMap();
        for(Map.Entry<Integer, ConsumerAgentAttribute> entry: getMapping().entrySet()) {
            copy.put(
                    entry.getKey(),
                    entry.getValue().copy()
            );
        }
        return copy;
    }

    public MapSupplier getMapSupplier() {
        return mapSupplier;
    }

    public Map<Integer, ConsumerAgentAttribute> getMapping() {
        return yearMapping;
    }

    public void put(int year, ConsumerAgentAttribute attr) {
        yearMapping.put(year, attr);
    }

    @Override
    public boolean hasAttribute(int year) {
        return yearMapping.containsKey(year);
    }

    @Override
    public ConsumerAgentAttribute getAttribute(int year) {
        return yearMapping.get(year);
    }

    @Override
    public ConsumerAgentAttribute removeAttribute(int year) {
        return yearMapping.remove(year);
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                ChecksumComparable.getNameChecksum(getGroup()),
                ChecksumComparable.getMapChecksumWithMapping(
                        getMapping(),
                        i -> i,
                        Nameable::getName)
        );
    }
}
