package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.mra.component.special.DefaultHandleDecisionMakingComponent;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class DefaultHandleDecisionMakingComponentPR extends BinaryPRBase<DefaultHandleDecisionMakingComponent> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(DefaultHandleDecisionMakingComponentPR.class);

    public static final DefaultHandleDecisionMakingComponentPR INSTANCE = new DefaultHandleDecisionMakingComponentPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<DefaultHandleDecisionMakingComponent> getType() {
        return DefaultHandleDecisionMakingComponent.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(DefaultHandleDecisionMakingComponent object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getA());
        data.putDouble(object.getB());
        data.putDouble(object.getC());
        data.putDouble(object.getD());
        data.putDouble(object.getWeightFT());
        data.putDouble(object.getWeightNPV());
        data.putDouble(object.getWeightSocial());
        data.putDouble(object.getWeightLocal());
        data.putDouble(object.getLogisticFactor());

        manager.prepare(object.getModel());
        manager.prepare(object.getNodeFilterScheme());

        return data;
    }

    @Override
    protected void doSetupPersist(DefaultHandleDecisionMakingComponent object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getModel()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DefaultHandleDecisionMakingComponent doInitalizeRestore(BinaryJsonData data, RestoreManager manager) {
        DefaultHandleDecisionMakingComponent object = new DefaultHandleDecisionMakingComponent();
        object.setName(data.getText());
        object.setA(data.getDouble());
        object.setB(data.getDouble());
        object.setC(data.getDouble());
        object.setD(data.getDouble());
        object.setWeightFT(data.getDouble());
        object.setWeightNPV(data.getDouble());
        object.setWeightSocial(data.getDouble());
        object.setWeightLocal(data.getDouble());
        object.setLogisticFactor(data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, DefaultHandleDecisionMakingComponent object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));

        object.setModel(manager.ensureGet(data.getLong()));
        object.setNodeFilterScheme(manager.ensureGet(data.getLong()));
    }
}
