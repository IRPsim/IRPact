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
import de.unileipzig.irpact.io.param.input.process2.modular.InModularProcessModel2;
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
public enum TreeViewStructureEnum {
    NULL(),

    ROOT(null, IOConstants.ROOT),

    INFO(ROOT, IOConstants.INFORMATIONS),
    INFO_ABOUTIRPACT(INFO, InIRPactVersion.class),
    INFO_ABOUTSCENARIO(INFO, InScenarioVersion.class),
    INFO_INFO(INFO, InInformation.class),

    SETT(ROOT, IOConstants.SETTINGS),
    SETT_GENERAL(SETT, IOConstants.GENERAL_SETTINGS),
    SETT_GENERAL_LOG(SETT_GENERAL, IOConstants.LOGGING),
    SETT_SPECIAL(SETT, IOConstants.SPECIAL_SETTINGS),
    SETT_SPECIAL_BIN(SETT_SPECIAL, VisibleBinaryData.class),

    SETT_RESULT2(SETT, IOConstants.RESULT_DATA2),
    SETT_RESULT2_BUCKET(SETT_RESULT2, InBucketAnalyser.class),
    SETT_RESULT2_NEIGHBORS(SETT_RESULT2, InNeighbourhoodOverview.class),

    SETT_VISURESULT2(SETT, IOConstants.RESULT_VISUALISATION2),
    SETT_VISURESULT2_CUSTOMAVGQUANTIL(SETT_VISURESULT2, InCustomAverageQuantilRangeImage.class),
    SETT_VISURESULT2_CUSTOMAVGQUANTIL_RANGE(SETT_VISURESULT2_CUSTOMAVGQUANTIL, InQuantileRange.class),
    SETT_VISURESULT2_SPECIALAVGQUANTIL(SETT_VISURESULT2, InSpecialAverageQuantilRangeImage.class),
    SETT_VISURESULT2_COMPARED(SETT_VISURESULT2, InComparedAnnualImage.class),
    SETT_VISURESULT2_COMPAREDZIP(SETT_VISURESULT2, InComparedAnnualZipImage.class),
    SETT_VISURESULT2_ADOPTIONPHASE(SETT_VISURESULT2, InAdoptionPhaseOverviewImage.class),
    SETT_VISURESULT2_PROCESSPHASE(SETT_VISURESULT2, InProcessPhaseOverviewImage.class),
    SETT_VISURESULT2_INTEREST(SETT_VISURESULT2, InInterestOverviewImage.class),
    SETT_VISURESULT2_ANNUALBUCKET(SETT_VISURESULT2, InAnnualBucketImage.class),
    SETT_VISURESULT2_ANNUALMILIEU(SETT_VISURESULT2, InAnnualMilieuImage.class),
    SETT_VISURESULT2_ANNUALINTEREST(SETT_VISURESULT2, InAnnualInterestImage.class),
    SETT_VISURESULT2_CUMUANNUALINTEREST(SETT_VISURESULT2, InCumulatedAnnualInterestImage.class),

    SETT_VISUNETWORK(SETT, IOConstants.NETWORK_VISUALISATION),
    SETT_VISUNETWORK_GENERAL(SETT_VISUNETWORK, InGraphvizGeneral.class),
    SETT_VISUNETWORK_AGENTCOLOR(SETT_VISUNETWORK, IOConstants.GRAPHVIZ_AGENT_COLOR_MAPPING),

    SETT_COLOR(SETT, IOConstants.COLOR_SETTINGS),
    SETT_COLOR_PALETTE(SETT_COLOR, InColorPalette.class),
    SETT_COLOR_ARGB(SETT_COLOR, InColorARGB.class),

    SPECIALINPUT(ROOT, IOConstants.SPECIAL_INPUT),
    SPECIALINPUT_PVACT(SPECIALINPUT, IOConstants.SPECIAL_INPUT_PVACT),
    SPECIALINPUT_PVACT_CONSTRATE(SPECIALINPUT_PVACT, IOConstants.SPECIAL_INPUT_PVACT_CONSTRATE),
    SPECIALINPUT_PVACT_RENORATE(SPECIALINPUT_PVACT, IOConstants.SPECIAL_INPUT_PVACT_RENORATE),

