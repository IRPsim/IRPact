package de.unileipzig.irpact.util.scenarios.pvact.util;

import de.unileipzig.irpact.core.process.ra.RAConstants;
import de.unileipzig.irpact.core.process2.handler.InitializationHandler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.init.general.InLinearePercentageAgentAttributeScaler;
import de.unileipzig.irpact.io.param.input.process2.modular.components.reeval.ca.InLinearePercentageAgentAttributeUpdater;
import de.unileipzig.irpact.io.param.input.process2.modular.models.ca.InBasicCAModularProcessModel;
import de.unileipzig.irpact.util.scenarios.CorporateDesignUniLeipzig;
import de.unileipzig.irpact.util.scenarios.pvact.RealData;
import de.unileipzig.irpact.util.scenarios.util.AttributeNameManager;
import de.unileipzig.irpact.util.scenarios.util.ModuleManager;

/**
 * @author Daniel Abitz
 */
public class RealDataModularProcessModelTemplate extends DefaultModularProcessModelTemplate {

    public RealDataModularProcessModelTemplate(String mpmName) {
        super(mpmName);
        init();
    }

    public RealDataModularProcessModelTemplate(ModuleManager mm, AttributeNameManager anm, String mpmName) {
        super(mm, anm, mpmName);
        init();
    }

    protected void init() {
        setColorPalette(CorporateDesignUniLeipzig.IN_CD_UL);
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

    @Override
    protected void setupInitialAdopter() {
        useRealDataBasedInitialAdopter();
    }

    @Override
    protected void setupWeights() {
        findWeightModule(getNpvWeightModuleName()).setScalar(RealData.WEIGHT_NPV);
        findWeightModule(getPpWeightModuleName()).setScalar(RealData.WEIGHT_EK);
        findWeightModule(getEnvWeightModuleName()).setScalar(RealData.WEIGHT_NEP);
        findWeightModule(getNovWeightModuleName()).setScalar(RealData.WEIGHT_NS);
        findWeightModule(getLocalShareWeightModuleName()).setScalar(RealData.WEIGHT_LOCALE);
        findWeightModule(getSocialShareWeightModuleName()).setScalar(RealData.WEIGHT_SOCIAL);
    }

    @Override
    protected void applyInitComponents(InBasicCAModularProcessModel mpm) {
        super.applyInitComponents(mpm);

        InAgentAttributeScaler novScaler = new InAgentAttributeScaler();
        novScaler.setName(getNovScalerName());
        novScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        novScaler.setAttribute(anm.get(getNoveltySeekingAttributeName()));

        mpm.addInitializationHandlers(
                novScaler
        );
    }

    @Override
    protected void applyCustom(InBasicCAModularProcessModel mpm) {
        InLinearePercentageAgentAttributeScaler envScaler = new InLinearePercentageAgentAttributeScaler();
        envScaler.setName(getEnvScalerName());
        envScaler.setPriority(InitializationHandler.HIGH_PRIORITY);
        envScaler.setAttribute(anm.get(getEnvironmentalConcernAttributeName()));
        envScaler.setM(RAConstants.DEFAULT_M);
        envScaler.setN(RAConstants.DEFAULT_N);

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
}