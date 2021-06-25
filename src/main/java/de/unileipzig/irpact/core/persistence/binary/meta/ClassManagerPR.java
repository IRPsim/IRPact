package de.unileipzig.irpact.core.persistence.binary.meta;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.io.param.inout.persist.binary.BinaryPersistData;
import de.unileipzig.irpact.jadex.persistance.JadexPersistable;
import de.unileipzig.irpact.core.persistence.binary.ClassManager;
import de.unileipzig.irpact.core.persistence.binary.io.BinaryPersistJson;
import de.unileipzig.irptools.util.Util;

import java.util.Map;

/**
 * @author Daniel Abitz
 */
public final class ClassManagerPR extends PersistableBase implements JadexPersistable {

    public static final String UID_PREFIX = MetaPR.UID_PREFIX;
    public static final long UID = 1;

    protected ObjectNode root;
    protected ClassManager manager;

    public ClassManagerPR(ObjectNode root, ClassManager manager) {
        this.root = root;
        this.manager = manager;
        setUID(UID);
    }

    public ObjectNode getRoot() {
        return root;
    }

    public ClassManager getManager() {
        return manager;
    }

    protected void updateRoot() {
        root.removeAll();
        toJson(manager);
    }

    public void toJson(ClassManager classManager) {
        ObjectNode root = getRoot();
        for(Map.Entry<String, Long> class2idEntry: classManager.getClass2Id().entrySet()) {
            root.put(class2idEntry.getKey(), class2idEntry.getValue());
        }
    }

    public void fromJson(ClassManager classManager) {
        ObjectNode root = getRoot();
        for(Map.Entry<String, JsonNode> entry: Util.iterateFields(root)) {
            long id = entry.getValue().longValue();
            classManager.set(entry.getKey(), id);
        }
    }

    @Override
    public BinaryPersistData toPersistData() {
        updateRoot();
        return BinaryPersistJson.toData(root, UID_PREFIX, getUID());
    }
}
