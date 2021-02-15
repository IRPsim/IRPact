package de.unileipzig.irpact.core.simulation;

import de.unileipzig.irpact.core.log.IRPLogging;
import de.unileipzig.irpact.core.simulation.tasks.BasicNonSimulationTask;
import de.unileipzig.irpact.core.simulation.tasks.BasicSimulationTask;
import de.unileipzig.irpact.commons.BinaryData;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.io.IOException;
import java.util.*;

/**
 * @author Daniel Abitz
 */
public class BasicBinaryDataManager implements BinaryDataManager {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(BasicBinaryDataManager.class);

    protected SimulationEnvironment environment;

    public BasicBinaryDataManager() {
    }

    public void setEnvironment(SimulationEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public void handle(Collection<? extends BinaryData> rawData) throws Exception {
        NavigableMap<Long, BinaryData> sortedData = removeSortedData(rawData);
        List<BinaryData> listedData = new ArrayList<>(sortedData.values());
        handleListedData(listedData);
        handleRest(rawData);
    }

    private NavigableMap<Long, BinaryData> removeSortedData(Collection<? extends BinaryData> rawData) {
        NavigableMap<Long, BinaryData> map = new TreeMap<>();
        Iterator<? extends BinaryData> iter = rawData.iterator();
        while(iter.hasNext()) {
            BinaryData bd = iter.next();
            if(bd.getID() >= 0L) {
                map.put(bd.getID(), bd);
                iter.remove();
            }
        }
        return map;
    }

    private void handleListedData(List<? extends BinaryData> sortedData) {
        for(int i = 0; i < sortedData.size(); i++) {
            if(i != sortedData.get(i).getID()) {
                LOGGER.info("id mismatch {} != {}", i, sortedData.get(i).getID());
                return;
            }
        }
    }

    private void handleRest(Collection<? extends BinaryData> rawData) throws IOException {
        for(BinaryData bd: rawData) {
            if(bd.getID() == BasicNonSimulationTask.ID) {
                handleAppTask(bd);
            }
            else if(bd.getID() == BasicSimulationTask.ID) {
                handleSimulationTask(bd);
            }
            else {
                LOGGER.info("unsupported id: {}", bd.getID());
            }
        }
    }

    private void handleAppTask(BinaryData bd) throws IOException {
        byte[] content = bd.getBytes();
        BasicNonSimulationTask task = new BasicNonSimulationTask(content);
        try {
            task.run();
        } catch (Exception e) {
            LOGGER.error("task '" + task.getInfo() + "' failed", e);
        }
    }

    private void handleSimulationTask(BinaryData bd) throws IOException {
        byte[] content = bd.getBytes();
        BasicSimulationTask task = new BasicSimulationTask(content);
        environment.getInitializationData().addTask(task);
    }
}
