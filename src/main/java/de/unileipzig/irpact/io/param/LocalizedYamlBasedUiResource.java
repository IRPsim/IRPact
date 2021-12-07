package de.unileipzig.irpact.io.param;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.unileipzig.irpact.commons.util.JsonUtil;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irptools.util.log.IRPLogger;

/**
 * @author Daniel Abitz
 */
public class LocalizedYamlBasedUiResource extends LocalizedJsonBasedUiResource {

    private static final IRPLogger LOGGER = IRPLogging.getLogger(LocalizedYamlBasedUiResource.class);

    public static final String FILE_NAME = "loc";
    public static final String FILE_EXTENSION = "yaml";

    protected ObjectNode root;

    public LocalizedYamlBasedUiResource() {
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

    @Override
    protected IRPLogger getLogger() {
        return LOGGER;
    }
}