    ATTRNAMES(ROOT, InAttributeName.class),

    FILES(ROOT, IOConstants.FILES),
    FILES_PV(FILES, InPVFile.class),
    FILES_REALADOPTION(FILES, InRealAdoptionDataFile.class),
    FILES_SPATIAL(FILES, InSpatialTableFile.class),

    DISTRIBUTIONS(ROOT, IOConstants.DISTRIBUTIONS),
    DISTRIBUTIONS_BERNOULLI(DISTRIBUTIONS, InBernoulliDistribution.class),
    DISTRIBUTIONS_BOOLEAN(DISTRIBUTIONS, InBooleanDistribution.class),
    DISTRIBUTIONS_DIRAC(DISTRIBUTIONS, InDiracUnivariateDistribution.class),
    DISTRIBUTIONS_MP(DISTRIBUTIONS, InFiniteMassPointsDiscreteDistribution.class),
    DISTRIBUTIONS_MP_MP(DISTRIBUTIONS_MP, InMassPoint.class),
    DISTRIBUTIONS_NORM(DISTRIBUTIONS, InNormalDistribution.class),
    DISTRIBUTIONS_BOUNDNORM(DISTRIBUTIONS, InBoundedNormalDistribution.class),
    DISTRIBUTIONS_SLOWTRUNCNORM(DISTRIBUTIONS, InSlowTruncatedNormalDistribution.class),
    DISTRIBUTIONS_TRUNCNORM(DISTRIBUTIONS, InTruncatedNormalDistribution.class),
    DISTRIBUTIONS_BOUNDUNIDOUBLE(DISTRIBUTIONS, InBoundedUniformDoubleDistribution.class),
    DISTRIBUTIONS_BOUNDUNIINT(DISTRIBUTIONS, InBoundedUniformIntegerDistribution.class),

    AGENTS(ROOT, IOConstants.AGENTS),
    AGENTS_CONSUMER(AGENTS, IOConstants.CONSUMER),
    AGENTS_CONSUMER_GROUP(AGENTS_CONSUMER, IOConstants.CONSUMER_GROUP),
    AGENTS_CONSUMER_GROUP_GENERAL(AGENTS_CONSUMER_GROUP, InGeneralConsumerAgentGroup.class),
    AGENTS_CONSUMER_GROUP_PVACT(AGENTS_CONSUMER_GROUP, InPVactConsumerAgentGroup.class),
    AGENTS_CONSUMER_ATTR(AGENTS_CONSUMER, IOConstants.CONSUMER_ATTR),
    AGENTS_CONSUMER_ATTR_GENERALGRP(AGENTS_CONSUMER_ATTR, InGeneralConsumerAgentGroupAttribute.class),
    AGENTS_CONSUMER_ATTR_GENERALANNUAL(AGENTS_CONSUMER_ATTR, InGeneralConsumerAgentAnnualGroupAttribute.class),
    AGENTS_CONSUMER_ATTR_SPLITGRP(AGENTS_CONSUMER_ATTR, InNameSplitConsumerAgentGroupAttribute.class),
    AGENTS_CONSUMER_ATTR_SPLITANNUAL(AGENTS_CONSUMER_ATTR, InNameSplitConsumerAgentAnnualGroupAttribute.class),
    AGENTS_CONSUMER_AFF(AGENTS_CONSUMER, IOConstants.CONSUMER_AFFINITY),
    AGENTS_CONSUMER_AFF_GRP(AGENTS_CONSUMER_AFF, InAffinities.class),
    AGENTS_CONSUMER_AFF_COMPLEX(AGENTS_CONSUMER_AFF, InComplexAffinityEntry.class),
    AGENTS_CONSUMER_AFF_NAMESPLIT(AGENTS_CONSUMER_AFF, InNameSplitAffinityEntry.class),
    AGENTS_CONSUMER_INTEREST(AGENTS_CONSUMER, IOConstants.CONSUMER_INTEREST),
    AGENTS_CONSUMER_INTEREST_THRESHOLD(AGENTS_CONSUMER_INTEREST, InProductThresholdInterestSupplyScheme.class),
    AGENTS_CONSUMER_INTEREST_THRESHOLD_ENTRY(AGENTS_CONSUMER_INTEREST_THRESHOLD, InProductGroupThresholdEntry.class),
    AGENTS_POP(AGENTS, IOConstants.POPULATION),
    AGENTS_POP_FIX(AGENTS_POP, InFixConsumerAgentPopulation.class),
    AGENTS_POP_FILE(AGENTS_POP, InFileBasedConsumerAgentPopulation.class),
    AGENTS_POP_FILEPVACT(AGENTS_POP, InFileBasedPVactConsumerAgentPopulation.class),

