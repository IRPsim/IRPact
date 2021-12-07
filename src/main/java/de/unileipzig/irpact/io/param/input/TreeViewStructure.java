package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.io.param.EdnPath;
import de.unileipzig.irpact.io.param.IOConstants;
import de.unileipzig.irpact.io.param.ListEdnPath;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.color.InColorARGB;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.io.param.input.distribution.*;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.interest.InProductGroupThresholdEntry;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irpact.io.param.input.postdata.InNeighbourhoodOverview;
import de.unileipzig.irpact.io.param.input.process.ra.InDisabledNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InEntireNetworkNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InMaxDistanceNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactIndividualGlobalModerateExtremistUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactUpdatableGlobalModerateExtremistUncertainty;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InCommunicationModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InIfElseActionModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InNOPModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.InRewireModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InBernoulliModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InThresholdReachedModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InCsvValueLoggingModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval.InRunUntilFailureModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.logging.InPhaseLoggingModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval.InReevaluatorModule3;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InUncertaintySupplierInitializer;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca.*;
import de.unileipzig.irpact.io.param.input.process2.modular.models.ca.InBasicCAModularProcessModel;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactAttributeBasedInitialAdoption;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.*;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InROutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result2.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Daniel Abitz
 */
@Deprecated
public final class TreeViewStructure {

    private static final List<EdnPath> ALL_PATHS = new ArrayList<>();
    public static final List<EdnPath> PATHS = Collections.unmodifiableList(ALL_PATHS);

    public static final EdnPath ROOT = new ListEdnPath(IOConstants.ROOT).addTo(ALL_PATHS);

    public static final EdnPath INFO = resolve(ROOT, IOConstants.INFORMATIONS);
    public static final EdnPath INFO_ABOUTIRPACT = resolve(INFO, InIRPactVersion.class);
    public static final EdnPath INFO_ABOUTSCENARIO = resolve(INFO, InScenarioVersion.class);
    public static final EdnPath INFO_INFO = resolve(INFO, InInformation.class);

    public static final EdnPath SETT = resolve(ROOT, IOConstants.SETTINGS);
    public static final EdnPath SETT_GENERAL = resolve(SETT, IOConstants.GENERAL_SETTINGS);
    public static final EdnPath SETT_GENERAL_LOG = resolve(SETT_GENERAL, IOConstants.LOGGING);
    public static final EdnPath SETT_SPECIAL = resolve(SETT, IOConstants.SPECIAL_SETTINGS);
    public static final EdnPath SETT_SPECIAL_BIN = resolve(SETT_SPECIAL, VisibleBinaryData.class);

    public static final EdnPath SETT_RESULT2 = resolve(SETT, IOConstants.RESULT_DATA2);
    public static final EdnPath SETT_RESULT2_BUCKET = resolve(SETT_RESULT2, InBucketAnalyser.class);
    public static final EdnPath SETT_RESULT2_NEIGHBORS = resolve(SETT_RESULT2, InNeighbourhoodOverview.class);

    public static final EdnPath SETT_VISURESULT2 = resolve(SETT, IOConstants.RESULT_VISUALISATION2);
    public static final EdnPath SETT_VISURESULT2_CUSTOMAVGQUANTIL = resolve(SETT_VISURESULT2, InCustomAverageQuantilRangeImage.class);
    public static final EdnPath SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE = resolve(SETT_VISURESULT2_CUSTOMAVGQUANTIL, InQuantileRange.class);
    public static final EdnPath SETT_VISURESULT2_SPECIALAVGQUANTIL = resolve(SETT_VISURESULT2, InSpecialAverageQuantilRangeImage.class);
    public static final EdnPath SETT_VISURESULT2_COMPARED = resolve(SETT_VISURESULT2, InComparedAnnualImage.class);
    public static final EdnPath SETT_VISURESULT2_COMPAREDZIP = resolve(SETT_VISURESULT2, InComparedAnnualZipImage.class);
    public static final EdnPath SETT_VISURESULT2_ADOPTIONPHASE = resolve(SETT_VISURESULT2, InAdoptionPhaseOverviewImage.class);
    public static final EdnPath SETT_VISURESULT2_PROCESSPHASE = resolve(SETT_VISURESULT2, InProcessPhaseOverviewImage.class);
    public static final EdnPath SETT_VISURESULT2_INTEREST = resolve(SETT_VISURESULT2, InInterestOverviewImage.class);
    public static final EdnPath SETT_VISURESULT2_ANNUALBUCKET = resolve(SETT_VISURESULT2, InAnnualBucketImage.class);

