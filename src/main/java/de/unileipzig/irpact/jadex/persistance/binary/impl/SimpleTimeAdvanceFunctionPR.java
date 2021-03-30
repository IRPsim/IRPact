package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.time.UnitStepDiscreteTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SimpleTimeAdvanceFunctionPR extends BinaryPRBase<UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SimpleTimeAdvanceFunctionPR.class);

    public static final SimpleTimeAdvanceFunctionPR INSTANCE = new SimpleTimeAdvanceFunctionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction> getType() {
        return UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putLong(object.getAmountToAdd());
        data.putText(UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction.getName(object.getUnit()));

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction object = new UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction();
        object.setAmountToAdd(data.getLong());
        object.setUnit(UnitStepDiscreteTimeModel.SimpleTimeAdvanceFunction.forName(data.getText()));
        return object;
    }
}
