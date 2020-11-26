package de.unileipzig.irpact.v2.io.input2;

import de.unileipzig.irptools.util.MultiAnnotationResource;

/**
 * @author Daniel Abitz
 */
public class InputResource extends MultiAnnotationResource {

    public static final String ICONSUMERAGENTGROUP_LABEL = "IConsumerAgentGroup_label";
    public static final String ICONSUMERAGENTGROUP_PRIORITIES = "IConsumerAgentGroup_priorities";
    public static final String ICONSUMERAGENTGROUP_DESCRIPTION = "IConsumerAgentGroup_description";
    public static final String ICONSUMERAGENTGROUP_CONSUMERAGENTINFORMATIONAUTHORITY = "IConsumerAgentGroup_consumerAgentInformationAuthority";
    public static final String ICONSUMERAGENTGROUP_NUMBEROFCONSUMERAGENTS = "IConsumerAgentGroup_numberOfConsumerAgents";

    public static final String IWATTSSTROGATZMODEL_LABEL = "IWattsStrogatzModel_label";
    public static final String IWATTSSTROGATZMODEL_PRIORITIES = "IWattsStrogatzModel_priorities";
    public static final String IWATTSSTROGATZMODEL_DESCRIPTION = "IWattsStrogatzModel_description";
    public static final String IWATTSSTROGATZMODEL_WSMK = "IWattsStrogatzModel_wsmK";
    public static final String IWATTSSTROGATZMODEL_WSMBETA = "IWattsStrogatzModel_wsmBeta";
    public static final String IWATTSSTROGATZMODEL_WSMSELFREFERENTIAL = "IWattsStrogatzModel_wsmSelfReferential";
    public static final String IWATTSSTROGATZMODEL_WSMSEED = "IWattsStrogatzModel_wsmSeed";

    public static final String IFREEMULTIGRAPHTOPOLOGY_LABEL = "IFreeMultiGraphTopology_label";
    public static final String IFREEMULTIGRAPHTOPOLOGY_PRIORITIES = "IFreeMultiGraphTopology_priorities";
    public static final String IFREEMULTIGRAPHTOPOLOGY_DESCRIPTION = "IFreeMultiGraphTopology_description";
    public static final String IFREEMULTIGRAPHTOPOLOGY_FTEDGECOUNT = "IFreeMultiGraphTopology_ftEdgeCount";
    public static final String IFREEMULTIGRAPHTOPOLOGY_FTSELFREFERENTIAL = "IFreeMultiGraphTopology_ftSelfReferential";
    public static final String IFREEMULTIGRAPHTOPOLOGY_FTSEED = "IFreeMultiGraphTopology_ftSeed";

    //=========================
    //...
    //=========================

    public static final InputResource DEFAULT = new InputResource().initDefault();
    public static final InputResource GAMS_ONLY = new InputResource().initGams();
    public static final InputResource EDN_ONLY = new InputResource().initEdn();

    public InputResource() {
    }

    public InputResource initDefault() {
        initGams();
        initEdn();
        return this;
    }

    public InputResource initGams() {
//        checkedPut(ICONSUMERAGENTGROUP_CONSUMERAGENTINFORMATIONAUTHORITY, "Todo");
//        checkedPut(ICONSUMERAGENTGROUP_NUMBEROFCONSUMERAGENTS, "Todo");
//
//        checkedPut(IWATTSSTROGATZMODEL_WSMK, "Todo");
//        checkedPut(IWATTSSTROGATZMODEL_WSMBETA, "Todo");
//        checkedPut(IWATTSSTROGATZMODEL_WSMSELFREFERENTIAL, "Todo");
//        checkedPut(IWATTSSTROGATZMODEL_WSMSEED, "Todo");
//
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTEDGECOUNT, "Todo");
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTSELFREFERENTIAL, "Todo");
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_FTSEED, "Todo");

        return this;
    }

    public InputResource initEdn() {
//        checkedPut(ICONSUMERAGENTGROUP_LABEL, "Todo");
//        checkedPut(ICONSUMERAGENTGROUP_PRIORITIES, "Todo");
//        checkedPut(ICONSUMERAGENTGROUP_DESCRIPTION, "Todo");
//
//        checkedPut(IWATTSSTROGATZMODEL_LABEL, "Todo");
//        checkedPut(IWATTSSTROGATZMODEL_PRIORITIES, "Todo");
//        checkedPut(IWATTSSTROGATZMODEL_DESCRIPTION, "Todo");
//
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_LABEL, "Todo");
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_PRIORITIES, "Todo");
//        checkedPut(IFREEMULTIGRAPHTOPOLOGY_DESCRIPTION, "Todo");

        return this;
    }
}
