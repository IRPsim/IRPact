package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irpact.io.input.awareness.InAwareness;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConsumerAgentGroup {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Konsumer")
                .setEdnPriority(0)
                .putCache("Konsumer");
        res.newElementBuilder()
                .setEdnLabel("Gruppen")
                .setEdnPriority(0)
                .putCache("Gruppen");

        res.newElementBuilder()
                .setEdnLabel("Gruppe-Attribut-Mapping")
                .setEdnPriority(3)
                .putCache("Gruppe-Attribut-Mapping");

        res.newElementBuilder()
                .setEdnLabel("Gruppe-Awareness-Mapping")
                .setEdnPriority(4)
                .putCache("Gruppe-Awareness-Mapping");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConsumerAgentGroup.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppen")
        );

        res.putPath(
                InConsumerAgentGroup.class, "cagAttributes",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppe-Attribut-Mapping")
        );

        res.putPath(
                InConsumerAgentGroup.class, "cagAwareness",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Gruppe-Awareness-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Attribute der KG")
                .setGamsDescription("Attribute")
                .store(InConsumerAgentGroup.class, "cagAttributes");

        res.newEntryBuilder()
                .setGamsIdentifier("Awareness der KG")
                .setGamsDescription("genutzte Awareness")
                .store(InConsumerAgentGroup.class, "cagAwareness");

        res.newEntryBuilder()
                .setGamsIdentifier("[ungenutzt] informationAuthority")
                .setGamsDescription("informationAuthority")
                .store(InConsumerAgentGroup.class, "informationAuthority");

        res.newEntryBuilder()
                .setGamsIdentifier("Agenten in der KG")
                .setGamsDescription("Anzahl der Agenten in der Konsumergruppe")
                .store(InConsumerAgentGroup.class, "numberOfAgentsX");
    }

    public String _name;

    @FieldDefinition
    public InConsumerAgentGroupAttribute[] cagAttributes;

    @FieldDefinition
    public InAwareness[] cagAwareness;

    @FieldDefinition
    public double informationAuthority;

    @FieldDefinition
    public int numberOfAgentsX;

    public InConsumerAgentGroup() {
    }

    public InConsumerAgentGroup(
            String name,
            double informationAuthority,
            int numberOfAgents,
            Collection<? extends InConsumerAgentGroupAttribute> attributes,
            InAwareness awareness) {
        this._name = name;
        this.informationAuthority = informationAuthority;
        this.numberOfAgentsX = numberOfAgents;
        this.cagAttributes = attributes.toArray(new InConsumerAgentGroupAttribute[0]);
        this.cagAwareness = new InAwareness[]{awareness};
    }

    public String getName() {
        return _name;
    }

    public double getInformationAuthority() {
        return informationAuthority;
    }

    public int getNumberOfAgents() {
        return numberOfAgentsX;
    }

    public InConsumerAgentGroupAttribute[] getAttributes() {
        return cagAttributes;
    }

    public InAwareness getAwareness() {
        return cagAwareness[0];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConsumerAgentGroup)) return false;
        InConsumerAgentGroup that = (InConsumerAgentGroup) o;
        return Double.compare(that.informationAuthority, informationAuthority) == 0
                && numberOfAgentsX == that.numberOfAgentsX
                && Objects.equals(_name, that._name)
                && Arrays.equals(cagAttributes, that.cagAttributes)
                && Arrays.equals(cagAwareness, that.cagAwareness);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(_name, informationAuthority, numberOfAgentsX, cagAwareness);
        result = 31 * result + Arrays.hashCode(cagAttributes);
        return result;
    }

    @Override
    public String toString() {
        return "InConsumerAgentGroup{" +
                "_name='" + _name + '\'' +
                ", cagAttributes=" + Arrays.toString(cagAttributes) +
                ", informationAuthority=" + informationAuthority +
                ", numberOfAgents=" + numberOfAgentsX +
                ", cagAwareness4" + Arrays.toString(cagAwareness) +
                '}';
    }
}
