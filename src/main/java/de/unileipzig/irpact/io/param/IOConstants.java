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
    public static final String NETWORK_VISUALISATION = "network_visualisation";
    public static final String DATA_OUTPUT = "data_output";

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

    public static final String PROCESS_MODEL = "process_model";
    public static final String PROCESS_FILTER = "process_filter";
    public static final String PROCESS_MODEL_RA_UNCERT = "process_model_ra_uncert";
    public static final String PROCESS_MODULAR_COMPONENTS = "process_modular_components";
    public static final String PROCESS_MODULAR2 = "process_modular2";
    public static final String PROCESS_MODULAR2_MODEL = "process_modular2_model";
    public static final String PROCESS_MODULAR2_COMPONENTS = "process_modular2_components";
    public static final String PROCESS_MODULAR2_COMPONENTS_CALC = "process_modular2_components_calc";
    public static final String PROCESS_MODULAR2_COMPONENTS_EVAL = "process_modular2_components_eval";

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


    public static final String EDN_LABEL = "edn_label";
    public static final String EDN_DESCRIPTION = "edn_description";
    public static final String EDN_DELTA = "edn_delta";

    public static final String GAMS_IDENTIFIER = "gams_identifier";
    public static final String GAMS_DESCRIPTION = "gams_description";
    public static final String GAMS_HIDDEN = "gams_hidden";
    public static final String GAMS_UNIT = "gams_unit";
    public static final String GAMS_DOMAIN = "gams_domain";
    public static final String GAMS_DEFAULT = "gams_default";

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
