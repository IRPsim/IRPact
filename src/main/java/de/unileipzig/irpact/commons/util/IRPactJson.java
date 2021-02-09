package de.unileipzig.irpact.commons.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Daniel Abitz
 */
public final class IRPactJson {

    public static final SmileMapper SMILE = new SmileMapper();
    public static final ObjectMapper JSON = new ObjectMapper();

    private IRPactJson() {
    }

    public static byte[] toBytes(ObjectMapper mapper, JsonNode node) throws IOException {
        return toBytes(mapper, node, null);
    }

    public static byte[] toBytes(ObjectMapper mapper, JsonNode node, PrettyPrinter printer) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try(JsonGenerator gen = mapper.getFactory().createGenerator(baos)) {
            if(printer != null) {
                gen.setPrettyPrinter(printer);
            }
            gen.writeTree(node);
            gen.flush();
        }
        return baos.toByteArray();
    }

    public static byte[] toBytesWithSmile(JsonNode node) throws IOException {
        return toBytes(SMILE, node, null);
    }

    public static JsonNode fromBytes(ObjectMapper mapper, byte[] data) throws IOException {
        return mapper.readTree(data);
    }

    public static JsonNode fromBytesWithSmile(byte[] data) throws IOException {
        return fromBytes(SMILE, data);
    }
}
