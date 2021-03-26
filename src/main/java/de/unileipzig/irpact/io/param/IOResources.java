package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.MultiCounter;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.start.optact.gvin.AgentGroup;
import de.unileipzig.irpact.start.optact.in.*;
import de.unileipzig.irpact.start.optact.network.IFreeMultiGraphTopology;
import de.unileipzig.irpact.start.optact.network.IWattsStrogatzModel;
import de.unileipzig.irptools.graphviz.def.GraphvizTreeResource;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.TreeResourceApplier;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.NoSuchElementException;

import static de.unileipzig.irptools.Constants.*;

/**
 * @author Daniel Abitz
 */
public class IOResources extends TreeAnnotationResource {

    /**
     * @author Daniel Abitz
     */
    public static final class Data {

        private final MultiCounter COUNTER = new MultiCounter();
        private final LocData DATA;

        public Data(LocData data) {
            this.DATA = data;
        }

        public MultiCounter getCounter() {
            return COUNTER;
        }

        public LocData getData() {
            return DATA;
        }
    }

    private static IOResources instance;
    public static IOResources getInstance() {
        if(instance == null) {
            instance = new IOResources();
        }
        return instance;
    }

    public IOResources() {
        this(Locale.GERMAN);
    }

    public IOResources(java.nio.file.Path pathToResource) {
        LocData locData = loadLocData(pathToResource);
        IOResources.Data userData = new Data(locData);
        setUserData(userData);
        init();
    }

    public IOResources(Locale locale) {
        LocData locData = loadLocData(locale);
        IOResources.Data userData = new Data(locData);
        setUserData(userData);
        init();
    }

