package de.unileipzig.irpact.v2.io.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                path = {"Products/FixedProduct"}
        )
)
public class IFixedProduct {

    public String _name;

    @FieldDefinition
    public IProductGroup fpGroup;

    @FieldDefinition
    public IFixedProductAttribute[] fpAttributes;

    public IFixedProduct() {
    }

    public String getName() {
        return _name;
    }

    public IProductGroup getFpGroup() {
        return fpGroup;
    }

    public IFixedProductAttribute[] getFpAttributes() {
        return fpAttributes;
    }
}
