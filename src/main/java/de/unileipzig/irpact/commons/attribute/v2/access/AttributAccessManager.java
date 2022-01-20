package de.unileipzig.irpact.commons.attribute.v2.access;

import de.unileipzig.irpact.commons.attribute.v2.simple.Attribute2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Daniel Abitz
 */
public class AttributAccessManager {

    protected Map<Object, AttributeAccess2> accessMap;

    public AttributAccessManager() {
        this(new HashMap<>());
    }

    public AttributAccessManager(Map<Object, AttributeAccess2> accessMap) {
        this.accessMap = accessMap;
    }

    public void register(AttributeAccess2 access) {
        accessMap.put(access.getOwner(), access);
    }

    public boolean unregister(AttributeAccess2 access) {
        return accessMap.remove(access.getOwner()) != null;
    }

    public Attribute2 get(String name) {
        for(AttributeAccess2 access: accessMap.values()) {
            if(access.hasAttribute(name)) {
                return access.getAttribute(name);
            }
        }
        return null;
    }

    public Map<Object, Attribute2> getAll(String name) {
        Map<Object, Attribute2> map = new HashMap<>();
        for(AttributeAccess2 access: accessMap.values()) {
            if(access.hasAttribute(name)) {
                map.put(access.getOwner(), access.getAttribute(name));
            }
        }
        return map;
    }
}
