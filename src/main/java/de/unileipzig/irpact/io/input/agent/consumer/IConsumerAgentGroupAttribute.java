package de.unileipzig.irpact.io.input.agent.consumer;

import de.unileipzig.irpact.io.input.distribution.IUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Agents/Consumer/Attributes"}
        )
)
public class IConsumerAgentGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Agents/Consumer/Attribute-Distribution-Mapping"}
            )
    )
    public IUnivariateDoubleDistribution cagAttrDistribution;

    public IConsumerAgentGroupAttribute() {
    }

    public IConsumerAgentGroupAttribute(String name, IUnivariateDoubleDistribution cagAttrDistribution) {
        this._name = name;
        this.cagAttrDistribution = cagAttrDistribution;
    }

    public String getName() {
        return _name;
    }

    public IUnivariateDoubleDistribution getCagAttrDistribution() {
        return cagAttrDistribution;
    }
}
