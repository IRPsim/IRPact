package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true
)
public class InGlobal {

    @FieldDefinition(
            name = "Tax_PS_vat",
            gams = @GamsParameter(
                    description = "Mehrwertsteuer",
                    identifier = "MWST"
            ),
            edn = @EdnParameter(
                    label = "Skalare und einfache Zeitreihen/Scalar"
            )
    )
    public double mwst;

    @FieldDefinition(
            name = "X_MS_DE_country",
            gams = @GamsParameter(
                    description = "Wahl Deutschland",
                    identifier = "Wahl Deutschland",
                    defaultValue = "1",
                    rule = {"IF (sca_X_MS_DE_country == 1, sca_X_MS_CH_country = 0)",
                            "IF (sca_X_MS_DE_country == 0, sca_X_MS_CH_country = 1)"}
            ),
            edn = @EdnParameter(
                    label = "Skalare und einfache Zeitreihen/Scalar"
            )
    )
    public boolean de;

    @FieldDefinition(
            name = "X_MS_CH_country",
            gams = @GamsParameter(
                    description = "Wahl Schweiz",
                    identifier = "Wahl Schweiz",
                    defaultValue = "0",
                    rule = {"IF (sca_X_MS_CH_country == 1, sca_X_MS_DE_country = 0)",
                            "IF (sca_X_MS_CH_country == 0, sca_X_MS_DE_country = 1)"}
            ),
            edn = @EdnParameter(
                    label = "Skalare und einfache Zeitreihen/Scalar"
            )
    )
    public boolean ch;

    @FieldDefinition(
            name = "C_MS_E",
            timeSeries = Ii.class,
            gams = @GamsParameter(
                    description = "Marktpreis Strom",
                    identifier = "Marktpreis Strom",
                    unit = "[EUR / MWh]"
            ),
            edn = @EdnParameter(
                    label = "Skalare und einfache Zeitreihen/TimeSeries"
            )
    )
    public DoubleTimeSeries marktpreis;

    @FieldDefinition(
            name = "SOH_pss_sector",
            gams = @GamsParameter(
                    description = "Bitte geben Sie hier an, wie die Technologiekosten welchem Sektor (Strom, Wärme, Reserve etc.) (anteilig) zugeordnet werden sollen.",
                    identifier = "Sektorzuweisung der Technologiekosten",
                    domain = "[0, 1]"
            ),
            edn = @EdnParameter(
                    //path = "Komponenten/Speichertechnologien/Hoheitssystematik/Sektorenschlüssel",
                    label = "Link/Table",
                    set = "set_tech_DES_ES"
            )
    )
    @TableInfo(first = Sector.class, second = Pss.class, value = double.class)
    public Table<Sector, Pss, Double> zuweisung;

    @FieldDefinition(
            name = "F_E_EGrid_energy",
            timeSeries = Ii.class,
            gams = @GamsParameter(
                    description = "Bitte geben Sie hier die an die verschiedenen Akteure (Netz-, Politik- und Vertriebsseite) zu zahlenden Arbeitstarife für den Strom-Netzbezug an",
                    identifier = "Strom-Arbeitstarife Netzbezug",
                    unit = "[EUR / MWh]",
                    defaultValue = "0"
            ),
            edn = @EdnParameter(
                    //path = "Komponenten/Verbrauchertechnologien/Endkundentarife",
                    label = "Link/TableWithTimeSeries",
                    delta = Constants.TRUE1,
                    set = "set_load_DS_E"
            )
    )
    @TableInfo(first = SideFares.class, second = Pss.class, value = DoubleTimeSeries.class)
    public Table<SideFares, Pss, DoubleTimeSeries> energy;

    public InGlobal() {
    }
}
