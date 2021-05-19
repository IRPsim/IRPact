package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.commons.util.IRPactJson;
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

    public void addRunInfo(RunInfo info) {
        infos.add(info);
    }

    public void parseRoot() {
        parseRuns();
    }

    private void parseRuns() {
        ArrayNode runs = (ArrayNode) root.get("runs");
        if(runs != null) {
            for(int i = 0; i < runs.size(); i++) {
                ArrayNode entry = (ArrayNode) runs.get(i);
                RunInfo info = new RunInfo();
                info.setStartTime(entry.get(0).longValue());
                info.setEndTime(entry.get(1).longValue());
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
            entry.add(info.getStartTimeMillis());
            entry.add(info.getEndTimeMillis());
        }
    }

    @Override
    public BinaryPersistData toPersistData() {
        updateRoot();
        return BinaryPersistJson.toData(root, UID_PREFIX, getUID());
    }
}
