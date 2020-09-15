package de.unileipzig.irpact.uitest.v1;

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
    public int name;

    public Product() {
    }

    public Product(String name, int nameX) {
        this._name = name;

    }

    public static Product[] x() {
        return new Product[]{
                new Product("Auto", 0),
                new Product("Haus", 0),
        };
    }

    public String getName() {
        return _name;
    }

    @Override
    public String toString() {
        return "Product{" +
                "_name='" + _name + '\'' +
                ", name=" + name +
                '}';
    }
}
