package de.unileipzig.irpact.jadex.persistance.binaryjson.data;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryPRBase;
import de.unileipzig.irpact.core.spatial.data.SpatialDataCollection;
import de.unileipzig.irpact.core.spatial.distribution.SpatialInformationSupplier;
import de.unileipzig.irpact.io.param.input.InRoot;
import de.unileipzig.irpact.io.param.input.spatial.dist.InSpatialDistributionWithCollection;
import de.unileipzig.irpact.core.persistence.binaryjson.BinaryJsonData;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class SpatialInformationSupplierPR extends BinaryPRBase<SpatialInformationSupplier> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(SpatialInformationSupplierPR.class);

    public static final SpatialInformationSupplierPR INSTANCE = new SpatialInformationSupplierPR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<SpatialInformationSupplier> getType() {
        return SpatialInformationSupplier.class;
    }

    //=========================
    //persist
    //=========================

    @Override
    protected BinaryJsonData doInitalizePersist(SpatialInformationSupplier object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putText(object.getName());
        data.putBoolean(object.isRemoveOnDraw());
        data.putLong(object.getIdManager().peekId());

        manager.prepare(object.getRandom());

        return data;
    }

    @Override
    protected void doSetupPersist(SpatialInformationSupplier object, BinaryJsonData data, PersistManager manager) throws PersistException {
        data.putIdValue(object.getRandom(), manager.ensureGetUIDFunction());
    }

    //=========================
    //restore
    //=========================

    @Override
    protected SpatialInformationSupplier doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        SpatialInformationSupplier object = new SpatialInformationSupplier();
        object.setName(data.getText());
        object.setRemoveOnDraw(data.getBoolean());
        object.getIdManager().reset(data.getLong());
        return object;
    }

    @Override
    protected void doSetupRestore(BinaryJsonData data, SpatialInformationSupplier object, RestoreManager manager) throws RestoreException {
        object.setRandom(manager.ensureGet(data.getLong()));

        tryLoadSpatialData(object);
    }

    protected void tryLoadSpatialData(SpatialInformationSupplier object) throws RestoreException {
        try {
            InRoot root = getRestoreHelper().getInRoot();
            LOGGER.trace("try to find spatial distribution '{}'", object.getName());
            InSpatialDistributionWithCollection distribution = root.getSpatialDistribution(InSpatialDistributionWithCollection.class, object.getName());
            if(distribution == null) {
                throw new RestoreException("distribution '{}' not found", object.getName());
            }

            LOGGER.trace("loading spatial data '{}'", distribution.getFile().getName());
            SpatialDataCollection dataColl = distribution.parseCollection(getRestoreHelper().getUpdater());
            object.setSpatialData(dataColl);
        } catch (ParsingException e) {
            throw new RestoreException(e);
        }
    }
}
