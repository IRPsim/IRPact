package de.unileipzig.irpact.core.process;

import de.unileipzig.irpact.core.agent.consumer.ConsumerAgentGroup;
import de.unileipzig.irpact.core.misc.ValidationException;
import de.unileipzig.irpact.core.simulation.SimulationEnvironment;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class BasicProcessModelManager implements ProcessModelManager {

    protected SimulationEnvironment environment;
    protected Map<String, ProcessModel> models;

    public BasicProcessModelManager() {
        this(new HashMap<>());
    }

    public BasicProcessModelManager(Map<String, ProcessModel> models) {
        this.models = models;
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize() {
        for(ProcessModel model: getProcessModels()) {
            model.initialize();
        }
    }

    @Override
    public void validate() throws ValidationException {
        for(ProcessModel model: getProcessModels()) {
            model.validate();
        }
    }

    @Override
    public void setup() {
        for(ProcessModel model: getProcessModels()) {
            model.setup();
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
