package de.unileipzig.irpact.core.simulation.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irpact.io.inout.binary.HiddenBinaryData;
import de.unileipzig.irpact.io.input.binary.VisibleBinaryData;

/**
 * @author Daniel Abitz
 */
public abstract class AbstractTask implements Task {

    public static final String INFO = "i";

    protected ObjectNode node;

    public AbstractTask(ObjectNode node) {
        this.node = node;
    }

    protected abstract long getID();

    public void setInfo(String info) {
        node.put(INFO, info);
    }

    @Override
    public String getInfo() {
        JsonNode info = node.get(INFO);
        return info.textValue();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> R toBinary(Class<R> c) throws Exception {
        byte[] content = IRPactJson.toBytesWithSmile(node);
        if(c == HiddenBinaryData.class) {
            HiddenBinaryData hdb = new HiddenBinaryData();
            hdb.setID(getID());
            hdb.setBytes(content);
            return (R) hdb;
        }
        else if(c == VisibleBinaryData.class) {
            VisibleBinaryData hdb = new VisibleBinaryData();
            hdb.setID(getID());
            hdb.setBytes(content);
            return (R) hdb;
        }
        else {
            throw new IllegalArgumentException("unsupported type: " + c.getName());
        }
    }
}
