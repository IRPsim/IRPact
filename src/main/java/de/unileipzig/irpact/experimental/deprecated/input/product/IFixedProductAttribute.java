package de.unileipzig.irpact.experimental.deprecated.input.product;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = {"Products/FixedAttributes"}
        )
)
public class IFixedProductAttribute {

    public String _name;

    @FieldDefinition
    public IProductGroupAttribute fpaGroupAttribute;

    @FieldDefinition
    public double fpaValue;

    public IFixedProductAttribute() {
    }

    public IFixedProductAttribute(String name, IProductGroupAttribute fpaGroupAttribute, double fpaValue) {
        this._name = name;
        this.fpaGroupAttribute = fpaGroupAttribute;
        this.fpaValue = fpaValue;
    }

    public String getName() {
        return _name;
    }

    public IProductGroupAttribute getFpaGroupAttribute() {
        return fpaGroupAttribute;
    }

    public double getFpaValue() {
        return fpaValue;
    }
}
