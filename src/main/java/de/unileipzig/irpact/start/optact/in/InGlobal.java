package de.unileipzig.irpact.start.optact.in;

import de.unileipzig.irptools.Constants;
import de.unileipzig.irptools.defstructure.annotation.*;
import de.unileipzig.irptools.util.DoubleTimeSeries;
import de.unileipzig.irptools.util.Table;

/**
 * @author Daniel Abitz
 */
@Definition(
        global = true,
        edn = @Edn(
                path = {"Skalare und einfache Zeitreihen"},
                description = "Hier sind die skalaren Werte und einfache Zeitreihen"
        )
)
public class InGlobal {

    @FieldDefinition(
            name = "Tax_PS_vat",
            gams = @GamsParameter(
                    description = "Mehrwertsteuer",
                    identifier = "MWST"
            ),
            edn = @EdnParameter(
                    path = "Skalare und einfache Zeitreihen/Scalar",
                    description = {"", "Hier stehen skalare Werte"}
            )
    )
    public double mwst;

    @FieldDefinition(
            name = "X_MS_DE_country",
            gams = @GamsParameter(
                    description = "Wahl Deutschland",
                    identifier = "Wahl Deutschland",
                    defaultValue = "1",
                    domain = "[0|1]",
                    rule = {"IF (sca_X_MS_DE_country == 1, sca_X_MS_CH_country = 0)",
                            "IF (sca_X_MS_DE_country == 0, sca_X_MS_CH_country = 1)"}
            ),
            edn = @EdnParameter(
                    path = "Skalare und einfache Zeitreihen/Scalar"
            )
    )
    public boolean de;

    @FieldDefinition(
            name = "X_MS_CH_country",
            gams = @GamsParameter(
                    description = "Wahl Schweiz",
                    identifier = "Wahl Schweiz",
                    defaultValue = "0",
                    domain = "[0|1]",
                    rule = {"IF (sca_X_MS_CH_country == 1, sca_X_MS_DE_country = 0)",
                            "IF (sca_X_MS_CH_country == 0, sca_X_MS_DE_country = 1)"}
            ),
            edn = @EdnParameter(
                    path = "Skalare und einfache Zeitreihen/Scalar"
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
                    path = "Skalare und einfache Zeitreihen/TimeSeries",
                    description = {"", "Hier stehen einfache Zeitreihen"}
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
                    path = "Link/Table",
                    description = {"Hier stehen Tables-Einträge.", "Hier stehen primitive Tables-Einträge"},
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
                    path = "Link/TableWithTimeSeries",
                    description = {"Hier stehen Tables-Einträge.", "Hier stehen Zeitreihen als Tables-Einträge"},
                    delta = Constants.TRUE1,
                    set = "set_load_DS_E"
            )
    )
    @TableInfo(first = SideFares.class, second = Pss.class, value = DoubleTimeSeries.class)
    public Table<SideFares, Pss, DoubleTimeSeries> energy;

    public InGlobal() {
    }
}
