package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
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

    protected List<AppTask> appTasks = new ArrayList<>();
    protected List<SimulationTask> simulationTasks = new ArrayList<>();

    public BasicBinaryTaskManager() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    public void copyFrom(BasicBinaryTaskManager other) {
        appTasks.addAll(other.appTasks);
        simulationTasks.addAll(other.simulationTasks);
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
            switch (id) {
                case PredefinedAppTask.ID:
                    PredefinedAppTask aTask = new PredefinedAppTask(data.getBytes());
                    appTasks.add(aTask);
                    break;

                case PredefinedSimulationTask.ID:
                    PredefinedSimulationTask sTask = new PredefinedSimulationTask(data.getBytes());
                    simulationTasks.add(sTask);
                    break;
            }
        } catch (Exception e) {
            LOGGER.error("failed to create task with id '" + data.getID() + "', content: '" + data.print() + "'", e);
        }
    }

    @Override
    public void runAppTasks() {
        for(AppTask task: appTasks) {
            try {
                task.run();
            } catch (Exception e) {
                LOGGER.error("AppTask '" + task.getInfo() + "' failed", e);
            }
        }
    }

    @Override
    public void runSimulationTasks(SimulationEnvironment environment) {
        for(SimulationTask task: simulationTasks) {
            try {
                task.run(environment);
            } catch (Exception e) {
                LOGGER.error("SimulationTask '" + task.getInfo() + "' failed", e);
            }
        }
    }
}
