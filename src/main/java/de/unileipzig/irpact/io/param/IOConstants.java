package de.unileipzig.irpact.io.param;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public final class IOConstants {

    public static final String ROOT = "ROOT";

    public static final String INFORMATIONS = "informations";
    public static final String INFORMATIONS_OUT = "informations_out";

    public static final String SETTINGS = "settings";
    public static final String GENERAL_SETTINGS = "general_settings";
    public static final String LOGGING = "logging";
    public static final String SPECIAL_SETTINGS = "special_settings";
    public static final String RESULT_VISUALISATION = "result_visualisation";
    public static final String RESULT_DATA2 = "result_data2";
    public static final String RESULT_VISUALISATION2 = "result_visualisation2";
    public static final String NETWORK_VISUALISATION = "network_visualisation";
    public static final String DATA_OUTPUT = "data_output";
    public static final String COLOR_SETTINGS = "color_settings";

    public static final String SPECIAL_INPUT = "special_input";
    public static final String SPECIAL_INPUT_PVACT = "special_input_pvact";
    public static final String SPECIAL_INPUT_PVACT_CONSTRATE = "special_input_pvact_constrate";
    public static final String SPECIAL_INPUT_PVACT_RENORATE = "special_input_pvact_renorate";

    public static final String FILES = "files";

    public static final String DISTRIBUTIONS = "distributions";

    public static final String AGENTS = "agents";
    public static final String CONSUMER = "consumer";
    public static final String CONSUMER_GROUP = "consumer_group";
    public static final String CONSUMER_ATTR = "consumer_attr";
    public static final String CONSUMER_AFFINITY = "consumer_affinity";
    public static final String CONSUMER_INTEREST = "consumer_interest";
    public static final String POPULATION = "population";

    public static final String NETWORK = "network";
    public static final String TOPOLOGY = "topology";
    public static final String DIST_FUNC = "dist_func";

    public static final String PRODUCTS = "products";
    public static final String PRODUCTS_GROUP = "products_group";
    public static final String PRODUCTS_ATTR = "products_attr";
    public static final String PRODUCTS_FINDING_SCHEME = "products_finding_scheme";
    public static final String INITAL_ADOPTERS = "initial_adopters";

    public static final String PROCESS_MODEL = "process_model";
    public static final String PROCESS_MODULAR_COMPONENTS = "process_modular_components";
    public static final String PROCESS_MODULAR2 = "process_modular2";
    public static final String PROCESS_MODULAR2_MODEL = "process_modular2_model";
    public static final String PROCESS_MODULAR2_COMPONENTS = "process_modular2_components";
    public static final String PROCESS_MODULAR2_COMPONENTS_CALC = "process_modular2_components_calc";
    public static final String PROCESS_MODULAR2_COMPONENTS_EVAL = "process_modular2_components_eval";

    public static final String PROCESS_MODEL4 = "process_model4";
    public static final String PROCESS_MODEL4_UNCERT = "process_model4_uncertainty";
    public static final String PROCESS_MODEL4_DISTANCE = "process_model4_distance";
    public static final String PROCESS_MODEL4_INIT = "process_model4_init";
    public static final String PROCESS_MODEL4_REEVAL = "process_model4_reeval";
    public static final String PROCESS_MODEL4_GENERALMODULES = "process_model4_generalmodules";
    public static final String PROCESS_MODEL4_GENERALMODULES_ACTION = "process_model4_generalmodules_action";
    public static final String PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT = "process_model4_generalmodules_numberinput";
    public static final String PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL = "process_model4_generalmodules_numbereval";
    public static final String PROCESS_MODEL4_GENERALMODULES_BOOL = "process_model4_generalmodules_bool";
    public static final String PROCESS_MODEL4_GENERALMODULES_SYSTEM = "process_model4_generalmodules_system";
    public static final String PROCESS_MODEL4_GENERALMODULES_INDEPENDENT = "process_model4_generalmodules_independent";
    public static final String PROCESS_MODEL4_PVACTMODULES = "process_model4_pvactmodules";
    public static final String PROCESS_MODEL4_PVACTMODULES_ACTION = "process_model4_pvactmodules_action";
    public static final String PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT = "process_model4_pvactmodules_numberinput";
    public static final String PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING = "process_model4_pvactmodules_numberlogging";
    public static final String PROCESS_MODEL4_PVACTMODULES_PVGENERAL = "process_model4_pvactmodules_pvgeneral";
    public static final String PROCESS_MODEL4_PVACTMODULES_PVLOGGING = "process_model4_pvactmodules_pvlogging";

    public static final String SPATIAL = "spatial";
    public static final String SPATIAL_MODEL = "spatial_model";
    public static final String SPATIAL_MODEL_DIST = "spatial_model_dist";
    public static final String SPATIAL_MODEL_DIST_FILE = "spatial_model_dist_file";
    public static final String SPATIAL_MODEL_DIST_FILE_CUSTOMPOS = "spatial_model_dist_file_custompos";
    public static final String SPATIAL_MODEL_DIST_FILE_FILEPOS = "spatial_model_dist_file_filepos";

    public static final String TIME = "time";

    public static final String GRAPHVIZ = "graphviz";
    public static final String GRAPHVIZ_GENERAL = "graphviz_general";
    public static final String GRAPHVIZ_AGENT_COLOR_MAPPING = "graphviz_agent_color_mapping";

    public static final String SUBMODULE = "submodule";
    public static final String SUBMODULE_GRAPHVIZDEMO = "submodule_graphvizdemo";

    public static final String DEV = "dev";
    public static final String TEST = "test_data";
    public static final String DEPRECATED = "deprecated";

    public static final String EDN_LABEL = "edn_label";
    public static final String EDN_DESCRIPTION = "edn_description";
    public static final String EDN_DELTA = "edn_delta";

    public static final String GAMS_IDENTIFIER = "gams_identifier";
    public static final String GAMS_DESCRIPTION = "gams_description";
    public static final String GAMS_HIDDEN = "gams_hidden";
    public static final String GAMS_UNIT = "gams_unit";
    public static final String GAMS_DOMAIN = "gams_domain";
    public static final String GAMS_DEFAULT = "gams_default";

    public static final String GRAPH_EDGEHEADING = "graph_edgeheading";
    public static final String GRAPH_COLORHEADING = "graph_colorheading";
    public static final String GRAPH_BORDERHEADING = "graph_borderheading";
    public static final String GRAPH_SHAPEHEADING = "graph_shapeheading";
    public static final String GRAPH_ICONHEADING = "graph_iconheading";

    public static final String GRAPHNODE_COLORLABEL = "graphnode_colorlabel";
    public static final String GRAPHNODE_BORDERLABEL = "graphnode_borderlabel";
    public static final String GRAPHNODE_SHAPELABEL = "graphnode_shapelabel";

    public static final String GRAPHEDGE_LABEL = "graphedge_label";

    private IOConstants() {
    }

    public static void validateNames() {
        try {
            Field[] fields = IOConstants.class.getDeclaredFields();
            Set<String> fieldNames = new HashSet<>();
            for(Field field: fields) {
                String value = (String) field.get(null);
                if(!fieldNames.add(value)) {
                    throw new IllegalArgumentException("name '" + value + "' already exists (field: '" + field.getName() + "')");
                }
            }
            System.out.println("tested " + fieldNames.size() + " fields");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
