package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;

/**
 * @author Daniel Abitz
 */
public class LocalizedYamlBasedTreeResource extends LocalizedJsonBasedTreeResource {

    public static final String FILE_NAME = "loc";
    public static final String FILE_EXTENSION = "yaml";

    protected ObjectNode root;

    public LocalizedYamlBasedTreeResource() {
    }

    @Override
    protected String getFileNameBase() {
        return FILE_NAME;
    }

    @Override
    protected String getFileExtension() {
        return FILE_EXTENSION;
    }

    @Override
    protected ObjectMapper getMapper() {
        return JsonUtil.YAML;
    }
}
