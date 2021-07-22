package de.unileipzig.irpact.core.postprocessing.data;

import de.unileipzig.irpact.commons.util.JsonUtil;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public class DefaultLocalizedResultDataYaml {

    protected DefaultLocalizedResultDataYaml() {
    }

    public static LocalizedResultData get() {
        LocalizedResultDataYaml data = new LocalizedResultDataYaml(Locale.GERMAN, JsonUtil.YAML.createObjectNode());
        //xlsx
        //zip
        data.setSheetName(FileExtension.XLSX, DataToAnalyse.ZIP, "Adoptionen");
        data.setInformations(FileExtension.XLSX, DataToAnalyse.ZIP, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Jährliche Adoptionsdaten nach PLZ"
        });
        data.setColumns(FileExtension.XLSX, DataToAnalyse.ZIP, new String[]{
                "Jahr",
                "PLZ",
                "Adoptionen",
                "Kumulierte Adoptionen"
        });
        //phase
        data.setSheetName(FileExtension.XLSX, DataToAnalyse.PHASE, "Adoptionen");
        data.setInformations(FileExtension.XLSX, DataToAnalyse.PHASE, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Jährliche Adoptionsdaten nach Adoptionsphase"
        });
        data.setColumns(FileExtension.XLSX, DataToAnalyse.PHASE, new String[]{
                "Jahr",
                "Adoptionsphase",
                "Adoptionen",
                "Kumulierte Adoptionen"
        });
        //all
        data.setSheetName(FileExtension.XLSX, DataToAnalyse.ALL_AGENTS, "Adoptionen");
        data.setInformations(FileExtension.XLSX, DataToAnalyse.ALL_AGENTS, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Auflistung der Adoptionen und Nichtadoption aller Agenter"
        });
        data.setColumns(FileExtension.XLSX, DataToAnalyse.ALL_AGENTS, new String[]{
                "Agent",
                "Milieu",
                "ID der räumlichen Daten",
                "PLZ",
                "adoptiertes Produkt",
                "Produktgruppe",
                "Adoptionsphase",
                "Zeitpunkt"
        });

        //csv
        //zip
        data.setCharset(FileExtension.CSV, DataToAnalyse.ZIP, "UTF-8");
        data.setDelimiter(FileExtension.CSV, DataToAnalyse.ZIP, ";");
        data.setCommentPrefix(FileExtension.CSV, DataToAnalyse.ZIP, "# ");
        data.setInformations(FileExtension.CSV, DataToAnalyse.ZIP, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Jährliche Adoptionsdaten nach PLZ"
        });
        data.setColumns(FileExtension.CSV, DataToAnalyse.ZIP, new String[]{
                "Jahr",
                "PLZ",
                "Adoptionen",
                "Kumulierte Adoptionen"
        });
        //phase
        data.setCharset(FileExtension.CSV, DataToAnalyse.PHASE, "UTF-8");
        data.setDelimiter(FileExtension.CSV, DataToAnalyse.PHASE, ";");
        data.setCommentPrefix(FileExtension.CSV, DataToAnalyse.PHASE, "# ");
        data.setInformations(FileExtension.CSV, DataToAnalyse.PHASE, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Jährliche Adoptionsdaten nach Adoptionsphase"
        });
        data.setColumns(FileExtension.CSV, DataToAnalyse.PHASE, new String[]{
                "Jahr",
                "Adoptionsphase",
                "Adoptionen",
                "Kumulierte Adoptionen"
        });
        //all
        data.setCharset(FileExtension.CSV, DataToAnalyse.ALL_AGENTS, "UTF-8");
        data.setDelimiter(FileExtension.CSV, DataToAnalyse.ALL_AGENTS, ";");
        data.setCommentPrefix(FileExtension.CSV, DataToAnalyse.ALL_AGENTS, "# ");
        data.setInformations(FileExtension.CSV, DataToAnalyse.ALL_AGENTS, new String[]{
                "Automatisch erstellte Tabelle (Datum: {})",
                "Inhalt: Auflistung der Adoptionen und Nichtadoption aller Agenter"
        });
        data.setColumns(FileExtension.CSV, DataToAnalyse.ALL_AGENTS, new String[]{
                "Agent",
                "Milieu",
                "ID der räumlichen Daten",
                "PLZ",
                "adoptiertes Produkt",
                "Produktgruppe",
                "Adoptionsphase",
                "Zeitpunkt"
        });

        return data;
    }
}
