package de.unileipzig.irpact.jadex.persistance.binary.impl;

import de.unileipzig.irpact.commons.exception.RestoreException;
import de.unileipzig.irpact.commons.persistence.*;
import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.process.FixProcessModelFindingScheme;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonData;
import de.unileipzig.irpact.jadex.persistance.binary.BinaryJsonPersistanceManager;
import de.unileipzig.irpact.jadex.simulation.JadexSimulationEnvironment;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class FixProcessModelFindingSchemePR extends BinaryPRBase<FixProcessModelFindingScheme> {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(FixProcessModelFindingSchemePR.class);

    public static final FixProcessModelFindingSchemePR INSTANCE = new FixProcessModelFindingSchemePR();

    @Override
    protected IRPLogger log() {
        return LOGGER;
    }

    @Override
    public Class<FixProcessModelFindingScheme> getType() {
        return FixProcessModelFindingScheme.class;
    }

    @Override
    public Persistable initalizePersist(FixProcessModelFindingScheme object, PersistManager manager) {
        BinaryJsonData data = BinaryJsonPersistanceManager.initData(object, manager);
        data.putText(object.getName());
        data.putText(object.getModel().getName());
        storeHash(object, data);
        return data;
    }

    @Override
    public FixProcessModelFindingScheme initalizeRestore(Persistable persistable, RestoreManager manager) {
        BinaryJsonData data = check(persistable);
        FixProcessModelFindingScheme object = new FixProcessModelFindingScheme();
        object.setName(data.getText());
        return object;
    }

    @Override
    public void setupRestore(Persistable persistable, FixProcessModelFindingScheme object, RestoreManager manager) {
    }

    @Override
    protected void doFinalizeRestore(Persistable persistable, FixProcessModelFindingScheme object, RestoreManager manager) throws RestoreException {
        BinaryJsonData data = check(persistable);
        String modelName = data.getText();

        JadexSimulationEnvironment env = manager.ensureGetInstanceOf(JadexSimulationEnvironment.class);
        ProcessModel model = env.getProcessModels().getProcessModel(modelName);
        if(model == null) {
            throw new RestoreException("model '" + modelName + "' not found");
        }
        object.setModel(model);
    }
}
