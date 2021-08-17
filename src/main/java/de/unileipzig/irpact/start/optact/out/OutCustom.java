package de.unileipzig.irpact.start.optact.out;

import de.unileipzig.irpact.io.param.irpopt.SideCustom;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;

/**
 * @author Daniel Abitz
 */
@Definition(
        copy = SideCustom.class
)
public class OutCustom {

    public String _name;

    @FieldDefinition(
            name = "S_DS",
            gams = @GamsParameter(
                    description = "Neue Anzahl der Kunden",
                    identifier = "OUTKGA"
            )
    )
    public int numberOut;

    @FieldDefinition(
            name = "IuOSonnentankNetzversorgung_Summe",
            gams = @GamsParameter(
                    description = "Summe der Zeitreihe oder ID multipliziert mit der Mwst.",
                    identifier = "SNS"
            )
    )
    public double summe;

    public OutCustom() {
    }

    public OutCustom(String name) {
        _name = name;
    }

    public OutCustom(String name, int size) {
        _name = name;
        numberOut = size;
        summe = 0;
    }
}
