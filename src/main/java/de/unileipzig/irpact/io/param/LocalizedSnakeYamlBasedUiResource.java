package de.unileipzig.irpact.io.param;

import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * @author Daniel Abitz
 */
//for using aliasses
public class LocalizedSnakeYamlBasedUiResource extends LocalizedUiResource {

    public static final String FILE_NAME = "loc";
    public static final String FILE_EXTENSION = "yaml";

    protected Map<String, Object> data;
    private boolean escapeQuotes = true;

    public LocalizedSnakeYamlBasedUiResource() {
    }

    public void setEscapeQuotes(boolean escapeQuotes) {
        this.escapeQuotes = escapeQuotes;
    }

    public boolean isEscapeQuotes() {
        return escapeQuotes;
    }

    protected String getFileNameBase() {
        return FILE_NAME;
    }

    protected String getFileExtension() {
        return FILE_EXTENSION;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected String getString(String key, String tag) throws NoSuchElementException, IllegalArgumentException {
        if(data == null) {
            throw new IllegalStateException("no data loaded");
        }

        Object keyObj = data.get(key);
        if(keyObj == null) {
            throw new NoSuchElementException("missing: " + key);
        }
        if(!(keyObj instanceof Map)) {
            throw new IllegalArgumentException("no object: " + key);
        }
        Object paramObj = ((Map<String, Object>) keyObj).get(tag);
        if(paramObj == null) {
            return null;
        } else {
            if(paramObj instanceof String) {
                return formatJson((String) paramObj);
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
        Yaml yaml = new Yaml();
        data = yaml.load(reader);
    }
}
