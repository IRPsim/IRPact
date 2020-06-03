package de.unileipzig.irpact.commons;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Daniel Abitz
 */
public final class JsonUtil {

    public static final ObjectMapper mapper = new ObjectMapper();
    public static final PrettyPrinter defaultPrinter = new DefaultPrettyPrinter();
    public static final PrettyPrinter prettyPrinter = new DefaultPrettyPrinter()
            .withArrayIndenter(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);

    public static <T extends JsonNode> T readJson(Path source) throws IOException {
        try(BufferedReader reader = Files.newBufferedReader(source, StandardCharsets.UTF_8);
            JsonParser par = mapper.getFactory().createParser(reader)) {
            return par.readValueAsTree();
        }
    }

    public static String writeJson(JsonNode node, PrettyPrinter printer) {
        try {
            StringWriter sw = new StringWriter();
            try(JsonGenerator gen = mapper.getFactory().createGenerator(sw)) {
                if(printer != null) {
                    gen.setPrettyPrinter(printer);
                }
                gen.writeTree(node);
            }
            return sw.toString();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public static void writeJson(JsonNode node, Path path, PrettyPrinter printer) throws IOException {
        try(BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            JsonGenerator gen = mapper.getFactory().createGenerator(writer)) {
            if(printer != null) {
                gen.setPrettyPrinter(printer);
            }
            gen.writeTree(node);
            gen.flush();
        }
    }
}
