package de.unileipzig.irpact.jadex.persistance.binary.meta;

import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.persistence.PersistableBase;
import de.unileipzig.irpact.commons.util.IRPactJson;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public class SettingsPR extends PersistableBase {

    protected static final String MODE_KEY = "0";
    protected static final int SINGLE_ENTRY_MODE = 0;
    protected static final int COMBINED_ENTRY_MODE = 1;

    protected static final String HAS_CLASSMANAGER_KEY = "1";

    protected ObjectNode root;

    public SettingsPR(JsonNodeCreator creator) {
        this(creator.objectNode());
    }

    public SettingsPR(ObjectNode root) {
        this.root = root;
        setUID(0);
        setSingleEntryMode();
        setHasNoClassManager();
    }

    public ObjectNode getRoot() {
        return root;
    }

    public byte[] toBytes() throws IOException {
        return IRPactJson.toBytesWithSmile(root);
    }

    public void setSingleEntryMode() {
        root.put(MODE_KEY, SINGLE_ENTRY_MODE);
    }

    public void setCombinedEntryMode() {
        root.put(MODE_KEY, COMBINED_ENTRY_MODE);
    }

    public boolean isSingleEntryMode() {
        return root.get(MODE_KEY).intValue() == SINGLE_ENTRY_MODE;
    }

    public boolean isCombinedEntryMode() {
        return root.get(MODE_KEY).intValue() == COMBINED_ENTRY_MODE;
    }

    public void setHasClassManager() {
        root.put(HAS_CLASSMANAGER_KEY, true);
    }

    public void setHasNoClassManager() {
        root.put(HAS_CLASSMANAGER_KEY, false);
    }

    public boolean hasClassManager() {
        return root.get(HAS_CLASSMANAGER_KEY).booleanValue();
    }
}
