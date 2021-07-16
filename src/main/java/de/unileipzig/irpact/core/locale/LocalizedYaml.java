package de.unileipzig.irpact.core.locale;

import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.databind.JsonNode;
import de.unileipzig.irpact.commons.locale.LocalizedBase;
import de.unileipzig.irpact.commons.util.StringUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.logging.IRPSection;
import de.unileipzig.irpact.core.logging.LoggingHelper;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Objects;
import java.util.Set;

/**
 * @author Daniel Abitz
 */
public class LocalizedYaml extends LocalizedBase implements LoggingHelper {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LocalizedYaml.class);

    protected JsonNode root;

    public LocalizedYaml(Locale supported, JsonNode root) {
        super(supported);
        this.root = root;
    }

    public LocalizedYaml(Set<Locale> supported, JsonNode root) {
        super(supported);
        this.root = root;
    }

    @Override
    public IRPSection getDefaultSection() {
        return IRPSection.UTIL;
    }

    @Override
    public IRPLogger getDefaultLogger() {
        return LOGGER;
    }

    public JsonNode getRoot() {
        return root;
    }

    protected static String toExpr(Object... parts) {
        if(parts == null) {
            return null;
        }
        if(parts.length == 0) {
            return "/";
        }

        StringBuilder sb = new StringBuilder();
        for(Object part: parts) {
            String partStr = Objects.toString(part);
            if(!partStr.startsWith("/")) {
                sb.append("/");
            }
            sb.append(partStr);
        }
        return sb.toString();
    }

    @Override
    public String getString(Object[] keys) throws MissingResourceException {
        JsonPointer ptr = compile(keys);
        String value = searchString(ptr);
        if(value == null) {
            warn("missing data: {}", ptr);
            throw new MissingResourceException("missing data: " + ptr, "", "");
        } else {
            return value;
        }
    }

    @Override
    public String getFormattedString(Object[] keys, Object[] args) {
        String str = getString(keys);
        return StringUtil.format(str, args);
    }

    @Override
    public String[] getStringArray(Object[] keys) throws MissingResourceException {
        JsonPointer ptr = compile(keys);
        String[] arr = searchStringArray(ptr);
        if(arr == null) {
            warn("missing data: {}", ptr);
            throw new MissingResourceException("missing data: " + ptr, "", "");
        } else {
            return arr;
        }
    }

    protected static JsonPointer compile(Object... parts) {
        if(parts != null && parts.length == 1 && parts[0] instanceof JsonPointer) {
            return (JsonPointer) parts[0];
        } else {
            return JsonPointer.compile(toExpr(parts));
        }
    }

    public JsonNode search(Object... parts) {
        return getRoot().at(compile(parts));
    }

    protected String searchString(JsonPointer ptr) {
        return searchString(null, ptr);
    }
    @SuppressWarnings("SameParameterValue")
    protected String searchString(String ifMissing, JsonPointer ptr) {
        JsonNode node = getRoot().at(ptr);
        return node == null
                ? ifMissing
                : node.asText();
    }

    protected String[] searchStringArray(JsonPointer ptr) {
        return searchStringArray(null, ptr);
    }
    @SuppressWarnings("SameParameterValue")
    protected String[] searchStringArray(String[] ifMissing, JsonPointer ptr) {
        JsonNode node = getRoot().at(ptr);
        if(node == null || !node.isArray()) {
            return ifMissing;
        } else {
            String[] arr = new String[node.size()];
            for(int i = 0; i < node.size(); i++) {
                arr[i] = node.get(i).asText();
            }
            return arr;
        }
    }
}
