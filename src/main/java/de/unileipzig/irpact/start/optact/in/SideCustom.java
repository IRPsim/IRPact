package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.DoubleTimeSeries;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "side_cust",
        gams = @Gams(
                description = "Kundengruppe in IRPact",
                identifier = "KG"
        ),
        edn = @Edn(
                path = "Sets/Kundengruppen",
                description = {"Hier sind Sets.", "Hier sind Kundengruppen."}
        )
)
public class SideCustom extends Side {

    @FieldDefinition(
            name = "S_DS",
            gams = @GamsParameter(
                    defaultValue = "10",
                    description = "Anzahl der Kunden",
                    identifier = "KGA"
            )
    )
    public int number;

    @FieldDefinition(
            name = "kg_modifier",
            gams = @GamsParameter(
                    defaultValue = "5",
                    description = "Erhöht die Anzahl der Kunden in der Gruppe um den gewünschten Wert.",
                    identifier = "KGAM"
            )
    )
    public int delta;

    @FieldDefinition(
            name = "IuO_ESector_CustSide",
            timeSeries = Ii.class,
            gams = @GamsParameter(
                    unit = "[EUR]",
                    description = "Stromsparte je Kundengruppe",
                    identifier = "SK"
            )
    )
    public DoubleTimeSeries timeStuff;

    public SideCustom() {
    }

    public SideCustom(String name, int number, int delta, DoubleTimeSeries timeStuff) {
        _name = name;
        this.timeStuff = timeStuff;
        this.number = number;
        this.delta = delta;
    }
}
