package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.resource.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
public abstract class LocalizedJsonBasedUiResource extends LocalizedUiResource {

    protected ObjectNode root;
    private boolean escapeQuotes = true;

    public LocalizedJsonBasedUiResource() {
    }

    public void setEscapeQuotes(boolean escapeQuotes) {
        this.escapeQuotes = escapeQuotes;
    }

    public boolean isEscapeQuotes() {
        return escapeQuotes;
    }

    protected abstract String getFileNameBase();

    protected abstract String getFileExtension();

    protected abstract ObjectMapper getMapper();

    @Override
    public String printAll(String key) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    protected String getString(String key, String tag) throws NoSuchElementException, IllegalArgumentException {
        JsonNode keyNode = root.get(key);
        if(keyNode == null) {
            throw new NoSuchElementException("missing: " + key);
        }
        if(!keyNode.isObject()) {
            throw new IllegalArgumentException("no object: " + key);
        }
        JsonNode paramNode = keyNode.get(tag);
        if(paramNode == null) {
            return null;
        } else {
            if(paramNode.isTextual()) {
                return formatJson(paramNode.textValue());
            } else {
                throw new IllegalArgumentException("no text: " + key + " -> " + tag);
            }
        }
    }

    protected String formatJson(String input) {
        String formatted = input;
        if(escapeQuotes) {
            formatted = formatted.replace("\"", "\\\"");
        }
        return formatted;
    }

    public void load(ResourceLoader loader, Locale locale) throws IOException {
        InputStream in = loader.getLocalized(getFileNameBase(), locale, getFileExtension());
        if(in == null) {
            throw new IllegalArgumentException("missing localized input: " + LocaleUtil.buildName(getFileNameBase(), locale, getFileExtension()));
        }

        try(Reader reader = new InputStreamReader(in, StandardCharsets.UTF_8)) {
            load(reader);
            this.locale = locale;
        } finally {
            in.close();
        }
    }

    public void load(java.nio.file.Path pathToResource) throws IOException {
        try(Reader reader = Files.newBufferedReader(pathToResource, StandardCharsets.UTF_8)) {
            load(reader);
        }
    }

    protected void load(Reader reader) throws IOException {
        root = (ObjectNode) getMapper().readTree(reader);
    }
}
