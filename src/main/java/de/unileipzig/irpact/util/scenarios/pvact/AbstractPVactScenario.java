package de.unileipzig.irpact.util.scenarios.pvact;

import de.unileipzig.irpact.core.logging.IRPLevel;
import de.unileipzig.irpact.core.postprocessing.LazyData2FileLinker;
import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.core.spatial.twodim.Metric2D;
import de.unileipzig.irpact.core.postprocessing.image.SupportedEngine;
import de.unileipzig.irpact.io.param.input.InGeneral;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.affinity.InAffinities;
import de.unileipzig.irpact.io.param.input.affinity.InAffinityEntry;
import de.unileipzig.irpact.io.param.input.affinity.InComplexAffinityEntry;
import de.unileipzig.irpact.io.param.input.agent.consumer.InConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.consumer.InPVactConsumerAgentGroup;
import de.unileipzig.irpact.io.param.input.agent.population.InFileBasedPVactConsumerAgentPopulation;
import de.unileipzig.irpact.io.param.input.distribution.InDiracUnivariateDistribution;
import de.unileipzig.irpact.io.param.input.file.InFile;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.file.InSpatialTableFile;
import de.unileipzig.irpact.io.param.input.network.InUnlinkedGraphTopology;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irpact.io.param.input.postdata.InNeighbourhoodOverview;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.io.param.input.process.ra.InMaxDistanceNodeFilterDistanceScheme;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.*;
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
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.product.initial.InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData;
import de.unileipzig.irpact.io.param.input.visualisation.network.InConsumerAgentGroupColor;
import de.unileipzig.irpact.io.param.input.visualisation.result.InGenericOutputImage;
import de.unileipzig.irpact.io.param.input.names.InAttributeName;
import de.unileipzig.irpact.io.param.input.network.InFreeNetworkTopology;
import de.unileipzig.irpact.io.param.input.network.InNoDistance;
import de.unileipzig.irpact.io.param.input.network.InNumberOfTies;
import de.unileipzig.irpact.io.param.input.process.ra.InRAProcessModel;
import de.unileipzig.irpact.io.param.input.spatial.InSpace2D;
import de.unileipzig.irpact.io.param.input.spatial.dist.InFileBasedPVactMilieuSupplier;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistribution;
import de.unileipzig.irpact.io.param.input.time.InUnitStepDiscreteTimeModel;
import de.unileipzig.irpact.io.param.input.visualisation.result2.*;
import de.unileipzig.irpact.util.scenarios.AbstractScenario;
import de.unileipzig.irpact.util.scenarios.CorporateDesignUniLeipzig;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ModularProcessModelManager;

