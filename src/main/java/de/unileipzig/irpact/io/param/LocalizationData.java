package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.commons.util.MultiCounter;
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
public final class LocalizationData {

    protected ObjectNode root;

    public LocalizationData(ObjectNode root) {
        this.root = root;
    }

    public LocalizationData(Path pathToFile) throws IOException {
        this(pathToFile, StandardCharsets.UTF_8);
    }

    public LocalizationData(Path pathToFile, Charset charset) throws IOException {
        this(JsonUtil.read(pathToFile, charset, JsonUtil.YAML));
    }

    public LocalizationData(Reader reader) throws IOException {
        this.root = (ObjectNode) JsonUtil.YAML.readTree(reader);
    }

    public String getString(String key, String tag) {
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

    public Boolean getBoolean(String key, String tag) {
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
            if(paramNode.isBoolean()) {
                return paramNode.booleanValue();
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
            setValidString(getGamsUnit(key), builder::setGamsUnit);
            setValidString(getGamsDomain(key), builder::setGamsDomain);
            setValidString(getGamsDefault(key), builder::setGamsDefault);
        };
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(String key0, String key1) {
        String key = ParamUtil.concData(key0, key1);
        return applyEntryBuilder(key);
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(Class<?> c) {
        return applyEntryBuilder(c.getSimpleName());
    }

    public Consumer<TreeAnnotationResource.EntryBuilder> applyEntryBuilder(Class<?> c, String field) {
        return applyEntryBuilder(c.getSimpleName(), field);
    }

    private void setValidString(String input, Consumer<? super String> consumer) {
        if(input != null && !input.isEmpty()) {
            consumer.accept(input);
        }
    }

    private String getEdnLabel(String key) {
        return getString(key, EDN_LABEL);
    }

    private String getEdnDescription(String key) {
        return getString(key, EDN_DESCRIPTION);
    }

    private String getGamsIdentifier(String key) {
        return getString(key, GAMS_IDENTIFIER);
    }

    private String getGamsDescription(String key) {
        return getString(key, GAMS_DESCRIPTION);
    }

    private String getGamsUnit(String key) {
        return getString(key, GAMS_UNIT);
    }

    private String getGamsDomain(String key) {
        return getString(key, GAMS_DOMAIN);
    }

    private String getGamsDefault(String key) {
        return getString(key, GAMS_DEFAULT);
    }

    //=========================
    //util
    //=========================

    public static void addPathElement(TreeAnnotationResource res, String dataKey, String priorityKeyForCounter) {
        IOResources.Data userData = res.getUserDataAs();
        MultiCounter counter = userData.getCounter();
        LocalizationData loc = userData.getData();

        TreeAnnotationResource.PathElementBuilder builder = res.newElementBuilder();
        builder = builder.peek(loc.applyPathElementBuilder(dataKey));
        if(priorityKeyForCounter != null) {
            builder = builder.setEdnPriority(counter.getAndInc(priorityKeyForCounter));
        }
        builder.putCache(dataKey);
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c) {
        IOResources.Data userData = res.getUserDataAs();
        LocalizationData loc = userData.getData();
        ParamUtil.computeEntryBuilderIfAbsent(res, c).peek(loc.applyEntryBuilder(c));
    }

    public static void addEntry(TreeAnnotationResource res, Class<?> c, String field) {
        IOResources.Data userData = res.getUserDataAs();
        LocalizationData loc = userData.getData();
        ParamUtil.computeEntryBuilderIfAbsent(res, c, field).peek(loc.applyEntryBuilder(c, field));
    }

    public static void putClassPath(TreeAnnotationResource res, Class<?> c, String... keys) {
        res.putPath(c, res.getCachedElements(keys));
    }

    public static void putFieldPath(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        res.putPath(c, field, res.getCachedElements(keys));
    }

    public static void putFieldPathAndAddEntry(TreeAnnotationResource res, Class<?> c, String field, String... keys) {
        putFieldPath(res, c, field, keys);
        addEntry(res, c, field);
    }
}
