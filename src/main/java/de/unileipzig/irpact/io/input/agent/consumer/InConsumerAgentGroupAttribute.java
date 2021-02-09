package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irpact.io.input.InAttributeName;
import de.unileipzig.irpact.io.input.distribution.InUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agenten", "Konsumer", "Attribute"},
                priorities = {"-7", "0", "1"}
        )
)
public class InConsumerAgentGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Agenten", "Konsumer", "Attribute-Name-Mapping"},
                    priorities = {"-7", "0", "3"}
            )
    )
    public InAttributeName cagAttrName;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Agenten", "Konsumer", "Attribute-Verteilung-Mapping"},
                    priorities = {"-7", "0", "4"}
            )
    )
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
