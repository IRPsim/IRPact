package de.unileipzig.irpact.io.param.input.process.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.process.modular.ModularProcessModel;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.start.IRPactInputParser;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class ModuleHelper {

    public static final String MODULAR_GRAPH = "modulargraph";

    public static final String ICON_TEST = "fas fa-skull-crossbones";

    public static ProcessModel searchModel(IRPactInputParser parser, String name) throws ParsingException {
        ProcessModelManager manager = parser.getEnvironment().getProcessModels();
        for(ProcessModel model: manager.getProcessModels()) {
            if(Objects.equals(model.getName(), name)) {
                return model;
            }
        }
        throw new ParsingException("missing restored process model: " + name);
    }

    public static Module searchModule(IRPactInputParser parser, String name) throws ParsingException {
        ProcessModelManager manager = parser.getEnvironment().getProcessModels();
        for(ProcessModel model: manager.getProcessModels()) {
            if(model instanceof ModularProcessModel) {
                ModularProcessModel mpm = (ModularProcessModel) model;
                Module module = mpm.searchModule(name);
                if(module != null) {
                    return module;
                }
            }
        }
        throw new ParsingException("missing restored module: " + name);
    }
}
