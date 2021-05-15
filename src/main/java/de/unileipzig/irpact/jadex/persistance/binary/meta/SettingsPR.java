package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SettingsPR {

    public static final long UID = 0;
    public static final String UID_STR = "0";
    public static final String PREFIX = "y0y";
    public static final String PREFIX_Y = "y";

    protected ObjectNode root;

    public SettingsPR(JsonNodeCreator creator) {
        this(creator.objectNode());
    }

    public SettingsPR(ObjectNode root) {
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
}
