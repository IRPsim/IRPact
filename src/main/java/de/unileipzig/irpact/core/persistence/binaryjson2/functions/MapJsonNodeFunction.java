package de.unileipzig.irpact.core.persistence.binaryjson2.functions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeCreator;

/**
 * @author Daniel Abitz
 */
public interface MapJsonNodeFunction<I> {

    JsonNode toJson(JsonNodeCreator creator, I input);

    I fromJson(JsonNode node);
}
