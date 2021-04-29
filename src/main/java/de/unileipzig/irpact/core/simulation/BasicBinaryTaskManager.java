package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.misc.InitializationStage;
import de.unileipzig.irpact.core.simulation.tasks.*;
import de.unileipzig.irpact.commons.util.data.BinaryData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicBinaryTaskManager implements BinaryTaskManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicBinaryTaskManager.class);

    protected SimulationEnvironment environment;
    protected List<InitalizationStageTask> stageTasks = new ArrayList<>();

    public BasicBinaryTaskManager() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void copyFrom(BasicBinaryTaskManager other) {
        stageTasks.addAll(other.stageTasks);
    }

    @Override
    public void handle(Collection<? extends BinaryData> rawData) {
        for(BinaryData bd: rawData) {
            handle(bd);
        }
    }

    @Override
    public void handle(BinaryData data) {
        if(data == null) {
            return;
        }

        try {
            int id = (int) data.getID();
            InitalizationStageTask task = null;
            switch (id) {
                case PredefinedPreAgentCreationTask.ID:
                    task = new PredefinedPreAgentCreationTask(data.getBytes());
                    break;

                case PredefinedPostAgentCreationTask.ID:
                    task = new PredefinedPostAgentCreationTask(data.getBytes());
                    break;

                case PredefinedPrePlatformCreationTask.ID:
                    task = new PredefinedPrePlatformCreationTask(data.getBytes());
                    break;
            }
            if(task != null) {
                stageTasks.add(task);
            }
        } catch (Exception e) {
            LOGGER.error("failed to create task with id '" + data.getID() + "', content: '" + data.print() + "'", e);
        }
    }

    @Override
    public void runInitializationStageTasks(InitializationStage stage, SimulationEnvironment environment) {
        runInitializationStageTasks0(stage, environment);
    }

    @Override
    public void runAllInitializationStageTasks(SimulationEnvironment environment) {
        runInitializationStageTasks0(null, environment);
    }

    protected void runInitializationStageTasks0(InitializationStage stage, SimulationEnvironment environment) {
        for(InitalizationStageTask task: stageTasks) {
            if(stage != null && stage != task.getStage()) {
                continue;
            }

            try {
                task.run(environment);
            } catch (Exception e) {
                LOGGER.error("SimulationTask '" + task.getInfo() + "' failed", e);
            }
        }
    }
}
