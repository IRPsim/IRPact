package de.unileipzig.irpact.start.hardcodeddemo.def.in;

import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.Edn;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        edn = @Edn(
                label = "Produkte",
                description = "angebotene Produkte",
                icon = "fa fa-map-marker",
                useOwnSet = "1"
        )
)
public class Product {

    public String _name;

    @FieldDefinition(
            gams = @GamsParameter(
                    description = "Name des Produktes",
                    identifier = "Produktname"
            )
    )
    public String name;

    public Product() {
    }

    public Product(String name) {
        this._name = name;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_name='" + _name + '\'' +
                ", name=" + name +
                '}';
    }
}
