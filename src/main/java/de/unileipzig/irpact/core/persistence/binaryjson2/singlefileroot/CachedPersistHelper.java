package de.unileipzig.irpact.core.persistence.binaryjson2.singlefileroot;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;
import de.unileipzig.irpact.commons.util.JsonUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class CachedPersistHelper extends AbstractPersistHelper {

    protected JsonNodeCreator creator = JsonUtil.JSON.getNodeFactory();
    protected Map<Long, ArrayNode> nodes = new HashMap<>();

    public CachedPersistHelper() {
    }

    public Map<Long, ArrayNode> getNodes() {
        return nodes;
    }

    @Override
    protected ArrayNode getNode(long uid) {
        ArrayNode node = nodes.get(uid);
        if(node == null) {
            node = creator.arrayNode();;
            nodes.put(uid, node);
        }
        return node;
    }

    @Override
    protected void handleNode(long uid, ArrayNode node) {
        if(nodes.containsKey(uid)) {
            if(nodes.get(uid) != node) {
                throw new IllegalArgumentException("node already exists for uid" + uid);
            }
        } else {
            nodes.put(uid, node);
        }
    }
}
