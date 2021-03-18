package de.unileipzig.irpact.io.spec;

import de.unileipzig.irpact.core.process.ra.RAConstants;

/**
 * @author Daniel Abitz
 */
public final class SpecificationConstants {

    public static final String JSON_EXTENSION_WITH_DOT = ".json";

    public static final String DIR_NONE = "";
    public static final String DIR_ConsumerAgentGroups = "ConsumerAgentGroups";
    public static final String DIR_Awareness = "Awareness";
    public static final String DIR_Distributions = "Distributions";
    public static final String DIR_SpatialDistributions = "SpatialDistributions";
    public static final String DIR_DistanceEvaluators = "DistanceEvaluators";
    public static final String DIR_ProductGroups = "ProductGroups";
    public static final String DIR_FixProducts = "FixProducts";
    public static final String DIR_ProductFindingSchemes = "ProductFindingSchemes";
    public static final String DIR_ProductInterestSupplyScheme = "ProductInterestSupplyScheme";
    public static final String DIR_Schemes = "Schemes";

    public static final String FILE_General = "GeneralSettings.json";
    public static final String FILE_Affinities = "ConsumerAgentGroupAffinities.json";
    public static final String FILE_SocialNetwork = "SocialNetwork.json";
    public static final String FILE_ProcessModel = "ProcessModel.json";
    public static final String FILE_SpatialModel = "SpatialModel.json";
    public static final String FILE_TimeModel = "TimeModel.json";
    public static final String FILE_BinaryData = "BinaryData.json";
    public static final String FILE_Files = "Files.json";

    public static final String TAG_numberOfAgents = "numberOfAgents";
    public static final String TAG_attributes = "attributes";
    public static final String TAG_name = "name";
    public static final String TAG_group = "group";
    public static final String TAG_type = "type";
    public static final String TAG_parameters = "parameters";
    public static final String TAG_parameter = "parameter";
    public static final String TAG_masspoints = "masspoints";
    public static final String TAG_value = "value";
    public static final String TAG_lowerBound = "lowerBound";
    public static final String TAG_upperBound = "upperBound";
    public static final String TAG_distribution = "distribution";
    public static final String TAG_spatialDistribution = "spatialDistribution";
    public static final String TAG_productFindingScheme = "productFindingScheme";
    public static final String TAG_informationAuthority = "informationAuthority";
    public static final String TAG_initialWeight = "initialWeight";
    public static final String TAG_numberOfTies = "numberOfTies";
    public static final String TAG_fixProducts = "fixProducts";
    public static final String TAG_fixProduct = "fixProduct";
    public static final String TAG_a = "a";
    public static final String TAG_b = "b";
    public static final String TAG_c = "c";
    public static final String TAG_d = "d";
    public static final String TAG_x = "x";
    public static final String TAG_y = "y";
    public static final String TAG_adopterPoints = "adopterPoints";
    public static final String TAG_interestedPoints = "interesetedPoints";
    public static final String TAG_awarePoints = "awarePoints";
    public static final String TAG_unknownPoints = "unknownPoints";
    public static final String TAG_orientation = "orientation";
    public static final String TAG_slope = "slope";
    public static final String TAG_euclid = "Euclid";
    public static final String TAG_manhatten = "Manhatten";
    public static final String TAG_timePerTickInMs = "timePerTickInMs";
    public static final String TAG_seed = "seed";
    public static final String TAG_timeout = "timeout";
    public static final String TAG_logLevel = "logLevel";
    public static final String TAG_logAll = "logAll";
    public static final String TAG_startYear = "startYear";
    public static final String TAG_endYear = "endYear";
    public static final String TAG_version = "version";
    public static final String TAG_interest = "interest";
    public static final String TAG_distanceEvaluator = "distanceEvaluator";
    public static final String TAG_id = "id";
    public static final String TAG_content = "content";
    public static final String TAG_list = "list";
    public static final String TAG_binary = "binary";
    public static final String TAG_pvFiles = "pvFiles";
    public static final String TAG_spatialTableFiles = "spatialTableFiles";
    public static final String TAG_metric = "metric";
    public static final String TAG_file = "file";
    public static final String TAG_logisticFactor = "file";
    public static final String TAG_selectBy = "selectBy";
    public static final String TAG_groupBy = "groupBy";
    public static final String TAG_topology = "topology";
    public static final String TAG_spatialTableFile = "spatialTable";
    public static final String TAG_attribute = "attribute";
    public static final String TAG_consumerAgentGroup = "consumerAgentGroup";
    public static final String TAG_consumerAgentGroups = "consumerAgentGroups";
    public static final String TAG_uncertainties = "uncertainties";
    public static final String TAG_uncertainty = "uncertainty";
    public static final String TAG_convergence = "convergence";

    public static final String TAG_NOVELTY_SEEKING = RAConstants.NOVELTY_SEEKING;
    public static final String TAG_INDEPENDENT_JUDGMENT_MAKING = RAConstants.DEPENDENT_JUDGMENT_MAKING;
    public static final String TAG_ENVIRONMENTAL_CONCERN = RAConstants.ENVIRONMENTAL_CONCERN;
    public static final String TAG_FINANCIAL_THRESHOLD = RAConstants.FINANCIAL_THRESHOLD;
    public static final String TAG_ADOPTION_THRESHOLD = RAConstants.ADOPTION_THRESHOLD;
    public static final String TAG_COMMUNICATION_FREQUENCY_SN = RAConstants.COMMUNICATION_FREQUENCY_SN;
    public static final String TAG_REWIRING_RATE = RAConstants.REWIRING_RATE;
    public static final String TAG_INITIAL_ADOPTER = RAConstants.INITIAL_ADOPTER;
    public static final String TAG_INTEREST_THRESHOLD = RAConstants.INTEREST_THRESHOLD;

    private SpecificationConstants() {
    }
}
