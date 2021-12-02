package de.unileipzig.irpact.util.scenarios.pvact.util;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process.ra.RAModelData;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.core.process2.modular.reevaluate.Reevaluator;
import de.unileipzig.irpact.io.param.input.file.InPVFile;
import de.unileipzig.irpact.io.param.input.file.InRealAdoptionDataFile;
import de.unileipzig.irpact.io.param.input.postdata.InBucketAnalyser;
import de.unileipzig.irpact.io.param.input.postdata.InNeighbourhoodOverview;
import de.unileipzig.irpact.io.param.input.postdata.InPostDataAnalysis;
import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.io.param.input.process2.modular.InModule2;
import de.unileipzig.irpact.io.param.input.process2.modular.models.ca.InBasicCAModularProcessModel;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.action.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InBernoulliModule_boolgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.bool.InThresholdReachedModule_boolgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InLogisticModule_calcgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InMulScalarModule_calcgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.InSumModule_calcgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.input.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.calc.logging.InCsvValueLoggingModule_calcloggraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.eval.InRunUntilFailureModule_evalgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.*;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.evalra.logging.InPhaseLoggingModule_evalragraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.ca.modules.reeval.InReevaluatorModule_reevalgraphnode2;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InUncertaintySupplierInitializer;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca.*;
import de.unileipzig.irpact.io.param.input.visualisation.result2.*;
import de.unileipzig.irpact.util.scenarios.CorporateDesignUniLeipzig;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ModularProcessModelManager;
import de.unileipzig.irpact.util.scenarios.util.AttributeNameManager;
import de.unileipzig.irpact.util.scenarios.util.ModularProcessModelTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

/**
 * @author Daniel Abitz
 */
public class DefaultModularProcessModelTemplate implements ModularProcessModelTemplate {

    protected ModularProcessModelManager mpmm;
    protected AttributeNameManager anm;

    protected List<InOutputImage2> images;
    protected List<InPostDataAnalysis> datas;
    protected InBasicCAModularProcessModel mpm;
    protected String mpmName;

    protected Supplier<? extends InUncertaintySupplier> uncertaintySupplier;
    protected Supplier<? extends InNodeDistanceFilterScheme> distanceFilterSupplier;
    protected Supplier<? extends InPVFile> pvFileSupplier;
    protected Supplier<? extends InRealAdoptionDataFile> realAdoptionFileSupplier;

    public DefaultModularProcessModelTemplate(String mpmName) {
        this(new ModularProcessModelManager(), new AttributeNameManager(), mpmName);
    }

    public DefaultModularProcessModelTemplate(ModularProcessModelManager mpmm, AttributeNameManager anm, String mpmName) {
        this.mpmm = mpmm;
        this.anm = anm;
        this.mpmName = mpmName;
    }

    //=========================
    //module names
    //=========================

    protected String commuAttrModuleName = "ATTR_COMMUNICATION";
    public void setCommuAttrModuleName(String commuAttrModuleName) {
        this.commuAttrModuleName = commuAttrModuleName;
    }
    public String getCommuAttrModuleName() {
        return commuAttrModuleName;
    }

    protected String communicationFactorModuleName = "FACTOR_COMMUNICATION";
    public void setCommunicationFactorModuleName(String communicationFactorModuleName) {
        this.communicationFactorModuleName = communicationFactorModuleName;
    }
    public String getCommunicationFactorModuleName() {
        return communicationFactorModuleName;
    }

    protected String testCommunicationModuleName = "TEST_COMMUNICATION";
    public void setTestCommunicationModuleName(String testCommunicationModuleName) {
        this.testCommunicationModuleName = testCommunicationModuleName;
    }
    public String getTestCommunicationModuleName() {
        return testCommunicationModuleName;
    }

    protected String communicationModuleName = "COMMU_ACTION";
    public void setCommunicationModuleName(String communicationModuleName) {
        this.communicationModuleName = communicationModuleName;
    }
    public String getCommunicationModuleName() {
        return communicationModuleName;
    }

    protected String rewireAttrModuleName = "ATTR_REWIRE";
    public void setRewireAttrModuleName(String rewireAttrModuleName) {
        this.rewireAttrModuleName = rewireAttrModuleName;
    }
    public String getRewireAttrModuleName() {
        return rewireAttrModuleName;
    }

    protected String rewireFactorModuleName = "FACTOR_REWIRE";
    public void setRewireFactorModuleName(String rewireFactorModuleName) {
        this.rewireFactorModuleName = rewireFactorModuleName;
    }
    public String getRewireFactorModuleName() {
        return rewireFactorModuleName;
    }

    protected String testRewireModuleName = "TEST_REWIRE";
    public void setTestRewireModuleName(String testRewireModuleName) {
        this.testRewireModuleName = testRewireModuleName;
    }
    public String getTestRewireModuleName() {
        return testRewireModuleName;
    }

    protected String rewireModuleName = "REWIRE_ACTION";
    public void setRewireModuleName(String rewireModuleName) {
        this.rewireModuleName = rewireModuleName;
    }
    public String getRewireModuleName() {
        return rewireModuleName;
    }

    protected String nopModuleName = "NOP";
    public void setNopModuleName(String nopModuleName) {
        this.nopModuleName = nopModuleName;
    }
    public String getNopModuleName() {
        return nopModuleName;
    }

    protected String ifElseRewireModuleName = "IF_ELSE_REWIRE";
    public void setIfElseRewireModuleName(String ifElseRewireModuleName) {
        this.ifElseRewireModuleName = ifElseRewireModuleName;
    }
    public String getIfElseRewireModuleName() {
        return ifElseRewireModuleName;
    }

    protected String ifElseCommunicationModuleName = "IF_ELSE_COMMU";
    public void setIfElseCommunicationModuleName(String ifElseCommunicationModuleName) {
        this.ifElseCommunicationModuleName = ifElseCommunicationModuleName;
    }
    public String getIfElseCommunicationModuleName() {
        return ifElseCommunicationModuleName;
    }

    protected String avgNPVModuleName = "AVG_NPV";
    public void setAvgNPVModuleName(String avgNPVModuleName) {
        this.avgNPVModuleName = avgNPVModuleName;
    }
    public String getAvgNPVModuleName() {
        return avgNPVModuleName;
    }

    protected String npvModuleName = "NPV";
    public void setNpvModuleName(String npvModuleName) {
        this.npvModuleName = npvModuleName;
    }
    public String getNpvModuleName() {
        return npvModuleName;
    }

    protected String logisticNpvModuleName = "LOGISTIC_NPV";
    public void setLogisticNpvModuleName(String logisticNpvModuleName) {
        this.logisticNpvModuleName = logisticNpvModuleName;
    }
    public String getLogisticNpvModuleName() {
        return logisticNpvModuleName;
    }

    protected String npvLoggerModuleName = "NPV_THROUGHPUT_LOGGER";
    public void setNpvLoggerModuleName(String npvLoggerModuleName) {
        this.npvLoggerModuleName = npvLoggerModuleName;
    }
    public String getNpvLoggerModuleName() {
        return npvLoggerModuleName;
    }

    protected String npvReevalLoggerModuleName = "NPV_END_OF_YEAR_LOGGER";
    public void setNpvReevalLoggerModuleName(String npvReevalLoggerModuleName) {
        this.npvReevalLoggerModuleName = npvReevalLoggerModuleName;
    }
    public String getNpvReevalLoggerModuleName() {
        return npvReevalLoggerModuleName;
    }

