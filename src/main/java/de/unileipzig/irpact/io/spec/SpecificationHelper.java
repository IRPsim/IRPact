package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irptools.util.Util;

import java.util.Map;
import java.util.Objects;

import static de.unileipzig.irpact.io.spec.SpecificationConstants.*;

/**
 * @author Daniel Abitz
 */
public final class SpecificationHelper {

    protected JsonNode root;

    public SpecificationHelper(JsonNode root) {
        this.root = root;
    }

    public JsonNode root() {
        return root;
    }

    public ObjectNode rootAsObject() {
        return (ObjectNode) root;
    }

    public ArrayNode rootAsArray() {
        return (ArrayNode) root;
    }

    public void clear() {
        if(root.isArray()) {
            rootAsArray().removeAll();
        } else {
            rootAsObject().removeAll();
        }
    }

    public int size() {
        return root.size();
    }

    public SpecificationHelper get(int index) {
        return new SpecificationHelper(root.get(index));
    }

    public String getName() {
        return IRPactJson.getText(root, TAG_name, null);
    }

    public void setName(String name) {
        rootAsObject().put(TAG_name, name);
    }

    public void setGroup(String name) {
        rootAsObject().put(TAG_group, name);
    }

    public String getGroup() {
        return IRPactJson.getText(root, TAG_group, null);
    }

    public void setValue(double value) {
        rootAsObject().put(TAG_value, value);
    }

    public String getType() {
        return IRPactJson.getText(root, TAG_type, null);
    }

    public void setType(String name) {
        rootAsObject().put(TAG_type, name);
    }

    public double getValue() {
        return IRPactJson.getDouble(root, TAG_value, Double.NaN);
    }

    public int getNumberOfAgents() {
        return IRPactJson.getInt(root, TAG_numberOfAgents, -1);
    }

    public void setNumberOfAgents(int value) {
        rootAsObject().put(TAG_numberOfAgents, value);
    }

    public ArrayNode getAttributes() {
        return Util.getOrCreateArray(rootAsObject(), TAG_attributes);
    }

    public SpecificationHelper getAttributesSpec() {
        return new SpecificationHelper(getAttributes());
    }

    public JsonNode getParameters() {
        return root.get(TAG_parameters);
    }

    public SpecificationHelper getParametersSpec() {
        return new SpecificationHelper(getParameters());
    }

    public ObjectNode addObject() {
        return rootAsArray().addObject();
    }

    public SpecificationHelper addObjectSpec() {
        return new SpecificationHelper(addObject());
    }

    public double getParametersDouble(String key) {
        JsonNode params = getParameters();
        if(params == null) {
            throw new NullPointerException("no parameters");
        }
        return IRPactJson.getDouble(params, key, Double.NaN);
    }

    public long getParametersLong(String key) {
        JsonNode params = getParameters();
        if(params == null) {
            throw new NullPointerException("no parameters");
        }
        return IRPactJson.getLong(params, key, -1L);
    }

    public int getParametersInt(String key) {
        JsonNode params = getParameters();
        if(params == null) {
            throw new NullPointerException("no parameters");
        }
        return IRPactJson.getInt(params, key, -1);
    }

    public String getParametersText(String key) {
        JsonNode params = getParameters();
        if(params == null) {
            throw new NullPointerException("no parameters");
        }
        return IRPactJson.getText(params, key, null);
    }

    public double getParametersValue() {
        JsonNode params = getParameters();
        if(params == null) {
            return Double.NaN;
        }
        return IRPactJson.getDouble(params, TAG_value, Double.NaN);
    }

    public void setParametersValue(double value) {
        setParameters(TAG_value, value);
    }

    public void setParameters(String key, long value) {
        ObjectNode params = Util.getOrCreateObject(rootAsObject(), TAG_parameters);
        params.put(key, value);
    }

    public void setParameters(String key, double value) {
        ObjectNode params = Util.getOrCreateObject(rootAsObject(), TAG_parameters);
        params.put(key, value);
    }

    public void setParametersValue(String value) {
        setParameters(TAG_value, value);
    }

    public void setParameters(String key, String value) {
        ObjectNode params = Util.getOrCreateObject(rootAsObject(), TAG_parameters);
        params.put(key, value);
    }

    public void setParameter(double value) {
        rootAsObject().put(TAG_parameter, value);
    }

    public void setDistribution(String name) {
        rootAsObject().put(TAG_distribution, name);
    }

    public void setInitialWeight(double weight) {
        rootAsObject().put(TAG_initialWeight, weight);
    }

    public ObjectNode getObject(String name) {
        return Util.getOrCreateObject(rootAsObject(), name);
    }

    public ArrayNode getArray(String name) {
        return Util.getOrCreateArray(rootAsObject(), name);
    }

    public SpecificationHelper getObjectSpec(String name) {
        return new SpecificationHelper(getObject(name));
    }

    public SpecificationHelper getArraySpec(String name) {
        return new SpecificationHelper(getArray(name));
    }

    public void set(String key, String value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, long value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, double value) {
        rootAsObject().put(key, value);
    }

    public void set(String key, boolean value) {
        rootAsObject().put(key, value);
    }

    public boolean hasArrayEntry(String value) {
        for(JsonNode node: iterateElements()) {
            if(node.isTextual() && Objects.equals(node.textValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public void add(String text) {
        rootAsArray().add(text);
    }

    public void addIfNotExists(String value) {
        if(!hasArrayEntry(value)) {
            rootAsArray().add(value);
        }
    }

    public boolean isInline(String key) {
        JsonNode node = root.get(key);
        if(node == null) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.isObject();
    }

    public boolean isInline() {
        return root.isObject();
    }

    public Iterable<JsonNode> iterateElements() {
        return Util.iterateElements(root);
    }

    public Iterable<Map.Entry<String, JsonNode>> iterateFields() {
        return Util.iterateFields(root);
    }

    public String getText(String key) {
        JsonNode node = root.get(key);
        if(node == null || !node.isTextual()) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.textValue();
    }

    public String getText() {
        if(root == null || !root.isTextual()) {
            throw new IllegalArgumentException("missing node");
        }
        return root.textValue();
    }

    public int getInt(String key) {
        JsonNode node = root.get(key);
        if(node == null || !node.isNumber()) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.intValue();
    }

    public boolean getBoolean(String key) {
        JsonNode node = root.get(key);
        if(node == null || !node.isBoolean()) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.booleanValue();
    }

    public long getLong(String key) {
        JsonNode node = root.get(key);
        if(node == null || !node.isNumber()) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.longValue();
    }

    public double getDouble(String key) {
        JsonNode node = root.get(key);
        if(node == null || !node.isNumber()) {
            throw new IllegalArgumentException("missing node: " + key);
        }
        return node.doubleValue();
    }

    public double getDouble() {
        if(root == null || !root.isNumber()) {
            throw new IllegalArgumentException("missing node");
        }
        return root.doubleValue();
    }
}
