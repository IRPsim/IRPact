package de.unileipzig.irpact.commons.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

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

    public static <T extends JsonNode> T readJson(Path source) throws IOException {
        return readJson(source, StandardCharsets.UTF_8);
    }

    public static <T extends JsonNode> T readJson(Path source, Charset charset) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(source, charset);
            JsonParser par = JSON.getFactory().createParser(reader)) {
            return par.readValueAsTree();
        }
    }

    public static void writeJson(JsonNode node, Path path, PrettyPrinter printer) throws IOException {
        writeJson(node, path, StandardCharsets.UTF_8, printer);
    }

    public static void writeJson(JsonNode node, Path path, Charset charset, PrettyPrinter printer) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, charset);
            JsonGenerator gen = JSON.getFactory().createGenerator(writer)) {
            if(printer != null) {
                gen.setPrettyPrinter(printer);
            }
            gen.writeTree(node);
            gen.flush();
        }
    }
}
