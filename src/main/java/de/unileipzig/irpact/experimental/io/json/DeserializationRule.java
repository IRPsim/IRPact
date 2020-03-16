package de.unileipzig.irpact.experimental.io.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface DeserializationRule<R> {

    R deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException;
}
