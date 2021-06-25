package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.network.BasicSocialNetwork;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicSocialNetworkPR extends BinaryPRBase<BasicSocialNetwork> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicSocialNetworkPR.class);

    public static final BasicSocialNetworkPR INSTANCE = new BasicSocialNetworkPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<BasicSocialNetwork> getType() {
        return BasicSocialNetwork.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(BasicSocialNetwork object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);

        manager.prepare(object.getGraph());
        manager.prepare(object.getGraphTopologyScheme());

        return data;
    }

    @Override
    protected void doSetupPersist(BasicSocialNetwork object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putLong(manager.ensureGetUID(object.getGraph()));
        data.putLong(manager.ensureGetUID(object.getGraphTopologyScheme()));
    }

    //=========================
    //restore
    //=========================

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected BasicSocialNetwork doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        BasicSocialNetwork object = new BasicSocialNetwork();
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, BasicSocialNetwork object, RestoreManager manager) throws RestoreException {
        object.setEnvironment(getEnvironment(manager));
        object.setGraph(manager.ensureGet(data.getLong()));
        object.setGraphTopologyScheme(manager.ensureGet(data.getLong()));
    }
}