    NETWORK(ROOT, IOConstants.NETWORK),
    NETWORK_TOPO(NETWORK, IOConstants.TOPOLOGY),
    NETWORK_TOPO_UNLINKED(NETWORK_TOPO, InUnlinkedGraphTopology.class),
    NETWORK_TOPO_COMPLETE(NETWORK_TOPO, InCompleteGraphTopology.class),
    NETWORK_TOPO_FREE(NETWORK_TOPO, InFreeNetworkTopology.class),
    NETWORK_TOPO_FREE_TIES(NETWORK_TOPO_FREE, InNumberOfTies.class),
    NETWORK_DISTFUNC(NETWORK, IOConstants.DIST_FUNC),
    NETWORK_DISTFUNC_NO(NETWORK, InNoDistance.class),
    NETWORK_DISTFUNC_INVERSE(NETWORK, InInverse.class),

    PRODUCTS(ROOT, IOConstants.PRODUCTS),
    PRODUCTS_GROUP(PRODUCTS, IOConstants.PRODUCTS_GROUP),
    PRODUCTS_GROUP_BASIC(PRODUCTS_GROUP, InBasicProductGroup.class),
    PRODUCTS_ATTR(PRODUCTS, IOConstants.PRODUCTS_ATTR),
    PRODUCTS_ATTR_BASIC(PRODUCTS_ATTR, InBasicProductGroupAttribute.class),
    PRODUCTS_ATTR_SPLIT(PRODUCTS_ATTR, InNameSplitProductGroupAttribute.class),
    PRODUCTS_FIX(PRODUCTS, InFixProduct.class),
    PRODUCTS_FIXATTR(PRODUCTS, InFixProductAttribute.class),
    PRODUCTS_FINDSCHE(PRODUCTS, IOConstants.PRODUCTS_FINDING_SCHEME),
    PRODUCTS_FINDSCHE_FIX(PRODUCTS_FINDSCHE, InFixProductFindingScheme.class),

    PRODUCTS_INITADOPT(PRODUCTS, IOConstants.INITAL_ADOPTERS),
    PRODUCTS_INITADOPT_PVACTATTRBASED(PRODUCTS_INITADOPT, InPVactAttributeBasedInitialAdoption.class),
    PRODUCTS_INITADOPT_PVACTFILECAGBASED(PRODUCTS_INITADOPT, InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData.class),
    PRODUCTS_INITADOPT_PVACTFILEWEIGHTEDCAGBASED(PRODUCTS_INITADOPT, InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData.class),
    
