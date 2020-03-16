package de.unileipzig.irpact.experimental.io.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public interface SerializationRule<R> {

    void serialize(R value, JsonGenerator jg, SerializerProvider provider) throws IOException, JsonProcessingException;
}
