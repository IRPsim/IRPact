package de.unileipzig.irpact.util.scenarios.util;

import de.unileipzig.irpact.io.param.input.names.InAttributeName;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class AttributeNameManager {

    protected Map<String, InAttributeName> names;

    public AttributeNameManager() {
        this(new HashMap<>());
    }

    public AttributeNameManager(Map<String, InAttributeName> names) {
        this.names = names;
    }

    public InAttributeName get(String name) {
        return names.computeIfAbsent(name, InAttributeName::new);
    }
}