    //process model
    PROCESS_MODULAR4(ROOT, IOConstants.PROCESS_MODEL4),
    //graph
    PROCESS_MODULAR4_GRAPH(PROCESS_MODULAR4, InModularProcessModel2.class),
    //model
    PROCESS_MODULAR4_BASE(PROCESS_MODULAR4, InBasicCAModularProcessModel.class),
    //uncert
    PROCESS_MODEL4_UNCERT(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_UNCERT),
    PROCESS_MODEL4_UNCERT_PVACTGMEU(PROCESS_MODEL4_UNCERT, InPVactUpdatableGlobalModerateExtremistUncertainty.class),
    PROCESS_MODEL4_UNCERT_PVACTIMEU(PROCESS_MODEL4_UNCERT, InPVactIndividualGlobalModerateExtremistUncertaintySupplier.class),
    PROCESS_MODEL4_UNCERT_PVACTMEUOP(PROCESS_MODEL4_UNCERT, InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion.class),
    //peer
    PROCESS_MODEL4_DISTANCE(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_DISTANCE),
    PROCESS_MODEL4_DISTANCE_DISABLED(PROCESS_MODEL4_DISTANCE, InDisabledNodeFilterDistanceScheme.class),
    PROCESS_MODEL4_DISTANCE_ENTIRE(PROCESS_MODEL4_DISTANCE, InEntireNetworkNodeFilterDistanceScheme.class),
    PROCESS_MODEL4_DISTANCE_MAX(PROCESS_MODEL4_DISTANCE, InMaxDistanceNodeFilterDistanceScheme.class),
    //init
    PROCESS_MODEL4_INIT(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_INIT),
    PROCESS_MODULAR3_HANDLER_INIT_AGENTATTR(PROCESS_MODEL4_INIT, InAgentAttributeScaler.class),
    PROCESS_MODULAR3_HANDLER_INIT_LINPERATTR(PROCESS_MODEL4_INIT, InLinearePercentageAgentAttributeScaler.class),
    PROCESS_MODULAR3_HANDLER_INIT_UNCERT(PROCESS_MODEL4_INIT, InUncertaintySupplierInitializer.class),
    //PROCESS_MODULAR3_HANDLER_NEWPRODUCT = PROCESS_MODULAR3_HANDLER.resolve(IOConstants.PROCESS_MODULAR3_HANDLER_NEWPRODUCT).addTo(PATHS),
    //reeval
    PROCESS_MODEL4_REEVAL(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_REEVAL),
    PROCESS_MODULAR3_REEVAL_LINKER(PROCESS_MODEL4_REEVAL, InMultiReevaluator.class),
    PROCESS_MODULAR3_REEVAL_INTEREST(PROCESS_MODEL4_REEVAL, InAnnualInterestLogger.class),
    PROCESS_MODULAR3_REEVAL_UNCERT(PROCESS_MODEL4_REEVAL, InUncertaintyReevaluator.class),
    PROCESS_MODULAR3_REEVAL_CONSTRENO(PROCESS_MODEL4_REEVAL, InConstructionRenovationUpdater.class),
    PROCESS_MODULAR3_REEVAL_DEC(PROCESS_MODEL4_REEVAL, InDecisionMakingReevaluator.class),
    PROCESS_MODULAR3_REEVAL_IMPEDEDRESET(PROCESS_MODEL4_REEVAL, InImpededResetter.class),
    PROCESS_MODULAR3_REEVAL_LINPERUPDATER(PROCESS_MODEL4_REEVAL, InLinearePercentageAgentAttributeUpdater.class),
    //general modules
    PROCESS_MODEL4_GENERALMODULES(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_GENERALMODULES),
    //action
    PROCESS_MODEL4_GENERALMODULES_ACTION(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_ACTION),
    PROCESS_MODEL4_GENERALMODULES_ACTION_IFELSE(PROCESS_MODEL4_GENERALMODULES_ACTION, InIfElseActionModule3.class),
    PROCESS_MODEL4_GENERALMODULES_ACTION_NOP(PROCESS_MODEL4_GENERALMODULES_ACTION, InNOPModule3.class),
    //bool
    PROCESS_MODEL4_GENERALMODULES_BOOL(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_BOOL),
    PROCESS_MODEL4_GENERALMODULES_BOOL_BERNOULLI(PROCESS_MODEL4_GENERALMODULES_BOOL, InBernoulliModule3.class),
    PROCESS_MODEL4_GENERALMODULES_BOOL_THRESHOLD(PROCESS_MODEL4_GENERALMODULES_BOOL, InThresholdReachedModule3.class),
    //number input
    PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT),
    PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_VALUE(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InValueModule3.class),
    PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT_NAN(PROCESS_MODEL4_GENERALMODULES_NUMBERINPUT, InNaNModule3.class),
    //number eval
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL),
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_MULSCALAR(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL, InMulScalarModule3.class),
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_ADDSCALAR(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL, InAddScalarModule3.class),
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_LOGISTIC(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL, InLogisticModule3.class),
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_SUM(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL, InSumModule3.class),
    PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL_PRODUCT(PROCESS_MODEL4_GENERALMODULES_NUMBEREVAL, InProductModule3.class),
    //system
    PROCESS_MODEL4_GENERALMODULES_SYSTEM(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_SYSTEM),
    PROCESS_MODEL4_GENERALMODULES_SYSTEM_UNTILFAIL(PROCESS_MODEL4_GENERALMODULES_SYSTEM, InRunUntilFailureModule3.class),
    //independent
    PROCESS_MODEL4_GENERALMODULES_INDEPENDENT(PROCESS_MODEL4_GENERALMODULES, IOConstants.PROCESS_MODEL4_GENERALMODULES_INDEPENDENT),
    PROCESS_MODEL4_GENERALMODULES_INDEPENDENT_REEVAL(PROCESS_MODEL4_GENERALMODULES_INDEPENDENT, InReevaluatorModule3.class),
    //general modules
    PROCESS_MODEL4_PVACTMODULES(PROCESS_MODULAR4, IOConstants.PROCESS_MODEL4_PVACTMODULES),
    //action
    PROCESS_MODEL4_PVACTMODULES_ACTION(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_ACTION),
    PROCESS_MODEL4_PVACTMODULES_ACTION_COMMU(PROCESS_MODEL4_PVACTMODULES_ACTION, InCommunicationModule3.class),
    PROCESS_MODEL4_PVACTMODULES_ACTION_REWIRE(PROCESS_MODEL4_PVACTMODULES_ACTION, InRewireModule3.class),
    //number input
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_ATTR(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InAttributeInputModule3.class),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_GLOBALAVGNPV(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InGlobalAvgNPVModule3.class),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_NPV(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InNPVModule3.class),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_LOCAL(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InLocalShareOfAdopterModule3.class),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_SOCIAL(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InSocialShareOfAdopterModule3.class),
    PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT_AVGFIN(PROCESS_MODEL4_PVACTMODULES_NUMBERINPUT, InAvgFinModule3.class),
    //number logging
    PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING),
    PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING_CSV(PROCESS_MODEL4_PVACTMODULES_NUMBERLOGGING, InCsvValueLoggingModule3.class),
    //pv general
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_PVGENERAL),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DECISIONDECIDER(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InDecisionMakingDeciderModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_DOADOPT(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InDoAdoptModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_FEASIBILITY(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InFeasibilityModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INIT(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InInitializationModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_INTEREST(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InInterestModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_MAINBRANCH(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InMainBranchingModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASEUPDATER(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InPhaseUpdateModule3.class),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_YEARBASED(PROCESS_MODEL4_PVACTMODULES_PVGENERAL, InYearBasedAdoptionDeciderModule3.class),
    //pv logging
    PROCESS_MODEL4_PVACTMODULES_PVLOGGING(PROCESS_MODEL4_PVACTMODULES, IOConstants.PROCESS_MODEL4_PVACTMODULES_PVLOGGING),
    PROCESS_MODEL4_PVACTMODULES_PVGENERAL_PHASELOGGER(PROCESS_MODEL4_PVACTMODULES_PVLOGGING, InPhaseLoggingModule3.class),
    //====

    SPATIAL(ROOT, IOConstants.SPATIAL),
    SPATIAL_MODEL(SPATIAL, IOConstants.SPATIAL_MODEL),
    SPATIAL_MODEL_2D(SPATIAL_MODEL, InSpace2D.class),
    SPATIAL_DIST(SPATIAL, IOConstants.SPATIAL_MODEL_DIST),
    SPATIAL_DIST_FILE(SPATIAL_DIST, IOConstants.SPATIAL_MODEL_DIST_FILE),
    SPATIAL_DIST_FILE_FILEPOS(SPATIAL_DIST_FILE, IOConstants.SPATIAL_MODEL_DIST_FILE_FILEPOS),
    SPATIAL_DIST_FILE_FILEPOS_ALL(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSpatialInformationSupplier.class),
    SPATIAL_DIST_FILE_FILEPOS_SELECT(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSelectSpatialInformationSupplier.class),
    SPATIAL_DIST_FILE_FILEPOS_SELECTGROUP(SPATIAL_DIST_FILE_FILEPOS, InFileBasedSelectGroupSpatialInformationSupplier.class),
    SPATIAL_DIST_FILE_FILEPOS_PVMILIEU(SPATIAL_DIST_FILE_FILEPOS, InFileBasedPVactMilieuSupplier.class),
    SPATIAL_DIST_FILE_FILEPOS_PVMILIEUZIP(SPATIAL_DIST_FILE_FILEPOS, InFileBasedPVactMilieuZipSupplier.class),

    TIME(ROOT, IOConstants.TIME),
    TIME_DISCRET(TIME, InDiscreteTimeModel.class),
    TIME_UNITDISCRET(TIME, InUnitStepDiscreteTimeModel.class),

    SUBMODULE(ROOT, IOConstants.SUBMODULE),
    SUBMODULE_GV(SUBMODULE, IOConstants.SUBMODULE_GRAPHVIZDEMO),

    DEV(ROOT, IOConstants.DEV),
    DEV_TEST(DEV, IOConstants.TEST),
    DEV_TEST_DATA(DEV_TEST, InTestData.class),

    //DEPRECATED
    DEV_DEPRECATED(DEV, IOConstants.DEPRECATED),

    //SETT
    SETT_VISURESULT(DEV_DEPRECATED, IOConstants.RESULT_VISUALISATION),
    SETT_VISURESULT_GENERIC(SETT_VISURESULT, InGenericOutputImage.class),
    SETT_VISURESULT_GNU(SETT_VISURESULT, InGnuPlotOutputImage.class),
    SETT_VISURESULT_R(SETT_VISURESULT, InROutputImage.class),
    //SETT
    SETT_DATAOUTPUT(DEV_DEPRECATED, IOConstants.DATA_OUTPUT),
    //ROOT
//    PROCESS(DEV_DEPRECATED, IOConstants.PROCESS_MODEL),
//    PROCESS_RA(PROCESS, InRAProcessModel.class),
//    PROCESS_MRA(PROCESS, InModularRAProcessModel.class),
//    PROCESS_MRA_COMPONENTS(PROCESS_MRA, IOConstants.PROCESS_MODULAR_COMPONENTS),
//    PROCESS_MRA_COMPONENTS_DEFINTEREST(PROCESS_MRA_COMPONENTS, InDefaultHandleInterestComponent.class),
//    PROCESS_MRA_COMPONENTS_DEFFEAS(PROCESS_MRA_COMPONENTS, InDefaultHandleFeasibilityComponent.class),
//    PROCESS_MRA_COMPONENTS_DEFDEC(PROCESS_MRA_COMPONENTS, InDefaultHandleDecisionMakingComponent.class),
//    PROCESS_MRA_COMPONENTS_DEFACTION(PROCESS_MRA_COMPONENTS, InDefaultDoActionComponent.class),
//    PROCESS_MRA_COMPONENTS_SUMATTR(PROCESS_MRA_COMPONENTS, InSumAttributeComponent.class),
//    PROCESS_MRA_COMPONENTS_SUMINTER(PROCESS_MRA_COMPONENTS, InSumIntermediateComponent.class),
//    PROCESS_MRA_COMPONENTS_SUMTHRESH(PROCESS_MRA_COMPONENTS, InSumThresholdComponent.class),
//    PROCESS_MRA_COMPONENTS_NOTHING(PROCESS_MRA_COMPONENTS, InDoNothingComponent.class),

//    PROCESS_MODULAR2(PROCESS, IOConstants.PROCESS_MODULAR2),
//    PROCESS_MODULAR2_MODEL(PROCESS_MODULAR2, IOConstants.PROCESS_MODULAR2_MODEL),
//    PROCESS_MODULAR2_MODEL_SIMPLE(PROCESS_MODULAR2_MODEL, InConsumerAgentMPMWithAdoptionHandler.class),
//    PROCESS_MODULAR2_COMPONENTS(PROCESS_MODULAR2, IOConstants.PROCESS_MODULAR2_COMPONENTS),
//    PROCESS_MODULAR2_COMPONENTS_CALC(PROCESS_MODULAR2_COMPONENTS, IOConstants.PROCESS_MODULAR2_COMPONENTS_CALC),
//    PROCESS_MODULAR2_COMPONENTS_CALC_ADD(PROCESS_MODULAR2_COMPONENTS_CALC, InAddModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_INPUTATTR(PROCESS_MODULAR2_COMPONENTS_CALC, InAttributeInputModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_DISFIN(PROCESS_MODULAR2_COMPONENTS_CALC, InDisaggregatedFinancialModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_DISNPV(PROCESS_MODULAR2_COMPONENTS_CALC, InDisaggregatedNPVModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_ENVCON(PROCESS_MODULAR2_COMPONENTS_CALC, InEnvironmentalConcernModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_FINCOMP(PROCESS_MODULAR2_COMPONENTS_CALC, InFinancialComponentModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_LOGISTIC(PROCESS_MODULAR2_COMPONENTS_CALC, InLogisticModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_NOVSEEK(PROCESS_MODULAR2_COMPONENTS_CALC, InNoveltySeekingModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_NPV(PROCESS_MODULAR2_COMPONENTS_CALC, InNPVModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_PRODUCT(PROCESS_MODULAR2_COMPONENTS_CALC, InProductModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_PP(PROCESS_MODULAR2_COMPONENTS_CALC, InPurchasePowerModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_SHARELOCAL(PROCESS_MODULAR2_COMPONENTS_CALC, InShareOfAdopterInLocalNetworkModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_SHARESOCIAL(PROCESS_MODULAR2_COMPONENTS_CALC, InShareOfAdopterInSocialNetworkModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_SOCIALCOMP(PROCESS_MODULAR2_COMPONENTS_CALC, InSocialComponentModule_inputgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_SUM(PROCESS_MODULAR2_COMPONENTS_CALC, InSumModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTEDADD(PROCESS_MODULAR2_COMPONENTS_CALC, InWeightedAddModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_CALC_WEIGHTED(PROCESS_MODULAR2_COMPONENTS_CALC, InWeightedModule_calcgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL(PROCESS_MODULAR2_COMPONENTS, IOConstants.PROCESS_MODULAR2_COMPONENTS_EVAL),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_BRANCH(PROCESS_MODULAR2_COMPONENTS_EVAL, InBranchModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTACTION(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultActionModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTDECISION(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultDecisionMakingModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTFEAS(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultFeasibilityModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_DEFAULTINTEREST(PROCESS_MODULAR2_COMPONENTS_EVAL, InDefaultInterestModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_DONOTHING(PROCESS_MODULAR2_COMPONENTS_EVAL, InDoNothingModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_FILTER(PROCESS_MODULAR2_COMPONENTS_EVAL, InFilterModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_SIMPLEGET(PROCESS_MODULAR2_COMPONENTS_EVAL, InSimpleGetPhaseModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_STAGEEVAL(PROCESS_MODULAR2_COMPONENTS_EVAL, InStageEvaluationModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_SUMTHRESH(PROCESS_MODULAR2_COMPONENTS_EVAL, InSumThresholdEvaluationModule_evalgraphnode.class),
//    PROCESS_MODULAR2_COMPONENTS_EVAL_THRESH(PROCESS_MODULAR2_COMPONENTS_EVAL, InThresholdEvaluationModule_evalgraphnode.class),
    
    ;

    private static final List<TreeViewStructureEnum> PATHS = new ArrayList<>();
    private static final List<TreeViewStructureEnum> READ_PATHS = Collections.unmodifiableList(PATHS);

    static {
        for(TreeViewStructureEnum element: values()) {
            if(element.isNotNull()) {
                PATHS.add(element);
            }
        }
    }

    private final EdnPath PATH;

    TreeViewStructureEnum() {
        PATH = null;
    }

    TreeViewStructureEnum(TreeViewStructureEnum parent, String name) {
        if(parent == null) {
            PATH = new ListEdnPath(name);
        } else {
            PATH = parent.PATH.resolve(name);
        }
    }

    TreeViewStructureEnum(TreeViewStructureEnum parent, Class<?> c) {
        this(parent, c.getSimpleName());
    }

    public static List<TreeViewStructureEnum> getAllPaths() {
        return READ_PATHS;
    }

    private void checkNull() {
        if(isNull()) {
            throw new UnsupportedOperationException("null element");
        }
    }

    public boolean isNull() {
        return this == NULL;
    }

    public boolean isNotNull() {
        return !isNull();
    }

    public boolean isRoot() {
        return this == ROOT;
    }

    public boolean isNotRoot() {
        return isNotNull() && !isRoot();
    }

    public EdnPath getPath() {
        checkNull();
        return PATH;
    }
}
