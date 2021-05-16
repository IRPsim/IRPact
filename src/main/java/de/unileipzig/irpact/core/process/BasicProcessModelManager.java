package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.commons.checksum.ChecksumComparable;
import de.unileipzig.irpact.commons.exception.InitializationException;
import de.unileipzig.irpact.core.misc.MissingDataException;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicProcessModelManager implements ProcessModelManager {

    protected SimulationEnvironment environment;
    protected Map<String, ProcessModel> models;

    public BasicProcessModelManager() {
        this(new LinkedHashMap<>());
    }

    public BasicProcessModelManager(Map<String, ProcessModel> models) {
        this.models = models;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public int getChecksum() {
        return ChecksumComparable.getCollChecksum(getProcessModels());
    }

    @Override
    public void preAgentCreation() throws MissingDataException {
        for(ProcessModel model: getProcessModels()) {
            model.preAgentCreation();
        }
    }

    @Override
    public void preAgentCreationValidation() throws ValidationException {
        for(ProcessModel model: getProcessModels()) {
            model.preAgentCreationValidation();
        }
    }

    @Override
    public void postAgentCreation() throws MissingDataException, InitializationException {
        for(ProcessModel model: getProcessModels()) {
            model.postAgentCreation();
        }
    }

    @Override
    public void postAgentCreationValidation() throws ValidationException {
        for(ProcessModel model: getProcessModels()) {
            model.postAgentCreationValidation();
        }
    }

    @Override
    public void preSimulationStart() throws MissingDataException {
        for(ProcessModel model: getProcessModels()) {
            model.preSimulationStart();
        }
    }

    @Override
    public Collection<ProcessModel> getProcessModels() {
        return models.values();
    }

    @Override
    public boolean hasProcessModel(String name) {
        return models.containsKey(name);
    }

    @Override
    public ProcessModel getProcessModel(String name) {
        return models.get(name);
    }

    public void addProcessModel(ProcessModel model) {
        if(hasProcessModel(model.getName())) {
            throw new IllegalArgumentException("model name '" + model.getName() + "' already exists");
        }
        models.put(model.getName(), model);
    }
}
