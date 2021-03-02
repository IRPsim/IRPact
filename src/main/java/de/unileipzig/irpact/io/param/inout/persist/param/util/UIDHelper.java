package de.unileipzig.irpact.io.param.inout.persist.param.util;

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
    //JavaString
    //=========================

    protected Map<String, UIDJavaString> javaStringCache = new HashMap<>();

    public static boolean isValid(String name) {
        return PATTERN.matcher(name).matches();
    }

    public UIDJavaString buildString(String decodedName) {
        String name;
        if(isValid(decodedName)) {
            name = decodedName;
        } else {
            name = IRPactBase32.encodeUTF8ToString(decodedName);
        }
        if(hasSameName(name)) {
            return getUIDName(name);
        } else {
            UIDJavaString obj = new UIDJavaString();
            obj.initialize(name, decodedName);
            putUIDName(obj);
            return obj;
        }
    }

    private boolean hasSameName(String name) {
        return javaStringCache.containsKey(name);
    }

    private void putUIDName(UIDJavaString name) {
        if(hasSameName(name.getName())) {
            throw new IllegalArgumentException("name already exists: " + name.getName());
        }
        javaStringCache.put(name.getName(), name);
    }

    private UIDJavaString getUIDName(String name) {
        UIDJavaString str = javaStringCache.get(name);
        if(str == null) {
            throw new NoSuchElementException("name: " + name);
        }
        return str;
    }
}
