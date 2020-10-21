package de.unileipzig.irpact.v2.io.input.product;

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
                path = "Products/Attributes"
        )
)
public class IProductGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    path = "Products/Attribute-Distribution-Mapping"
            )
    )
    public IUnivariateDistribution distribution;

    public IProductGroupAttribute() {
    }

    public String getName() {
        return _name;
    }

    public IUnivariateDistribution getDistribution() {
        return distribution;
    }
}
