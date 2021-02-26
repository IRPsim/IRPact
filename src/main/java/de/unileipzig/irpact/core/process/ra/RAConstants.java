package de.unileipzig.irpact.core.process.ra;

/**
 * @author Daniel Abitz
 */
public final class RAConstants {

    public static final String XLSX_FILE = "irpacttempdata/BarwertrechnerMini_ES.xlsx";

    public static final String UNCERTAINTY_SUFFIX = "uncertainty";

    //Agent
    public static final String NOVELTY_SEEKING = "novelty_seeking";                   //A2
    public static final String NOVELTY_SEEKING_UNCERTAINTY = getUncertaintyAttributeName(NOVELTY_SEEKING);
    public static final String DEPENDENT_JUDGMENT_MAKING = "dependent_judgment_making";         //A3
    public static final String DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY = getUncertaintyAttributeName(DEPENDENT_JUDGMENT_MAKING);
    public static final String ENVIRONMENTAL_CONCERN = "environmental_concern";             //A4
    public static final String ENVIRONMENTAL_CONCERN_UNCERTAINTY = getUncertaintyAttributeName(ENVIRONMENTAL_CONCERN);
    public static final String SHARE_1_2_HOUSE = "share_1_2_house";                         //A5
    public static final String HOUSE_OWNER = "house_owner";                       //A6
    public static final String CONSTRUCTION_RATE = "construction_rate";                      //A7
    public static final String UNDER_CONSTRUCTION = "under_construction";
    public static final String RENOVATION_RATE = "renovation_rate";                        //A8
    public static final String UNDER_RENOVATION = "under_renovation";

    public static final String COMMUNICATION_FREQUENCY_SN = "communication_frequency";           //C1

    public static final String INITIAL_PRODUCT_AWARENESS = "initial_product_awareness";        //D1
    public static final String INTEREST_THRESHOLD = "interest_threshold";               //D2
    public static final String FINANCIAL_THRESHOLD = "financial_threshold";              //D3
    public static final String ADOPTION_THRESHOLD = "adoption_threshold";               //D4

    //Product
    public static final String INVESTMENT_COST = "investment_cost";                               //E1

    //Spatial
    public static final String ADDRESS = "INSP_Adressen";
    public static final String OWNERSHIP = "FS_Eigentum";
    public static final String NUMBER_OF_HOUSEHOLDS = "Mic_Haushaltsanzahl";
    public static final String ORIENTATION = "LoD2_Dachorientierung";
    public static final String SLOPE = "LoD2_Dachneigung";
    public static final String PURCHASE_POWER = "Mic_Kaufkraft_Index";          //A1
    public static final String DOM_MILIEU = "Mic_Dominantes_Milieu";

    public static final String PRIVATE = "PRIVAT";

    public static String getUncertaintyAttributeName(String name) {
        return name + "_" + UNCERTAINTY_SUFFIX;
    }
}
