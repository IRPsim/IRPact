package de.unileipzig.irpact.commons.locale;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class LocalizedYaml implements LocalizedData {

    protected Set<Locale> supported;
    protected JsonNode root;

    public LocalizedYaml(Locale supported, JsonNode root) {
        this(Collections.singleton(supported), root);
    }

    public LocalizedYaml(Set<Locale> supported, JsonNode root) {
        this.supported = supported;
        this.root = root;
    }

    @Override
    public Set<Locale> getSupportedLocales() {
        return supported;
    }

    public JsonNode getRoot() {
        return root;
    }
}