    protected static LocData loadLocData(Locale locale) {
        String langTag = locale.toLanguageTag();
        String fileName = "loc_" + langTag + ".yaml";
        String resName = "irpactdata/" + fileName;
        InputStream in = IOResources.class.getResourceAsStream(resName);
        if(in == null) {
            throw new NoSuchElementException("resource '" + resName + "' not found");
        } else {
            try(Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return new LocData(reader);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
    }

    protected static LocData loadLocData(java.nio.file.Path pathToFile) {
        try {
            return new LocData(pathToFile);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    protected void init() {
        initGraphviz();
        initOptactPart1();
        initOptActPart2();
        initAct();
        //===
        update();
    }

    //=========================
    //
    //=========================

    protected void initGraphviz() {
        GraphvizTreeResource gvRes = new GraphvizTreeResource();
        gvRes.initGams();
        gvRes.initEdn();
        gvRes.update();
        putAll(gvRes);
        putCache(IOConstants.GRAPHVIZ, gvRes.graphviz);
    }

    //=========================
    //OptAct Part 1
    //=========================

    protected void initOptactPart1() {
        put(AgentGroup.class, "numberOfAgents", GAMS_IDENTIFIER, "Anzahl der Agenten");
        put(AgentGroup.class, "numberOfAgents", GAMS_DESCRIPTION, "Anzahl der Agenten");
        put(AgentGroup.class, "agentColor", GAMS_IDENTIFIER, "zu nutzende Farbe");
        put(AgentGroup.class, "agentColor", GAMS_DESCRIPTION, "Farbe, welche diese Gruppe im Graphen haben soll. Wichtig: es wird nur der erste Wert verwendet! Falls keine Farbe gewählt wird, ist die Farbe schwarz.");

        put(IWattsStrogatzModel.class, "wsmK", GAMS_IDENTIFIER, "Knotengrad1");
        put(IWattsStrogatzModel.class, "wsmK", GAMS_DESCRIPTION, "Knotengrad k");
        put(IWattsStrogatzModel.class, "wsmBeta", GAMS_IDENTIFIER, "Rewire Wahrscheinlichkeit");
        put(IWattsStrogatzModel.class, "wsmBeta", GAMS_DESCRIPTION, "Wahrscheinlichkeit, dass eine Kante umgelegt wird.");
        put(IWattsStrogatzModel.class, "wsmSelfReferential", GAMS_IDENTIFIER, "Selbstreferenzierung1 erlaubt?");
        put(IWattsStrogatzModel.class, "wsmSelfReferential", GAMS_DESCRIPTION, "On Knoten Kanten zu sich selber erzeugen dürfen.");
        put(IWattsStrogatzModel.class, "wsmSeed", GAMS_IDENTIFIER, "Seed1");
        put(IWattsStrogatzModel.class, "wsmSeed", GAMS_DESCRIPTION, "Seed für den Zufallsgenerator.");
        put(IWattsStrogatzModel.class, "wsmUseThis", GAMS_IDENTIFIER, "Diese Topologie nutzen1?");
        put(IWattsStrogatzModel.class, "wsmUseThis", GAMS_DESCRIPTION, "Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!");

        put(IFreeMultiGraphTopology.class, "ftEdgeCount", GAMS_IDENTIFIER, "Knotengrad2");
        put(IFreeMultiGraphTopology.class, "ftEdgeCount", GAMS_DESCRIPTION, "Knotengrad k");
        put(IFreeMultiGraphTopology.class, "ftSelfReferential", GAMS_IDENTIFIER, "Selbstreferenzierung2 erlaubt?");
        put(IFreeMultiGraphTopology.class, "ftSelfReferential", GAMS_DESCRIPTION, "On Knoten Kanten zu sich selber erzeugen dürfen.");
        put(IFreeMultiGraphTopology.class, "ftSeed", GAMS_IDENTIFIER, "Seed2");
        put(IFreeMultiGraphTopology.class, "ftSeed", GAMS_DESCRIPTION, "Seed für den Zufallsgenerator.");
        put(IFreeMultiGraphTopology.class, "ftUseThis", GAMS_IDENTIFIER, "Diese Topologie nutzen2?");
        put(IFreeMultiGraphTopology.class, "ftUseThis", GAMS_DESCRIPTION, "Soll diese Topology verwendet werden? Hinweis: es wird nur die erste gültige genutzt! Der Rest wird ignoriert!");

        PathElement AgentGroup_Element = newElement();
        AgentGroup_Element.set(EDN_LABEL, "Agentengruppen für Graphviz");
        AgentGroup_Element.set(SPECIAL_EDN_PRIORITIES, "0");
        AgentGroup_Element.set(EDN_DESCRIPTION, "Agentengruppe, welche im Graphen dargestellt werden sollen.");
        putCache("AgentGroup_Element", AgentGroup_Element);
        Path agPath = buildPath(AgentGroup_Element);
        putPath(AgentGroup.class, agPath);

        PathElement topo = newElement();
        topo.set(EDN_LABEL, "Topologie");
        topo.set(SPECIAL_EDN_PRIORITIES, "0");
        topo.set(EDN_DESCRIPTION, "unterstützte Topologien");

        PathElement IWattsStrogatzModel_Element = newElement();
        IWattsStrogatzModel_Element.set(EDN_LABEL, "Watts-Strogatz-Model");
        IWattsStrogatzModel_Element.set(SPECIAL_EDN_PRIORITIES, "0");
        IWattsStrogatzModel_Element.set(EDN_DESCRIPTION, "Watts-Strogatz-Model");
        Path IWattsStrogatzModel_Path = buildPath(getCachedElement(IOConstants.GRAPHVIZ), topo, IWattsStrogatzModel_Element);
        putPath(IWattsStrogatzModel.class, IWattsStrogatzModel_Path);

        PathElement IFreeMultiGraphTopology_Element = newElement();
        IFreeMultiGraphTopology_Element.set(EDN_LABEL, "Freie Topologie");
        IFreeMultiGraphTopology_Element.set(SPECIAL_EDN_PRIORITIES, "0");
        IFreeMultiGraphTopology_Element.set(EDN_DESCRIPTION, "Freie Topologie");
        Path IFreeMultiGraphTopology_Path = buildPath(getCachedElement(IOConstants.GRAPHVIZ), topo, IFreeMultiGraphTopology_Element);
        putPath(IFreeMultiGraphTopology.class, IFreeMultiGraphTopology_Path);
    }

    //=========================
    //OptAct Part 2
    //=========================

    protected Entry Ii_Entry;
    protected Entry LoadDS_Entry;
    protected Entry LoadDSE_Entry;
    protected Entry LoadDSLOA_Entry;
    protected Entry Pss_Entry;
    protected Entry Sector_Entry;
    protected Entry Side_Entry;
    protected Entry SideCustom_Entry;
    protected Entry SideFares_Entry;
    protected Entry TechDEGEN_Entry;
    protected Entry TechDES_Entry;
    protected Entry TechDESES_Entry;
    protected Entry TechDESPV_Entry;
    protected Entry TechDESTO_Entry;

    protected PathElement OptAct_Element;
    protected PathElement InGlobal_Element;
    protected PathElement InGlobal_scalar_Element;
    protected PathElement InGlobal_timeseries_Element;

    protected PathElement Link_Element;
    protected PathElement Link_Table_Element;
    protected PathElement Link_TableWithTimeSeries_Element;

    protected PathElement Set_Element;
    protected PathElement Set_Strom_Element;
    protected PathElement Set_Kundengruppen_Element;
    protected PathElement Set_Stromspeicher_Element;
    protected PathElement Set_PV_Element;

    protected Entry InGlobal_mwst_Entry;
    protected Path InGlobal_mwst_Path;

    protected Entry InGlobal_de_Entry;
    protected Path InGlobal_de_Path;

    protected Entry InGlobal_ch_Entry;
    protected Path InGlobal_ch_Path;

    protected Entry InGlobal_marktpreis_Entry;
    protected Path InGlobal_marktpreis_Path;

    protected Entry InGlobal_zuweisung_Entry;
    protected Path InGlobal_zuweisung_Path;

    protected Entry InGlobal_energy_Entry;
    protected Path InGlobal_energy_Path;

    protected Path LoadDSE_Path;

    protected Entry LoadDSE_ldse_Entry;

    protected Path SideCustom_Path;
    protected Entry SideCustom_number_Entry;
    protected Entry SideCustom_delta_Entry;
    protected Entry SideCustom_timeStuff_Entry;

    protected Path TechDESES_Path;
    protected Entry TechDESES_foerderung_Entry;

    protected Path TechDESPV_Path;
    protected Entry TechDESPV_a_Entry;

    protected void initOptActPart2() {
        Ii_Entry = newEntry();
        Ii_Entry.set(GENERAL_NAME, "ii");
        Ii_Entry.set(GAMS_DESCRIPTION, "Simulationshorizont");
        Ii_Entry.set(GAMS_IDENTIFIER, "SH");
        Ii_Entry.set(GAMS_HIDDEN, TRUE1);
        Ii_Entry.set(GAMS_TYPE, GAMS_TIMESERIES);
        putEntry(Ii.class, Ii_Entry);
        //===
        OptAct_Element = newElement();
        OptAct_Element.set(EDN_LABEL, "OptAct");
        OptAct_Element.set(EDN_DESCRIPTION, "Modul mit Testparametern für die Kopplung.");
        OptAct_Element.set(SPECIAL_EDN_PRIORITIES, "0");
        putCache("OPTACT", OptAct_Element);
        
        InGlobal_Element = newElement();
        InGlobal_Element.set(EDN_LABEL, "Skalare und einfache Zeitreihen");
        InGlobal_Element.set(EDN_DESCRIPTION, "Hier sind die skalaren Werte und einfache Zeitreihen");

        InGlobal_scalar_Element = newElement();
        InGlobal_scalar_Element.set(EDN_LABEL, "Skalare");
        InGlobal_scalar_Element.set(EDN_DESCRIPTION, "Hier stehen skalare Werte");

        InGlobal_timeseries_Element = newElement();
        InGlobal_timeseries_Element.set(EDN_LABEL, "Zeitreihen");
        InGlobal_timeseries_Element.set(EDN_DESCRIPTION, "Hier stehen einfache Zeitreihen");

        Link_Element = newElement();
        Link_Element.set(EDN_LABEL, "Link");
        Link_Element.set(EDN_DESCRIPTION, "Hier stehen Tables-Einträge.");

        Link_Table_Element = newElement();
        Link_Table_Element.set(EDN_LABEL, "Table");
        Link_Table_Element.set(EDN_DESCRIPTION, "Hier stehen primitive Tables-Einträge.");

        Link_TableWithTimeSeries_Element = newElement();
        Link_TableWithTimeSeries_Element.set(EDN_LABEL, "TableWithTimeSeries");
        Link_TableWithTimeSeries_Element.set(EDN_DESCRIPTION, "Hier stehen Zeitreihen als Tables-Einträge.");

        InGlobal_mwst_Entry = newEntry();
        InGlobal_mwst_Entry.set(GENERAL_NAME, "Tax_PS_vat");
        InGlobal_mwst_Entry.set(GAMS_DESCRIPTION, "Mehrwertsteuer");
        InGlobal_mwst_Entry.set(GAMS_IDENTIFIER, "MWST");
        putEntry(InGlobal.class, "mwst", InGlobal_mwst_Entry);

        InGlobal_mwst_Path = buildPath(OptAct_Element, InGlobal_Element, InGlobal_scalar_Element);
        putPath(InGlobal.class, "mwst", InGlobal_mwst_Path);

        InGlobal_de_Entry = newEntry();
        InGlobal_de_Entry.set(GENERAL_NAME, "X_MS_DE_country");
        InGlobal_de_Entry.set(GAMS_DESCRIPTION, "Wahl Deutschland");
        InGlobal_de_Entry.set(GAMS_IDENTIFIER, "Wahl Deutschland");
        InGlobal_de_Entry.set(GAMS_DEFAULT, "1");
        InGlobal_de_Entry.set(GAMS_DOMAIN, "[0|1]");
        //TODO rule noch einbauen
        putEntry(InGlobal.class, "de", InGlobal_de_Entry);

        InGlobal_de_Path = buildPath(OptAct_Element, InGlobal_Element, InGlobal_scalar_Element);
        putPath(InGlobal.class, "de", InGlobal_de_Path);

        InGlobal_ch_Entry = newEntry();
        InGlobal_ch_Entry.set(GENERAL_NAME, "X_MS_CH_country");
        InGlobal_ch_Entry.set(GAMS_DESCRIPTION, "Wahl Schweiz");
        InGlobal_ch_Entry.set(GAMS_IDENTIFIER, "Wahl Schweiz");
        InGlobal_ch_Entry.set(GAMS_DEFAULT, "0");
        InGlobal_ch_Entry.set(GAMS_DOMAIN, "[0|1]");
        //TODO rule noch einbauen
        putEntry(InGlobal.class, "ch", InGlobal_ch_Entry);

        InGlobal_ch_Path = buildPath(OptAct_Element, InGlobal_Element, InGlobal_scalar_Element);
        putPath(InGlobal.class, "ch", InGlobal_ch_Path);

        InGlobal_marktpreis_Entry = newEntry();
        InGlobal_marktpreis_Entry.set(GENERAL_NAME, "C_MS_E");
        InGlobal_marktpreis_Entry.set(GAMS_DESCRIPTION, "Marktpreis Strom");
        InGlobal_marktpreis_Entry.set(GAMS_IDENTIFIER, "Marktpreis Strom");
        InGlobal_marktpreis_Entry.set(GAMS_UNIT, "[EUR / MWh]");
        putEntry(InGlobal.class, "marktpreis", InGlobal_marktpreis_Entry);

        InGlobal_marktpreis_Path = buildPath(OptAct_Element, InGlobal_Element, InGlobal_timeseries_Element);
        putPath(InGlobal.class, "marktpreis", InGlobal_marktpreis_Path);

        InGlobal_zuweisung_Entry = newEntry();
        InGlobal_zuweisung_Entry.set(GENERAL_NAME, "SOH_pss_sector");
        InGlobal_zuweisung_Entry.set(GAMS_DESCRIPTION, "Bitte geben Sie hier an, wie die Technologiekosten welchem Sektor (Strom, Wärme, Reserve etc.) (anteilig) zugeordnet werden sollen.");
        InGlobal_zuweisung_Entry.set(GAMS_IDENTIFIER, "Sektorzuweisung der Technologiekosten");
        InGlobal_zuweisung_Entry.set(GAMS_DOMAIN, "[0, 1]");
        InGlobal_zuweisung_Entry.set(EDN_SET, "set_tech_DES_ES");
        putEntry(InGlobal.class, "zuweisung", InGlobal_zuweisung_Entry);

        InGlobal_zuweisung_Path = buildPath(OptAct_Element, Link_Element, Link_Table_Element);
        putPath(InGlobal.class, "zuweisung", InGlobal_zuweisung_Path);

        InGlobal_energy_Entry = newEntry();
        InGlobal_energy_Entry.set(GENERAL_NAME, "F_E_EGrid_energy");
        InGlobal_energy_Entry.set(GAMS_DESCRIPTION, "Bitte geben Sie hier die an die verschiedenen Akteure (Netz-, Politik- und Vertriebsseite) zu zahlenden Arbeitstarife für den Strom-Netzbezug an");
        InGlobal_energy_Entry.set(GAMS_IDENTIFIER, "Strom-Arbeitstarife Netzbezug");
        InGlobal_energy_Entry.set(GAMS_UNIT, "[EUR / MWh]");
        InGlobal_energy_Entry.set(GAMS_DEFAULT, "0");
        InGlobal_energy_Entry.set(SPECIAL_EDN_DELTA, TRUE1);
        InGlobal_energy_Entry.set(EDN_SET, "set_load_DS_E");
        putEntry(InGlobal.class, "energy", InGlobal_energy_Entry);

        InGlobal_energy_Path = buildPath(OptAct_Element, Link_Element, Link_TableWithTimeSeries_Element);
        putPath(InGlobal.class, "energy", InGlobal_energy_Path);
        //===
        LoadDS_Entry = newEntry();
        LoadDS_Entry.set(GENERAL_NAME, "load_DS");
        LoadDS_Entry.set(GAMS_DESCRIPTION, "Lastgang");
        LoadDS_Entry.set(GAMS_IDENTIFIER, "Lastgang");
        LoadDS_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(LoadDS.class, LoadDS_Entry);
        //===
        LoadDSE_Entry = newEntry();
        LoadDSE_Entry.set(GENERAL_NAME, "load_DS_E");
        LoadDSE_Entry.set(GAMS_DESCRIPTION, "Strom-Verbrauchertechnologie");
        LoadDSE_Entry.set(GAMS_IDENTIFIER, "Strom-Verbrauchertechnologie");
        putEntry(LoadDSE.class, LoadDSE_Entry);

        Set_Element = newElement();
        Set_Element.set(EDN_LABEL, "Set");
        Set_Element.set(EDN_DESCRIPTION, "Hier sind Sets.");

        Set_Strom_Element = newElement();
        Set_Strom_Element.set(EDN_LABEL, "Strom-Verbrauchertechnologie");
        Set_Strom_Element.set(EDN_DESCRIPTION, "Hier sind Strom-Verbrauchertechnologie");

        LoadDSE_Path = buildPath(OptAct_Element, Set_Element, Set_Strom_Element);
        putPath(LoadDSE.class, LoadDSE_Path);

        LoadDSE_ldse_Entry = newEntry();
        LoadDSE_ldse_Entry.set(GENERAL_NAME, "L_DS_E");
        LoadDSE_ldse_Entry.set(GAMS_DESCRIPTION, "Bitte geben Sie hier ein gewünschtes elektrische Lastprofil ein");
        LoadDSE_ldse_Entry.set(GAMS_IDENTIFIER, "Elektrisches Lastprofil");
        LoadDSE_ldse_Entry.set(GAMS_UNIT, "[MWh]");
        LoadDSE_ldse_Entry.set(GAMS_DOMAIN, "[0,)");
        putEntry(LoadDSE.class, "ldse", LoadDSE_ldse_Entry);
        //===
        LoadDSLOA_Entry = newEntry();
        LoadDSLOA_Entry.set(GENERAL_NAME, "load_DSLOA");
        LoadDSLOA_Entry.set(GAMS_DESCRIPTION, "Verbrauchertechnologie");
        LoadDSLOA_Entry.set(GAMS_IDENTIFIER, "Verbrauchertechnologie");
        LoadDSLOA_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(LoadDSLOA.class, LoadDSLOA_Entry);
        //===
        Pss_Entry = newEntry();
        Pss_Entry.set(GENERAL_NAME, "pss");
        Pss_Entry.set(GAMS_DESCRIPTION, "Prosumstorer");
        Pss_Entry.set(GAMS_IDENTIFIER, "Prosumstorer");
        Pss_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(Pss.class, Pss_Entry);
        //===
        Sector_Entry = newEntry();
        Sector_Entry.set(GENERAL_NAME, "sector");
        Sector_Entry.set(GAMS_DESCRIPTION, "Energiesektor");
        Sector_Entry.set(GAMS_IDENTIFIER, "Energiesektor");
        Sector_Entry.set(GAMS_HIDDEN, TRUE1);
        Sector_Entry.set(GAMS_DEFAULT, "E");
        putEntry(Sector.class, Sector_Entry);
        //===
        Side_Entry = newEntry();
        Side_Entry.set(GENERAL_NAME, "side");
        Side_Entry.set(GAMS_DESCRIPTION, "Marktteilnehmer");
        Side_Entry.set(GAMS_IDENTIFIER, "MT");
        Side_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(Side.class, Side_Entry);
        //===
        SideCustom_Entry = newEntry();
        SideCustom_Entry.set(GENERAL_NAME, "side_cust");
        SideCustom_Entry.set(GAMS_DESCRIPTION, "Kundengruppe in IRPact");
        SideCustom_Entry.set(GAMS_IDENTIFIER, "KG");
        putEntry(SideCustom.class, SideCustom_Entry);

        Set_Kundengruppen_Element = newElement();
        Set_Kundengruppen_Element.set(EDN_LABEL, "Kundengruppen");
        Set_Kundengruppen_Element.set(EDN_DESCRIPTION, "Hier sind Kundengruppen.");

        SideCustom_Path = buildPath(OptAct_Element, Set_Element, Set_Kundengruppen_Element);
        putPath(SideCustom.class, SideCustom_Path);

        SideCustom_number_Entry = newEntry();
        SideCustom_number_Entry.set(GENERAL_NAME, "S_DS");
        SideCustom_number_Entry.set(GAMS_DESCRIPTION, "Anzahl der Kunden");
        SideCustom_number_Entry.set(GAMS_IDENTIFIER, "KGA");
        SideCustom_number_Entry.set(GAMS_DEFAULT, "10");
        putEntry(SideCustom.class, "number", SideCustom_number_Entry);

        SideCustom_delta_Entry = newEntry();
        SideCustom_delta_Entry.set(GENERAL_NAME, "kg_modifier");
        SideCustom_delta_Entry.set(GAMS_DESCRIPTION, "Erhöht die Anzahl der Kunden in der Gruppe um den gewünschten Wert.");
        SideCustom_delta_Entry.set(GAMS_IDENTIFIER, "KGAM");
        SideCustom_delta_Entry.set(GAMS_DEFAULT, "5");
        putEntry(SideCustom.class, "delta", SideCustom_delta_Entry);

        SideCustom_timeStuff_Entry = newEntry();
        SideCustom_timeStuff_Entry.set(GENERAL_NAME, "IuO_ESector_CustSide");
        SideCustom_timeStuff_Entry.set(GAMS_DESCRIPTION, "Stromsparte je Kundengruppe");
        SideCustom_timeStuff_Entry.set(GAMS_IDENTIFIER, "SK");
        SideCustom_timeStuff_Entry.set(GAMS_UNIT, "[EUR]");
        putEntry(SideCustom.class, "timeStuff", SideCustom_timeStuff_Entry);
        //===
        SideFares_Entry = newEntry();
        SideFares_Entry.set(GENERAL_NAME, "side_fares");
        SideFares_Entry.set(GAMS_DESCRIPTION, "Tarifteilnehmer");
        SideFares_Entry.set(GAMS_IDENTIFIER, "Tarifteilnehmer");
        SideFares_Entry.set(GAMS_DEFAULT, "SMS, NS, PS");
        SideFares_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(SideFares.class, SideFares_Entry);
        //===
        TechDEGEN_Entry = newEntry();
        TechDEGEN_Entry.set(GENERAL_NAME, "tech_DEGEN");
        TechDEGEN_Entry.set(GAMS_DESCRIPTION, "Erzeugertechnologie");
        TechDEGEN_Entry.set(GAMS_IDENTIFIER, "Erzeugertechnologie");
        TechDEGEN_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(TechDEGEN.class, TechDEGEN_Entry);
        //===
        TechDES_Entry = newEntry();
        TechDES_Entry.set(GENERAL_NAME, "tech_DES");
        TechDES_Entry.set(GAMS_DESCRIPTION, "Dezentrale Energietechnologie");
        TechDES_Entry.set(GAMS_IDENTIFIER, "Dezentrale Energietechnologie");
        TechDES_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(TechDES.class, TechDES_Entry);
        //===
        TechDESES_Entry = newEntry();
        TechDESES_Entry.set(GENERAL_NAME, "tech_DES_ES");
        TechDESES_Entry.set(GAMS_DESCRIPTION, "Stromspeicher");
        TechDESES_Entry.set(GAMS_IDENTIFIER, "Stromspeicher");
        putEntry(TechDESES.class, TechDESES_Entry);

        Set_Stromspeicher_Element = newElement();
        Set_Stromspeicher_Element.set(EDN_LABEL, "Stromspeicher");
        Set_Stromspeicher_Element.set(EDN_DESCRIPTION, "Hier sind Stromspeicher.");

        TechDESES_Path = buildPath(OptAct_Element, Set_Element, Set_Stromspeicher_Element);
        putPath(TechDESES.class, TechDESES_Path);

        TechDESES_foerderung_Entry = newEntry();
        TechDESES_foerderung_Entry.set(GENERAL_NAME, "Inc_PS_ES");
        TechDESES_foerderung_Entry.set(GAMS_DESCRIPTION, "Bitte tragen Sie hier die spezifische Förderung der öffentlichen Hand für Stromspeicher ein");
        TechDESES_foerderung_Entry.set(GAMS_IDENTIFIER, "Förderung für Stromspeicher durch Politik");
        TechDESES_foerderung_Entry.set(GAMS_UNIT, "[EUR / MWh]");
        TechDESES_foerderung_Entry.set(GAMS_DOMAIN, "[0,)");
        TechDESES_foerderung_Entry.set(GAMS_DEFAULT, "0");
        putEntry(TechDESES.class, "foerderung", TechDESES_foerderung_Entry);
        //===
        TechDESPV_Entry = newEntry();
        TechDESPV_Entry.set(GENERAL_NAME, "tech_DES_PV");
        TechDESPV_Entry.set(GAMS_DESCRIPTION, "PV-Anlage");
        TechDESPV_Entry.set(GAMS_IDENTIFIER, "PV-Anlage");
        putEntry(TechDESPV.class, TechDESPV_Entry);

        Set_PV_Element = newElement();
        Set_PV_Element.set(EDN_LABEL, "PV");
        Set_PV_Element.set(EDN_DESCRIPTION, "Hier sind PV.");

        TechDESPV_Path = buildPath(OptAct_Element, Set_Element, Set_PV_Element);
        putPath(TechDESPV.class, TechDESPV_Path);

        TechDESPV_a_Entry = newEntry();
        TechDESPV_a_Entry.set(GENERAL_NAME, "A_DES_PV");
        TechDESPV_a_Entry.set(GAMS_DESCRIPTION, "Bitte geben Sie hier die gesamte Modulfläche der PV-Anlage an");
        TechDESPV_a_Entry.set(GAMS_IDENTIFIER, "Modulfläche PV-Anlage");
        TechDESPV_a_Entry.set(GAMS_UNIT, "[m2]");
        TechDESPV_a_Entry.set(GAMS_DOMAIN, "[0,)");
        TechDESPV_a_Entry.set(GAMS_DEFAULT, "0");
        putEntry(TechDESPV.class, "a", TechDESPV_a_Entry);
        //===
        TechDESTO_Entry = newEntry();
        TechDESTO_Entry.set(GENERAL_NAME, "tech_DESTO");
        TechDESTO_Entry.set(GAMS_DESCRIPTION, "Speichertechnologie");
        TechDESTO_Entry.set(GAMS_IDENTIFIER, "Speichertechnologie");
        TechDESTO_Entry.set(GAMS_HIDDEN, TRUE1);
        putEntry(TechDESTO.class, TechDESTO_Entry);
    }

    //=========================
    //
    //=========================

    protected void initAct() {
        try {
            TreeResourceApplier.callAllInputs(InRoot.INPUT_WITH_ROOT, this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