    protected String ppInputModuleName = "PP";
    public void setPpInputModuleName(String ppInputModuleName) {
        this.ppInputModuleName = ppInputModuleName;
    }
    public String getPpInputModuleName() {
        return ppInputModuleName;
    }

    protected String avgPpInputModuleName = "AVG_PP";
    public void setAvgPpInputModuleName(String avgPpInputModuleName) {
        this.avgPpInputModuleName = avgPpInputModuleName;
    }
    public String getAvgPpInputModuleName() {
        return avgPpInputModuleName;
    }

    protected String logiticPpModuleName = "LOGISTIC_PP";
    public void setLogiticPpModuleName(String logiticPpModuleName) {
        this.logiticPpModuleName = logiticPpModuleName;
    }
    public String getLogiticPpModuleName() {
        return logiticPpModuleName;
    }

    protected String npvWeightModuleName = "NPV_WEIGHT";
    public void setNpvWeightModuleName(String npvWeightModuleName) {
        this.npvWeightModuleName = npvWeightModuleName;
    }
    public String getNpvWeightModuleName() {
        return npvWeightModuleName;
    }

    protected String ppWeightModuleName = "PP_WEIGHT";
    public void setPpWeightModuleName(String ppWeightModuleName) {
        this.ppWeightModuleName = ppWeightModuleName;
    }
    public String getPpWeightModuleName() {
        return ppWeightModuleName;
    }

    protected String finComponentModuleName = "FIN_COMPONENT";
    public void setFinComponentModuleName(String finComponentModuleName) {
        this.finComponentModuleName = finComponentModuleName;
    }
    public String getFinComponentModuleName() {
        return finComponentModuleName;
    }

    protected String envInputModuleName = "ENV";
    public void setEnvInputModuleName(String envInputModuleName) {
        this.envInputModuleName = envInputModuleName;
    }
    public String getEnvInputModuleName() {
        return envInputModuleName;
    }

    protected String envLoggerModuleName = "ENV_THROUGHPUT_LOGGER";
    public void setEnvLoggerModuleName(String envLoggerModuleName) {
        this.envLoggerModuleName = envLoggerModuleName;
    }
    public String getEnvLoggerModuleName() {
        return envLoggerModuleName;
    }

    protected String envReevalLoggerName = "ENV_END_OF_YEAR_LOGGER";
    public void setEnvReevalLoggerName(String envReevalLoggerName) {
        this.envReevalLoggerName = envReevalLoggerName;
    }
    public String getEnvReevalLoggerName() {
        return envReevalLoggerName;
    }

    protected String envWeightModuleName = "ENV_WEIGHT";
    public void setEnvWeightModuleName(String envWeightModuleName) {
        this.envWeightModuleName = envWeightModuleName;
    }
    public String getEnvWeightModuleName() {
        return envWeightModuleName;
    }

    protected String novInputModuleName = "NOV";
    public void setNovInputModuleName(String novInputModuleName) {
        this.novInputModuleName = novInputModuleName;
    }
    public String getNovInputModuleName() {
        return novInputModuleName;
    }

    protected String novLoggerModuleName = "NOV_THROUGHPUT_LOGGER";
    public void setNovLoggerModuleName(String novLoggerModuleName) {
        this.novLoggerModuleName = novLoggerModuleName;
    }
    public String getNovLoggerModuleName() {
        return novLoggerModuleName;
    }

    protected String novReevalLoggerModuleName = "NOV_END_OF_YEAR_LOGGER";
    public void setNovReevalLoggerModuleName(String novReevalLoggerModuleName) {
        this.novReevalLoggerModuleName = novReevalLoggerModuleName;
    }
    public String getNovReevalLoggerModuleName() {
        return novReevalLoggerModuleName;
    }

    protected String novWeightModuleName = "NOV_WEIGHT";
    public void setNovWeightModuleName(String novWeightModuleName) {
        this.novWeightModuleName = novWeightModuleName;
    }
    public String getNovWeightModuleName() {
        return novWeightModuleName;
    }

    protected String localShareModuleName = "LOCAL_SHARE";
    public void setLocalShareModuleName(String localShareModuleName) {
        this.localShareModuleName = localShareModuleName;
    }
    public String getLocalShareModuleName() {
        return localShareModuleName;
    }

    protected String localShareLoggerModuleName = "LOCAL_THROUGHPUT_LOGGER";
    public void setLocalShareLoggerModuleName(String localShareLoggerModuleName) {
        this.localShareLoggerModuleName = localShareLoggerModuleName;
    }
    public String getLocalShareLoggerModuleName() {
        return localShareLoggerModuleName;
    }

    protected String localShareReevalLoggerModuleName = "LOCAL_END_OF_YEAR_LOGGER";
    public void setLocalShareReevalLoggerModuleName(String localShareReevalLoggerModuleName) {
        this.localShareReevalLoggerModuleName = localShareReevalLoggerModuleName;
    }
    public String getLocalShareReevalLoggerModuleName() {
        return localShareReevalLoggerModuleName;
    }

    protected String socialShareModuleName = "SOCIAL_SHARE";
    public void setSocialShareModuleName(String socialShareModuleName) {
        this.socialShareModuleName = socialShareModuleName;
    }
    public String getSocialShareModuleName() {
        return socialShareModuleName;
    }

    protected String socialShareLoggerModuleName = "SOCIAL_THROUGHPUT_LOGGER";
    public void setSocialShareLoggerModuleName(String socialShareLoggerModuleName) {
        this.socialShareLoggerModuleName = socialShareLoggerModuleName;
    }
    public String getSocialShareLoggerModuleName() {
        return socialShareLoggerModuleName;
    }

    protected String socialShareReevalLoggerModuleName = "SOCIAL_END_OF_YEAR_LOGGER";
    public void setSocialShareReevalLoggerModuleName(String socialShareReevalLoggerModuleName) {
        this.socialShareReevalLoggerModuleName = socialShareReevalLoggerModuleName;
    }
    public String getSocialShareReevalLoggerModuleName() {
        return socialShareReevalLoggerModuleName;
    }

    protected String localShareWeightModuleName = "LOCAL_WEIGHT";
    public void setLocalShareWeightModuleName(String localShareWeightModuleName) {
        this.localShareWeightModuleName = localShareWeightModuleName;
    }
    public String getLocalShareWeightModuleName() {
        return localShareWeightModuleName;
    }

    protected String socialShareWeightModuleName = "SOCIAL_WEIGHT";
    public void setSocialShareWeightModuleName(String socialShareWeightModuleName) {
        this.socialShareWeightModuleName = socialShareWeightModuleName;
    }
    public String getSocialShareWeightModuleName() {
        return socialShareWeightModuleName;
    }

    protected String socialComponentModuleName = "SOC_COMPONENT";
    public void setSocialComponentModuleName(String socialComponentModuleName) {
        this.socialComponentModuleName = socialComponentModuleName;
    }
    public String getSocialComponentModuleName() {
        return socialComponentModuleName;
    }

    protected String finThresholdInputModuleName = "FIN_THRESHOLD";
    public void setFinThresholdInputModuleName(String finThresholdInputModuleName) {
        this.finThresholdInputModuleName = finThresholdInputModuleName;
    }
    public String getFinThresholdInputModuleName() {
        return finThresholdInputModuleName;
    }

