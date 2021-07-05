package de.unileipzig.irpact.core.util.img;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.locale.LocalizedYaml;
import de.unileipzig.irpact.commons.util.JsonUtil;

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

    public String get(DataToVisualize mode, String key) {
        JsonNode modeNode = getRoot().get(mode.name());
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

    public void set(DataToVisualize mode, String key, String value) {
        ObjectNode modeNode = JsonUtil.computeObjectIfAbsent((ObjectNode) getRoot(), mode.name());
        modeNode.put(key, value);
    }

    @Override
    public String getSep(DataToVisualize mode) {
        return get(mode, "sep");
    }

    public void setSep(DataToVisualize mode, String value) {
        set(mode, "sep", value);
    }

    @Override
    public String getEncoding(DataToVisualize mode) {
        return get(mode, "encoding");
    }

    public void setEncoding(DataToVisualize mode, String value) {
        set(mode, "encoding", value);
    }

    @Override
    public String getTitle(DataToVisualize mode) {
        return get(mode, "title");
    }

    public void setTitle(DataToVisualize mode, String value) {
        set(mode, "title", value);
    }

    @Override
    public String getXArg(DataToVisualize mode) {
        return get(mode, "xarg");
    }

    public void setXArg(DataToVisualize mode, String value) {
        set(mode, "xarg", value);
    }

    @Override
    public String getYArg(DataToVisualize mode) {
        return get(mode, "yarg");
    }

    public void setYArg(DataToVisualize mode, String value) {
        set(mode, "yarg", value);
    }

    @Override
    public String getGrpArg(DataToVisualize mode) {
        return get(mode, "grparg");
    }

    public void setGrpArg(DataToVisualize mode, String value) {
        set(mode, "grparg", value);
    }

    @Override
    public String getDistinctArg(DataToVisualize mode) {
        return get(mode, "distinctarg");
    }

    public void setDistinctArg(DataToVisualize mode, String value) {
        set(mode, "distinctarg", value);
    }

    @Override
    public String getFillArg(DataToVisualize mode) {
        return get(mode, "fillarg");
    }

    public void setFillArg(DataToVisualize mode, String value) {
        set(mode, "fillarg", value);
    }

    @Override
    public String getXLab(DataToVisualize mode) {
        return get(mode, "xlab");
    }

    public void setXLab(DataToVisualize mode, String value) {
        set(mode, "xlab", value);
    }

    @Override
    public String getYLab(DataToVisualize mode) {
        return get(mode, "ylab");
    }

    public void setYLab(DataToVisualize mode, String value) {
        set(mode, "ylab", value);
    }

    @Override
    public String getGrpLab(DataToVisualize mode) {
        return get(mode, "grplab");
    }

    public void setGrpLab(DataToVisualize mode, String value) {
        set(mode, "grplab", value);
    }

    @Override
    public String getDistinctLab(DataToVisualize mode) {
        return get(mode, "distinctlab");
    }

    public void setDistinctLab(DataToVisualize mode, String value) {
        set(mode, "distinctlab", value);
    }

    @Override
    public String getDistinct0Lab(DataToVisualize mode) {
        return get(mode, "distinct0lab");
    }

    public void setDistinct0Lab(DataToVisualize mode, String value) {
        set(mode, "distinct0lab", value);
    }

    @Override
    public String getDistinct1Lab(DataToVisualize mode) {
        return get(mode, "distinct1lab");
    }

    public void setDistinct1Lab(DataToVisualize mode, String value) {
        set(mode, "distinct1lab", value);
    }

    @Override
    public String getFillLab(DataToVisualize mode) {
        return get(mode, "filllab");
    }

    public void setFillLab(DataToVisualize mode, String value) {
        set(mode, "filllab", value);
    }
}
