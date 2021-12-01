package de.unileipzig.irpact.io.param.input;

import de.unileipzig.irpact.commons.util.MultiCounter;
import de.unileipzig.irpact.io.param.EdnPath;
import de.unileipzig.irpact.io.param.IOConstants;
import de.unileipzig.irpact.io.param.IOResources;
import de.unileipzig.irpact.io.param.ListEdnPath;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InNameSplitAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.*;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.agent.population.InFixConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.binary.VisibleBinaryData;
import de.unileipzig.irpact.io.param.input.distribution.*;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irpact.io.param.input.postdata.InNeighbourhoodOverview;
import de.unileipzig.irpact.io.param.input.process.modular.ca.InConsumerAgentMPMWithAdoptionHandler;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.calc.*;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval.*;
import de.unileipzig.irpact.io.param.input.process.mra.InModularRAProcessModel;
import de.unileipzig.irpact.io.param.input.process.mra.component.*;
import de.unileipzig.irpact.io.param.input.process.ra.*;
import de.unileipzig.irpact.io.param.input.process.ra.InMaxDistanceNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactIndividualGlobalModerateExtremistUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InPVactUpdatableGlobalModerateExtremistUncertainty;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.InBasicCAModularProcessModel;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InThresholdReachedModule_boolgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InBernoulliModule_boolgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InCsvValueLoggingModule_calcloggraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval.InRunUntilFailureModule_evalgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.logging.InPhaseLoggingModule_evalragraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval.InReevaluatorModule_reevalgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.reevaluate.*;
import de.unileipzig.irpact.io.param.input.process2.modular.handler.InAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.handler.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.handler.InUncertaintySupplierInitializer;
import de.unileipzig.irpact.io.param.input.product.*;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactAttributeBasedInitialAdoption;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.color.InColorARGB;
import de.unileipzig.irpact.io.param.input.color.InColorPalette;
import de.unileipzig.irpact.io.param.input.visualisation.network.InGraphvizGeneral;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGnuPlotOutputImage;
import de.unileipzig.irpact.io.param.input.visualisation.result.InROutputImage;
import de.unileipzig.irpact.io.param.input.interest.InProductGroupThresholdEntry;
import de.unileipzig.irpact.io.param.input.interest.InProductThresholdInterestSupplyScheme;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.*;
import de.unileipzig.irpact.io.param.input.process.ra.InEntireNetworkNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.*;
import de.unileipzig.irpact.io.param.input.time.InDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result2.*;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.util.ArrayList;
import java.util.List;

import static de.unileipzig.irpact.io.param.ParamUtil.*;

/**
 * @author Daniel Abitz
 */
@SuppressWarnings("unused")
public class InRootUI {

    public static final List<EdnPath> PATHS = new ArrayList<>();

    public static final EdnPath ROOT = new ListEdnPath(IOConstants.ROOT).addTo(PATHS); //!!!

    public static final EdnPath INFO = ROOT.resolve(IOConstants.INFORMATIONS).addTo(PATHS);
    public static final EdnPath INFO_ABOUTIRPACT = INFO.resolve(InIRPactVersion.thisName()).addTo(PATHS);
    public static final EdnPath INFO_ABOUTSCENARIO = INFO.resolve(InScenarioVersion.thisName()).addTo(PATHS);
    public static final EdnPath INFO_INFO = INFO.resolve(InInformation.thisName()).addTo(PATHS);

