package de.unileipzig.irpact.uitest.out2;

import de.unileipzig.irpact.uitest.in1.AgentGroup;
import de.unileipzig.irpact.uitest.in1.Product;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.Table;

/**
 * @author Daniel Abitz
 */
@Definition(
        gams = @Gams(
                hidden = "1"
        ),
        global = true
)
public class OutputScalars {

    @FieldDefinition(
            gams = @GamsParameter(
                    identifier = "Produktadaptionen",
                    description = "Anzahl der Adaptionen der Gruppe je Produkt im Jahr"
            ),
            edn = @EdnParameter(
                    label = "Ergebnis - Produktadaptionen",
                    description = "Gibt die Anzahl der Adaptionen fuer die Agentengruppe in diesem Jahr an.",
                    icon = "fa fa-map-marker"
            )
    )
    @TableInfo(first = AgentGroup.class, second = Product.class, value = double.class)
    public Table<AgentGroup, Product, Double> adaptions;

    public OutputScalars() {
    }

    public OutputScalars(Table<AgentGroup, Product, Double> adaptions) {
        this.adaptions = adaptions;
    }

    public Table<AgentGroup, Product, Double> getAdaptions() {
        return adaptions;
    }

    @Override
    public String toString() {
        return "GlobalScalars{" +
                "adaptions=" + adaptions +
                '}';
    }
}