    public static final EdnPath SETT_VISUNETWORK = resolve(SETT, IOConstants.NETWORK_VISUALISATION);
    public static final EdnPath SETT_VISUNETWORK_GENERAL = resolve(SETT_VISUNETWORK, InGraphvizGeneral.class);
    public static final EdnPath SETT_VISUNETWORK_AGENTCOLOR = resolve(SETT_VISUNETWORK, IOConstants.GRAPHVIZ_AGENT_COLOR_MAPPING);

    public static final EdnPath SETT_COLOR = resolve(SETT, IOConstants.COLOR_SETTINGS);
    public static final EdnPath SETT_COLOR_PALETTE = resolve(SETT_COLOR, InColorPalette.class);
    public static final EdnPath SETT_COLOR_ARGB = resolve(SETT_COLOR, InColorARGB.class);

    public static final EdnPath SPECIALINPUT = resolve(ROOT, IOConstants.SPECIAL_INPUT);
    public static final EdnPath SPECIALINPUT_PVACT = resolve(SPECIALINPUT, IOConstants.SPECIAL_INPUT_PVACT);
    public static final EdnPath SPECIALINPUT_PVACT_CONSTRATE = resolve(SPECIALINPUT_PVACT, IOConstants.SPECIAL_INPUT_PVACT_CONSTRATE);
    public static final EdnPath SPECIALINPUT_PVACT_RENORATE = resolve(SPECIALINPUT_PVACT, IOConstants.SPECIAL_INPUT_PVACT_RENORATE);

    public static final EdnPath ATTRNAMES = resolve(ROOT, InAttributeName.class);

    public static final EdnPath FILES = resolve(ROOT, IOConstants.FILES);
    public static final EdnPath FILES_PV = resolve(FILES, InPVFile.class);
    public static final EdnPath FILES_REALADOPTION = resolve(FILES, InRealAdoptionDataFile.class);
    public static final EdnPath FILES_SPATIAL = resolve(FILES, InSpatialTableFile.class);

    public static final EdnPath DISTRIBUTIONS = resolve(ROOT, IOConstants.DISTRIBUTIONS);
    public static final EdnPath DISTRIBUTIONS_BERNOULLI = resolve(DISTRIBUTIONS, InBernoulliDistribution.class);
    public static final EdnPath DISTRIBUTIONS_BOOLEAN = resolve(DISTRIBUTIONS, InBooleanDistribution.class);
    public static final EdnPath DISTRIBUTIONS_DIRAC = resolve(DISTRIBUTIONS, InDiracUnivariateDistribution.class);
    public static final EdnPath DISTRIBUTIONS_MP = resolve(DISTRIBUTIONS, InFiniteMassPointsDiscreteDistribution.class);
    public static final EdnPath DISTRIBUTIONS_MP_MP = resolve(DISTRIBUTIONS_MP, InMassPoint.class);
    public static final EdnPath DISTRIBUTIONS_NORM = resolve(DISTRIBUTIONS, InNormalDistribution.class);
    public static final EdnPath DISTRIBUTIONS_BOUNDNORM = resolve(DISTRIBUTIONS, InBoundedNormalDistribution.class);
    public static final EdnPath DISTRIBUTIONS_SLOWTRUNCNORM = resolve(DISTRIBUTIONS, InSlowTruncatedNormalDistribution.class);
    public static final EdnPath DISTRIBUTIONS_TRUNCNORM = resolve(DISTRIBUTIONS, InTruncatedNormalDistribution.class);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIDOUBLE = resolve(DISTRIBUTIONS, InBoundedUniformDoubleDistribution.class);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIINT = resolve(DISTRIBUTIONS, InBoundedUniformIntegerDistribution.class);

