package de.unileipzig.irpact.core.process.ra;

import de.unileipzig.irpact.core.spatial.PrintIdAndAttribute;
import de.unileipzig.irpact.core.spatial.SpatialInformationPrinter;
import de.unileipzig.irpact.io.param.ParamUtil;

/**
 * @author Daniel Abitz
 */
public final class RAConstants {

    public static final double DEFAULT_LOGISTIC_FACTOR = 0.005;
    public static final double DEFAULT_SPEED_OF_CONVERGENCE = 0.5;
    public static final double DEFAULT_ATTIDUTE_GAP = 1.75;
    public static final double DEFAULT_NEUTRAL_CHANCE = 0.5;
    public static final double DEFAULT_CONVERGENCE_CHANCE = 0.25;
    public static final double DEFAULT_DIVERGENCE_CHANCE = 0.25;
    public static final double DEFAULT_MODERATE_UNCERTAINTY = 1.4;
    public static final double DEFAULT_EXTREMIST_UNCERTAINTY = 0.35;

    //Agent
    public static final String NOVELTY_SEEKING = "novelty_seeking";                   //A2
    public static final String NOVELTY_SEEKING_UNCERTAINTY = getUncertaintyAttributeName(NOVELTY_SEEKING);
    public static final String DEPENDENT_JUDGMENT_MAKING = "dependent_judgment_making";         //A3
    public static final String DEPENDENT_JUDGMENT_MAKING_UNCERTAINTY = getUncertaintyAttributeName(DEPENDENT_JUDGMENT_MAKING);
    public static final String ENVIRONMENTAL_CONCERN = "environmental_concern";             //A4
    public static final String ENVIRONMENTAL_CONCERN_UNCERTAINTY = getUncertaintyAttributeName(ENVIRONMENTAL_CONCERN);
    public static final String SHARE_1_2_HOUSE = "Anzahl_HH_Dummy";                         //A5
    public static final String SHARE_1_2_HOUSE_COUNT = "Anzahl_HH";                         //A5
    public static final String HOUSE_OWNER = "Privatbesitz";                       //A6
    public static final String HOUSE_OWNER_STR = "Eigentuemer";                       //A6
    public static final String CONSTRUCTION_RATE = "construction_rate";                      //A7
    public static final String RENOVATION_RATE = "renovation_rate";                        //A8

    public static final String REWIRING_RATE = "rewiring_rate";                         //B6

    public static final String COMMUNICATION_FREQUENCY_SN = "communication_frequency";           //C1

    public static final String INITIAL_PRODUCT_AWARENESS = "initial_product_awareness";        //D1
    public static final String INTEREST_THRESHOLD = "interest_threshold";               //D2
    public static final String FINANCIAL_THRESHOLD = "financial_threshold";              //D3
    public static final String ADOPTION_THRESHOLD = "adoption_threshold";               //D4
    public static final String INITIAL_ADOPTER = "initial_adopter";                     //D5
    public static final String INITIAL_PRODUCT_INTEREST = "initial_product_interest";        //D6

    //Product
    public static final String INVESTMENT_COST = "investment_cost";                               //E1

    //spatial datei
    public static final String ID = "ID";
    public static final String ADDRESS = "Adresse";
    public static final String ZIP = "PLZ";
    public static final String ORIENTATION = "Dachorient";
    public static final String SLOPE = "Dachneig";
    public static final String AREA = "Flaeche";
    public static final String X_CENT = "X_Zentroid";
    public static final String Y_CENT = "Y_Zentroid";
    public static final String PURCHASE_POWER = "KK_Index";          //A1
    public static final String PURCHASE_POWER_EUR = "KK_Euro";          //A1
    public static final String PURCHASE_POWER_EUR_ADDR = "KK_Euro_Adresse";          //A1
    public static final String DOM_MILIEU = "Milieu";

    public static final String REAL_ADOPTION_DATA_CUMULATED = "LaufendeSumme";
    public static final String REAL_ADOPTION_DATA_UNCUMULATED = "Anzahl";

    public static final String PRIVATE = "PRIVAT";

    public static final String UNCERTAINTY_SUFFIX = "uncertainty";
    public static final String RATE_OF_CONVERGENCE = "rate_of_convergence";

    public static final String AWARENESS = "awareness";
    public static final String INTEREST = "interest";

    public static final String NET_PRESENT_VALUE = "net_present_value";

    public static String getUncertaintyAttributeName(String name) {
        return ParamUtil.concData(name, UNCERTAINTY_SUFFIX);
    }

    public static String[] UNCERTAINTY_ATTRIBUTES = { NOVELTY_SEEKING, DEPENDENT_JUDGMENT_MAKING, ENVIRONMENTAL_CONCERN };

    public static String[] DEFAULT_ATTRIBUTES = {
            PURCHASE_POWER_EUR,
            NOVELTY_SEEKING,
            DEPENDENT_JUDGMENT_MAKING,
            ENVIRONMENTAL_CONCERN,
            SHARE_1_2_HOUSE,
            HOUSE_OWNER,
            CONSTRUCTION_RATE,
            RENOVATION_RATE
    };

    public static final SpatialInformationPrinter PRINTER = new PrintIdAndAttribute("gid", "lid", ID);
}
