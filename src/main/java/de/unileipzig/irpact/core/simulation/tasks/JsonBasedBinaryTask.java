package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.NameableBase;
import de.unileipzig.irpact.commons.util.JsonUtil;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Implements a binary task based on json.
 *
 * @author Daniel Abitz
 */
public abstract class JsonBasedBinaryTask extends NameableBase implements BinaryTask {

    protected static final String NO_INFO = "no info";
    public static final String INFO_KEY = "i";

    protected ObjectNode root;

    public JsonBasedBinaryTask(ObjectNode root) {
        this.root = root;
    }

    public void setInfo(String info) {
        root.put(INFO_KEY, info);
    }

    public ObjectNode getRoot() {
        return root;
    }

    @Override
    public String getInfo() {
        JsonNode info = root.get(INFO_KEY);
        return info.textValue();
    }

    @Override
    public byte[] getBytes() {
        try {
            return JsonUtil.toBytesWithSmile(root);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