    public static final EdnPath AGENTS = resolve(ROOT, IOConstants.AGENTS);
    public static final EdnPath AGENTS_CONSUMER = resolve(AGENTS, IOConstants.CONSUMER);
    public static final EdnPath AGENTS_CONSUMER_GROUP = resolve(AGENTS_CONSUMER, IOConstants.CONSUMER_GROUP);
    public static final EdnPath AGENTS_CONSUMER_GROUP_GENERAL = resolve(AGENTS_CONSUMER_GROUP, InGeneralConsumerAgentGroup.class);
    public static final EdnPath AGENTS_CONSUMER_GROUP_PVACT = resolve(AGENTS_CONSUMER_GROUP, InPVactConsumerAgentGroup.class);
    public static final EdnPath AGENTS_CONSUMER_ATTR = resolve(AGENTS_CONSUMER, IOConstants.CONSUMER_ATTR);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALGRP = resolve(AGENTS_CONSUMER_ATTR, InGeneralConsumerAgentGroupAttribute.class);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALANNUAL = resolve(AGENTS_CONSUMER_ATTR, InGeneralConsumerAgentAnnualGroupAttribute.class);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITGRP = resolve(AGENTS_CONSUMER_ATTR, InNameSplitConsumerAgentGroupAttribute.class);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITANNUAL = resolve(AGENTS_CONSUMER_ATTR, InNameSplitConsumerAgentAnnualGroupAttribute.class);
    public static final EdnPath AGENTS_CONSUMER_AFF = resolve(AGENTS_CONSUMER, IOConstants.CONSUMER_AFFINITY);
    public static final EdnPath AGENTS_CONSUMER_AFF_GRP = resolve(AGENTS_CONSUMER_AFF, InAffinities.class);
    public static final EdnPath AGENTS_CONSUMER_AFF_COMPLEX = resolve(AGENTS_CONSUMER_AFF, InComplexAffinityEntry.class);
    public static final EdnPath AGENTS_CONSUMER_AFF_NAMESPLIT = resolve(AGENTS_CONSUMER_AFF, InNameSplitAffinityEntry.class);
    public static final EdnPath AGENTS_CONSUMER_INTEREST = resolve(AGENTS_CONSUMER, IOConstants.CONSUMER_INTEREST);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD = resolve(AGENTS_CONSUMER_INTEREST, InProductThresholdInterestSupplyScheme.class);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY = resolve(AGENTS_CONSUMER_INTEREST_THRESHOLD, InProductGroupThresholdEntry.class);
    public static final EdnPath AGENTS_POP = resolve(AGENTS, IOConstants.POPULATION);
    public static final EdnPath AGENTS_POP_FIX = resolve(AGENTS_POP, InFixConsumerAgentPopulation.class);
    public static final EdnPath AGENTS_POP_FILE = resolve(AGENTS_POP, InFileBasedConsumerAgentPopulation.class);
    public static final EdnPath AGENTS_POP_FILEPVACT = resolve(AGENTS_POP, InFileBasedPVactConsumerAgentPopulation.class);

    public static final EdnPath NETWORK = resolve(ROOT, IOConstants.NETWORK);
    public static final EdnPath NETWORK_TOPO = resolve(NETWORK, IOConstants.TOPOLOGY);
    public static final EdnPath NETWORK_TOPO_UNLINKED = resolve(NETWORK_TOPO, InUnlinkedGraphTopology.class);
    public static final EdnPath NETWORK_TOPO_COMPLETE = resolve(NETWORK_TOPO, InCompleteGraphTopology.class);
    public static final EdnPath NETWORK_TOPO_FREE = resolve(NETWORK_TOPO, InFreeNetworkTopology.class);
    public static final EdnPath NETWORK_TOPO_FREE_TIES = resolve(NETWORK_TOPO_FREE, InNumberOfTies.class);
    public static final EdnPath NETWORK_DISTFUNC = resolve(NETWORK, IOConstants.DIST_FUNC);
    public static final EdnPath NETWORK_DISTFUNC_NO = resolve(NETWORK, InNoDistance.class);
    public static final EdnPath NETWORK_DISTFUNC_INVERSE = resolve(NETWORK, InInverse.class);

    public static final EdnPath PRODUCTS = resolve(ROOT, IOConstants.PRODUCTS);
    public static final EdnPath PRODUCTS_GROUP = resolve(PRODUCTS, IOConstants.PRODUCTS_GROUP);
    public static final EdnPath PRODUCTS_GROUP_BASIC = resolve(PRODUCTS_GROUP, InBasicProductGroup.class);
    public static final EdnPath PRODUCTS_ATTR = resolve(PRODUCTS, IOConstants.PRODUCTS_ATTR);
    public static final EdnPath PRODUCTS_ATTR_BASIC = resolve(PRODUCTS_ATTR, InBasicProductGroupAttribute.class);
    public static final EdnPath PRODUCTS_ATTR_SPLIT = resolve(PRODUCTS_ATTR, InNameSplitProductGroupAttribute.class);
    public static final EdnPath PRODUCTS_FIX = resolve(PRODUCTS, InFixProduct.class);
    public static final EdnPath PRODUCTS_FIXATTR = resolve(PRODUCTS, InFixProductAttribute.class);
    public static final EdnPath PRODUCTS_FINDSCHE = resolve(PRODUCTS, IOConstants.PRODUCTS_FINDING_SCHEME);
    public static final EdnPath PRODUCTS_FINDSCHE_FIX = resolve(PRODUCTS_FINDSCHE, InFixProductFindingScheme.class);