    protected String finThresholdCheckModuleName = "FIN_CHECK";
    public void setFinThresholdCheckModuleName(String finThresholdCheckModuleName) {
        this.finThresholdCheckModuleName = finThresholdCheckModuleName;
    }
    public String getFinThresholdCheckModuleName() {
        return finThresholdCheckModuleName;
    }

    protected String adoptThresholdInputModuleName = "ADOPT_THRESHOLD";
    public void setAdoptThresholdInputModuleName(String adoptThresholdInputModuleName) {
        this.adoptThresholdInputModuleName = adoptThresholdInputModuleName;
    }
    public String getAdoptThresholdInputModuleName() {
        return adoptThresholdInputModuleName;
    }

    protected String utilityModuleName = "UTILITY_SUM";
    public void setUtilityModuleName(String utilityModuleName) {
        this.utilityModuleName = utilityModuleName;
    }
    public String getUtilityModuleName() {
        return utilityModuleName;
    }

    protected String utilityLoggerModuleName = "UTILITY_THROUGHPUT_LOGGER";
    public void setUtilityLoggerModuleName(String utilityLoggerModuleName) {
        this.utilityLoggerModuleName = utilityLoggerModuleName;
    }
    public String getUtilityLoggerModuleName() {
        return utilityLoggerModuleName;
    }

    protected String utilityReevalLoggerModuleName = "UTILITY_END_OF_YEAR_LOGGER";
    public void setUtilityReevalLoggerModuleName(String utilityReevalLoggerModuleName) {
        this.utilityReevalLoggerModuleName = utilityReevalLoggerModuleName;
    }
    public String getUtilityReevalLoggerModuleName() {
        return utilityReevalLoggerModuleName;
    }

    protected String decisionMakingModuleName = "DECISION_MAKING";
    public void setDecisionMakingModuleName(String decisionMakingModuleName) {
        this.decisionMakingModuleName = decisionMakingModuleName;
    }
    public String getDecisionMakingModuleName() {
        return decisionMakingModuleName;
    }

    protected String reallyAdoptModuleName = "REALLY_ADOPT_TEST";//propabiliy_test?
    public void setReallyAdoptModuleName(String reallyAdoptModuleName) {
        this.reallyAdoptModuleName = reallyAdoptModuleName;
    }
    public String getReallyAdoptModuleName() {
        return reallyAdoptModuleName;
    }

    protected String doAdoptModuleName = "ADOPT_IF_POSSIBLE";
    public void setDoAdoptModuleName(String doAdoptModuleName) {
        this.doAdoptModuleName = doAdoptModuleName;
    }
    public String getDoAdoptModuleName() {
        return doAdoptModuleName;
    }

    protected String mainBranchingModuleName = "CORE";
    public void setMainBranchingModuleName(String mainBranchingModuleName) {
        this.mainBranchingModuleName = mainBranchingModuleName;
    }
    public String getMainBranchingModuleName() {
        return mainBranchingModuleName;
    }

    protected String initModuleName = "INIT";
    public void setInitModuleName(String initModuleName) {
        this.initModuleName = initModuleName;
    }
    public String getInitModuleName() {
        return initModuleName;
    }

    protected String interestModuleName = "INTEREST";
    public void setInterestModuleName(String interestModuleName) {
        this.interestModuleName = interestModuleName;
    }
    public String getInterestModuleName() {
        return interestModuleName;
    }

    protected String feasibilityModuleName = "FEASIBILITY";
    public void setFeasibilityModuleName(String feasibilityModuleName) {
        this.feasibilityModuleName = feasibilityModuleName;
    }
    public String getFeasibilityModuleName() {
        return feasibilityModuleName;
    }

    protected String phaseLoggerModuleName = "PHASE_LOGGER";
    public void setPhaseLoggerModuleName(String phaseLoggerModuleName) {
        this.phaseLoggerModuleName = phaseLoggerModuleName;
    }
    public String getPhaseLoggerModuleName() {
        return phaseLoggerModuleName;
    }

    protected String phaseUpdaterModuleName = "PHASE_UPDATER";
    public void setPhaseUpdaterModuleName(String phaseUpdaterModuleName) {
        this.phaseUpdaterModuleName = phaseUpdaterModuleName;
    }
    public String getPhaseUpdaterModuleName() {
        return phaseUpdaterModuleName;
    }

    protected String novScalerName = "NOV_SCALER";
    public void setNovScalerName(String novScalerName) {
        this.novScalerName = novScalerName;
    }
    public String getNovScalerName() {
        return novScalerName;
    }

    protected String envScalerName = "ENV_SCALER";
    public void setEnvScalerName(String envScalerName) {
        this.envScalerName = envScalerName;
    }
    public String getEnvScalerName() {
        return envScalerName;
    }

    protected String envUpdaterName = "ENV_UPDATER";
    public void setEnvUpdaterName(String envUpdaterName) {
        this.envUpdaterName = envUpdaterName;
    }
    public String getEnvUpdaterName() {
        return envUpdaterName;
    }

    protected String uncertInitName = "UNCERT_INIT";
    public void setUncertInitName(String uncertInitName) {
        this.uncertInitName = uncertInitName;
    }
    public String getUncertInitName() {
        return uncertInitName;
    }

    //=========================
    //attr names
    //=========================

    protected String communicationFrequencySNAttributeName = RAConstants.COMMUNICATION_FREQUENCY_SN;
    public void setCommunicationFrequencySNAttributeName(String communicationFrequencySNAttributeName) {
        this.communicationFrequencySNAttributeName = communicationFrequencySNAttributeName;
    }
    public String getCommunicationFrequencySNAttributeName() {
        return communicationFrequencySNAttributeName;
    }

    protected String rewireRateAttributeName = RAConstants.REWIRING_RATE;
    public void setRewireRateAttributeName(String rewireRateAttributeName) {
        this.rewireRateAttributeName = rewireRateAttributeName;
    }
    public String getRewireRateAttributeName() {
        return rewireRateAttributeName;
    }

    protected String purchasePowerEurAttributeName = RAConstants.PURCHASE_POWER_EUR;
    public void setPurchasePowerEurAttributeName(String purchasePowerEurAttributeName) {
        this.purchasePowerEurAttributeName = purchasePowerEurAttributeName;
    }
    public String getPurchasePowerEurAttributeName() {
        return purchasePowerEurAttributeName;
    }

    protected String environmentalConcernAttributeName = RAConstants.ENVIRONMENTAL_CONCERN;
    public void setEnvironmentalConcernAttributeName(String environmentalConcernAttributeName) {
        this.environmentalConcernAttributeName = environmentalConcernAttributeName;
    }
    public String getEnvironmentalConcernAttributeName() {
        return environmentalConcernAttributeName;
    }

    protected String noveltySeekingAttributeName = RAConstants.NOVELTY_SEEKING;
    public void setNoveltySeekingAttributeName(String noveltySeekingAttributeName) {
        this.noveltySeekingAttributeName = noveltySeekingAttributeName;
    }
    public String getNoveltySeekingAttributeName() {
        return noveltySeekingAttributeName;
    }

