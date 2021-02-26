package de.unileipzig.irpact.io.param.inout.persistance;

import de.unileipzig.irpact.commons.persistence.UIDManager;
import de.unileipzig.irpact.commons.util.IRPactBase32;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 * @author Daniel Abitz
 */
public class UIDHelper {

    private static final Pattern PATTERN = Pattern.compile("[A-Za-z0-9ÄÖÜäöüß_]+");

    public UIDHelper() {
    }

    //=========================
    //General
    //=========================

    public static boolean isValid(String name) {
        return PATTERN.matcher(name).matches();
    }

    public UIDName build(String decodedName, UIDManager manager) {
        String name;
        if(isValid(decodedName)) {
            name = decodedName;
        } else {
            name = IRPactBase32.encodeUTF8ToString(decodedName);
        }
        if(hasSameName(name)) {
            return getUIDName(name);
        } else {
            UIDName obj = new UIDName();
            obj.setAll(manager.getUID(), name, decodedName);
            putUIDName(obj);
            return obj;
        }
    }

    //=========================
    //UIDName
    //=========================

    protected Map<String, Long> uidNameToId = new HashMap<>();
    protected Map<Long, UIDName> uidNameCache = new HashMap<>();

    public boolean hasSameName(UIDName name) {
        return uidNameToId.containsKey(name.getName());
    }

    public boolean hasSameName(String name) {
        return uidNameToId.containsKey(name);
    }

    public boolean hasSameId(UIDName name) {
        return uidNameCache.containsKey(name.getUID());
    }

    public boolean hasSameId(long id) {
        return uidNameCache.containsKey(id);
    }

    public void putUIDName(UIDName name) {
        if(hasSameName(name)) {
            throw new IllegalArgumentException("name already exists: " + name.getName());
        }
        if(hasSameId(name)) {
            throw new IllegalArgumentException("id already exists: " + name.getUID());
        }
        uidNameToId.put(name.getName(), name.getUID());
        uidNameCache.put(name.getUID(), name);
    }

    public UIDName getUIDName(String name) {
        Long id = uidNameToId.get(name);
        if(id == null) {
            throw new NoSuchElementException("name: " + name);
        }
        return getUIDName(id);
    }

    public UIDName getUIDName(long uid) {
        UIDName name = uidNameCache.get(uid);
        if(name == null) {
            throw new NoSuchElementException("id: " + uid);
        }
        return name;
    }
}
