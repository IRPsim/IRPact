package de.unileipzig.irpact.v2.io.input.product;

import de.unileipzig.irpact.v2.io.input.distribution.IUnivariateDoubleDistribution;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Products/GroupAttributes"}
        )
)
public class IProductGroupAttribute {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    label = {"Products/Attribute-Distribution-Mapping"}
            )
    )
    public IUnivariateDoubleDistribution pgAttrDistribution;

    public IProductGroupAttribute() {
    }

    public IProductGroupAttribute(String name, IUnivariateDoubleDistribution pgAttrDistribution) {
        this._name = name;
        this.pgAttrDistribution = pgAttrDistribution;
    }

    public String getName() {
        return _name;
    }

    public IUnivariateDoubleDistribution getPgAttrDistribution() {
        return pgAttrDistribution;
    }
}