    protected String financialThresholdAttributeName = RAConstants.FINANCIAL_THRESHOLD;
    public void setFinancialThresholdAttributeName(String financialThresholdAttributeName) {
        this.financialThresholdAttributeName = financialThresholdAttributeName;
    }
    public String getFinancialThresholdAttributeName() {
        return financialThresholdAttributeName;
    }

    protected String adoptionThresholdAttributeName = RAConstants.ADOPTION_THRESHOLD;
    public void setAdoptionThresholdAttributeName(String adoptionThresholdAttributeName) {
        this.adoptionThresholdAttributeName = adoptionThresholdAttributeName;
    }
    public String getAdoptionThresholdAttributeName() {
        return adoptionThresholdAttributeName;
    }

    //=========================
    //stuff
    //=========================

    public void setModularProcessModelName(String mpmName) {
        this.mpmName = mpmName;
    }
    public String getModularProcessModelName() {
        return mpmName;
    }
    protected String getValidModularProcessModelName() {
        if(mpmName == null) {
            throw new NullPointerException("name is null");
        }
        if(mpmName.isEmpty()) {
            throw new IllegalArgumentException("name is empty");
        }

        return mpmName;
    }

    public void setUncertaintySupplier(Supplier<? extends InUncertaintySupplier> uncertaintySupplier) {
        this.uncertaintySupplier = uncertaintySupplier;
    }
    public Supplier<? extends InUncertaintySupplier> getUncertaintySupplier() {
        return uncertaintySupplier;
    }

    protected InUncertaintySupplier uncertainty;
    protected InUncertaintySupplier getValidUncertaintySupplier() {
        if(uncertainty != null) {
            return uncertainty;
        }

        if(uncertaintySupplier == null) {
            throw new NoSuchElementException("missing uncertainty supplier");
        }

        uncertainty = uncertaintySupplier.get();
        if(uncertainty == null) {
            throw new NoSuchElementException("uncertainty is null");
        }
        return uncertainty;
    }

    public void setRealAdoptionFileSupplier(Supplier<? extends InRealAdoptionDataFile> realAdoptionFileSupplier) {
        this.realAdoptionFileSupplier = realAdoptionFileSupplier;
    }
    public Supplier<? extends InRealAdoptionDataFile> getRealAdoptionFileSupplier() {
        return realAdoptionFileSupplier;
    }

    protected InRealAdoptionDataFile realAdoptionFile;
    protected InRealAdoptionDataFile getValidRealAdoptionFile() {
        if(realAdoptionFile != null) {
            return realAdoptionFile;
        }

        if(realAdoptionFileSupplier == null) {
            throw new NoSuchElementException("missing real adoption file supplier");
        }

        realAdoptionFile = realAdoptionFileSupplier.get();
        if(pvFile == null) {
            throw new NoSuchElementException("real adoption file is null");
        }
        return realAdoptionFile;
    }

    public void setPvFileSupplier(Supplier<? extends InPVFile> pvFileSupplier) {
        this.pvFileSupplier = pvFileSupplier;
    }
    public Supplier<? extends InPVFile> getPvFileSupplier() {
        return pvFileSupplier;
    }

    protected InPVFile pvFile;
    protected InPVFile getValidPvFile() {
        if(pvFile != null) {
            return pvFile;
        }

        if(pvFileSupplier == null) {
            throw new NoSuchElementException("missing pvfile supplier");
        }

        pvFile = pvFileSupplier.get();
        if(pvFile == null) {
            throw new NoSuchElementException("pvfile is null");
        }
        return pvFile;
    }

    public void setDistanceFilterSupplier(Supplier<? extends InNodeDistanceFilterScheme> distanceFilterSupplier) {
        this.distanceFilterSupplier = distanceFilterSupplier;
    }
    public Supplier<? extends InNodeDistanceFilterScheme> getDistanceFilterSupplier() {
        return distanceFilterSupplier;
    }

    protected InNodeDistanceFilterScheme distanceFilterScheme;
    protected InNodeDistanceFilterScheme getValidDistanceFilterScheme() {
        if(distanceFilterScheme != null) {
            return distanceFilterScheme;
        }

        if(distanceFilterSupplier == null) {
            throw new NoSuchElementException("missing distance filter supplier");
        }

        distanceFilterScheme = distanceFilterSupplier.get();
        if(distanceFilterScheme == null) {
            throw new NoSuchElementException("scheme is null");
        }
        return distanceFilterScheme;
    }

    //=========================
    //moduls
    //=========================

    public <R extends InModule2> R findModule(String name) {
        return mpmm.findModuleAuto(name);
    }

    public InMulScalarModule_calcgraphnode2 findWeightModule(String name) {
        return findModule(name);
    }

    public InCsvValueLoggingModule_calcloggraphnode2 findLoggingModule(String name) {
        return findModule(name);
    }

    protected InCsvValueLoggingModule_calcloggraphnode2 createDefaultLoggingModule(String name) {
        InCsvValueLoggingModule_calcloggraphnode2 module = new InCsvValueLoggingModule_calcloggraphnode2();
        module.setName(name);
        module.setEnabled(true);
        module.setPrintHeader(true);
        module.setLogDefaultCall(true);
        module.setLogReevaluatorCall(false);
        module.setStoreXlsx(true);
        return module;
    }

    protected InCsvValueLoggingModule_calcloggraphnode2 createReevaluatorLoggingModule(String name) {
        InCsvValueLoggingModule_calcloggraphnode2 module = new InCsvValueLoggingModule_calcloggraphnode2();
        module.setName(name);
        module.setEnabled(true);
        module.setPrintHeader(true);
        module.setLogDefaultCall(false);
        module.setLogReevaluatorCall(true);
        module.setStoreXlsx(true);
        return module;
    }

    protected void setupCommunicationModule(InCommunicationModule_actiongraphnode2 module) {
        module.setRaEnabled(true);
        module.setRaLoggingEnabled(true);
        module.setRaStoreXlsx(true);
        module.setRaKeepCsv(false);
        module.setRaOpinionLogging(false);
        module.setRaUnceraintyLogging(false);
        module.setAdopterPoints(RAModelData.DEFAULT_ADOPTER_POINTS);
        module.setInterestedPoints(RAModelData.DEFAULT_INTERESTED_POINTS);
        module.setAwarePoints(RAModelData.DEFAULT_AWARE_POINTS);
        module.setUnknownPoints(RAModelData.DEFAULT_UNKNOWN_POINTS);
        module.setSpeedOfConvergence(RAConstants.DEFAULT_SPEED_OF_CONVERGENCE);
        module.setAttitudeGap(RAConstants.DEFAULT_ATTIDUTE_GAP);
        module.setChanceNeutral(RAConstants.DEFAULT_NEUTRAL_CHANCE);
        module.setChanceConvergence(RAConstants.DEFAULT_CONVERGENCE_CHANCE);
        module.setChanceDivergence(RAConstants.DEFAULT_DIVERGENCE_CHANCE);
        module.setUncertaintySupplier(getValidUncertaintySupplier());
    }

