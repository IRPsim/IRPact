package de.unileipzig.irpact.io.spec;

/**
 * @author Daniel Abitz
 */
public final class SpecificationConstants {

    public static final String JSON_EXTENSION = ".json";

    public static final String DIR_NONE = "";
    public static final String DIR_ConsumerAgentGroups = "ConsumerAgentGroups";
    public static final String DIR_Awareness = "Awareness";
    public static final String DIR_Distributions = "Distributions";
    public static final String DIR_SpatialDistributions = "SpatialDistributions";
    public static final String DIR_DistanceEvaluators = "DistanceEvaluators";
    public static final String DIR_ProductGroups = "ProductGroups";

    public static final String FILE_General = "GeneralSettings.json";
    public static final String FILE_Affinities = "ConsumerAgentGroupAffinities.json";
    public static final String FILE_SocialNetwork = "SocialNetwork.json";
    public static final String FILE_ProcessModel = "ProcessModel.json";
    public static final String FILE_SpatialModel = "SpatialModel.json";
    public static final String FILE_TimeModel = "TimeModel.json";

    public static final String TAG_numberOfAgents = "numberOfAgents";
    public static final String TAG_attributes = "attributes";
    public static final String TAG_name = "name";
    public static final String TAG_type = "type";
    public static final String TAG_parameters = "parameters";
    public static final String TAG_value = "value";
    public static final String TAG_lowerBound = "lowerBound";
    public static final String TAG_upperBound = "upperBound";
    public static final String TAG_distribution = "distribution";
    public static final String TAG_spatialDistribution = "spatialDistribution";
    public static final String TAG_initialWeight = "initialWeight";
    public static final String TAG_numberOfTies = "numberOfTies";
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
    public static final String TAG_startYear = "startYear";
    public static final String TAG_endYear = "endYear";
    public static final String TAG_version = "version";
    public static final String TAG_awareness = "awareness";
    public static final String TAG_distanceEvaluator = "distanceEvaluator";

    private SpecificationConstants() {
    }
}
