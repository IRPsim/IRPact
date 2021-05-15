package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.jadex.persistance.binary.ClassManager;
import de.unileipzig.irptools.util.Util;

import java.io.IOException;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class ClassManagerPR extends PersistableBase {

    public static final long UID = 1;
    public static final String UID_STR = "1";
    public static final String PREFIX = "y1y";
    public static final String PREFIX_Y = "y";

    protected ObjectNode root;

    public ClassManagerPR(JsonNodeCreator creator) {
        this(creator.objectNode());
    }

    public ClassManagerPR(ObjectNode root) {
        this.root = root;
    }

    public ObjectNode getRoot() {
        return root;
    }

    public long getUID() {
        return UID;
    }

    public byte[] toBytes() throws IOException {
        return IRPactJson.toBytesWithSmile(root);
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
}