    public static final EdnPath SETT = ROOT.resolve(IOConstants.SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_GENERAL = SETT.resolve(IOConstants.GENERAL_SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_GENERAL_LOG = SETT_GENERAL.resolve(IOConstants.LOGGING).addTo(PATHS);
    public static final EdnPath SETT_SPECIAL = SETT.resolve(IOConstants.SPECIAL_SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_SPECIAL_BIN = SETT_SPECIAL.resolve(VisibleBinaryData.thisName()).addTo(PATHS);

    public static final EdnPath SETT_RESULT2 = SETT.resolve(IOConstants.RESULT_DATA2).addTo(PATHS);
    public static final EdnPath SETT_RESULT2_BUCKET = SETT_RESULT2.resolve(InBucketAnalyser.thisName()).addTo(PATHS);
    public static final EdnPath SETT_RESULT2_NEIGHBORS = SETT_RESULT2.resolve(InNeighbourhoodOverview.thisName()).addTo(PATHS);

    public static final EdnPath SETT_VISURESULT2 = SETT.resolve(IOConstants.RESULT_VISUALISATION2).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_CUSTOMAVGQUANTIL = SETT_VISURESULT2.resolve(InCustomAverageQuantilRangeImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE = SETT_VISURESULT2_CUSTOMAVGQUANTIL.resolve(InQuantileRange.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_SPECIALAVGQUANTIL = SETT_VISURESULT2.resolve(InSpecialAverageQuantilRangeImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_COMPARED = SETT_VISURESULT2.resolve(InComparedAnnualImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_COMPAREDZIP = SETT_VISURESULT2.resolve(InComparedAnnualZipImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_ADOPTIONPHASE = SETT_VISURESULT2.resolve(InAdoptionPhaseOverviewImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_PROCESSPHASE = SETT_VISURESULT2.resolve(InProcessPhaseOverviewImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_INTEREST = SETT_VISURESULT2.resolve(InInterestOverviewImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT2_ANNUALBUCKET = SETT_VISURESULT2.resolve(InAnnualBucketImage.thisName()).addTo(PATHS);

    public static final EdnPath SETT_VISUNETWORK = SETT.resolve(IOConstants.NETWORK_VISUALISATION).addTo(PATHS);
    public static final EdnPath SETT_VISUNETWORK_GENERAL = SETT_VISUNETWORK.resolve(InGraphvizGeneral.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISUNETWORK_AGENTCOLOR = SETT_VISUNETWORK.resolve(IOConstants.GRAPHVIZ_AGENT_COLOR_MAPPING).addTo(PATHS);

    public static final EdnPath SETT_COLOR = SETT.resolve(IOConstants.COLOR_SETTINGS).addTo(PATHS);
    public static final EdnPath SETT_COLOR_PALETTE = SETT_COLOR.resolve(InColorPalette.thisName()).addTo(PATHS);
    public static final EdnPath SETT_COLOR_ARGB = SETT_COLOR.resolve(InColorARGB.thisName()).addTo(PATHS);

    public static final EdnPath SPECIALINPUT = ROOT.resolve(IOConstants.SPECIAL_INPUT).addTo(PATHS);
    public static final EdnPath SPECIALINPUT_PVACT = SPECIALINPUT.resolve(IOConstants.SPECIAL_INPUT_PVACT).addTo(PATHS);
    public static final EdnPath SPECIALINPUT_PVACT_CONSTRATE = SPECIALINPUT_PVACT.resolve(IOConstants.SPECIAL_INPUT_PVACT_CONSTRATE).addTo(PATHS);
    public static final EdnPath SPECIALINPUT_PVACT_RENORATE = SPECIALINPUT_PVACT.resolve(IOConstants.SPECIAL_INPUT_PVACT_RENORATE).addTo(PATHS);

    public static final EdnPath ATTRNAMES = ROOT.resolve(InAttributeName.thisName()).addTo(PATHS);

    public static final EdnPath FILES = ROOT.resolve(IOConstants.FILES).addTo(PATHS);
    public static final EdnPath FILES_PV = FILES.resolve(InPVFile.thisName()).addTo(PATHS);
    public static final EdnPath FILES_REALADOPTION = FILES.resolve(InRealAdoptionDataFile.thisName()).addTo(PATHS);
    public static final EdnPath FILES_SPATIAL = FILES.resolve(InSpatialTableFile.thisName()).addTo(PATHS);

    public static final EdnPath DISTRIBUTIONS = ROOT.resolve(IOConstants.DISTRIBUTIONS).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BERNOULLI = DISTRIBUTIONS.resolve(InBernoulliDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOOLEAN = DISTRIBUTIONS.resolve(InBooleanDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_DIRAC = DISTRIBUTIONS.resolve(InDiracUnivariateDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_MP = DISTRIBUTIONS.resolve(InFiniteMassPointsDiscreteDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_MP_MP = DISTRIBUTIONS_MP.resolve(InMassPoint.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_NORM = DISTRIBUTIONS.resolve(InNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDNORM = DISTRIBUTIONS.resolve(InBoundedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_SLOWTRUNCNORM = DISTRIBUTIONS.resolve(InSlowTruncatedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_TRUNCNORM = DISTRIBUTIONS.resolve(InTruncatedNormalDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIDOUBLE = DISTRIBUTIONS.resolve(InBoundedUniformDoubleDistribution.thisName()).addTo(PATHS);
    public static final EdnPath DISTRIBUTIONS_BOUNDUNIINT = DISTRIBUTIONS.resolve(InBoundedUniformIntegerDistribution.thisName()).addTo(PATHS);

    public static final EdnPath AGENTS = ROOT.resolve(IOConstants.AGENTS).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER = AGENTS.resolve(IOConstants.CONSUMER).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_GROUP).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP_GENERAL = AGENTS_CONSUMER_GROUP.resolve(InGeneralConsumerAgentGroup.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_GROUP_PVACT = AGENTS_CONSUMER_GROUP.resolve(InPVactConsumerAgentGroup.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_ATTR).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALGRP = AGENTS_CONSUMER_ATTR.resolve(InGeneralConsumerAgentGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_GENERALANNUAL = AGENTS_CONSUMER_ATTR.resolve(InGeneralConsumerAgentAnnualGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITGRP = AGENTS_CONSUMER_ATTR.resolve(InNameSplitConsumerAgentGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_ATTR_SPLITANNUAL = AGENTS_CONSUMER_ATTR.resolve(InNameSplitConsumerAgentAnnualGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_AFFINITY).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_GRP = AGENTS_CONSUMER_AFF.resolve(InAffinities.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_COMPLEX = AGENTS_CONSUMER_AFF.resolve(InComplexAffinityEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_AFF_NAMESPLIT = AGENTS_CONSUMER_AFF.resolve(InNameSplitAffinityEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST = AGENTS_CONSUMER.resolve(IOConstants.CONSUMER_INTEREST).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD = AGENTS_CONSUMER_INTEREST.resolve(InProductThresholdInterestSupplyScheme.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY = AGENTS_CONSUMER_INTEREST_THRESHOLD.resolve(InProductGroupThresholdEntry.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP = AGENTS.resolve(IOConstants.POPULATION).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FIX = AGENTS_POP.resolve(InFixConsumerAgentPopulation.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FILE = AGENTS_POP.resolve(InFileBasedConsumerAgentPopulation.thisName()).addTo(PATHS);
    public static final EdnPath AGENTS_POP_FILEPVACT = AGENTS_POP.resolve(InFileBasedPVactConsumerAgentPopulation.thisName()).addTo(PATHS);

    public static final EdnPath NETWORK = ROOT.resolve(IOConstants.NETWORK).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO = NETWORK.resolve(IOConstants.TOPOLOGY).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_UNLINKED = NETWORK_TOPO.resolve(InUnlinkedGraphTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_COMPLETE = NETWORK_TOPO.resolve(InCompleteGraphTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_FREE = NETWORK_TOPO.resolve(InFreeNetworkTopology.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_TOPO_FREE_TIES = NETWORK_TOPO_FREE.resolve(InNumberOfTies.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC = NETWORK.resolve(IOConstants.DIST_FUNC).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC_NO = NETWORK.resolve(InNoDistance.thisName()).addTo(PATHS);
    public static final EdnPath NETWORK_DISTFUNC_INVERSE = NETWORK.resolve(InInverse.thisName()).addTo(PATHS);

    public static final EdnPath PRODUCTS = ROOT.resolve(IOConstants.PRODUCTS).addTo(PATHS);
    public static final EdnPath PRODUCTS_GROUP = PRODUCTS.resolve(IOConstants.PRODUCTS_GROUP).addTo(PATHS);
    public static final EdnPath PRODUCTS_GROUP_BASIC = PRODUCTS_GROUP.resolve(InBasicProductGroup.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR = PRODUCTS.resolve(IOConstants.PRODUCTS_ATTR).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR_BASIC = PRODUCTS_ATTR.resolve(InBasicProductGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_ATTR_SPLIT = PRODUCTS_ATTR.resolve(InNameSplitProductGroupAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FIX = PRODUCTS.resolve(InFixProduct.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FIXATTR = PRODUCTS.resolve(InFixProductAttribute.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_FINDSCHE = PRODUCTS.resolve(IOConstants.PRODUCTS_FINDING_SCHEME).addTo(PATHS);
    public static final EdnPath PRODUCTS_FINDSCHE_FIX = PRODUCTS_FINDSCHE.resolve(InFixProductFindingScheme.thisName()).addTo(PATHS);

    public static final EdnPath PRODUCTS_INITADOPT = PRODUCTS.resolve(IOConstants.INITAL_ADOPTERS).addTo(PATHS);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTATTRBASED = PRODUCTS_INITADOPT.resolve(InPVactAttributeBasedInitialAdoption.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTFILECAGBASED = PRODUCTS_INITADOPT.resolve(InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData.thisName()).addTo(PATHS);
    public static final EdnPath PRODUCTS_INITADOPT_PVACTFILEWEIGHTEDCAGBASED = PRODUCTS_INITADOPT.resolve(InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData.thisName()).addTo(PATHS);

    //process model
    public static final EdnPath PROCESS_MODULAR4 = ROOT.resolve(IOConstants.PROCESS_MODEL4).addTo(PATHS);
    //model
    public static final EdnPath PROCESS_MODULAR4_BASE = PROCESS_MODULAR4.resolve(InBasicCAModularProcessModel.thisName()).addTo(PATHS);
    //uncert
    public static final EdnPath PROCESS_MODEL4_UNCERT = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_UNCERT).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTGMEU = PROCESS_MODEL4_UNCERT.resolve(InPVactUpdatableGlobalModerateExtremistUncertainty.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTIMEU = PROCESS_MODEL4_UNCERT.resolve(InPVactIndividualGlobalModerateExtremistUncertaintySupplier.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_UNCERT_PVACTMEUOP = PROCESS_MODEL4_UNCERT.resolve(InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion.thisName()).addTo(PATHS);
    //peer
    public static final EdnPath PROCESS_MODEL4_DISTANCE = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_DISTANCE).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_DISABLED = PROCESS_MODEL4_DISTANCE.resolve(InDisabledNodeFilterDistanceScheme.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_ENTIRE = PROCESS_MODEL4_DISTANCE.resolve(InEntireNetworkNodeFilterDistanceScheme.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_DISTANCE_MAX = PROCESS_MODEL4_DISTANCE.resolve(InMaxDistanceNodeFilterDistanceScheme.thisName()).addTo(PATHS);
    //init
    public static final EdnPath PROCESS_MODEL4_INIT = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_INIT).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_AGENTATTR = PROCESS_MODEL4_INIT.resolve(InAgentAttributeScaler.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_LINPERATTR = PROCESS_MODEL4_INIT.resolve(InLinearePercentageAgentAttributeScaler.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_HANDLER_INIT_UNCERT = PROCESS_MODEL4_INIT.resolve(InUncertaintySupplierInitializer.thisName()).addTo(PATHS);
    //public static final EdnPath PROCESS_MODULAR3_HANDLER_NEWPRODUCT = PROCESS_MODULAR3_HANDLER.resolve(IOConstants.PROCESS_MODULAR3_HANDLER_NEWPRODUCT).addTo(PATHS);
    //reeval
    public static final EdnPath PROCESS_MODEL4_REEVAL = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_REEVAL).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_LINKER = PROCESS_MODEL4_REEVAL.resolve(InMultiReevaluator.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_INTEREST = PROCESS_MODEL4_REEVAL.resolve(InAnnualInterestLogger.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_UNCERT = PROCESS_MODEL4_REEVAL.resolve(InUncertaintyReevaluator.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_CONSTRENO = PROCESS_MODEL4_REEVAL.resolve(InConstructionRenovationUpdater.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_DEC = PROCESS_MODEL4_REEVAL.resolve(InDecisionMakingReevaluator.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_IMPEDEDRESET = PROCESS_MODEL4_REEVAL.resolve(InImpededResetter.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR3_REEVAL_LINPERUPDATER = PROCESS_MODEL4_REEVAL.resolve(InLinearePercentageAgentAttributeUpdater.thisName()).addTo(PATHS);
    //general modules
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES).addTo(PATHS);
    //action
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_ACTION).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE = PROCESS_MODEL4_GENERALMODULES_ACTION.resolve(InIfElseActionModule_actiongraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_ACTION_NOP = PROCESS_MODEL4_GENERALMODULES_ACTION.resolve(InNOP_actiongraphnode2.thisName()).addTo(PATHS);
    //bool
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_BOOL).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL_BERNOULLI = PROCESS_MODEL4_GENERALMODULES_BOOL.resolve(InBernoulliModule_boolgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD = PROCESS_MODEL4_GENERALMODULES_BOOL.resolve(InThresholdReachedModule_boolgraphnode2.thisName()).addTo(PATHS);
    //number input
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_VALUE = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InValueModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_NAN = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InNaNModule_inputgraphnode2.thisName()).addTo(PATHS);
    //number eval
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_MULSCALAR = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InMulScalarModule_calcgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_ADDSCALAR = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InAddScalarModule_calcgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InLogisticModule_calcgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_SUM = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InSumModule_calcgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_PRODUCT = PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT.resolve(InProductModule_calcgraphnode2.thisName()).addTo(PATHS);
    //system
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_SYSTEM = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_SYSTEM).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL = PROCESS_MODEL4_GENERALMODULES_SYSTEM.resolve(InRunUntilFailureModule_evalgraphnode2.thisName()).addTo(PATHS);
    //independent
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_INDEPENDENT = PROCESS_MODEL4_GENERALMODULES.resolve(IOConstants.PROCESS_MODEL4_GENERALMODULES_INDEPENDENT).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL = PROCESS_MODEL4_GENERALMODULES_INDEPENDENT.resolve(InReevaluatorModule_reevalgraphnode2.thisName()).addTo(PATHS);
    //general modules
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES = PROCESS_MODULAR4.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES).addTo(PATHS);
    //action
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION = PROCESS_MODEL4_PVACTMODULES.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES_ACTION).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION_COMMU = PROCESS_MODEL4_PVACTMODULES_ACTION.resolve(InCommunicationModule_actiongraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_ACTION_REWIRE = PROCESS_MODEL4_PVACTMODULES_ACTION.resolve(InRewireModule_actiongraphnode2.thisName()).addTo(PATHS);
    //number input
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT = PROCESS_MODEL4_PVACTMODULES.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_ATTR = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InAttributeInputModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_GLOBALAVGNPV = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InGlobalAvgNPVModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_NPV = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InNPVModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_LOCAL = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InLocalShareOfAdopterModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SOCIAL = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InSocialShareOfAdopterModule_inputgraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_AVGFIN = PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT.resolve(InAvgFinModule_inputgraphnode2.thisName()).addTo(PATHS);
    //number logging
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING = PROCESS_MODEL4_PVACTMODULES.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING_CSV = PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING.resolve(InCsvValueLoggingModule_calcloggraphnode2.thisName()).addTo(PATHS);
    //pv general
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL = PROCESS_MODEL4_PVACTMODULES.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES_PVGENERAL).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DECISIONDECIDER = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InDecisionMakingDeciderModule2_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DOADOPT = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InDoAdoptModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_FEASIBILITY = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InFeasibilityModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INIT = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InInitializationModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INTEREST = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InInterestModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_MAINBRANCH = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InMainBranchingModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASEUPDATER = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InPhaseUpdateModule_evalragraphnode2.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_YEARBASED = PROCESS_MODEL4_PVACTMODULES_PVGENERAL.resolve(InYearBasedAdoptionDeciderModule_evalragraphnode2.thisName()).addTo(PATHS);
    //pv logging
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVLOGGING = PROCESS_MODEL4_PVACTMODULES.resolve(IOConstants.PROCESS_MODEL4_PVACTMODULES_PVLOGGING).addTo(PATHS);
    public static final EdnPath PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASELOGGER = PROCESS_MODEL4_PVACTMODULES_PVLOGGING.resolve(InPhaseLoggingModule_evalragraphnode2.thisName()).addTo(PATHS);
    //====

    public static final EdnPath SPATIAL = ROOT.resolve(IOConstants.SPATIAL).addTo(PATHS);
    public static final EdnPath SPATIAL_MODEL = SPATIAL.resolve(IOConstants.SPATIAL_MODEL).addTo(PATHS);
    public static final EdnPath SPATIAL_MODEL_2D = SPATIAL_MODEL.resolve(InSpace2D.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST = SPATIAL.resolve(IOConstants.SPATIAL_MODEL_DIST).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE = SPATIAL_DIST.resolve(IOConstants.SPATIAL_MODEL_DIST_FILE).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS = SPATIAL_DIST_FILE.resolve(IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_ALL = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECT = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSelectSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedSelectGroupSpatialInformationSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEU = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedPVactMilieuSupplier.thisName()).addTo(PATHS);
    public static final EdnPath SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP = SPATIAL_DIST_FILE_FILEPOS.resolve(InFileBasedPVactMilieuZipSupplier.thisName()).addTo(PATHS);

    public static final EdnPath TIME = ROOT.resolve(IOConstants.TIME).addTo(PATHS);
    public static final EdnPath TIME_DISCRET = TIME.resolve(InDiscreteTimeModel.thisName()).addTo(PATHS);
    public static final EdnPath TIME_UNITDISCRET = TIME.resolve(InUnitStepDiscreteTimeModel.thisName()).addTo(PATHS);

    public static EdnPath SUBMODULE = ROOT.resolve(IOConstants.SUBMODULE).addTo(PATHS);
    public static EdnPath SUBMODULE_GV = SUBMODULE.resolve(IOConstants.SUBMODULE_GRAPHVIZDEMO).addTo(PATHS);

    public static EdnPath DEV = ROOT.resolve(IOConstants.DEV).addTo(PATHS);
    public static EdnPath DEV_TEST = DEV.resolve(IOConstants.TEST).addTo(PATHS);
    public static EdnPath DEV_TEST_DATA = DEV_TEST.resolve(InTestData.thisName()).addTo(PATHS);

    //DEPRECATED
    public static EdnPath DEV_DEPRECATED = DEV.resolve(IOConstants.DEPRECATED).addTo(PATHS);

    //SETT
    public static final EdnPath SETT_VISURESULT = DEV_DEPRECATED.resolve(IOConstants.RESULT_VISUALISATION).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_GENERIC = SETT_VISURESULT.resolve(InGenericOutputImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_GNU = SETT_VISURESULT.resolve(InGnuPlotOutputImage.thisName()).addTo(PATHS);
    public static final EdnPath SETT_VISURESULT_R = SETT_VISURESULT.resolve(InROutputImage.thisName()).addTo(PATHS);
    //SETT
    public static final EdnPath SETT_DATAOUTPUT = DEV_DEPRECATED.resolve(IOConstants.DATA_OUTPUT).addTo(PATHS);
    //ROOT
    public static final EdnPath PROCESS = DEV_DEPRECATED.resolve(IOConstants.PROCESS_MODEL).addTo(PATHS);
    public static final EdnPath PROCESS_RA = PROCESS.resolve(InRAProcessModel.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA = PROCESS.resolve(InModularRAProcessModel.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS = PROCESS_MRA.resolve(IOConstants.PROCESS_MODULAR_COMPONENTS).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFINTEREST = PROCESS_MRA_COMPONENTS.resolve(InDefaultHandleInterestComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFFEAS = PROCESS_MRA_COMPONENTS.resolve(InDefaultHandleFeasibilityComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFDEC = PROCESS_MRA_COMPONENTS.resolve(InDefaultHandleDecisionMakingComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_DEFACTION = PROCESS_MRA_COMPONENTS.resolve(InDefaultDoActionComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMATTR = PROCESS_MRA_COMPONENTS.resolve(InSumAttributeComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMINTER = PROCESS_MRA_COMPONENTS.resolve(InSumIntermediateComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_SUMTHRESH = PROCESS_MRA_COMPONENTS.resolve(InSumThresholdComponent.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MRA_COMPONENTS_NOTHING = PROCESS_MRA_COMPONENTS.resolve(InDoNothingComponent.thisName()).addTo(PATHS);

    public static final EdnPath PROCESS_MODULAR2 = PROCESS.resolve(IOConstants.PROCESS_MODULAR2).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_MODEL = PROCESS_MODULAR2.resolve(IOConstants.PROCESS_MODULAR2_MODEL).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_MODEL_SIMPLE = PROCESS_MODULAR2_MODEL.resolve(InConsumerAgentMPMWithAdoptionHandler.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS = PROCESS_MODULAR2.resolve(IOConstants.PROCESS_MODULAR2_COMPONENTS).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC = PROCESS_MODULAR2_COMPONENTS.resolve(IOConstants.PROCESS_MODULAR2_COMPONENTS_CALC).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_ADD = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InAddModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_INPUTATTR = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InAttributeInputModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_DISFIN = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InDisaggregatedFinancialModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_DISNPV = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InDisaggregatedNPVModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_ENVCON = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InEnvironmentalConcernModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_FINCOMP = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InFinancialComponentModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_LOGISTIC = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InLogisticModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_NOVSEEK = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InNoveltySeekingModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_NPV = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InNPVModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_PRODUCT = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InProductModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_PP = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InPurchasePowerModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SHARELOCAL = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InShareOfAdopterInLocalNetworkModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SHARESOCIAL = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InShareOfAdopterInSocialNetworkModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SOCIALCOMP = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InSocialComponentModule_inputgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_SUM = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InSumModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTEDADD = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InWeightedAddModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTED = PROCESS_MODULAR2_COMPONENTS_CALC.resolve(InWeightedModule_calcgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL = PROCESS_MODULAR2_COMPONENTS.resolve(IOConstants.PROCESS_MODULAR2_COMPONENTS_EVAL).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_BRANCH = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InBranchModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTACTION = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InDefaultActionModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTDECISION = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InDefaultDecisionMakingModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTFEAS = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InDefaultFeasibilityModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTINTEREST = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InDefaultInterestModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_DONOTHING = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InDoNothingModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_FILTER = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InFilterModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_SIMPLEGET = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InSimpleGetPhaseModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_STAGEEVAL = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InStageEvaluationModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_SUMTHRESH = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InSumThresholdEvaluationModule_evalgraphnode.thisName()).addTo(PATHS);
    public static final EdnPath PROCESS_MODULAR2_COMPONENTS_EVAL_THRESH = PROCESS_MODULAR2_COMPONENTS_EVAL.resolve(InThresholdEvaluationModule_evalgraphnode.thisName()).addTo(PATHS);

    public InRootUI() {
    }

    @SuppressWarnings("StatementWithEmptyBody")
    public static void initRes(TreeAnnotationResource res) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();

        for(EdnPath path: PATHS) {
            if(path == ROOT) {
                //ignore
            }
            else {
                addPathElement(res, path);
            }
        }
    }

    public static void applyRes(TreeAnnotationResource res) {
        //optact rest
        res.getCachedElement("OPTACT").setParent(res.getCachedElement(SUBMODULE.getLast()));
        res.getCachedElement("AgentGroup_Element").setParent(res.getCachedElement("OPTACT"));
        res.getCachedElement(IOConstants.GRAPHVIZ).setParent(res.getCachedElement("OPTACT"));
    }
}
