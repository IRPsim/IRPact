package de.unileipzig.irpact.util.scenarios.pvact.toymodels.util;

import de.unileipzig.irpact.io.param.input.process.ra.InNodeDistanceFilterScheme;
import de.unileipzig.irpact.io.param.input.process.ra.uncert.InUncertaintySupplier;
import de.unileipzig.irpact.util.scenarios.pvact.util.DefaultModularProcessModelTemplate;

/**
 * @author Daniel Abitz
 */
public class ToyModeltModularProcessModelTemplate extends DefaultModularProcessModelTemplate {

    public ToyModeltModularProcessModelTemplate(String mpmName) {
        super(mpmName);
    }

    public void setUncertaintySupplierInstance(InUncertaintySupplier supplier) {
        setUncertaintySupplier(() -> supplier);
    }

    public InUncertaintySupplier getUncertaintySupplierInstance() {
        return getValidUncertaintySupplier();
    }

    public void setDistanceFilterSupplierInstance(InNodeDistanceFilterScheme scheme) {
        setDistanceFilterSupplier(() -> scheme);
    }

    public InNodeDistanceFilterScheme getDistanceFilterSupplierInstance() {
        return getValidDistanceFilterScheme();
    }

    public void setAllWeights(double value) {
        getNpvWeightModule().setScalar(value);
        getPpWeightModule().setScalar(value);
        getLocalWeightModule().setScalar(value);
        getSocialWeightModule().setScalar(value);
        getEnvWeightModule().setScalar(value);
        getNovWeightModule().setScalar(value);
    }
}
