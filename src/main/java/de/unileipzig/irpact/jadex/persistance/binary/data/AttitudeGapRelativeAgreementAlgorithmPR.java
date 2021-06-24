package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.ra.alg.AttitudeGapRelativeAgreementAlgorithm;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class AttitudeGapRelativeAgreementAlgorithmPR extends BinaryPRBase<AttitudeGapRelativeAgreementAlgorithm> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(AttitudeGapRelativeAgreementAlgorithmPR.class);

    public static final AttitudeGapRelativeAgreementAlgorithmPR INSTANCE = new AttitudeGapRelativeAgreementAlgorithmPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<AttitudeGapRelativeAgreementAlgorithm> getType() {
        return AttitudeGapRelativeAgreementAlgorithm.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(AttitudeGapRelativeAgreementAlgorithm object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getAttitudeGap());
        data.putBoolean(object.isLogDataFallback());
        data.putDouble(object.getWeight(AttitudeGapRelativeAgreementAlgorithm.Mode.NEUTRAL));
        data.putDouble(object.getWeight(AttitudeGapRelativeAgreementAlgorithm.Mode.CONVERGENCE));
        data.putDouble(object.getWeight(AttitudeGapRelativeAgreementAlgorithm.Mode.DIVERGENCE));

        manager.prepare(object.getEnvironment());
        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(AttitudeGapRelativeAgreementAlgorithm object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getEnvironment()));
        data.putLong(manager.ensureGetUID(object.getRandom()));
    }

    //=========================
    //restore
    //=========================

    @Override
    protected AttitudeGapRelativeAgreementAlgorithm doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        AttitudeGapRelativeAgreementAlgorithm object = new AttitudeGapRelativeAgreementAlgorithm();
        object.setName(data.getText());
        object.setAttitudeGap(data.getDouble());
        object.setLogDataFallback(data.getBoolean());
        object.setWeightes(data.getDouble(), data.getDouble(), data.getDouble());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, AttitudeGapRelativeAgreementAlgorithm object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(manager.ensureGet(data.getLong()));
        object.setRandom(manager.ensureGet(data.getLong()));
    }
}
