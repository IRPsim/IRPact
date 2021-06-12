package de.unileipzig.irpact.commons.locale;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Collections;
import java.util.Locale;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class LocalizedYaml implements LocalizedData {

//    -> zwei title
//    https://stackoverflow.com/questions/58156935/multiple-key-title-and-splitting-of-legends-in-gnuplot
//
//    -> split
//  https://stackoverflow.com/questions/41049785/gnuplot-custom-legend-with-two-different-specs/41051836
//
//    -> split v2
//    https://stackoverflow.com/questions/26831102/how-to-split-the-key-in-gnuplot/26832659
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
