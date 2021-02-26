package de.unileipzig.irpact.io.param.input.agent.consumer;

import de.unileipzig.irpact.commons.distribution.UnivariateDoubleDistribution;
import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.agent.consumer.BasicConsumerAgentGroupAttribute;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.io.param.input.InAttributeName;
import de.unileipzig.irpact.io.param.input.InEntity;
import de.unileipzig.irpact.io.param.input.InputParser;
import de.unileipzig.irpact.io.param.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irpact.util.RemoveFromRoot;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@RemoveFromRoot
@Definition
public class InConsumerAgentGroupAttribute implements InEntity {

    public static void initRes(TreeAnnotationResource res) {
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
                res.getCachedElement("Attribute"),
                res.getCachedElement("Attribute-Name-Mapping")
        );

        res.putPath(
                InConsumerAgentGroupAttribute.class, "cagAttrDistribution",
                res.getCachedElement("Agenten"),
                res.getCachedElement("Konsumer"),
                res.getCachedElement("Attribute"),
                res.getCachedElement("Attribute-Verteilung-Mapping")
        );

        res.newEntryBuilder()
                .setGamsIdentifier("Name des KG-Attributes")
                .setGamsDescription("Attributname")
                .store(InConsumerAgentGroupAttribute.class, "cagAttrName");

        res.newEntryBuilder()
                .setGamsIdentifier("Verteilungsfunktion des KG-Attributes")
                .setGamsDescription("genutzte Funktion")
                .store(InConsumerAgentGroupAttribute.class, "cagAttrDistribution");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(InConsumerAgentGroupAttribute.class);

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

    @Override
    public String getName() {
        return _name;
    }

    public String splitName(String suffixWithoutDelimiter) {
        return getName().substring(suffixWithoutDelimiter.length() + 1);
    }

    public InAttributeName getCagAttrName() {
        return cagAttrName;
    }

    public InUnivariateDoubleDistribution getCagAttrDistribution() {
        return cagAttrDistribution;
    }

    @Override
    public Object parse(InputParser parser) throws ParsingException {
        BasicConsumerAgentGroupAttribute bCagAttr = new BasicConsumerAgentGroupAttribute();
        bCagAttr.setName(getCagAttrName().getName());

        UnivariateDoubleDistribution dist = parser.parseEntityTo(getCagAttrDistribution());
        bCagAttr.setDistribution(dist);

        return bCagAttr;
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
