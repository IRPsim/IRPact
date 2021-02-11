package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition
public class InConsumerAgentGroupAttribute {

    public static void initRes(TreeAnnotationResource res) {
        res.newElementBuilder()
                .setEdnLabel("Attribute")
                .setEdnPriority(1)
                .putCache("Attribute");

        res.newElementBuilder()
                .setEdnLabel("Attribute-Name-Mapping")
                .setEdnPriority(4)
                .putCache("Attribute-Name-Mapping");

        res.newElementBuilder()
                .setEdnLabel("Attribute-Verteilung-Mapping")
                .setEdnPriority(5)
                .putCache("Attribute-Verteilung-Mapping");
    }
    public static void applyRes(TreeAnnotationResource res) {
        res.putPath(
                InConsumerAgentGroupAttribute.class,
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute")
        );

        res.putPath(
                InConsumerAgentGroupAttribute.class, "cagAttrName",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute-Name-Mapping")
        );

        res.putPath(
                InConsumerAgentGroupAttribute.class, "cagAttrDistribution",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute-Verteilung-Mapping")
        );
    }

    public String _name;

    @FieldDefinition
    public InAttributeName cagAttrName;

    @FieldDefinition
    public InUnivariateDoubleDistribution cagAttrDistribution;

    public InConsumerAgentGroupAttribute() {
    }

    public InConsumerAgentGroupAttribute(String name, InAttributeName attributeName, InUnivariateDoubleDistribution distribution) {
        this._name = name;
        this.cagAttrName = attributeName;
        this.cagAttrDistribution = distribution;
    }

    public String getName() {
        return _name;
    }

    public InAttributeName getCagAttrName() {
        return cagAttrName;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return cagAttrDistribution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InConsumerAgentGroupAttribute)) return false;
        InConsumerAgentGroupAttribute that = (InConsumerAgentGroupAttribute) o;
        return Objects.equals(_name, that._name) && Objects.equals(cagAttrName, that.cagAttrName) && Objects.equals(cagAttrDistribution, that.cagAttrDistribution);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, cagAttrName, cagAttrDistribution);
    }

    @Override
    public String toString() {
        return "InConsumerAgentGroupAttribute{" +
                "_name='" + _name + '\'' +
                ", cagAttrName=" + cagAttrName +
                ", cagAttrDistribution=" + cagAttrDistribution +
                '}';
    }
}
