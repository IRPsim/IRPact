package de.unileipzig.irpact.io.param.input.affinity;

import de.unileipzig.irpact.commons.NamedData;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroupAffinityMapping;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InAffinityEntry implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InAffinityEntry.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Affinity")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Ausgangsgruppe")
                .setGamsDescription("Ausgangsgruppe")
                .store(InGeneral.class, "srcCag");
        res.newEntryBuilder()
                .setGamsIdentifier("Zielgruppe")
                .setGamsDescription("Zielgruppe")
                .store(InGeneral.class, "tarCag");
        res.newEntryBuilder()
                .setGamsIdentifier("Affinity-Wert")
                .setGamsDescription("Affinity-Wert")
                .store(InGeneral.class, "affinityValue");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroup srcCag;

    @FieldDefinition
    public InConsumerAgentGroup tarCag;

    @FieldDefinition
    public double affinityValue;

    public InAffinityEntry() {
    }

    public InAffinityEntry(String name, InConsumerAgentGroup src, InConsumerAgentGroup tar, double value) {
        this._name = name;
        this.srcCag = src;
        this.tarCag = tar;
        this.affinityValue = value;
    }

    public static ConsumerAgentGroupAffinityMapping buildMapping(
            InAffinityEntry[] entries,
            NamedData data) {
        BasicConsumerAgentGroupAffinityMapping mapping = new BasicConsumerAgentGroupAffinityMapping();
        for(InAffinityEntry entry: entries) {
            ConsumerAgentGroup srcCag = data.getAs(entry.srcCag.getName());
            ConsumerAgentGroup tarCag = data.getAs(entry.tarCag.getName());
            mapping.put(srcCag, tarCag, entry.affinityValue);
        }
        return mapping;
    }

    @Override
    public String getName() {
        return _name;
    }

    public InConsumerAgentGroup getSrcCag() {
        return srcCag;
    }

    public InConsumerAgentGroup getTarCag() {
        return tarCag;
    }

    public double getAffinityValue() {
        return affinityValue;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InAffinityEntry)) return false;
        InAffinityEntry that = (InAffinityEntry) o;
        return Double.compare(that.affinityValue, affinityValue) == 0 && Objects.equals(_name, that._name) && Objects.equals(srcCag, that.srcCag) && Objects.equals(tarCag, that.tarCag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, srcCag, tarCag, affinityValue);
    }

    @Override
    public String toString() {
        return "InAffinityEntry{" +
                "_name='" + _name + '\'' +
                ", srcCag=" + srcCag.getName() +
                ", tarCag=" + tarCag.getName() +
                ", affinityValue=" + affinityValue +
                '}';
    }
}
