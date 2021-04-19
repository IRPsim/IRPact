package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.Gams;
import de.unileipzig.irptools.defstructure.annotation.GamsParameter;
import de.unileipzig.irptools.util.DoubleTimeSeries;

/**
 * @author Daniel Abitz
 */
@Definition(
        name = "side",
        gams = @Gams(
                description = "Marktteilnehmer",
                identifier = "MT",
                hidden = Constants.TRUE1
        )
)
public class Side {

        public String _name;

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
}
