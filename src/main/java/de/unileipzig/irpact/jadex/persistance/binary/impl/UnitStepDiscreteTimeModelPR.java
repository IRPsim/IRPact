package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irpact.jadex.time.UnitStepDiscreteTimeModel;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class UnitStepDiscreteTimeModelPR extends BinaryPRBase<UnitStepDiscreteTimeModel> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(UnitStepDiscreteTimeModelPR.class);

    public static final UnitStepDiscreteTimeModelPR INSTANCE = new UnitStepDiscreteTimeModelPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<UnitStepDiscreteTimeModel> getType() {
        return UnitStepDiscreteTimeModel.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(UnitStepDiscreteTimeModel object, PersistManager manager) {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());

        manager.prepare(object.getTimeAdvanceFunction());

        return data;
    }

    @Override
    protected void doSetupPersist(UnitStepDiscreteTimeModel object, BinaryJsonData data, PersistManager manager) {
        data.putLong(manager.ensureGetUID(object.getTimeAdvanceFunction()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected UnitStepDiscreteTimeModel doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        UnitStepDiscreteTimeModel object = new UnitStepDiscreteTimeModel();
        object.setName(data.getText());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, UnitStepDiscreteTimeModel object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGetInstanceOf(JadexSimulationEnvironment.class));
        object.setTimeAdvanceFunction(manager.ensureGet(data.getLong()));
    }
}
