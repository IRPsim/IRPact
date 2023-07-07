package de.unileipzig.irpact.io.param.irpopt;

import de.unileipzig.irpact.develop.Todo;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.TreeAnnotationResource;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "side_cust",
        gams = @Gams(
                description = "Kundengruppe",
                identifier = "Kundengruppe"
        )
)
public class SideCustom extends Side {

    @TreeAnnotationResource.Init
    public static void initRes(TreeAnnotationResource res) {
    }
    @TreeAnnotationResource.Apply
    public static void applyRes(TreeAnnotationResource res) {
    }

    @Todo("ENTFERNEN")
    @FieldDefinition(
            name = "S_DS",
            gams = @GamsParameter(
                    defaultValue = "10",
                    description = "Anzahl der Kunden",
                    identifier = "KGA"
            )
    )
    public int number;

    @Todo("ENTFERNEN")
    @FieldDefinition(
            name = "kg_modifier",
            gams = @GamsParameter(
                    defaultValue = "5",
                    description = "Erhöht die Anzahl der Kunden in der Gruppe um den gewünschten Wert.",
                    identifier = "KGAM"
            )
    )
    public int delta;

    @Todo("ENTFERNEN")
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

    @Override
    public SideCustom copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public SideCustom newCopy(CopyCache cache) {
        return new SideCustom();
    }
}
