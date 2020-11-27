package de.unileipzig.irpact.io.input.awareness;

import de.unileipzig.irpact.io.input.agent.consumer.IConsumerAgentGroup;
import de.unileipzig.irpact.io.input.distribution.IUnivariateDoubleDistribution;
import de.unileipzig.irpact.io.input.product.IFixedProduct;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Products/FixedProduct-Awareness"}
        )
)
public class IFixedProductAwareness {

    public String _name;

    @FieldDefinition
    public IConsumerAgentGroup awarenessAgentGroup;

    @FieldDefinition
    public IFixedProduct awarenessFixedProduct;

    @FieldDefinition
    public IUnivariateDoubleDistribution awarenessDistribution;

    public IFixedProductAwareness() {
    }

    public IFixedProductAwareness(String name, IConsumerAgentGroup awarenessAgentGroup, IFixedProduct awarenessFixedProduct, IUnivariateDoubleDistribution awarenessDistribution) {
        this._name = name;
        this.awarenessAgentGroup = awarenessAgentGroup;
        this.awarenessFixedProduct = awarenessFixedProduct;
        this.awarenessDistribution = awarenessDistribution;
    }
}