import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractPVactScenario extends AbstractScenario {

    protected static final String NPV_WEIGHT = "NPV_WEIGHT";
    protected static final String PP_WEIGHT = "PP_WEIGHT";
    protected static final String NOV_WEIGHT = "NOV_WEIGHT";
    protected static final String ENV_WEIGHT = "ENV_WEIGHT";
    protected static final String LOCAL_WEIGHT = "LOCAL_WEIGHT";
    protected static final String SOCIAL_WEIGHT = "SOCIAL_WEIGHT";
    protected static final String COMMUNICATION = "COMMU_ACTION";

    protected final InDiracUnivariateDistribution dirac0 = new InDiracUnivariateDistribution("dirac0", 0);
    protected final InDiracUnivariateDistribution dirac1 = new InDiracUnivariateDistribution("dirac1", 1);

    protected Map<String, InAttributeName> nameCache = new HashMap<>();

    protected String spatialDataName;
    protected String realAdoptionDataName;
    protected String pvDataName;

    @Deprecated
    protected int totalAgents = -1;
    protected boolean runPvAct = true;

    public AbstractPVactScenario() {
        super();
    }

    public AbstractPVactScenario(String name, String creator, String description) {
        super(name, creator, description);
    }

    public void setRunPvAct(boolean runPvAct) {
        this.runPvAct = runPvAct;
    }

    public boolean isRunPvAct() {
        return runPvAct;
    }

    @Deprecated
    public void setTotalAgents(int totalAgents) {
        this.totalAgents = totalAgents;
    }

    @Deprecated
    public int getTotalAgents() {
        return totalAgents;
    }

    public String getPVDataName() {
        return pvDataName;
    }
    public void setPvDataName(String pvDataName) {
        this.pvDataName = pvDataName;
    }
    public AbstractPVactScenario withPvDataName(String pvDataName) {
        setPvDataName(pvDataName);
        return this;
    }

    public String getSpatialFileName() {
        return spatialDataName;
    }
    public void setSpatialDataName(String spatialDataName) {
        this.spatialDataName = spatialDataName;
    }
    public AbstractPVactScenario withSpatialDataName(String spatialDataName) {
        setSpatialDataName(spatialDataName);
        return this;
    }

    public String getRealAdoptionDataName() {
        return realAdoptionDataName;
    }
    public void setRealAdoptionDataName(String realAdoptionDataName) {
        this.realAdoptionDataName = realAdoptionDataName;
    }
    public AbstractPVactScenario withRealAdoptionDataName(String realAdoptionDataName) {
        setRealAdoptionDataName(realAdoptionDataName);
        return this;
    }

    public AbstractPVactScenario withFiles(
            String pvDataName,
            String realAdoptionDataName,
            String spatialDataName) {
        return withPvDataName(pvDataName)
                .withRealAdoptionDataName(realAdoptionDataName)
                .withSpatialDataName(spatialDataName);
    }

    protected InPVFile pvFile;
    public InPVFile getPVFile() {
        if(pvFile == null) {
            pvFile = new InPVFile(getPVDataName());
        }
        return pvFile;
    }
    public InPVFile computePVFileIfAbsent() {
        if(isCached(getPVDataName())) {
            return getCached(getPVDataName());
        } else {
            InPVFile pvFile = getPVFile();
            cache(getPVDataName(), pvFile);
            return pvFile;
        }
    }

    protected InSpatialTableFile spatialTableFile;
    public InSpatialTableFile getSpatialFile() {
        if(spatialTableFile == null) {
            spatialTableFile = new InSpatialTableFile(getSpatialFileName());
            spatialTableFile.setCoverage(1.0);
        }
        return spatialTableFile;
    }
    public InSpatialTableFile computeSpatialFileIfAbsent() {
        if(isCached(getSpatialFileName())) {
            return getCached(getSpatialFileName());
        } else {
            InSpatialTableFile spatialTableFile = getSpatialFile();
            cache(getSpatialFileName(), spatialTableFile);
            return spatialTableFile;
        }
    }

    protected InRealAdoptionDataFile realAdoptionFile;
    public InRealAdoptionDataFile getRealAdoptionDataFile() {
        if(realAdoptionFile == null) {
            realAdoptionFile = new InRealAdoptionDataFile(getRealAdoptionDataName());
        }
        return realAdoptionFile;
    }
    public InRealAdoptionDataFile computeRealAdoptionDataFileIfAbsent() {
        if(isCached(getRealAdoptionDataName())) {
            return getCached(getRealAdoptionDataName());
        } else {
            InRealAdoptionDataFile realAdoptionFile = getRealAdoptionDataFile();
            cache(getRealAdoptionDataName(), realAdoptionFile);
            return realAdoptionFile;
        }
    }

    public InAttributeName getAttribute(String text) {
        return nameCache.computeIfAbsent(text, InAttributeName::new);
    }

    public InSpace2D createSpace2D(String name) {
        return new InSpace2D(name, Metric2D.HAVERSINE_KM);
    }

    public InGeneral createGeneralPart() {
        InGeneral general = new InGeneral();
        general.seed = 42;
        general.timeout = TimeUnit.MINUTES.toMillis(1);
        general.runOptActDemo = false;
        general.runPVAct = true;
        general.setLogLevel(IRPLevel.INFO);
        general.logAllIRPact = true;
        general.enableAllResultLogging();
        general.setFirstSimulationYear(2015);
        general.lastSimulationYear = 2015;
        return general;
    }

    protected static final String DEFAULT_CONSUMER_INIT_ADOPTER = "DEFAULT_CONSUMER_INIT_ADOPTER";
    protected InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData getDefaultPVactFileBasedInitialAdopter() {
        if(isCached(DEFAULT_CONSUMER_INIT_ADOPTER)) {
            return getCached(DEFAULT_CONSUMER_INIT_ADOPTER);
        } else {
            InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData initAdopter = new InPVactFileBasedConsumerGroupBasedInitialAdoptionWithRealData();
            initAdopter.setName(DEFAULT_CONSUMER_INIT_ADOPTER);
            initAdopter.setFile(getRealAdoptionDataFile());
            cache(initAdopter.getName(), initAdopter);
            return initAdopter;
        }
    }

    protected static final String DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER = "DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER";
    protected InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData getDefaultPVactFileBasedWeightedInitialAdopter() {
        if(isCached(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER)) {
            return getCached(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER);
        } else {
            InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData initAdopter = new InPVactFileBasedWeightedConsumerGroupBasedInitialAdoptionWithRealData();
            initAdopter.setName(DEFAULT_WEIGHTED_CONSUMER_INIT_ADOPTER);
            initAdopter.setFile(getRealAdoptionDataFile());
            initAdopter.setScale(true);
            initAdopter.setFixError(true);
            cache(initAdopter.getName(), initAdopter);
            return initAdopter;
        }
    }

    @Override
    public void setupGeneral(InGeneral general) {
        super.setupGeneral(general);
        general.runPVAct = runPvAct;
        general.runOptActDemo = false;
    }

    @Override
    public void setupRoot(InRoot root) {
        super.setupRoot(root);
        List<InAttributeName> attributes = new ArrayList<>();
        for(String attr: RAConstants.DEFAULT_ATTRIBUTES) {
            attributes.add(getAttribute(attr));
        }
        root.setAttributeNames(attributes);
    }

    public static void setColors(InRoot root, InConsumerAgentGroup... cags) {
        InConsumerAgentGroupColor[] colors = InConsumerAgentGroupColor.copyColors();
        InConsumerAgentGroupColor.roundRobin(colors, cags);
        root.setConsumerAgentGroupColors(colors);
    }

    public InGenericOutputImage[] createDefaultImages() {
        InGenericOutputImage[] defaults = InGenericOutputImage.createDefaultImages(computeRealAdoptionDataFileIfAbsent());
        InGenericOutputImage.setEnableAll(true, defaults);
        InGenericOutputImage.setEngine(SupportedEngine.GNUPLOT, defaults);
        return defaults;
    }

    public InPVactUpdatableGlobalModerateExtremistUncertainty createInPVactUpdatableGlobalModerateExtremistUncertainty(
            String name,
            double extremParam,
            double extremUncert,
            double moderateUncert) {
        InPVactUpdatableGlobalModerateExtremistUncertainty uncertainty = new InPVactUpdatableGlobalModerateExtremistUncertainty();
        uncertainty.setName(name);
        uncertainty.setExtremistParameter(extremParam);
        uncertainty.setExtremistUncertainty(extremUncert);
        uncertainty.setModerateUncertainty(moderateUncert);
        uncertainty.setLowerBoundInclusive(true);
        uncertainty.setUpperBoundInclusive(true);
        return uncertainty;
    }

    public InPVactIndividualGlobalModerateExtremistUncertaintySupplier createInPVactIndividualGlobalModerateExtremistUncertaintySupplier(
            String name,
            double extremParam,
            double extremUncert,
            double moderateUncert) {
        InPVactIndividualGlobalModerateExtremistUncertaintySupplier uncertainty = new InPVactIndividualGlobalModerateExtremistUncertaintySupplier();
        uncertainty.setName(name);
        uncertainty.setExtremistParameter(extremParam);
        uncertainty.setExtremistUncertainty(extremUncert);
        uncertainty.setModerateUncertainty(moderateUncert);
        uncertainty.setLowerBoundInclusive(true);
        uncertainty.setUpperBoundInclusive(true);
        return uncertainty;
    }

    public InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion createInPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion(
            String name,
            double extremParam,
            double extremUncert,
            double moderateUncert) {
        InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion uncertainty = new InPVactGlobalModerateExtremistUncertaintyWithUpdatableOpinion();
        uncertainty.setName(name);
        uncertainty.setExtremistParameter(extremParam);
        uncertainty.setExtremistUncertainty(extremUncert);
        uncertainty.setModerateUncertainty(moderateUncert);
        uncertainty.setLowerBoundInclusive(true);
        uncertainty.setUpperBoundInclusive(true);
        return uncertainty;
    }

    public InUpdatableGlobalModerateExtremistUncertainty createGlobalUnvertaintySupplier(
            String name,
            double extremParam,
            double extremUncert,
            double moderateUncert,
            String[] attrs) {
        InUpdatableGlobalModerateExtremistUncertainty uncertainty = new InUpdatableGlobalModerateExtremistUncertainty();
        uncertainty.setName(name);
        uncertainty.setExtremistParameter(extremParam);
        uncertainty.setExtremistUncertainty(extremUncert);
        uncertainty.setModerateUncertainty(moderateUncert);
        uncertainty.setAttributeNames(getAttributeNames(attrs));
        uncertainty.setLowerBoundInclusive(true);
        uncertainty.setUpperBoundInclusive(true);
        return uncertainty;
    }

    public InMaxDistanceNodeFilterDistanceScheme createNodeFilterScheme(double distance) {
        InMaxDistanceNodeFilterDistanceScheme scheme = new InMaxDistanceNodeFilterDistanceScheme();
        scheme.setName("MAX_DISTANCE_SCHEME");
        scheme.setMaxDistance(distance);
        return scheme;
    }

    public InRAProcessModel createDefaultProcessModel(String name, InUncertaintySupplier uncertainty, double speedOfConvergence) {
        return createDefaultProcessModel(name, uncertainty, speedOfConvergence, null);
    }

    public InRAProcessModel createDefaultProcessModel(String name, InUncertaintySupplier uncertainty, double speedOfConvergence, InNodeDistanceFilterScheme scheme) {
        InRAProcessModel processModel = new InRAProcessModel();
        processModel.setName(name);
        processModel.setDefaultValues();
        processModel.setPvFile(getPVFile());
        processModel.setUncertainty(uncertainty);
        processModel.setSpeedOfConvergence(speedOfConvergence);
        processModel.setSkipAwareness(false);
        processModel.setSkipFeasibility(false);
        processModel.setForceEvaluate(true);
        processModel.setAdoptionCertaintyBase(1.0);
        processModel.setAdoptionCertaintyFactor(1.0);
        processModel.setNodeFilterScheme(scheme);
        return processModel;
    }

    public InBasicCAModularProcessModel createDefaultModularProcessModel(
            String name,
            InUncertaintySupplier uncertainty,
            InNodeDistanceFilterScheme scheme,
            List<InOutputImage2> images,
            List<InPostDataAnalysis> postDatas) {
        return createDefaultModularProcessModel(name, uncertainty, scheme, images, postDatas, new ModularProcessModelManager());
    }

    public InCsvValueLoggingModule_calcloggraphnode2 createDefaultLoggingModule(String name) {
        InCsvValueLoggingModule_calcloggraphnode2 module = new InCsvValueLoggingModule_calcloggraphnode2();
        module.setName(name);
        module.setPrintHeader(true);
        module.setLogDefaultCall(true);
        module.setLogReevaluatorCall(false);
        module.setStoreXlsx(true);
        return module;
    }

    public InCsvValueLoggingModule_calcloggraphnode2 createReevaluatorLoggingModule(String name) {
        InCsvValueLoggingModule_calcloggraphnode2 module = new InCsvValueLoggingModule_calcloggraphnode2();
        module.setName(name);
        module.setPrintHeader(true);
        module.setLogDefaultCall(false);
        module.setLogReevaluatorCall(true);
        module.setStoreXlsx(true);
        return module;
    }

    protected void setupCommunicationModuleLogging(InCommunicationModule_actiongraphnode2 module) {
        module.setRaOpinionLogging(false);
        module.setRaUnceraintyLogging(false);
    }

    public InBasicCAModularProcessModel createDefaultModularProcessModel(
            String name,
            InUncertaintySupplier uncertainty,
            InNodeDistanceFilterScheme scheme,
            List<InOutputImage2> images,
            List<InPostDataAnalysis> postDatas,
            ModularProcessModelManager mmp) {

        //ACTION
        InAttributeInputModule_inputgraphnode2 commuAttr = mmp.create("ATTR_COMMUNICATION", InAttributeInputModule_inputgraphnode2::new);
        commuAttr.setAttribute(getAttribute(RAConstants.COMMUNICATION_FREQUENCY_SN));
        InMulScalarModule_calcgraphnode2 commuAttrWeight = mmp.create("FACTOR_COMMUNICATION", InMulScalarModule_calcgraphnode2::new);
        commuAttrWeight.setScalar(1.0);
        commuAttrWeight.setInput(commuAttr);
        InBernoulliModule_boolgraphnode2 commuIf = mmp.create("TEST_COMMUNICATION", InBernoulliModule_boolgraphnode2::new);
        commuIf.setInput(commuAttrWeight);
        InCommunicationModule_actiongraphnode2 commuAction = mmp.create(COMMUNICATION, InCommunicationModule_actiongraphnode2::new);
        commuAction.setRaEnabled(true);
        commuAction.setRaLoggingEnabled(true);
        commuAction.setRaStoreXlsx(true);
        commuAction.setRaKeepCsv(false);
        commuAction.setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        commuAction.setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        commuAction.setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        commuAction.setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
        commuAction.setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        commuAction.setAttitudeGap(RAConstants.DEFAULT_ATTIDUTE_GAP);
        commuAction.setChanceNeutral(RAConstants.DEFAULT_NEUTRAL_CHANCE);
        commuAction.setChanceConvergence(RAConstants.DEFAULT_CONVERGENCE_CHANCE);
        commuAction.setChanceDivergence(RAConstants.DEFAULT_DIVERGENCE_CHANCE);
        commuAction.setUncertaintySupplier(uncertainty);
        setupCommunicationModuleLogging(commuAction);

        InAttributeInputModule_inputgraphnode2 rewireAttr = mmp.create("ATTR_REWIRE", InAttributeInputModule_inputgraphnode2::new);
        rewireAttr.setAttribute(getAttribute(RAConstants.REWIRING_RATE));
        InMulScalarModule_calcgraphnode2 rewireAttrWeight = mmp.create("FACTOR_REWIRE", InMulScalarModule_calcgraphnode2::new);
        rewireAttrWeight.setScalar(1.0);
        rewireAttrWeight.setInput(rewireAttr);
        InBernoulliModule_boolgraphnode2 rewireIf = mmp.create("TEST_REWIRE", InBernoulliModule_boolgraphnode2::new);
        rewireIf.setInput(rewireAttrWeight);
        InRewireModule_actiongraphnode2 rewireAction = mmp.create("REWIRE_ACTION", InRewireModule_actiongraphnode2::new);

        InNOP_actiongraphnode2 nop = mmp.create("NOP", InNOP_actiongraphnode2::new);

        InIfElseActionModule_actiongraphnode2 ifElseRewire = mmp.create("IF_ELSE_REWIRE", InIfElseActionModule_actiongraphnode2::new);
        ifElseRewire.setTestModule(rewireIf);
        ifElseRewire.setOnTrueModule(rewireAction);
        ifElseRewire.setOnFalseModule(nop);

        InIfElseActionModule_actiongraphnode2 ifElseCommu = mmp.create("IF_ELSE_COMMU", InIfElseActionModule_actiongraphnode2::new);
        ifElseCommu.setTestModule(commuIf);
        ifElseCommu.setOnTrueModule(commuAction);
        ifElseCommu.setOnFalseModule(ifElseRewire);

        //INIT
        InInitializationModule_evalragraphnode2 init = mmp.create("INIT", InInitializationModule_evalragraphnode2::new);

        //INTEREST
        InInterestModule_evalragraphnode2 interest = mmp.create("INTEREST", InInterestModule_evalragraphnode2::new);
        interest.setInput(ifElseCommu);

        //FEASIBILITY
        InFeasibilityModule_evalragraphnode2 feasibility = mmp.create("FEASIBITLITY", InFeasibilityModule_evalragraphnode2::new);
        feasibility.setInput(ifElseCommu);

        //===
        //DECISION

        //npv
        InGlobalAvgNPVModule_inputgraphnode2 avgNPV = mmp.create("AVG_NPV", InGlobalAvgNPVModule_inputgraphnode2::new);
        avgNPV.setPvFile(getPVFile());
        InNPVModule_inputgraphnode2 npv = mmp.create("NPV", InNPVModule_inputgraphnode2::new);
        npv.setPvFile(getPVFile());
        InLogisticModule_calcgraphnode2 logisticNPV = mmp.create("LOGISTIC_NPV", InLogisticModule_calcgraphnode2::new);
        logisticNPV.setValueL(1.0);
        logisticNPV.setValueK(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        logisticNPV.setXInput(npv);
        logisticNPV.setX0Input(avgNPV);

        InCsvValueLoggingModule_calcloggraphnode2 npvLogger = mmp.create(LazyData2FileLinker.NPV_LOGGER, this::createDefaultLoggingModule);
        npvLogger.setInput(logisticNPV);
        InCsvValueLoggingModule_calcloggraphnode2 npvReevalLogger = mmp.create(LazyData2FileLinker.NPV_REEVAL, this::createReevaluatorLoggingModule);
        npvReevalLogger.setInput(logisticNPV);

        //pp
        InAttributeInputModule_inputgraphnode2 pp = mmp.create("PP", InAttributeInputModule_inputgraphnode2::new);
        pp.setAttribute(getAttribute(RAConstants.PURCHASE_POWER_EUR));
        InAvgFinModule_inputgraphnode2 avgPP = mmp.create("AVG_PP", InAvgFinModule_inputgraphnode2::new);
        InLogisticModule_calcgraphnode2 logisticPP = mmp.create("LOGISTIC_PP", InLogisticModule_calcgraphnode2::new);
        logisticPP.setValueL(1.0);
        logisticPP.setValueK(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        logisticPP.setXInput(pp);
        logisticPP.setX0Input(avgPP);

        //fin comp
        InMulScalarModule_calcgraphnode2 npvWeight = mmp.create(NPV_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        npvWeight.setScalar(RealData.WEIGHT_NPV);
        npvWeight.setInput(npvLogger);
        InMulScalarModule_calcgraphnode2 ppWeight = mmp.create(PP_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        ppWeight.setScalar(RealData.WEIGHT_EK);
        ppWeight.setInput(logisticPP);
        InSumModule_calcgraphnode2 finComp = mmp.create("FIN_COMPONENT", InSumModule_calcgraphnode2::new);
        finComp.setInput(npvWeight, ppWeight);

        //env comp
        InAttributeInputModule_inputgraphnode2 envAttr = mmp.create("ENV", InAttributeInputModule_inputgraphnode2::new);
        envAttr.setAttribute(getAttribute(RAConstants.ENVIRONMENTAL_CONCERN));
        InCsvValueLoggingModule_calcloggraphnode2 envLogger = mmp.create(LazyData2FileLinker.ENV_LOGGER, this::createDefaultLoggingModule);
        envLogger.setInput(envAttr);
        InCsvValueLoggingModule_calcloggraphnode2 envReevalLogger = mmp.create(LazyData2FileLinker.ENV_REEVAL, this::createReevaluatorLoggingModule);
        envReevalLogger.setInput(envAttr);
        InMulScalarModule_calcgraphnode2 envWeight = mmp.create(ENV_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        envWeight.setScalar(RealData.WEIGHT_NEP);
        envWeight.setInput(envLogger);

        //nov comp
        InAttributeInputModule_inputgraphnode2 novAttr = mmp.create("NOV", InAttributeInputModule_inputgraphnode2::new);
        novAttr.setAttribute(getAttribute(RAConstants.NOVELTY_SEEKING));
        InCsvValueLoggingModule_calcloggraphnode2 novLogger = mmp.create(LazyData2FileLinker.NOV_LOGGER, this::createDefaultLoggingModule);
        novLogger.setInput(novAttr);
        InCsvValueLoggingModule_calcloggraphnode2 novReevalLogger = mmp.create(LazyData2FileLinker.NOV_REEVAL, this::createReevaluatorLoggingModule);
        novReevalLogger.setInput(novAttr);
        InMulScalarModule_calcgraphnode2 novWeight = mmp.create(NOV_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        novWeight.setScalar(RealData.WEIGHT_NS);
        novWeight.setInput(novLogger);

        //soc
        InLocalShareOfAdopterModule_inputgraphnode2 localShare = mmp.create("LOCAL_SHARE", InLocalShareOfAdopterModule_inputgraphnode2::new);
        localShare.setMaxToStore(2000);
        localShare.setNodeFilterScheme(scheme);
        InCsvValueLoggingModule_calcloggraphnode2 localLogger = mmp.create(LazyData2FileLinker.LOCAL_LOGGER, this::createDefaultLoggingModule);
        localLogger.setInput(localShare);
        InCsvValueLoggingModule_calcloggraphnode2 localReevalLogger = mmp.create(LazyData2FileLinker.LOCAL_REEVAL, this::createReevaluatorLoggingModule);
        localReevalLogger.setInput(localShare);

        InSocialShareOfAdopterModule_inputgraphnode2 socialShare = mmp.create("SOCIAL_SHARE", InSocialShareOfAdopterModule_inputgraphnode2::new);
        InCsvValueLoggingModule_calcloggraphnode2 socialLogger = mmp.create(LazyData2FileLinker.SOCIAL_LOGGER, this::createDefaultLoggingModule);
        socialLogger.setInput(socialShare);
        InCsvValueLoggingModule_calcloggraphnode2 socialReevalLogger = mmp.create(LazyData2FileLinker.SOCIAL_REEVAL, this::createReevaluatorLoggingModule);
        socialReevalLogger.setInput(socialShare);

        InMulScalarModule_calcgraphnode2 localWeight = mmp.create(LOCAL_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        localWeight.setScalar(RealData.WEIGHT_LOCALE);
        localWeight.setInput(localLogger);
        InMulScalarModule_calcgraphnode2 socialWeight = mmp.create(SOCIAL_WEIGHT, InMulScalarModule_calcgraphnode2::new);
        socialWeight.setScalar(RealData.WEIGHT_SOCIAL);
        socialWeight.setInput(socialLogger);
        InSumModule_calcgraphnode2 socComp = mmp.create("SOC_COMPONENT", InSumModule_calcgraphnode2::new);
        socComp.setInput(localWeight, socialWeight);

        //decision
        InAttributeInputModule_inputgraphnode2 finThreshold = mmp.create("FIN_THRESHOLD", InAttributeInputModule_inputgraphnode2::new);
        finThreshold.setAttribute(getAttribute(RAConstants.FINANCIAL_THRESHOLD));

        InThresholdReachedModule_boolgraphnode2 finCheck = mmp.create("FIN_CHECK", InThresholdReachedModule_boolgraphnode2::new);
        finCheck.setDraw(pp);
        finCheck.setThreshold(finThreshold);

        InAttributeInputModule_inputgraphnode2 adoptThreshold = mmp.create("ADOPT_THRESHOLD", InAttributeInputModule_inputgraphnode2::new);
        adoptThreshold.setAttribute(getAttribute(RAConstants.ADOPTION_THRESHOLD));

        InSumModule_calcgraphnode2 utilitySum = mmp.create("UTILITY_SUM", InSumModule_calcgraphnode2::new);
        utilitySum.setInput(
                finComp,
                envWeight,
                novWeight,
                socComp
        );

        InCsvValueLoggingModule_calcloggraphnode2 utilityLogger = mmp.create(LazyData2FileLinker.UTILITY_LOGGER, this::createDefaultLoggingModule);
        utilityLogger.setInput(utilitySum);

        InCsvValueLoggingModule_calcloggraphnode2 utilityReevalLogger = mmp.create(LazyData2FileLinker.UTILITY_REEVAL, this::createReevaluatorLoggingModule);
        utilityReevalLogger.setInput(utilitySum);

        InDecisionMakingDeciderModule2_evalragraphnode2 decisionMaking = mmp.create("DECISION_MAKING", InDecisionMakingDeciderModule2_evalragraphnode2::new);
        decisionMaking.setFinCheck(finCheck);
        decisionMaking.setThreshold(adoptThreshold);
        decisionMaking.setUtility(utilityLogger);

        InYearBasedAdoptionDeciderModule_evalragraphnode2 reallyDecider = mmp.create("REALLY_ADOPT_TEST", InYearBasedAdoptionDeciderModule_evalragraphnode2::new);
        reallyDecider.setBase(1);
        reallyDecider.setFactor(0);
        reallyDecider.setInput(decisionMaking);

        InDoAdoptModule_evalragraphnode2 adoptModule = mmp.create("ADOPT_IF_POSSIBLE", InDoAdoptModule_evalragraphnode2::new);
        adoptModule.setInput(reallyDecider);

        //MAIN
        InMainBranchingModule_evalragraphnode2 mainBranch = mmp.create("CORE", InMainBranchingModule_evalragraphnode2::new);
        mainBranch.setInit(init);
        mainBranch.setAwareness(interest);
        mainBranch.setFeasibility(feasibility);
        mainBranch.setDecision(adoptModule);
        mainBranch.setImpeded(ifElseCommu);
        mainBranch.setAdopted(ifElseCommu);

        InPhaseLoggingModule_evalragraphnode2 phaseLogger = mmp.create("PHASE_LOGGER", InPhaseLoggingModule_evalragraphnode2::new);
        phaseLogger.setInput(mainBranch);

        InPhaseUpdateModule_evalragraphnode2 phaseUpdater = mmp.create("PHASE_UPDATER", InPhaseUpdateModule_evalragraphnode2::new);
        phaseUpdater.setInput(phaseLogger);

        //PROCESS MODEL
        InRunUntilFailureModule_evalgraphnode2 startModule = mmp.create("START", InRunUntilFailureModule_evalgraphnode2::new);
        startModule.setInput(phaseUpdater);

        InBasicCAModularProcessModel processModel = new InBasicCAModularProcessModel();
        processModel.setName(name);
        processModel.setStartModule(startModule);

        //INIT
        InAgentAttributeScaler novScaler = new InAgentAttributeScaler();
        novScaler.setName("NOV_SCALER");
        novScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        novScaler.setAttribute(getAttribute(RAConstants.NOVELTY_SEEKING));

        InLinearePercentageAgentAttributeScaler envScaler = new InLinearePercentageAgentAttributeScaler();
        envScaler.setName("ENV_SCALER");
        envScaler.setM(RAConstants.DEFAULT_M);
        envScaler.setN(RAConstants.DEFAULT_N);
        envScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        envScaler.setAttribute(getAttribute(RAConstants.ENVIRONMENTAL_CONCERN));

        InLinearePercentageAgentAttributeUpdater envUpdater = new InLinearePercentageAgentAttributeUpdater();
        envUpdater.setName("ENV_UPDATER");
        envUpdater.setScaler(envScaler);

        InUncertaintySupplierInitializer uncertInit = new InUncertaintySupplierInitializer();
        uncertInit.setName("UNCERT_INIT");
        uncertInit.setPriority(InitializationHandler.LOW_PRIORITY);
        uncertInit.setUncertaintySuppliers(uncertainty);

        processModel.addInitializationHandlers(
                novScaler,
                envScaler,
                uncertInit
        );

        //NEW PRODUCT
        processModel.addNewProductHandlers(
                getDefaultPVactFileBasedWeightedInitialAdopter()
        );

        //START OF YEAR
        InImpededResetter impededResetter = new InImpededResetter();
        impededResetter.setName("IMPEDED_RESETTER");

        InUncertaintyReevaluator uncertUpdater = new InUncertaintyReevaluator();
        uncertUpdater.setName("UNCERT_UPDATER");
        uncertUpdater.setPriorty(Reevaluator.LOW_PRIORITY);
        uncertUpdater.setUncertaintySuppliers(uncertainty);

        processModel.addStartOfYearReevaluators(
                impededResetter,
                uncertUpdater
        );

        //MID OF YEAR
        InConstructionRenovationUpdater constructionRenovationUpdater = new InConstructionRenovationUpdater();
        constructionRenovationUpdater.setName("CONSTRUCTION_RENOVATION_UPDATER");

        processModel.addMidOfYearReevaluators(
                constructionRenovationUpdater,
                envUpdater
        );

        //END OF YEAR
        InPhaseLoggingModule_evalragraphnode2 decisionReevalPhaseLogger = mmp.create("PHASE_REEVAL_LOGGER", InPhaseLoggingModule_evalragraphnode2::new);
        decisionReevalPhaseLogger.setInput(reallyDecider);

        InPhaseUpdateModule_evalragraphnode2 decisionReevalPhaseUpdater = mmp.create("PHASE_REEVAL_UPDATER", InPhaseUpdateModule_evalragraphnode2::new);
        decisionReevalPhaseUpdater.setInput(decisionReevalPhaseLogger);

        InAnnualInterestLogger annualInterestLogger = new InAnnualInterestLogger();
        annualInterestLogger.setName("ANNUAL_INTEREST_LOGGER");
        InDecisionMakingReevaluator decisionMakingReevaluator = new InDecisionMakingReevaluator();
        decisionMakingReevaluator.setName("DECISION_REEVALUATOR");
        decisionMakingReevaluator.setModules(decisionReevalPhaseUpdater);

        InReevaluatorModule_reevalgraphnode2 reevalNode = mmp.create("REEVAL", InReevaluatorModule_reevalgraphnode2::new);
        reevalNode.setInput(
                npvReevalLogger,
                envReevalLogger,
                novReevalLogger,
                socialReevalLogger,
                localReevalLogger,
                utilityReevalLogger
        );

        InMultiReevaluator initLinker = new InMultiReevaluator();
        initLinker.setName("INIT_LINKER");
        initLinker.setModules(
                reevalNode
        );

        processModel.addInitializationReevaluators(
                initLinker
        );

        InMultiReevaluator endOfYearLinker = new InMultiReevaluator();
        endOfYearLinker.setName("END_OF_YEAR_LINKER");
        endOfYearLinker.setModules(
                reevalNode
        );

        processModel.addEndOfYearReevaluator(
                annualInterestLogger,
                decisionMakingReevaluator,
                endOfYearLinker
        );

        //logging
        InSpecialAverageQuantilRangeImage novQuantile1 = InSpecialAverageQuantilRangeImage.NOV;
        novQuantile1.setLoggingModule(novReevalLogger);
        images.add(novQuantile1);

        InSpecialAverageQuantilRangeImage envQuantile = InSpecialAverageQuantilRangeImage.ENV;
        envQuantile.setLoggingModule(envReevalLogger);
        images.add(envQuantile);

        InSpecialAverageQuantilRangeImage npvQuantile = InSpecialAverageQuantilRangeImage.NPV;
        npvQuantile.setLoggingModule(npvReevalLogger);
        images.add(npvQuantile);

        InSpecialAverageQuantilRangeImage socialQuantil = InSpecialAverageQuantilRangeImage.SOCIAL;
        socialQuantil.setLoggingModule(socialReevalLogger);
        images.add(socialQuantil);

        InSpecialAverageQuantilRangeImage localQuantil = InSpecialAverageQuantilRangeImage.LOCAL;
        localQuantil.setLoggingModule(localReevalLogger);
        images.add(localQuantil);

        InSpecialAverageQuantilRangeImage utilityQuantil = InSpecialAverageQuantilRangeImage.UTILITY;
        utilityQuantil.setLoggingModule(utilityReevalLogger);
        images.add(utilityQuantil);

        InAnnualBucketImage npvBucket = InAnnualBucketImage.NPV;
        npvBucket.setLoggingModule(npvReevalLogger);
        npvBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(npvBucket);

        InAnnualBucketImage envBucket = InAnnualBucketImage.ENV;
        envBucket.setLoggingModule(envReevalLogger);
        envBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(envBucket);

        InAnnualBucketImage novBucket = InAnnualBucketImage.NOV;
        novBucket.setLoggingModule(novReevalLogger);
        novBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(novBucket);

        InAnnualBucketImage socialBucket = InAnnualBucketImage.SOCIAL;
        socialBucket.setLoggingModule(socialReevalLogger);
        socialBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(socialBucket);

        InAnnualBucketImage localBucket = InAnnualBucketImage.LOCAL;
        localBucket.setLoggingModule(localReevalLogger);
        localBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(localBucket);

        InAnnualBucketImage utlityBucket = InAnnualBucketImage.UTILITY;
        utlityBucket.setLoggingModule(utilityReevalLogger);
        utlityBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        images.add(utlityBucket);

        //Custom-Test
//        InQuantileRange qr0 = new InQuantileRange();
//        qr0.setName("QR0");
//        qr0.setLowerBound(0.0);
//        qr0.setUpperBound(0.5);
//        InQuantileRange qr1 = new InQuantileRange();
//        qr1.setName("QR1");
//        qr1.setLowerBound(0.5);
//        qr1.setUpperBound(1.0);
//
//        InCustomAverageQuantilRangeImage customNovQuantile1 = new InCustomAverageQuantilRangeImage();
//        customNovQuantile1.setName("CUSTOM_NOV1");
//        customNovQuantile1.setCustomImageId(1);
//        customNovQuantile1.setQuantileRanges(qr0, qr1);
//        customNovQuantile1.setLoggingModule(novLogger);
//        images.add(customNovQuantile1);
//
//        InCustomAverageQuantilRangeImage customNovQuantile2 = new InCustomAverageQuantilRangeImage();
//        customNovQuantile2.setName("CUSTOM_NOV2");
//        customNovQuantile2.setCustomImageId(2);
//        customNovQuantile2.setQuantileRanges(qr0, qr1);
//        customNovQuantile2.setLoggingModule(novReevalLogger);
//        images.add(customNovQuantile2);

        //Adoption Phase Overview
        InAdoptionPhaseOverviewImage adoptionPhaseOverview = InAdoptionPhaseOverviewImage.DEFAULT;
        images.add(adoptionPhaseOverview);

        //Compared Annual
        InComparedAnnualImage annualImage = InComparedAnnualImage.DEFAULT;
        annualImage.setRealData(getRealAdoptionDataFile());
        images.add(annualImage);

        //Compared Annual Zip
        InComparedAnnualZipImage annualZipImage = InComparedAnnualZipImage.DEFAULT;
        annualZipImage.setRealData(getRealAdoptionDataFile());
        images.add(annualZipImage);

        //Interest
        InInterestOverviewImage interestOverview = InInterestOverviewImage.DEFAULT;
        images.add(interestOverview);

        //Process Phase Overview
        InProcessPhaseOverviewImage processPhaseOverview = InProcessPhaseOverviewImage.DEFAULT;
        images.add(processPhaseOverview);

        //bucket
        InBucketAnalyser novBucketData = new InBucketAnalyser();
        novBucketData.setName("NOV_BUCKET");
        novBucketData.setBucketRange(0.1);
        novBucketData.setLoggingModule(novLogger);
        postDatas.add(novBucketData);

        InBucketAnalyser envBucketData = new InBucketAnalyser();
        envBucketData.setName("ENV_BUCKET");
        envBucketData.setBucketRange(0.01);
        envBucketData.setLoggingModule(envLogger);
        postDatas.add(envBucketData);

        //neighborhood
        InNeighbourhoodOverview neighbourhoodOverview = new InNeighbourhoodOverview();
        neighbourhoodOverview.setName("NEIGHBOURHOOD");
        neighbourhoodOverview.setEnabled(true);
        neighbourhoodOverview.setStoreXlsx(true);
        neighbourhoodOverview.setNodeFilterScheme(scheme);
        postDatas.add(neighbourhoodOverview);

        //===
        return processModel;
    }


    public InUnitStepDiscreteTimeModel createOneWeekTimeModel(String name) {
        InUnitStepDiscreteTimeModel timeModel = new InUnitStepDiscreteTimeModel();
        timeModel.setName(name);
        timeModel.setAmountOfTime(1);
        timeModel.setUnit(ChronoUnit.WEEKS);
        return timeModel;
    }

    public InFileBasedPVactMilieuSupplier createSpatialDistribution(String name) {
        InFileBasedPVactMilieuSupplier supplier = new InFileBasedPVactMilieuSupplier();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        return supplier;
    }

    public InUnlinkedGraphTopology createUnlinkedTopology(String name) {
        return new InUnlinkedGraphTopology(name);
    }

    public InFreeNetworkTopology createFreeTopology(
            String name,
            InAffinities affinities,
            InConsumerAgentGroup[] cags,
            int numberOfEdges) {
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName(name);
        topology.setAffinities(affinities);
        topology.setNumberOfTies(new InNumberOfTies(name + "_ties", cags, numberOfEdges));
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setInitialWeight(1);
        topology.setAllowLessEdges(false);
        return topology;
    }

    public InFreeNetworkTopology createFreeTopology(
            String name,
            InAffinities affinities,
            Map<? extends InConsumerAgentGroup, Integer> edgeCount) {
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName(name);
        topology.setAffinities(affinities);
        topology.setNumberOfTies(createTies(edgeCount));
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setInitialWeight(1);
        topology.setAllowLessEdges(false);
        return topology;
    }

    public InFreeNetworkTopology createFreeTopology(
            String name,
            InAffinities affinities,
            InConsumerAgentGroup[] cags,
            int[] edgeCount) {
        InFreeNetworkTopology topology = new InFreeNetworkTopology();
        topology.setName(name);
        topology.setAffinities(affinities);
        topology.setNumberOfTies(createTies(cags, edgeCount));
        topology.setDistanceEvaluator(new InNoDistance("NoDist"));
        topology.setInitialWeight(1);
        topology.setAllowLessEdges(false);
        return topology;
    }

    public InNumberOfTies[] createTies(Map<? extends InConsumerAgentGroup, Integer> edgeCount) {
        InNumberOfTies[] ties = new InNumberOfTies[edgeCount.size()];
        int i = 0;
        for(Map.Entry<? extends InConsumerAgentGroup, Integer> entry: edgeCount.entrySet()) {
            InConsumerAgentGroup cag = entry.getKey();
            int count = entry.getValue();
            ties[i++] = new InNumberOfTies(cag.getName() + "_EDGES", cag, count);
        }
        return ties;
    }

    public InNumberOfTies[] createTies(InConsumerAgentGroup[] cags, int[] edgeCount) {
        InNumberOfTies[] ties = new InNumberOfTies[cags.length];
        for(int i = 0; i < cags.length; i++) {
            InConsumerAgentGroup cag = cags[i];
            int count = edgeCount[i];
            ties[i] = new InNumberOfTies(cag.getName() + "_EDGES", cag, count);
        }
        return ties;
    }

    public InFileBasedPVactConsumerAgentPopulation createPopulation(String name, int agents, InConsumerAgentGroup... cags) {
        InFileBasedPVactConsumerAgentPopulation supplier = new InFileBasedPVactConsumerAgentPopulation();
        supplier.setName(name);
        supplier.setFile(getSpatialFile());
        supplier.setRequiresDesiredSize(true);
        supplier.setConsumerAgentGroups(cags);
        if(agents < 1) {
            supplier.setUseAll(true);
            supplier.setDesiredSize(-1);
        } else {
            supplier.setUseAll(false);
            supplier.setDesiredSize(agents);
        }
        return supplier;
    }

    public InFile[] getDefaultFiles() {
        return new InFile[] {
                getPVFile(),
                getSpatialFile(),
                getRealAdoptionDataFile()
        };
    }

    public InFileBasedPVactConsumerAgentPopulation createFullPopulation(String name, InConsumerAgentGroup... cags) {
        return createPopulation(name, -1, cags);
    }

    public InComplexAffinityEntry createAffinityEntry(String prefix, InConsumerAgentGroup a, InConsumerAgentGroup b, double value) {
        return new InComplexAffinityEntry(prefix + "_" + a.getName() + "_" + b.getName(), a, b, value);
    }

    public InAffinities createAffinities(String name, InAffinityEntry... entries) {
        InAffinities affinities = new InAffinities();
        affinities.setName(name);
        affinities.setEntries(entries);
        return affinities;
    }

    public InAffinities createZeroAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildAll(cags, 0);
        return createAffinities(name, entries);
    }

    public InAffinities createEvenlyDistributedAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildAll(cags, 1.0 / cags.length);
        return createAffinities(name, entries);
    }

    public InAffinities createSelfLinkedAffinities(String name, InConsumerAgentGroup... cags) {
        InComplexAffinityEntry[] entries = InComplexAffinityEntry.buildSelfLinked(cags);
        return createAffinities(name, entries);
    }

    public InAffinities createAffinities(String name, InAffinityEntry[]... entries) {
        InAffinityEntry[] flatEntries = Arrays.stream(entries)
                .flatMap(Arrays::stream)
                .toArray(InAffinityEntry[]::new);
        return createAffinities(name, flatEntries);
    }

    public InAffinityEntry[] createEntries(String prefix, InConsumerAgentGroup from, InConsumerAgentGroup[] targets, double[] values) {
        InAffinityEntry[] entries = new InAffinityEntry[targets.length];
        for(int i = 0; i < entries.length; i++) {
            InConsumerAgentGroup to = targets[i];
            InComplexAffinityEntry entry = new InComplexAffinityEntry();
            String name = prefix == null || prefix.isEmpty()
                    ? from.getName() + "_" + to.getName()
                    : prefix + "_" + from.getName() + "_" + to.getName();
            entry.setName(name);
            entry.setSrcCag(from);
            entry.setTarCag(to);
            entry.setAffinityValue(values[i]);
            entries[i] = entry;
        }
        return entries;
    }

    public InPVactConsumerAgentGroup createNullAgent(String name) {
        return createNullAgent(name, null);
    }

    public InPVactConsumerAgentGroup createNullAgent(String name, InSpatialDistribution distribution) {
        InPVactConsumerAgentGroup grp = new InPVactConsumerAgentGroup();
        grp.setName(name);
        if(distribution != null) {
            grp.setSpatialDistribution(distribution);
        }

        //A1 in file
        grp.setNoveltySeeking(dirac0);                            //A2
        grp.setDependentJudgmentMaking(dirac0);                   //A3
        grp.setEnvironmentalConcern(dirac0);                      //A4
        //A5 in file
        //A6 in file
        grp.setConstructionRate(dirac0);                          //A7
        grp.setRenovationRate(dirac0);                            //A8

        grp.setRewire(dirac0);                                    //B6

        grp.setCommunication(dirac0);                             //C1
        grp.setRateOfConvergence(dirac0);                         //C3

        grp.setInitialProductAwareness(dirac0);                     //D1
        grp.setInterestThreshold(dirac0);                         //D2
        grp.setFinancialThreshold(dirac0);                       //D3
        grp.setAdoptionThreshold(dirac0);                        //D4
        grp.setInitialAdopter(dirac0);                            //D5
        grp.setInitialProductInterest(dirac0);                    //D6

        return grp;
    }
}
