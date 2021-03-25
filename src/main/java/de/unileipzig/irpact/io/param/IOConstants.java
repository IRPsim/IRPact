package de.unileipzig.irpact.io.param;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public final class IOConstants {

    //=========================
    //tags
    //=========================

    public static final String ROOT = "ROOT";

    public static final String GENERAL_SETTINGS = "general_settings";
    public static final String LOGGING = "logging";
    public static final String SPECIAL_SETTINGS = "special_settings";
    public static final String BINARY_DATA = "binary_data";

    public static final String NAMES = "names";

    public static final String FILES = "files";
    public static final String PV_FILES = "pv_files";
    public static final String TABLE_FILES = "table_files";

    public static final String DISTRIBUTIONS = "distributions";
    public static final String BOOLEAN = "boolean";
    public static final String DIRAC = "dirac";
    public static final String RANDOM_BOUNDED_INTEGER = "random_bounded_integer";
    public static final String FINITE_MASSPOINTS_DISCRETE_DISTRIBUTION = "finite_masspoints_discrete_distribution";
    public static final String MASSPOINT = "masspoint";

    public static final String AGENTS = "agents";
    public static final String CONSUMER = "consumer";
    public static final String CONSUMER_GROUP = "consumer_group";
    public static final String CONSUMER_GROUP_ATTR_MAPPING = "consumer_group_attr_mapping";
    public static final String CONSUMER_GROUP_INTEREST_MAPPING = "consumer_group_interest_mapping";
    public static final String CONSUMER_GROUP_PRODUCT_FINDING_MAPPING = "consumer_group_product_finding_mapping";
    public static final String CONSUMER_GROUP_SPATIAL_DIST_MAPPING = "consumer_group_spatial_dist_mapping";
    public static final String CONSUMER_ATTR = "consumer_attr";
    public static final String CONSUMER_ATTR_NAME_MAPPING = "consumer_attr_name_mapping";
    public static final String CONSUMER_ATTR_DIST_MAPPING = "consumer_attr_dist_mapping";
    public static final String CONSUMER_AFFINITY = "consumer_affinity";
    public static final String CONSUMER_INTEREST = "consumer_interest";
    public static final String CONSUMER_INTEREST_THRESHOLD = "consumer_interest_threshold";

    public static final String NETWORK = "network";
    public static final String TOPOLOGY = "topology";
    public static final String TOPOLOGY_EMPTY = "topology_empty";
    public static final String TOPOLOGY_COMPLETE = "topology_complete";
    public static final String TOPOLOGY_FREE = "topology_free";
    public static final String TOPOLOGY_FREE_EDGECOUNT = "topology_free_edgecount";
    public static final String DIST_FUNC = "dist_func";
    public static final String DIST_FUNC_NO = "dist_func_no";
    public static final String DIST_FUNC_INVERSE = "dist_func_inverse";

    public static final String PRODUCTS = "products";
    public static final String PRODUCTS_GROUP = "products_group";
    public static final String PRODUCTS_GROUP_ATTR_MAPPING = "products_attr_mapping";
    public static final String PRODUCTS_ATTR = "products_attr";
    public static final String PRODUCTS_ATTR_NAME_MAPPING = "products_attr_name_mapping";
    public static final String PRODUCTS_ATTR_DIST_MAPPING = "products_attr_dist_mapping";
    public static final String PRODUCTS_INITIAL = "products_initial";
    public static final String PRODUCTS_INITIAL_ATTR_MAPPING = "products_initial_attr_mapping";
    public static final String PRODUCTS_INITIAL_ATTR = "products_initial_attr";
    public static final String PRODUCTS_FINDING_SCHEME = "products_finding_scheme";
    public static final String PRODUCTS_FINDING_SCHEME_INITIAL = "products_finding_scheme_initial";

    public static final String PROCESS_MODEL = "process_model";
    public static final String PROCESS_MODEL_RA = "process_model_ra";
    public static final String PROCESS_MODEL_RA_UNCERT = "process_model_ra_uncert";
    public static final String PROCESS_MODEL_RA_UNCERT_AUTO = "process_model_ra_uncert_auto";
    public static final String PROCESS_MODEL_RA_UNCERT_CUSTOM = "process_model_ra_uncert_custom";

    public static final String SPATIAL = "spatial";
    public static final String SPATIAL_FILE = "spatial_model_file";
    public static final String SPATIAL_MODEL = "spatial_model";
    public static final String SPATIAL_MODEL_SPACE2D = "spatial_model_space2d";
    public static final String SPATIAL_MODEL_DIST = "spatial_model_dist";
    public static final String SPATIAL_MODEL_DIST_FILE = "spatial_model_dist_file";
    public static final String SPATIAL_MODEL_DIST_FILE_CUSTOMPOS = "spatial_model_dist_file_custompos";
    public static final String SPATIAL_MODEL_DIST_FILE_CUSTOMPOS_INDEP = "spatial_model_dist_file_custompos_indep";
    public static final String SPATIAL_MODEL_DIST_FILE_CUSTOMPOS_SELECTED = "spatial_model_dist_file_custompos_selected";
    public static final String SPATIAL_MODEL_DIST_FILE_CUSTOMPOS_SELECTEDWEIGHTED = "spatial_model_dist_file_custompos_selectedweighted";
    public static final String SPATIAL_MODEL_DIST_FILE_FILEPOS = "spatial_model_dist_file_filepos";
    public static final String SPATIAL_MODEL_DIST_FILE_FILEPOS_INDEP = "spatial_model_dist_file_filepos_indep";
    public static final String SPATIAL_MODEL_DIST_FILE_FILEPOS_SELECTED = "spatial_model_dist_file_filepos_selected";
    public static final String SPATIAL_MODEL_DIST_FILE_FILEPOS_SELECTEDWEIGHTED = "spatial_model_dist_file_filepos_selectedweighted";

    public static final String TIME = "time";
    public static final String TIME_DISCRETE_MS = "time_discrete_ms";
    public static final String TIME_DISCRETE_UNIT = "time_discrete_unit";

    public static final String GRAPHVIZ = "graphviz";
    public static final String GRAPHVIZ_AGENT_COLOR_MAPPING = "graphviz_agent_color_mapping";

    public static final String SUBMODULE = "submodule";
    public static final String SUBMODULE_GRAPHVIZDEMO = "submodule_graphvizdemo";

    //=========================
    //yaml
    //=========================

    public static final String EDN_LABEL = "edn_label";
    public static final String EDN_DESCRIPTION = "edn_description";

    public static final String GAMS_IDENTIFIER = "gams_identifier";
    public static final String GAMS_DESCRIPTION = "gams_description";

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
