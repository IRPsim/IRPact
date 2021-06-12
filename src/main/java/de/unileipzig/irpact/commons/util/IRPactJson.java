package de.unileipzig.irpact.commons.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import de.unileipzig.irpact.commons.logging.LazyPrinter;
import de.unileipzig.irpact.commons.logging.LazyToString;
import de.unileipzig.irptools.util.Util;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class IRPactJson {

    public static final SmileMapper SMILE = new SmileMapper();
    public static final ObjectMapper JSON = new ObjectMapper();
    public static final YAMLMapper YAML = new YAMLMapper();

    public static final PrettyPrinter PRETTY = Util.prettyPrinter;
    public static final PrettyPrinter DEFAULT = Util.defaultPrinter;

    private IRPactJson() {
    }

    public static String toString(JsonNode node) {
        return Util.printJson(node);
    }

    public static String toString(JsonNode node, PrettyPrinter printer) {
        return Util.printJson(node, printer);
    }

    public static String toString(JsonNode node, ObjectMapper mapper) {
        return Util.printJson(mapper, node);
    }

    public static String toString(JsonNode node, ObjectMapper mapper, PrettyPrinter printer) {
        return Util.printJson(mapper, node, printer);
    }

    public static LazyToString toLazyString(JsonNode node) {
        return new LazyPrinter(() -> toString(node));
    }

    public static LazyToString toLazyString(JsonNode node, PrettyPrinter printer) {
        return new LazyPrinter(() -> toString(node, printer));
    }

    public static LazyToString toLazyString(JsonNode node, ObjectMapper mapper) {
        return new LazyPrinter(() -> toString(node, mapper));
    }

    public static LazyToString toLazyString(JsonNode node, ObjectMapper mapper, PrettyPrinter printer) {
        return new LazyPrinter(() -> toString(node, mapper, printer));
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
        return read(source, charset, JSON);
    }

    public static <T extends JsonNode> T read(InputStream in, ObjectMapper mapper) throws IOException {
        try(JsonParser par = mapper.getFactory().createParser(in)) {
            par.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
            return par.readValueAsTree();
        }
    }

    public static <T extends JsonNode> T read(Path source, Charset charset, ObjectMapper mapper) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(source, charset);
            JsonParser par = mapper.getFactory().createParser(reader)) {
            return par.readValueAsTree();
        }
    }

    public static void writeJson(JsonNode node, Path path, PrettyPrinter printer) throws IOException {
        writeJson(node, path, StandardCharsets.UTF_8, printer);
    }

    public static void writeJson(JsonNode node, Path path, Charset charset, PrettyPrinter printer) throws IOException {
        write(node, path, charset, printer, JSON);
    }

    public static void write(JsonNode node, Path path, Charset charset, PrettyPrinter printer, ObjectMapper mapper) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, charset);
            JsonGenerator gen = mapper.getFactory().createGenerator(writer)) {
            if(printer != null) {
                gen.setPrettyPrinter(printer);
            }
            gen.writeTree(node);
            gen.flush();
        }
    }

    public static String getText(JsonNode parent, String key, String ifNotValid) {
        return getText(parent.get(key), ifNotValid);
    }

    public static String getText(JsonNode node, String ifNotValid) {
        if(node != null && node.isTextual()) {
            return node.textValue();
        } else {
            return ifNotValid;
        }
    }

    public static double getDouble(JsonNode parent, String key, double ifNotValid) {
        return getDouble(parent.get(key), ifNotValid);
    }

    public static double getDouble(JsonNode node, double ifNotValid) {
        if(node != null && node.isNumber()) {
            return node.doubleValue();
        } else {
            return ifNotValid;
        }
    }

    public static long getLong(JsonNode parent, String key, long ifNotValid) {
        return getLong(parent.get(key), ifNotValid);
    }

    public static long getLong(JsonNode node, long ifNotValid) {
        if(node != null && node.isNumber()) {
            return node.longValue();
        } else {
            return ifNotValid;
        }
    }

    public static int getInt(JsonNode parent, String key, int ifNotValid) {
        return getInt(parent.get(key), ifNotValid);
    }

    public static int getInt(JsonNode node, int ifNotValid) {
        if(node != null && node.isNumber()) {
            return node.intValue();
        } else {
            return ifNotValid;
        }
    }

    public static boolean hasText(JsonNode parent, String key, String value) {
        JsonNode node = parent.get(key);
        if(node == null || !node.isTextual()) {
            return false;
        }
        return Objects.equals(node.textValue(), value);
    }

    public static ObjectNode computeObjectIfAbsent(ObjectNode root, String fieldName) {
        JsonNode node = root.get(fieldName);
        if(node == null) {
            return root.putObject(fieldName);
        } else {
            if(node.isObject()) {
                return (ObjectNode) node;
            } else {
                throw new IllegalArgumentException("no object node: " + node.getNodeType());
            }
        }
    }

    public static ArrayNode computeArrayIfAbsent(ObjectNode root, String fieldName) {
        JsonNode node = root.get(fieldName);
        if(node == null) {
            return root.putArray(fieldName);
        } else {
            if(node.isArray()) {
                return (ArrayNode) node;
            } else {
                throw new IllegalArgumentException("no array node: " + node.getNodeType());
            }
        }
    }
}
