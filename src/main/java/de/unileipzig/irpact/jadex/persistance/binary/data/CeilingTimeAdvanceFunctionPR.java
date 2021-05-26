package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.time.UnitStepDiscreteTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class CeilingTimeAdvanceFunctionPR extends BinaryPRBase<UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(CeilingTimeAdvanceFunctionPR.class);

    public static final CeilingTimeAdvanceFunctionPR INSTANCE = new CeilingTimeAdvanceFunctionPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction> getType() {
        return UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putLong(object.getAmountToAdd());
        data.putText(UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction.getName(object.getUnit()));

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction object = new UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction();
        object.setAmountToAdd(data.getLong());
        object.setUnit(UnitStepDiscreteTimeModel.CeilingTimeAdvanceFunction.forName(data.getText()));

        return object;
    }
}
