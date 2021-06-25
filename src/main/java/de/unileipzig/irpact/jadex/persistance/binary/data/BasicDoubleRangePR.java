package de.unileipzig.irpact.jadex.persistance.binary.data;

import de.unileipzig.irpact.commons.persistence.PersistException;
import de.unileipzig.irpact.commons.persistence.PersistManager;
import de.unileipzig.irpact.commons.persistence.RestoreException;
import de.unileipzig.irpact.commons.persistence.RestoreManager;
import de.unileipzig.irpact.commons.util.DoubleRange;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.persistence.binary.BinaryJsonData;
import de.unileipzig.irpact.core.persistence.binary.BinaryPRBase;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class BasicDoubleRangePR extends BinaryPRBase<DoubleRange> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicDoubleRangePR.class);

    public static final BasicDoubleRangePR INSTANCE = new BasicDoubleRangePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    //=========================
    //persist
    //=========================

    @Override
    public Class<DoubleRange> getType() {
        return DoubleRange.class;
    }

    @Override
    protected BinaryJsonData doInitalizePersist(DoubleRange object, PersistManager manager) throws PersistException {
        BinaryJsonData data = initData(object, manager);
        data.putDouble(object.getLowerBound());
        data.putDouble(object.getUpperBound());
        data.putBoolean(object.isLowerBoundInclusive());
        data.putBoolean(object.isUpperBoundInclusive());

        return data;
    }

    //=========================
    //restore
    //=========================

    @Override
    protected DoubleRange doInitalizeRestore(BinaryJsonData data, RestoreManager manager) throws RestoreException {
        DoubleRange object = new DoubleRange();
        object.setLowerBound(data.getDouble());
        object.setUpperBound(data.getDouble());
        object.setLowerBoundInclusive(data.getBoolean());
        object.setUpperBoundInclusive(data.getBoolean());

        return object;
    }
}
