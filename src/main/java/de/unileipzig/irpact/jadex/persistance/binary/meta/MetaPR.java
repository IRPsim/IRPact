package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.core.util.MetaData;
import de.unileipzig.irpact.core.util.RunInfo;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.jadex.persistance.JadexPersistable;
import de.unileipzig.irpact.jadex.persistance.binary.io.BinaryPersistJson;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Daniel Abitz
 */
public final class MetaPR extends PersistableBase implements JadexPersistable {

    public static final String UID_PREFIX = "y";
    public static final long UID = 0;

    protected final List<RunInfo> infos = new ArrayList<>();

    protected ObjectNode root;

    public MetaPR(ObjectNode root) {
        this.root = root;
        setUID(UID);
    }

    public ObjectNode getRoot() {
        return root;
    }

    public void store(MetaData metaData) {
        infos.addAll(metaData.getAllRunInfos());
    }

    public void restore(MetaData metaData) {
        metaData.init(infos);
    }

    public void parseRoot() {
        parseRuns();
    }

    @SuppressWarnings("UnusedAssignment")
    private void parseRuns() {
        ArrayNode runs = (ArrayNode) root.get("runs");
        if(runs != null) {
            for(int i = 0; i < runs.size(); i++) {
                ArrayNode entry = (ArrayNode) runs.get(i);

                int index = 0;
                RunInfo info = new RunInfo();
                info.setId(entry.get(index++).intValue());
                info.setStartTime(entry.get(index++).longValue());
                info.setEndTime(entry.get(index++).longValue());
                info.setFirstSimulationYear(entry.get(index++).intValue());
                info.setActualFirstSimulationYear(entry.get(index++).intValue());
                info.setLastSimulationYear(entry.get(index++).intValue());

                infos.add(info);
            }
        }
    }

    public void updateRoot() {
        root.removeAll();
        putRuns();
    }

    private void putRuns() {
        ArrayNode runs = IRPactJson.computeArrayIfAbsent(root, "runs");
        for(RunInfo info: infos) {
            ArrayNode entry = runs.addArray();
            entry.add(info.getId());
            entry.add(info.getStartTimeMillis());
            entry.add(info.getEndTimeMillis());
            entry.add(info.getFirstSimulationYear());
            entry.add(info.getActualFirstSimulationYear());
            entry.add(info.getLastSimulationYear());
        }
    }

    @Override
    public BinaryPersistData toPersistData() {
        updateRoot();
        return BinaryPersistJson.toData(root, UID_PREFIX, getUID());
    }
}
