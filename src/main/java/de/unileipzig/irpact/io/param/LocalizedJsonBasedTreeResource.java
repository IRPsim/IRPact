package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.resource.LocaleUtil;
import de.unileipzig.irpact.commons.resource.ResourceLoader;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irptools.util.TreeAnnotationResource;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public abstract class LocalizedJsonBasedTreeResource extends LocalizedTreeResource {

    protected ObjectNode root;

    public LocalizedJsonBasedTreeResource() {
    }

    protected abstract String getFileNameBase();

    protected abstract String getFileExtension();

    protected abstract ObjectMapper getMapper();

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
