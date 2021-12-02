package de.unileipzig.irpact.util.scenarios.pvact.util;

import de.unileipzig.irpact.util.scenarios.pvact.RealData;
import de.unileipzig.irpact.util.scenarios.pvact.toymodels.util.ModularProcessModelManager;
import de.unileipzig.irpact.util.scenarios.util.AttributeNameManager;

/**
 * @author Daniel Abitz
 */
public class RealDataModularProcessModelTemplate extends DefaultModularProcessModelTemplate {

    public RealDataModularProcessModelTemplate(String mpmName) {
        super(mpmName);
    }

    public RealDataModularProcessModelTemplate(ModularProcessModelManager mpmm, AttributeNameManager anm, String mpmName) {
        super(mpmm, anm, mpmName);
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
}