package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;

@Definition(
        edn = @Edn(
                label = "Produkte",
                description = "angebotene Produkte"
        )
)
public class Product {

    public String _name;

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
                '}';
    }
}
