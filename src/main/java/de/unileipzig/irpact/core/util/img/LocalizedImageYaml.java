package de.unileipzig.irpact.core.util.img;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.locale.LocalizedYaml;
import de.unileipzig.irpact.commons.util.IRPactJson;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public class LocalizedImageYaml extends LocalizedYaml implements LocalizedImage {

    protected boolean escapeSpecialCharacters = false;

    public LocalizedImageYaml(Locale supported, JsonNode root) {
        super(supported, root);
    }

    public void setEscapeSpecialCharacters(boolean escapeSpecialCharacters) {
        this.escapeSpecialCharacters = escapeSpecialCharacters;
    }

    public boolean isEscapeSpecialCharacters() {
        return escapeSpecialCharacters;
    }

    protected static String mode2str(int mode) {
        switch (mode) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            default:
                return Integer.toString(mode);
        }
    }

    public String get(int mode, String key) {
        String modeStr = mode2str(mode);
        JsonNode modeNode = getRoot().get(modeStr);
        if(modeNode == null) {
            return null;
        }
        JsonNode valueNode = modeNode.get(key);
        if(valueNode != null && valueNode.isTextual()) {
            String text = valueNode.textValue();
            return tryEscape(text);
        } else {
            return null;
        }
    }

    protected String tryEscape(String input) {
        if(escapeSpecialCharacters && input != null) {
            return input.replace("\n", "\\n");
        } else {
            return input;
        }
    }

    public void set(int mode, String key, String value) {
        String modeStr = mode2str(mode);
        ObjectNode modeNode = IRPactJson.computeObjectIfAbsent((ObjectNode) getRoot(), modeStr);
        modeNode.put(key, value);
    }

    @Override
    public String getSep(int mode) {
        return get(mode, "sep");
    }

    public void setSep(int mode, String value) {
        set(mode, "sep", value);
    }

    @Override
    public String getEncoding(int mode) {
        return get(mode, "encoding");
    }

    public void setEncoding(int mode, String value) {
        set(mode, "encoding", value);
    }

    @Override
    public String getTitle(int mode) {
        return get(mode, "title");
    }

    public void setTitle(int mode, String value) {
        set(mode, "title", value);
    }

    @Override
    public String getXArg(int mode) {
        return get(mode, "xarg");
    }

    public void setXArg(int mode, String value) {
        set(mode, "xarg", value);
    }

    @Override
    public String getYArg(int mode) {
        return get(mode, "yarg");
    }

    public void setYArg(int mode, String value) {
        set(mode, "yarg", value);
    }

    @Override
    public String getGrpArg(int mode) {
        return get(mode, "grparg");
    }

    public void setGrpArg(int mode, String value) {
        set(mode, "grparg", value);
    }

    @Override
    public String getDistinctArg(int mode) {
        return get(mode, "distinctarg");
    }

    public void setDistinctArg(int mode, String value) {
        set(mode, "distinctarg", value);
    }

    @Override
    public String getFillArg(int mode) {
        return get(mode, "fillarg");
    }

    public void setFillArg(int mode, String value) {
        set(mode, "fillarg", value);
    }

    @Override
    public String getXLab(int mode) {
        return get(mode, "xlab");
    }

    public void setXLab(int mode, String value) {
        set(mode, "xlab", value);
    }

    @Override
    public String getYLab(int mode) {
        return get(mode, "ylab");
    }

    public void setYLab(int mode, String value) {
        set(mode, "ylab", value);
    }

    @Override
    public String getGrpLab(int mode) {
        return get(mode, "grplab");
    }

    public void setGrpLab(int mode, String value) {
        set(mode, "grplab", value);
    }

    @Override
    public String getDistinctLab(int mode) {
        return get(mode, "distinctlab");
    }

    public void setDistinctLab(int mode, String value) {
        set(mode, "distinctlab", value);
    }

    @Override
    public String getDistinct0Lab(int mode) {
        return get(mode, "distinct0lab");
    }

    public void setDistinct0Lab(int mode, String value) {
        set(mode, "distinct0lab", value);
    }

    @Override
    public String getDistinct1Lab(int mode) {
        return get(mode, "distinct1lab");
    }

    public void setDistinct1Lab(int mode, String value) {
        set(mode, "distinct1lab", value);
    }

    @Override
    public String getFillLab(int mode) {
        return get(mode, "filllab");
    }

    public void setFillLab(int mode, String value) {
        set(mode, "filllab", value);
    }
}