    protected void setupNPVLogisticModule(InLogisticModule_calcgraphnode2 module) {
        module.setValueL(1.0);
        module.setValueK(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        module.setXInput(findModule(getNpvModuleName()));
        module.setX0Input(findModule(getAvgNPVModuleName()));
    }

    protected void setupPPLogisticModule(InLogisticModule_calcgraphnode2 module) {
        module.setValueL(1.0);
        module.setValueK(RAConstants.DEFAULT_LOGISTIC_FACTOR);
        module.setXInput(findModule(getPpInputModuleName()));
        module.setX0Input(findModule(getAvgPpInputModuleName()));
    }

    protected void setupWeights() {
        findWeightModule(getNpvWeightModuleName()).setScalar(1.0);
        findWeightModule(getPpWeightModuleName()).setScalar(1.0);
        findWeightModule(getEnvWeightModuleName()).setScalar(1.0);
        findWeightModule(getNovWeightModuleName()).setScalar(1.0);
        findWeightModule(getLocalShareWeightModuleName()).setScalar(1.0);
        findWeightModule(getSocialShareWeightModuleName()).setScalar(1.0);
    }

    protected void setupFactors() {
        findWeightModule(getCommunicationFactorModuleName()).setScalar(1.0);
        findWeightModule(getRewireFactorModuleName()).setScalar(1.0);
    }

    protected void setupYearBasedAdoptionDecider(InYearBasedAdoptionDeciderModule_evalragraphnode2 module) {
        module.setEnabled(true);
        module.setBase(1);
        module.setFactor(1);
    }

    protected void setupPhaseLogger(InPhaseLoggingModule_evalragraphnode2 module) {
        module.setEnabled(true);
    }

    protected void setupEnvScaler(InLinearePercentageAgentAttributeScaler scaler) {
        scaler.setM(RAConstants.DEFAULT_M);
        scaler.setN(RAConstants.DEFAULT_N);
    }

    //=========================
    //data
    //=========================

    @Override
    public boolean addPostData(Collection<? super InPostDataAnalysis> c) {
        getModel(); //validate creation
        return c.addAll(datas);
    }

    protected boolean addPostData(InBasicCAModularProcessModel mpm, Collection<? super InPostDataAnalysis> c) {
        //bucket
        InBucketAnalyser novBucketData = new InBucketAnalyser();
        novBucketData.setName("NOV_BUCKET");
        novBucketData.setBucketRange(0.1);
        novBucketData.setLoggingModule(findLoggingModule(getNovLoggerModuleName()));
        boolean changed = c.add(novBucketData);

        InBucketAnalyser envBucketData = new InBucketAnalyser();
        envBucketData.setName("ENV_BUCKET");
        envBucketData.setBucketRange(0.01);
        envBucketData.setLoggingModule(findLoggingModule(getEnvLoggerModuleName()));
        changed |= c.add(envBucketData);

        //neighborhood
        InNeighbourhoodOverview neighbourhoodOverview = new InNeighbourhoodOverview();
        neighbourhoodOverview.setName("NEIGHBOURHOOD");
        neighbourhoodOverview.setEnabled(true);
        neighbourhoodOverview.setStoreXlsx(true);
        neighbourhoodOverview.setNodeFilterScheme(getValidDistanceFilterScheme());
        changed |= c.add(neighbourhoodOverview);

        return changed;
    }

    //=========================
    //images
    //=========================

    @Override
    public boolean addImages(Collection<? super InOutputImage2> c) {
        getModel(); //validate creation
        return c.addAll(images);
    }

    protected boolean addImages(InBasicCAModularProcessModel mpm, Collection<? super InOutputImage2> images) {
        InCsvValueLoggingModule_calcloggraphnode2 novReevalLogger = findLoggingModule(getNovReevalLoggerModuleName());
        InCsvValueLoggingModule_calcloggraphnode2 envReevalLogger = findLoggingModule(getEnvReevalLoggerName());
        InCsvValueLoggingModule_calcloggraphnode2 npvReevalLogger = findLoggingModule(getNpvReevalLoggerModuleName());
        InCsvValueLoggingModule_calcloggraphnode2 socialReevalLogger = findLoggingModule(getSocialShareReevalLoggerModuleName());
        InCsvValueLoggingModule_calcloggraphnode2 localReevalLogger = findLoggingModule(getLocalShareReevalLoggerModuleName());
        InCsvValueLoggingModule_calcloggraphnode2 utilityReevalLogger = findLoggingModule(getUtilityReevalLoggerModuleName());

        InSpecialAverageQuantilRangeImage novQuantile1 = InSpecialAverageQuantilRangeImage.NOV();
        novQuantile1.setLoggingModule(novReevalLogger);
        boolean changed = images.add(novQuantile1);

        InSpecialAverageQuantilRangeImage envQuantile = InSpecialAverageQuantilRangeImage.ENV();
        envQuantile.setLoggingModule(envReevalLogger);
        changed |= images.add(envQuantile);

        InSpecialAverageQuantilRangeImage npvQuantile = InSpecialAverageQuantilRangeImage.NPV();
        npvQuantile.setLoggingModule(npvReevalLogger);
        changed |= images.add(npvQuantile);

        InSpecialAverageQuantilRangeImage socialQuantil = InSpecialAverageQuantilRangeImage.SOCIAL();
        socialQuantil.setLoggingModule(socialReevalLogger);
        changed |= images.add(socialQuantil);

        InSpecialAverageQuantilRangeImage localQuantil = InSpecialAverageQuantilRangeImage.LOCAL();
        localQuantil.setLoggingModule(localReevalLogger);
        changed |= images.add(localQuantil);

        InSpecialAverageQuantilRangeImage utilityQuantil = InSpecialAverageQuantilRangeImage.UTILITY();
        utilityQuantil.setLoggingModule(utilityReevalLogger);
        changed |= images.add(utilityQuantil);

        InAnnualBucketImage npvBucket = InAnnualBucketImage.NPV();
        npvBucket.setLoggingModule(npvReevalLogger);
        npvBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(npvBucket);

        InAnnualBucketImage envBucket = InAnnualBucketImage.ENV();
        envBucket.setLoggingModule(envReevalLogger);
        envBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(envBucket);

        InAnnualBucketImage novBucket = InAnnualBucketImage.NOV();
        novBucket.setLoggingModule(novReevalLogger);
        novBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(novBucket);

        InAnnualBucketImage socialBucket = InAnnualBucketImage.SOCIAL();
        socialBucket.setLoggingModule(socialReevalLogger);
        socialBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(socialBucket);

        InAnnualBucketImage localBucket = InAnnualBucketImage.LOCAL();
        localBucket.setLoggingModule(localReevalLogger);
        localBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(localBucket);

        InAnnualBucketImage utlityBucket = InAnnualBucketImage.UTILITY();
        utlityBucket.setLoggingModule(utilityReevalLogger);
        utlityBucket.setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
        changed |= images.add(utlityBucket);

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
//        changed |= images.add(customNovQuantile1);
//
//        InCustomAverageQuantilRangeImage customNovQuantile2 = new InCustomAverageQuantilRangeImage();
//        customNovQuantile2.setName("CUSTOM_NOV2");
//        customNovQuantile2.setCustomImageId(2);
//        customNovQuantile2.setQuantileRanges(qr0, qr1);
//        customNovQuantile2.setLoggingModule(novReevalLogger);
//        changed |= images.add(customNovQuantile2);

        //Adoption Phase Overview
        InAdoptionPhaseOverviewImage adoptionPhaseOverview = InAdoptionPhaseOverviewImage.createDefault();
        changed |= images.add(adoptionPhaseOverview);

        //Compared Annual
        InComparedAnnualImage annualImage = InComparedAnnualImage.createDefault();
        annualImage.setRealData(getValidRealAdoptionFile());
        changed |= images.add(annualImage);

        //Compared Annual Zip
        InComparedAnnualZipImage annualZipImage = InComparedAnnualZipImage.createDefault();
        annualZipImage.setRealData(getValidRealAdoptionFile());
        changed |= images.add(annualZipImage);

        //Interest
        InInterestOverviewImage interestOverview = InInterestOverviewImage.createDefault();
        changed |= images.add(interestOverview);

        //Process Phase Overview
        InProcessPhaseOverviewImage processPhaseOverview = InProcessPhaseOverviewImage.createDefault();
        changed |= images.add(processPhaseOverview);

        return changed;
    }

    //=========================
    //components
    //=========================

    protected void applyComponents(InBasicCAModularProcessModel mpm) {
        applyInitComponents(mpm);
        applyStartOfYear(mpm);
        applyMidOfYear(mpm);
        applyEndOfYear(mpm);

        applySpecialEnvScaler(mpm);
    }

    protected void applySpecialEnvScaler(InBasicCAModularProcessModel mpm) {
        InLinearePercentageAgentAttributeScaler envScaler = new InLinearePercentageAgentAttributeScaler();
        envScaler.setName(getEnvScalerName());
        envScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        envScaler.setAttribute(anm.get(getEnvironmentalConcernAttributeName()));
        setupEnvScaler(envScaler);

        InLinearePercentageAgentAttributeUpdater envUpdater = new InLinearePercentageAgentAttributeUpdater();
        envUpdater.setName(getEnvUpdaterName());
        envUpdater.setScaler(envScaler);

        mpm.addInitializationHandlers(
                envScaler
        );

        mpm.addMidOfYearReevaluators(
                envUpdater
        );
    }

    protected void applyInitComponents(InBasicCAModularProcessModel mpm) {
        InAgentAttributeScaler novScaler = new InAgentAttributeScaler();
        novScaler.setName(getNovScalerName());
        novScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        novScaler.setAttribute(anm.get(getNoveltySeekingAttributeName()));

        InUncertaintySupplierInitializer uncertInit = new InUncertaintySupplierInitializer();
        uncertInit.setName(getUncertInitName());
        uncertInit.setPriority(InitializationHandler.LOW_PRIORITY);
        uncertInit.setUncertaintySuppliers(getValidUncertaintySupplier());

        mpm.addInitializationHandlers(
                novScaler,
                uncertInit
        );
    }

    protected void applyStartOfYear(InBasicCAModularProcessModel mpm) {
        InImpededResetter impededResetter = new InImpededResetter();
        impededResetter.setName("IMPEDED_RESETTER");

        InUncertaintyReevaluator uncertUpdater = new InUncertaintyReevaluator();
        uncertUpdater.setName("UNCERT_UPDATER");
        uncertUpdater.setPriorty(Reevaluator.LOW_PRIORITY);
        uncertUpdater.setUncertaintySuppliers(getValidUncertaintySupplier());

        mpm.addStartOfYearReevaluators(
                impededResetter,
                uncertUpdater
        );
    }

    protected void applyMidOfYear(InBasicCAModularProcessModel mpm) {
        InConstructionRenovationUpdater constructionRenovationUpdater = new InConstructionRenovationUpdater();
        constructionRenovationUpdater.setName("CONSTRUCTION_RENOVATION_UPDATER");

        mpm.addMidOfYearReevaluators(
                constructionRenovationUpdater
        );
    }

    protected void applyEndOfYear(InBasicCAModularProcessModel mpm) {
        InPhaseLoggingModule_evalragraphnode2 decisionReevalPhaseLogger = mpmm.create("PHASE_REEVAL_LOGGER", InPhaseLoggingModule_evalragraphnode2::new);
        decisionReevalPhaseLogger.setInput(findModule(getReallyAdoptModuleName()));

        InPhaseUpdateModule_evalragraphnode2 decisionReevalPhaseUpdater = mpmm.create("PHASE_REEVAL_UPDATER", InPhaseUpdateModule_evalragraphnode2::new);
        decisionReevalPhaseUpdater.setInput(decisionReevalPhaseLogger);

        InAnnualInterestLogger annualInterestLogger = new InAnnualInterestLogger();
        annualInterestLogger.setName("ANNUAL_INTEREST_LOGGER");

        InDecisionMakingReevaluator decisionMakingReevaluator = new InDecisionMakingReevaluator();
        decisionMakingReevaluator.setName("DECISION_REEVALUATOR");
        decisionMakingReevaluator.setModules(decisionReevalPhaseUpdater);

        InReevaluatorModule_reevalgraphnode2 reevalNode = mpmm.create("REEVAL", InReevaluatorModule_reevalgraphnode2::new);
        reevalNode.setInput(
                findLoggingModule(getNpvReevalLoggerModuleName()),
                findLoggingModule(getEnvReevalLoggerName()),
                findLoggingModule(getNovReevalLoggerModuleName()),
                findLoggingModule(getSocialShareReevalLoggerModuleName()),
                findLoggingModule(getLocalShareReevalLoggerModuleName()),
                findLoggingModule(getUtilityReevalLoggerModuleName())
        );

        InMultiReevaluator initLinker = new InMultiReevaluator();
        initLinker.setName("INIT_LINKER");
        initLinker.setModules(
                reevalNode
        );

        mpm.addInitializationReevaluators(
                initLinker
        );

        InMultiReevaluator endOfYearLinker = new InMultiReevaluator();
        endOfYearLinker.setName("END_OF_YEAR_LINKER");
        endOfYearLinker.setModules(
                reevalNode
        );

        mpm.addEndOfYearReevaluator(
                annualInterestLogger,
                decisionMakingReevaluator,
                endOfYearLinker
        );
    }

    //=========================
    //model
    //=========================

    protected InConsumerAgentActionModule2 createActionPart(String moduleName) {
        //communication
        InAttributeInputModule_inputgraphnode2 commuAttr = mpmm.create(getCommuAttrModuleName(), InAttributeInputModule_inputgraphnode2::new);
        commuAttr.setAttribute(anm.get(getCommunicationFrequencySNAttributeName()));

        InMulScalarModule_calcgraphnode2 commuAttrWeight = mpmm.create(getCommunicationFactorModuleName(), InMulScalarModule_calcgraphnode2::new);
        commuAttrWeight.setInput(commuAttr);

        InBernoulliModule_boolgraphnode2 commuIf = mpmm.create(getTestCommunicationModuleName(), InBernoulliModule_boolgraphnode2::new);
        commuIf.setInput(commuAttrWeight);

        InCommunicationModule_actiongraphnode2 commuAction = mpmm.create(getCommunicationModuleName(), InCommunicationModule_actiongraphnode2::new);
        setupCommunicationModule(commuAction);

        //rewire
        InAttributeInputModule_inputgraphnode2 rewireAttr = mpmm.create(getRewireAttrModuleName(), InAttributeInputModule_inputgraphnode2::new);
        rewireAttr.setAttribute(anm.get(getRewireRateAttributeName()));
        InMulScalarModule_calcgraphnode2 rewireAttrWeight = mpmm.create(getRewireFactorModuleName(), InMulScalarModule_calcgraphnode2::new);
        rewireAttrWeight.setInput(rewireAttr);
        InBernoulliModule_boolgraphnode2 rewireIf = mpmm.create(getTestRewireModuleName(), InBernoulliModule_boolgraphnode2::new);
        rewireIf.setInput(rewireAttrWeight);
        InRewireModule_actiongraphnode2 rewireAction = mpmm.create(getRewireModuleName(), InRewireModule_actiongraphnode2::new);

        //nop
        InNOP_actiongraphnode2 nop = mpmm.create(getNopModuleName(), InNOP_actiongraphnode2::new);

        //action / if-else
        InIfElseActionModule_actiongraphnode2 ifElseRewire = mpmm.create(getIfElseRewireModuleName(), InIfElseActionModule_actiongraphnode2::new);
        ifElseRewire.setTestModule(rewireIf);
        ifElseRewire.setOnTrueModule(rewireAction);
        ifElseRewire.setOnFalseModule(nop);

        InIfElseActionModule_actiongraphnode2 ifElseCommu = mpmm.create(moduleName, InIfElseActionModule_actiongraphnode2::new);
        ifElseCommu.setTestModule(commuIf);
        ifElseCommu.setOnTrueModule(commuAction);
        ifElseCommu.setOnFalseModule(ifElseRewire);

        //final
        setupFactors();

        return ifElseCommu;
    }
    protected InConsumerAgentActionModule2 getActionPart() {
        return mpmm.registerIfNotExists(getIfElseCommunicationModuleName(), this::createActionPart);
    }

    protected InSumModule_calcgraphnode2 createUtilityPart(String moduleName) {
        //npv
        InGlobalAvgNPVModule_inputgraphnode2 avgNPV = mpmm.create(getAvgNPVModuleName(), InGlobalAvgNPVModule_inputgraphnode2::new);
        avgNPV.setPvFile(getValidPvFile());

        InNPVModule_inputgraphnode2 npv = mpmm.create(getNpvModuleName(), InNPVModule_inputgraphnode2::new);
        npv.setPvFile(getValidPvFile());

        InLogisticModule_calcgraphnode2 logisticNPV = mpmm.create(getLogisticNpvModuleName(), InLogisticModule_calcgraphnode2::new);
        setupNPVLogisticModule(logisticNPV);

        //npv logging
        InCsvValueLoggingModule_calcloggraphnode2 npvLogger = mpmm.create(getNpvLoggerModuleName(), this::createDefaultLoggingModule);
        npvLogger.setInput(logisticNPV);

        InCsvValueLoggingModule_calcloggraphnode2 npvReevalLogger = mpmm.create(getNpvReevalLoggerModuleName(), this::createReevaluatorLoggingModule);
        npvReevalLogger.setInput(logisticNPV);

        //pp
        InAttributeInputModule_inputgraphnode2 pp = mpmm.create(getPpInputModuleName(), InAttributeInputModule_inputgraphnode2::new);
        pp.setAttribute(anm.get(getPurchasePowerEurAttributeName()));

        InAvgFinModule_inputgraphnode2 avgPP = mpmm.create(getAvgPpInputModuleName(), InAvgFinModule_inputgraphnode2::new);

        InLogisticModule_calcgraphnode2 logisticPP = mpmm.create(getLogiticPpModuleName(), InLogisticModule_calcgraphnode2::new);
        setupPPLogisticModule(logisticPP);

        //fin comp
        InMulScalarModule_calcgraphnode2 npvWeight = mpmm.create(getNpvWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        npvWeight.setInput(npvLogger);

        InMulScalarModule_calcgraphnode2 ppWeight = mpmm.create(getPpWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        ppWeight.setInput(logisticPP);

        InSumModule_calcgraphnode2 finComp = mpmm.create(getFinComponentModuleName(), InSumModule_calcgraphnode2::new);
        finComp.setInput(npvWeight, ppWeight);

        //env comp
        InAttributeInputModule_inputgraphnode2 envAttr = mpmm.create(getEnvInputModuleName(), InAttributeInputModule_inputgraphnode2::new);
        envAttr.setAttribute(anm.get(getEnvironmentalConcernAttributeName()));

        //env logging
        InCsvValueLoggingModule_calcloggraphnode2 envLogger = mpmm.create(getEnvLoggerModuleName(), this::createDefaultLoggingModule);
        envLogger.setInput(envAttr);

        InCsvValueLoggingModule_calcloggraphnode2 envReevalLogger = mpmm.create(getEnvReevalLoggerName(), this::createReevaluatorLoggingModule);
        envReevalLogger.setInput(envAttr);

        InMulScalarModule_calcgraphnode2 envWeight = mpmm.create(getEnvWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        envWeight.setInput(envLogger);

        //nov comp
        InAttributeInputModule_inputgraphnode2 novAttr = mpmm.create(getNovInputModuleName(), InAttributeInputModule_inputgraphnode2::new);
        novAttr.setAttribute(anm.get(getNoveltySeekingAttributeName()));

        InCsvValueLoggingModule_calcloggraphnode2 novLogger = mpmm.create(getNovLoggerModuleName(), this::createDefaultLoggingModule);
        novLogger.setInput(novAttr);

        InCsvValueLoggingModule_calcloggraphnode2 novReevalLogger = mpmm.create(getNovReevalLoggerModuleName(), this::createReevaluatorLoggingModule);
        novReevalLogger.setInput(novAttr);

        InMulScalarModule_calcgraphnode2 novWeight = mpmm.create(getNovWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        novWeight.setInput(novLogger);

        //local
        InLocalShareOfAdopterModule_inputgraphnode2 localShare = mpmm.create(getLocalShareModuleName(), InLocalShareOfAdopterModule_inputgraphnode2::new);
        localShare.setMaxToStore(2000);
        localShare.setNodeFilterScheme(getValidDistanceFilterScheme());

        InCsvValueLoggingModule_calcloggraphnode2 localLogger = mpmm.create(getLocalShareLoggerModuleName(), this::createDefaultLoggingModule);
        localLogger.setInput(localShare);

        InCsvValueLoggingModule_calcloggraphnode2 localReevalLogger = mpmm.create(getLocalShareReevalLoggerModuleName(), this::createReevaluatorLoggingModule);
        localReevalLogger.setInput(localShare);

        //social
        InSocialShareOfAdopterModule_inputgraphnode2 socialShare = mpmm.create(getSocialShareModuleName(), InSocialShareOfAdopterModule_inputgraphnode2::new);

        InCsvValueLoggingModule_calcloggraphnode2 socialLogger = mpmm.create(getSocialShareLoggerModuleName(), this::createDefaultLoggingModule);
        socialLogger.setInput(socialShare);

        InCsvValueLoggingModule_calcloggraphnode2 socialReevalLogger = mpmm.create(getSocialShareReevalLoggerModuleName(), this::createReevaluatorLoggingModule);
        socialReevalLogger.setInput(socialShare);

        //soc component
        InMulScalarModule_calcgraphnode2 localWeight = mpmm.create(getLocalShareWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        localWeight.setInput(localLogger);

        InMulScalarModule_calcgraphnode2 socialWeight = mpmm.create(getSocialShareWeightModuleName(), InMulScalarModule_calcgraphnode2::new);
        socialWeight.setInput(socialLogger);

        InSumModule_calcgraphnode2 socComp = mpmm.create(getSocialComponentModuleName(), InSumModule_calcgraphnode2::new);
        socComp.setInput(localWeight, socialWeight);

        //utility
        InAttributeInputModule_inputgraphnode2 finThreshold = mpmm.create(getFinThresholdInputModuleName(), InAttributeInputModule_inputgraphnode2::new);
        finThreshold.setAttribute(anm.get(getFinancialThresholdAttributeName()));

        InThresholdReachedModule_boolgraphnode2 finCheck = mpmm.create(getFinThresholdCheckModuleName(), InThresholdReachedModule_boolgraphnode2::new);
        finCheck.setDraw(pp);
        finCheck.setThreshold(finThreshold);

        InAttributeInputModule_inputgraphnode2 adoptThreshold = mpmm.create(getAdoptThresholdInputModuleName(), InAttributeInputModule_inputgraphnode2::new);
        adoptThreshold.setAttribute(anm.get(getAdoptionThresholdAttributeName()));

        InSumModule_calcgraphnode2 utilitySum = mpmm.create(moduleName, InSumModule_calcgraphnode2::new);
        utilitySum.setInput(
                finComp,
                envWeight,
                novWeight,
                socComp
        );

        InCsvValueLoggingModule_calcloggraphnode2 utilityLogger = mpmm.create(getUtilityLoggerModuleName(), this::createDefaultLoggingModule);
        utilityLogger.setInput(utilitySum);

        InCsvValueLoggingModule_calcloggraphnode2 utilityReevalLogger = mpmm.create(getUtilityReevalLoggerModuleName(), this::createReevaluatorLoggingModule);
        utilityReevalLogger.setInput(utilitySum);

        //final
        setupWeights();

        return utilitySum;
    }
    protected InSumModule_calcgraphnode2 getUtilityPart() {
        return mpmm.registerIfNotExists(getUtilityModuleName(), this::createUtilityPart);
    }

    protected InDoAdoptModule_evalragraphnode2 createDecisionMakingPart(String moduleName) {
        InSumModule_calcgraphnode2 utilitySum = getUtilityPart();

        InDecisionMakingDeciderModule2_evalragraphnode2 decisionMaking = mpmm.create(getDecisionMakingModuleName(), InDecisionMakingDeciderModule2_evalragraphnode2::new);
        decisionMaking.setFinCheck(findModule(getFinThresholdCheckModuleName()));
        decisionMaking.setThreshold(findModule(getAdoptThresholdInputModuleName()));
        decisionMaking.setUtility(utilitySum);

        InYearBasedAdoptionDeciderModule_evalragraphnode2 reallyDecider = mpmm.create(getReallyAdoptModuleName(), InYearBasedAdoptionDeciderModule_evalragraphnode2::new);
        reallyDecider.setInput(decisionMaking);
        setupYearBasedAdoptionDecider(reallyDecider);

        InDoAdoptModule_evalragraphnode2 adoptModule = mpmm.create(moduleName, InDoAdoptModule_evalragraphnode2::new);
        adoptModule.setInput(reallyDecider);

        return adoptModule;
    }
    protected InDoAdoptModule_evalragraphnode2 getDecisionMakingPart() {
        return mpmm.registerIfNotExists(getDoAdoptModuleName(), this::createDecisionMakingPart);
    }

    protected InInitializationModule_evalragraphnode2 createInitPart(String moduleName) {
        return mpmm.create(moduleName, InInitializationModule_evalragraphnode2::new);
    }
    protected InInitializationModule_evalragraphnode2 getInitPart() {
        return mpmm.registerIfNotExists(getInitModuleName(), this::createInitPart);
    }

    protected InInterestModule_evalragraphnode2 createInterestPart(String moduleName) {
        InInterestModule_evalragraphnode2 interest = mpmm.create(moduleName, InInterestModule_evalragraphnode2::new);
        interest.setInput(getActionPart());
        return interest;
    }
    protected InInterestModule_evalragraphnode2 getInterestPart() {
        return mpmm.registerIfNotExists(getInterestModuleName(), this::createInterestPart);
    }

    protected InFeasibilityModule_evalragraphnode2 createFeasibilityPart(String moduleName) {
        InFeasibilityModule_evalragraphnode2 feasibility = mpmm.create(moduleName, InFeasibilityModule_evalragraphnode2::new);
        feasibility.setInput(getActionPart());
        return feasibility;
    }
    protected InFeasibilityModule_evalragraphnode2 getFeasibilityPart() {
        return mpmm.registerIfNotExists(getFeasibilityModuleName(), this::createFeasibilityPart);
    }

    protected InConsumerAgentActionModule2 getImpededPart() {
        return getActionPart();
    }

    protected InConsumerAgentActionModule2 getAdoptedPart() {
        return getActionPart();
    }

    protected InMainBranchingModule_evalragraphnode2 createMainBranch(String moduleName) {
        InMainBranchingModule_evalragraphnode2 mainBranch = mpmm.create(moduleName, InMainBranchingModule_evalragraphnode2::new);
        mainBranch.setInit(getInitPart());
        mainBranch.setAwareness(getInterestPart());
        mainBranch.setFeasibility(getFeasibilityPart());
        mainBranch.setDecision(getDecisionMakingPart());
        mainBranch.setImpeded(getImpededPart());
        mainBranch.setAdopted(getAdoptedPart());
        return mainBranch;
    }
    protected InMainBranchingModule_evalragraphnode2 getMainBranch() {
        return mpmm.registerIfNotExists(getMainBranchingModuleName(), this::createMainBranch);
    }

    protected InPhaseUpdateModule_evalragraphnode2 createCorePart(String moduleName) {
        InPhaseLoggingModule_evalragraphnode2 phaseLogger = mpmm.create(getPhaseLoggerModuleName(), InPhaseLoggingModule_evalragraphnode2::new);
        setupPhaseLogger(phaseLogger);
        phaseLogger.setInput(getMainBranch());

        InPhaseUpdateModule_evalragraphnode2 phaseUpdater = mpmm.create(moduleName, InPhaseUpdateModule_evalragraphnode2::new);
        phaseUpdater.setInput(phaseLogger);

        return phaseUpdater;
    }
    protected InPhaseUpdateModule_evalragraphnode2 getCorePart() {
        return mpmm.registerIfNotExists(getPhaseUpdaterModuleName(), this::createCorePart);
    }

    public InBasicCAModularProcessModel createModel() {
        InRunUntilFailureModule_evalgraphnode2 startModule = mpmm.create("START", InRunUntilFailureModule_evalgraphnode2::new);
        startModule.setInput(getCorePart());

        InBasicCAModularProcessModel processModel = new InBasicCAModularProcessModel();
        processModel.setName(getValidModularProcessModelName());
        processModel.setStartModule(startModule);
        applyComponents(processModel);

        return processModel;
    }

    public InBasicCAModularProcessModel createModel(
            Collection<? super InOutputImage2> images,
            Collection<? super InPostDataAnalysis> postDatas) {

        InBasicCAModularProcessModel mpm = createModel();
        addImages(mpm, images);
        addPostData(mpm, postDatas);

        return mpm;
    }

    @Override
    public InBasicCAModularProcessModel getModel() {
        if(mpm == null) {
            mpm = createModel();

            images = new ArrayList<>();
            addImages(mpm, images);

            datas = new ArrayList<>();
            addPostData(mpm, datas);
        }
        return mpm;
    }
}
