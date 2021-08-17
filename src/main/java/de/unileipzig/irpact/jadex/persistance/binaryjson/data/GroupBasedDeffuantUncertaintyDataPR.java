package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.process.ra.uncert.GroupBasedDeffuantUncertaintyData;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class GroupBasedDeffuantUncertaintyDataPR extends BinaryPRBase<GroupBasedDeffuantUncertaintyData> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(GroupBasedDeffuantUncertaintyDataPR.class);

    public static final GroupBasedDeffuantUncertaintyDataPR INSTANCE = new GroupBasedDeffuantUncertaintyDataPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<GroupBasedDeffuantUncertaintyData> getType() {
        return GroupBasedDeffuantUncertaintyData.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(GroupBasedDeffuantUncertaintyData object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putDouble(object.getExtremistParameter());
        data.putDouble(object.getExtremistUncertainty());
        data.putDouble(object.getModerateUncertainty());
        data.putBoolean(object.isLowerBoundInclusive());
        data.putBoolean(object.isUpperBoundInclusive());

        manager.prepare(object.getConsumerAgentGroup());
        manager.nullablePrepareAll(object.getRanges().values());

        return data;
    }

    @Override
    protected void doSetupPersist(GroupBasedDeffuantUncertaintyData object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getConsumerAgentGroup()));
        data.putNullableIdMapWithStringKey(object.getRanges(), manager.ensureGetUIDFunction());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected GroupBasedDeffuantUncertaintyData doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        GroupBasedDeffuantUncertaintyData object = new GroupBasedDeffuantUncertaintyData();
        object.setName(data.getText());
        object.setExtremistParameter(data.getDouble());
        object.setExtremistUncertainty(data.getDouble());
        object.setModerateUncertainty(data.getDouble());
        object.setLowerBoundInclusive(data.getBoolean());
        object.setUpperBoundInclusive(data.getBoolean());

        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, GroupBasedDeffuantUncertaintyData object, RestoreManager manager) throws RestoreException {
        object.setConsumerAgentGroup(manager.ensureGet(data.getLong()));
        data.getNullableIdMapWithStringKey(manager.ensureGetFunction(), object.getRanges());
    }
}
