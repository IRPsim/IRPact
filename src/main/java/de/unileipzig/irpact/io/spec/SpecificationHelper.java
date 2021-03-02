package de.unileipzig.irpact.io.spec;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irptools.util.Util;

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

    public String getName() {
        return IRPactJson.getText(root, TAG_name, null);
    }

    public void setName(String name) {
        rootAsObject().put(TAG_name, name);
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

    public double getNumberOfAgents() {
        return IRPactJson.getDouble(root, TAG_numberOfAgents, Double.NaN);
    }

    public void setNumberOfAgents(double value) {
        rootAsObject().put(TAG_numberOfAgents, value);
    }

    public ArrayNode getAttributes() {
        return Util.getOrCreateArray(rootAsObject(), TAG_attributes);
    }

    public JsonNode getParameters() {
        return root.get(TAG_parameters);
    }

    public SpecificationHelper getParametersSpec() {
        return new SpecificationHelper(getParameters());
    }

    public SpecificationHelper addObjectSpec() {
        return new SpecificationHelper(rootAsArray().addObject());
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

    public boolean hasArrayEntry(String value) {
        for(JsonNode node: iterateElements()) {
            if(node.isTextual() && Objects.equals(node.textValue(), value)) {
                return true;
            }
        }
        return false;
    }

    public void addIfNotExists(String value) {
        if(!hasArrayEntry(value)) {
            rootAsArray().add(value);
        }
    }

    public Iterable<JsonNode> iterateElements() {
        return Util.iterateElements(root);
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
