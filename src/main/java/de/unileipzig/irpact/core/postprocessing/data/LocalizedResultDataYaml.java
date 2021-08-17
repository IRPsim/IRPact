package de.unileipzig.irpact.core.postprocessing.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.locale.LocalizedYaml;

import java.util.Locale;

/**
 * @author Daniel Abitz
 */
public class LocalizedResultDataYaml extends LocalizedYaml implements LocalizedResultData {

    public static final String CHARSET = "charset";
    public static final String DELIMITER = "delimiter";
    public static final String COMMENT_PREFIX = "commentPrefix";
    public static final String SHEETNAME = "sheetname";
    public static final String INFOS = "informations";
    public static final String COLUMNS = "columns";

    protected boolean escapeSpecialCharacters = false;

    public LocalizedResultDataYaml(Locale supported, JsonNode root) {
        super(supported, root);
    }

    public void setEscapeSpecialCharacters(boolean escapeSpecialCharacters) {
        this.escapeSpecialCharacters = escapeSpecialCharacters;
    }

    public boolean isEscapeSpecialCharacters() {
        return escapeSpecialCharacters;
    }

    public String get(FileExtension extension, DataToAnalyse mode, String key) {
        JsonNode extensionNode = getRoot().get(extension.getText());
        if(extensionNode == null) {
            return null;
        }
        JsonNode modeNode = extensionNode.get(mode.getText());
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

    public String[] getArray(FileExtension extension, DataToAnalyse mode, String key) {
        JsonNode extensionNode = getRoot().get(extension.getText());
        if(extensionNode == null) {
            return null;
        }
        JsonNode modeNode = extensionNode.get(mode.getText());
        if(modeNode == null) {
            return null;
        }
        JsonNode arrayNode = modeNode.get(key);
        if(arrayNode != null && arrayNode.isArray()) {
            String[] arr = new String[arrayNode.size()];
            for(int i = 0; i < arrayNode.size(); i++) {
                arr[i] = tryEscape(arrayNode.get(i).textValue());
            }
            return arr;
        } else {
            return null;
        }
    }

    public void set(FileExtension extension, DataToAnalyse mode, String key, String value) {
        ObjectNode extensionNode = JsonUtil.computeObjectIfAbsent((ObjectNode) getRoot(), extension.getText());
        ObjectNode modeNode = JsonUtil.computeObjectIfAbsent(extensionNode, mode.getText());
        modeNode.put(key, value);
    }

    public void setArray(FileExtension extension, DataToAnalyse mode, String key, String[] arr) {
        ObjectNode extensionNode = JsonUtil.computeObjectIfAbsent((ObjectNode) getRoot(), extension.getText());
        ObjectNode modeNode = JsonUtil.computeObjectIfAbsent(extensionNode, mode.getText());
        ArrayNode arrayNode = JsonUtil.computeArrayIfAbsent(modeNode, key);
        for(String str: arr) {
            arrayNode.add(str);
        }
    }

    protected String tryEscape(String input) {
        if(escapeSpecialCharacters && input != null) {
            return input.replace("\n", "\\n");
        } else {
            return input;
        }
    }

    @Override
    public String getSheetName(FileExtension extension, DataToAnalyse mode) {
        return get(extension, mode, SHEETNAME);
    }
    public void setSheetName(FileExtension extension, DataToAnalyse mode, String sheetname) {
        set(extension, mode, SHEETNAME, sheetname);
    }

    @Override
    public String[] getInformations(FileExtension extension, DataToAnalyse mode) {
        return getArray(extension, mode, INFOS);
    }
    public void setInformations(FileExtension extension, DataToAnalyse mode, String[] informations) {
        setArray(extension, mode, INFOS, informations);
    }

    @Override
    public String[] getColumns(FileExtension extension, DataToAnalyse mode) {
        return getArray(extension, mode, COLUMNS);
    }
    public void setColumns(FileExtension extension, DataToAnalyse mode, String[] columns) {
        setArray(extension, mode, COLUMNS, columns);
    }

    @Override
    public String getCharset(FileExtension extension, DataToAnalyse mode) {
        return get(extension, mode, CHARSET);
    }
    public void setCharset(FileExtension extension, DataToAnalyse mode, String sheetname) {
        set(extension, mode, CHARSET, sheetname);
    }

    @Override
    public String getDelimiter(FileExtension extension, DataToAnalyse mode) {
        return get(extension, mode, DELIMITER);
    }
    public void setDelimiter(FileExtension extension, DataToAnalyse mode, String sheetname) {
        set(extension, mode, DELIMITER, sheetname);
    }

    @Override
    public String getCommentPrefix(FileExtension extension, DataToAnalyse mode) {
        return get(extension, mode, COMMENT_PREFIX);
    }
    public void setCommentPrefix(FileExtension extension, DataToAnalyse mode, String sheetname) {
        set(extension, mode, COMMENT_PREFIX, sheetname);
    }
}
