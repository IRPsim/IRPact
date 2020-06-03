package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.EdnParameter;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;

@Definition(
        edn = @Edn(
                label = "Produkte",
                description = "angebotene Produkte"
        )
)
public class Product {

    public String _name;

    @FieldDefinition(
            edn = @EdnParameter(
                    description = "Dieser Wert wird ignoriert."
            )
    )
    public double irrelevant;

    public Product() {
    }

    public Product(String name) {
        this._name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_name='" + _name + '\'' +
                ", irrelevant=" + irrelevant +
                '}';
    }
}
