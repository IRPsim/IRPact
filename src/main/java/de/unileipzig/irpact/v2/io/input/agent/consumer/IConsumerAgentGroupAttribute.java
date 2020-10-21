package de.unileipzig.irpact.v2.io.input.agent.consumer;

import de.unileipzig.irpact.v2.io.input.distribution.IUnivariateDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = "Agents/Consumer/Attributes"
        )
)
public class IConsumerAgentGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Agents/Consumer/Attribute-Distribution-Mapping"
            )
    )
    public IUnivariateDistribution distribution;

    public IConsumerAgentGroupAttribute() {
    }

    public String getName() {
        return _name;
    }

    public IUnivariateDistribution getDistribution() {
        return distribution;
    }
}
