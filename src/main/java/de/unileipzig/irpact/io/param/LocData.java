package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.IRPactJson;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import static de.unileipzig.irpact.io.param.IOConstants.*;

/**
 * @author Daniel Abitz
 */
public class LocData {

    protected ObjectNode root;

    public LocData(ObjectNode root) {
        this.root = root;
    }

    public LocData(Path pathToFile) throws IOException {
        this(pathToFile, StandardCharsets.UTF_8);
    }

    public LocData(Path pathToFile, Charset charset) throws IOException {
        this(IRPactJson.read(pathToFile, charset, IRPactJson.YAML));
    }

    public LocData(Reader reader) throws IOException {
        this.root = (ObjectNode) IRPactJson.YAML.readTree(reader);
    }

    public String get(String key, String tag) {
        JsonNode keyNode = root.get(key);
        if(keyNode == null) {
            throw new NoSuchElementException(key);
        }
        if(!keyNode.isObject()) {
            throw new IllegalArgumentException("no object: " + key);
        }
        JsonNode paramNode = keyNode.get(tag);
        if(paramNode == null) {
            return null;
        } else {
            if(paramNode.isTextual()) {
                return paramNode.textValue();
            } else {
                throw new IllegalArgumentException("no text: " + key + " -> " + tag);
            }
        }
    }

    public Consumer<TreeAnnotationResource.PathElementBuilder> applyPathElementBuilder(String key) {
        return builder -> {
            setValidString(getEdnLabel(key), builder::setEdnLabel);
            setValidString(getEdnDescription(key), builder::setEdnDescription);
        };
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(String key) {
        return builder -> {
            setValidString(getGamsIdentifier(key), builder::setGamsIdentifier);
            setValidString(getGamsDescription(key), builder::setGamsDescription);
        };
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(String key0, String key1) {
        String key = ParamUtil.concData(key0, key1);
        return applyEntryBuilder(key);
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(Class<?> c) {
        return applyEntryBuilder(c.getName());
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(Class<?> c, String field) {
        return applyEntryBuilder(c.getName(), field);
    }

    private void setValidString(String input, Consumer<? super String> consumer) {
        if(input != null && !input.isEmpty()) {
            consumer.accept(input);
        }
    }

    private String getEdnLabel(String key) {
        return get(key, EDN_LABEL);
    }

    private String getEdnDescription(String key) {
        return get(key, EDN_DESCRIPTION);
    }

    private String getGamsIdentifier(String key) {
        return get(key, GAMS_IDENTIFIER);
    }

    private String getGamsDescription(String key) {
        return get(key, GAMS_DESCRIPTION);
    }
}
