package de.unileipzig.irpact.core.agent.consumer.attribute.v2;

import de.unileipzig.irpact.commons.ChecksumComparable;
import de.unileipzig.irpact.commons.DirectDerivable;
import de.unileipzig.irpact.commons.util.MapSupplier;
import de.unileipzig.irpact.develop.AddToPersist;

import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
@AddToPersist
public class BasicConsumerAgentAnnualGroupAttribute
        extends AbstractConsumerAgentGroupAttribute
        implements ConsumerAgentAnnualGroupAttribute {

    protected MapSupplier mapSupplier;
    protected Map<Integer, DirectDerivable<? extends ConsumerAgentAttribute>> yearMapping;

    public BasicConsumerAgentAnnualGroupAttribute() {
        this(MapSupplier.getDefault());
    }

    public BasicConsumerAgentAnnualGroupAttribute(MapSupplier mapSupplier) {
        this(mapSupplier, mapSupplier.newMap());
    }

    public BasicConsumerAgentAnnualGroupAttribute(
            MapSupplier mapSupplier,
            Map<Integer, DirectDerivable<? extends ConsumerAgentAttribute>> yearMapping) {
        this.mapSupplier = mapSupplier;
        this.yearMapping = yearMapping;
    }

    @Override
    public AbstractConsumerAgentGroupAttribute copy() {
        BasicConsumerAgentAnnualGroupAttribute copy = new BasicConsumerAgentAnnualGroupAttribute(
                getMapSupplier(),
                getMapSupplier().copyMap(getMapping())
        );
        copy.setName(getName());
        copy.setArtificial(isArtificial());
        return copy;
    }

    public ConsumerAgentAttribute deriveYear(int year) {
        DirectDerivable<? extends ConsumerAgentAttribute> derivable = yearMapping.get(year);
        if(derivable == null) {
            throw new NoSuchElementException("year '" + year + "' missing");
        }
        return derivable.derive();
    }

    protected BasicConsumerAgentAnnualAttribute newAttribute() {
        BasicConsumerAgentAnnualAttribute annualAttr = new BasicConsumerAgentAnnualAttribute();
        annualAttr.setName(getName());
        annualAttr.setGroup(this);
        annualAttr.setArtificial(isArtificial());
        return annualAttr;
    }

    public boolean has(int year) {
        return yearMapping.containsKey(year);
    }

    public DirectDerivable<? extends ConsumerAgentAttribute> put(int year, DirectDerivable<? extends ConsumerAgentAttribute> derivable) {
        return yearMapping.put(year, derivable);
    }

    public DirectDerivable<? extends ConsumerAgentAttribute> remove(int year) {
        return yearMapping.remove(year);
    }

    @Override
    public BasicConsumerAgentAnnualAttribute derive() {
        BasicConsumerAgentAnnualAttribute annualAttr = newAttribute();
        for(int year: yearMapping.keySet()) {
            ConsumerAgentAttribute attr = deriveYear(year);
            annualAttr.put(year, attr);
        }
        return annualAttr;
    }

    @Override
    public BasicConsumerAgentAnnualAttribute derive(Number input) {
        return derive(input.intValue());
    }

    @Override
    public BasicConsumerAgentAnnualAttribute derive(int year) {
        ConsumerAgentAttribute attr = deriveYear(year);
        BasicConsumerAgentAnnualAttribute annualAttr = newAttribute();
        annualAttr.put(year, attr);
        return annualAttr;
    }

    public MapSupplier getMapSupplier() {
        return mapSupplier;
    }

    public Map<Integer, DirectDerivable<? extends ConsumerAgentAttribute>> getMapping() {
        return yearMapping;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getChecksum(
                getName(),
                isArtificial(),
                ChecksumComparable.getMapChecksumWithNamedValue(getMapping())
        );
    }
}
