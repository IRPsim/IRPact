package de.unileipzig.irpact.core.logging;

/**
 * @author Daniel Abitz
 */
public final class InfoTag {

    public static final String NON_FATAL_ERROR = "[NFE]";
    public static final String FATAL_ERROR = "[FE]";
    public static final String RESULT = "[RESULT]";

    //data logging
    public static final String PVACT = "[PVact]";
    public static final String RELATIVE_AGREEMENT = "[RA]";
    public static final String INTEREST_UPDATE = "[IU]";
    public static final String GRAPH_UPDATE = "[GU]";
    public static final String SHARE_NETORK_LOCAL = "[SNL]";
    public static final String FINANCAL_COMPONENT = "[FC]";
    public static final String DECISION_MAKING = "[DM]";

    //result logging
    public static final String RESULT_ZIP_ADOPTIONS = "ZIP_ADOPTIONS";
    public static final String RESULT_ZIP_PHASE_ADOPTIONS = "ZIP_PHASE_ADOPTIONS";
    public static final String RESULT_EXACT_ADOPTIONS = "ALL_ADOPTIONS";

    //script logging
    public static final String SCRIPT_ZIP_ADOPTIONS = "ZIP_SCRIPT";
    public static final String SCRIPT_ZIP_ADOPTIONS_DATA = "ZIP_SCRIPT_DATA";
    public static final String SCRIPT_ZIP_PHASE_ADOPTIONS = "ZIP_PHASE_SCRIPT";
    public static final String SCRIPT_ZIP_PHASE_ADOPTIONS_DATA = "ZIP_PHASE_SCRIPT_DATA";

    private InfoTag() {
    }
}