    public static final EdnPath PRODUCTS_INITADOPT = resolve(PRODUCTS, IOConstants.INITAL_ADOPTERS);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTATTRBASED = resolve(PRODUCTS_INITADOPT, InPVactAttributeBasedInitialAdoption.class);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTFILECAGBASED = resolve(PRODUCTS_INITADOPT, InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData.class);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTFILEWEIGHTEDCAGBASED = resolve(PRODUCTS_INITADOPT, InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData.class);

    //process model
    public static final EdnPath PROCESS_MODULAR4 = resolve(ROOT, IOConstants.PROCESS_MODEL4);
    //model
    public static final EdnPath PROCESS_MODULAR4_BASE = resolve(PROCESS_MODULAR4, InBasicCAModularProcessModel.class);
    //uncert
    public static final EdnPath PROCESS_MODEL4_UNCERT = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_UNCERT);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTGMEU = resolve(PROCESS_MODEL4_UNCERT, InPVactUpdatableGlobalModerateExtremistUncertainty.class);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTIMEU = resolve(PROCESS_MODEL4_UNCERT, InPVactIndividualGlobalModerateExtremistUncertaintySupplier.class);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTMEUOP = resolve(PROCESS_MODEL4_UNCERT, InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion.class);
    //peer
    public static final EdnPath PROCESS_MODEL4_DISTANCE = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_DISTANCE);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_DISABLED = resolve(PROCESS_MODEL4_DISTANCE, InDisabledNodeFilterDistanceScheme.class);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_ENTIRE = resolve(PROCESS_MODEL4_DISTANCE, InEntireNetworkNodeFilterDistanceScheme.class);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_MAX = resolve(PROCESS_MODEL4_DISTANCE, InMaxDistanceNodeFilterDistanceScheme.class);
    //init
    public static final EdnPath PROCESS_MODEL4_INIT = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_INIT);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_AGENTATTR = resolve(PROCESS_MODEL4_INIT, InAgentAttributeScaler.class);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_LINPERATTR = resolve(PROCESS_MODEL4_INIT, InLinearePercentageAgentAttributeScaler.class);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_UNCERT = resolve(PROCESS_MODEL4_INIT, InUncertaintySupplierInitializer.class);
    //public static final EdnPath PROCESS_MODULAR3_HANDLER_NEWPRODUCT = PROCESS_MODULAR3_HANDLER.resolve(IOConstants.PROCESS_MODULAR3_HANDLER_NEWPRODUCT).addTo(PATHS);
    //reeval
    public static final EdnPath PROCESS_MODEL4_REEVAL = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_REEVAL);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_LINKER = resolve(PROCESS_MODEL4_REEVAL, InMultiReevaluator.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_INTEREST = resolve(PROCESS_MODEL4_REEVAL, InAnnualInterestLogger.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_UNCERT = resolve(PROCESS_MODEL4_REEVAL, InUncertaintyReevaluator.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_CONSTRENO = resolve(PROCESS_MODEL4_REEVAL, InConstructionRenovationUpdater.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_DEC = resolve(PROCESS_MODEL4_REEVAL, InDecisionMakingReevaluator.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_IMPEDEDRESET = resolve(PROCESS_MODEL4_REEVAL, InImpededResetter.class);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_LINPERUPDATER = resolve(PROCESS_MODEL4_REEVAL, InLinearePercentageAgentAttributeUpdater.class);
    //general modules
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_GENERALMODULES);
    //action
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_ACTION);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE = resolve(PROCESS_MODEL4_GENERALMODULES_ACTION, InIfElseActionModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION_NOP = resolve(PROCESS_MODEL4_GENERALMODULES_ACTION, InNOPModule3.class);
    //bool
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_BOOL);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL_BERNOULLI = resolve(PROCESS_MODEL4_GENERALMODULES_BOOL, InBernoulliModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD = resolve(PROCESS_MODEL4_GENERALMODULES_BOOL, InThresholdReachedModule3.class);
    //number input
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_VALUE = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InValueModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_NAN = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InNaNModule3.class);
    //number eval
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_MULSCALAR = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InMulScalarModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_ADDSCALAR = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InAddScalarModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InLogisticModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_SUM = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InSumModule3.class);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_PRODUCT = resolve(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InProductModule3.class);
    //system
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_SYSTEM = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_SYSTEM);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL = resolve(PROCESS_MODEL4_GENERALMODULES_SYSTEM, InRunUntilFailureModule3.class);
    //independent
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_INDEPENDENT = resolve(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_INDEPENDENT);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL = resolve(PROCESS_MODEL4_GENERALMODULES_INDEPENDENT, InReevaluatorModule3.class);
    //general modules
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES = resolve(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_PVACTMODULES);
    //action
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION = resolve(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_ACTION);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION_COMMU = resolve(PROCESS_MODEL4_PVACTMODULES_ACTION, InCommunicationModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION_REWIRE = resolve(PROCESS_MODEL4_PVACTMODULES_ACTION, InRewireModule3.class);
    //number input
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT = resolve(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_ATTR = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InAttributeInputModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_GLOBALAVGNPV = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InGlobalAvgNPVModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_NPV = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InNPVModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_LOCAL = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InLocalShareOfAdopterModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SOCIAL = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InSocialShareOfAdopterModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_AVGFIN = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InAvgFinModule3.class);
    //number logging
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING = resolve(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING_CSV = resolve(PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING, InCsvValueLoggingModule3.class);
    //pv general
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL = resolve(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_PVGENERAL);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DECISIONDECIDER = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InDecisionMakingDeciderModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DOADOPT = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InDoAdoptModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_FEASIBILITY = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InFeasibilityModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INIT = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InInitializationModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INTEREST = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InInterestModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_MAINBRANCH = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InMainBranchingModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASEUPDATER = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InPhaseUpdateModule3.class);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_YEARBASED = resolve(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InYearBasedAdoptionDeciderModule3.class);
    //pv logging
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVLOGGING = resolve(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_PVLOGGING);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASELOGGER = resolve(PROCESS_MODEL4_PVACTMODULES_PVLOGGING, InPhaseLoggingModule3.class);
    //====

    public static final EdnPath SPATIAL = resolve(ROOT, IOConstants.SPATIAL);
    public static final EdnPath SPATIAL_MODEL = resolve(SPATIAL, IOConstants.SPATIAL_MODEL);
    public static final EdnPath SPATIAL_MODEL_2D = resolve(SPATIAL_MODEL, InSpace2D.class);
    public static final EdnPath SPATIAL_DIST = resolve(SPATIAL, IOConstants.SPATIAL_MODEL_DIST);
    public static final EdnPath SPATIAL_DIST_FILE = resolve(SPATIAL_DIST, IOConstants.SPATIAL_MODEL_DIST_FILE);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS = resolve(SPATIAL_DIST_FILE, IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_ALL = resolve(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSpatialInformationSupplier.class);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECT = resolve(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSelectSpatialInformationSupplier.class);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP = resolve(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSelectGroupSpatialInformationSupplier.class);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEU = resolve(SPATIAL_DIST_FILE_FILEPOS, InFileBasedPVactMilieuSupplier.class);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP = resolve(SPATIAL_DIST_FILE_FILEPOS, InFileBasedPVactMilieuZipSupplier.class);

    public static final EdnPath TIME = resolve(ROOT, IOConstants.TIME);
    public static final EdnPath TIME_DISCRET = resolve(TIME, InDiscreteTimeModel.class);
    public static final EdnPath TIME_UNITDISCRET = resolve(TIME, InUnitStepDiscreteTimeModel.class);

    public static EdnPath SUBMODULE = resolve(ROOT, IOConstants.SUBMODULE);
    public static EdnPath SUBMODULE_GV = resolve(SUBMODULE, IOConstants.SUBMODULE_GRAPHVIZDEMO);

    public static EdnPath DEV = resolve(ROOT, IOConstants.DEV);
    public static EdnPath DEV_TEST = resolve(DEV, IOConstants.TEST);
    public static EdnPath DEV_TEST_DATA = resolve(DEV_TEST, InTestData.class);

    //DEPRECATED
    public static EdnPath DEV_DEPRECATED = resolve(DEV, IOConstants.DEPRECATED);

    //SETT
    public static final EdnPath SETT_VISURESULT = resolve(DEV_DEPRECATED, IOConstants.RESULT_VISUALISATION);
    public static final EdnPath SETT_VISURESULT_GENERIC = resolve(SETT_VISURESULT, InGenericOutputImage.class);
    public static final EdnPath SETT_VISURESULT_GNU = resolve(SETT_VISURESULT, InGnuPlotOutputImage.class);
    public static final EdnPath SETT_VISURESULT_R = resolve(SETT_VISURESULT, InROutputImage.class);
    //SETT
    public static final EdnPath SETT_DATAOUTPUT = resolve(DEV_DEPRECATED, IOConstants.DATA_OUTPUT);
//    //ROOT
//    public static final EdnPath PROCESS = resolve(DEV_DEPRECATED, IOConstants.PROCESS_MODEL);
//    public static final EdnPath PROCESS_RA = resolve(PROCESS, InRAProcessModel.class);
//    public static final EdnPath PROCESS_MRA = resolve(PROCESS, InModularRAProcessModel.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS = resolve(PROCESS_MRA, IOConstants.PROCESS_MODULAR_COMPONENTS);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFINTEREST = resolve(PROCESS_MRA_COMPONENTS, InDefaultHandleInterestComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFFEAS = resolve(PROCESS_MRA_COMPONENTS, InDefaultHandleFeasibilityComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFDEC = resolve(PROCESS_MRA_COMPONENTS, InDefaultHandleDecisionMakingComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFACTION = resolve(PROCESS_MRA_COMPONENTS, InDefaultDoActionComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMATTR = resolve(PROCESS_MRA_COMPONENTS, InSumAttributeComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMINTER = resolve(PROCESS_MRA_COMPONENTS, InSumIntermediateComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMTHRESH = resolve(PROCESS_MRA_COMPONENTS, InSumThresholdComponent.class);
//    public static final EdnPath PROCESS_MRA_COMPONENTS_NOTHING = resolve(PROCESS_MRA_COMPONENTS, InDoNothingComponent.class);
//
//    public static final EdnPath PROCESS_MODULAR2 = resolve(PROCESS, IOConstants.PROCESS_MODULAR2);
//    public static final EdnPath PROCESS_MODULAR2_MODEL = resolve(PROCESS_MODULAR2, IOConstants.PROCESS_MODULAR2_MODEL);
//    public static final EdnPath PROCESS_MODULAR2_MODEL_SIMPLE = resolve(PROCESS_MODULAR2_MODEL, InConsumerAgentMPMWithAdoptionHandler.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS = resolve(PROCESS_MODULAR2, IOConstants.PROCESS_MODULAR2_COMPONENTS);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC = resolve(PROCESS_MODULAR2_COMPONENTS, IOConstants.PROCESS_MODULAR2_COMPONENTS_CALC);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_ADD = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InAddModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_INPUTATTR = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InAttributeInputModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_DISFIN = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InDisaggregatedFinancialModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_DISNPV = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InDisaggregatedNPVModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_ENVCON = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InEnvironmentalConcernModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_FINCOMP = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InFinancialComponentModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_LOGISTIC = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InLogisticModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_NOVSEEK = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InNoveltySeekingModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_NPV = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InNPVModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_PRODUCT = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InProductModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_PP = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InPurchasePowerModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SHARELOCAL = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InShareOfAdopterInLocalNetworkModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SHARESOCIAL = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InShareOfAdopterInSocialNetworkModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SOCIALCOMP = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InSocialComponentModule_inputgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SUM = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InSumModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTEDADD = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InWeightedAddModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTED = resolve(PROCESS_MODULAR2_COMPONENTS_CALC, InWeightedModule_calcgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL = resolve(PROCESS_MODULAR2_COMPONENTS, IOConstants.PROCESS_MODULAR2_COMPONENTS_EVAL);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_BRANCH = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InBranchModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTACTION = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultActionModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTDECISION = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultDecisionMakingModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTFEAS = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultFeasibilityModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTINTEREST = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultInterestModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DONOTHING = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InDoNothingModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_FILTER = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InFilterModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_SIMPLEGET = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InSimpleGetPhaseModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_STAGEEVAL = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InStageEvaluationModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_SUMTHRESH = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InSumThresholdEvaluationModule_evalgraphnode.class);
//    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_THRESH = resolve(PROCESS_MODULAR2_COMPONENTS_EVAL, InThresholdEvaluationModule_evalgraphnode.class);

    private TreeViewStructure() {
    }
    
    private static EdnPath resolve(EdnPath parent, Class<?> c) {
        return resolve(parent, c.getSimpleName());
    }
    
    private static EdnPath resolve(EdnPath parent, String next) {
        EdnPath resolved = parent.resolve(next);
        ALL_PATHS.add(resolved);
        return resolved;
    }
}
