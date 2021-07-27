package de.unileipzig.irpact.io.param.input.process.modular.ca;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.process.ProcessModel;
import de.unileipzig.irpact.core.process.ProcessModelManager;
import de.unileipzig.irpact.core.process.modular.ModularProcessModel;
import de.unileipzig.irpact.core.process.modular.components.core.Module;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.io.param.ParamUtil;

import java.util.Objects;

/**
 * @author Daniel Abitz
 */
public final class MPMSettings {

    public static final String MPM_GRAPH = "modulargraph";

    //node

    public static final String INPUT_GRAPHNODE = "inputgraphnode";
    public static final String CALC_GRAPHNODE = "calcgraphnode";
    public static final String EVAL_GRAPHNODE = "evalgraphnode";

    public static final String INPUT_LABEL = "Eingabemodul";
    public static final String CALC_LABEL = "Berechnungsmodul";
    public static final String EVAL_LABEL = "Evaluierungsmodul";

    public static final String INPUT_SHAPE = ParamUtil.SHAPE_SQUARE;
    public static final String CALC_SHAPE = ParamUtil.SHAPE_GEAR;
    public static final String EVAL_SHAPE = ParamUtil.SHAPE_DIAMOND;

    public static final String INPUT_COLOR = ParamUtil.COLOR_GREEN;
    public static final String CALC_COLOR = ParamUtil.COLOR_BLUE;
    public static final String EVAL_COLOR = ParamUtil.COLOR_RED;

    public static final String INPUT_BORDER = ParamUtil.COLOR_GREEN;
    public static final String CALC_BORDER = ParamUtil.COLOR_BLUE;
    public static final String EVAL_BORDER = ParamUtil.COLOR_RED;

    //edge

    public static final String GRAPHEDGE = "graphedge";

    public static final String CALC_EDGE_LABEL = "Berechnungskante";
    public static final String EVAL_EDGE_LABEL = "Evaluierungskante";

    public static final String CALC_EDGE_COLOR = ParamUtil.COLOR_DARK_CYAN;
    public static final String EVAL_EDGE_COLOR = ParamUtil.COLOR_LIGHT_SLATE_GREY;

    //...

    public static final String ICON_TEST = "fas fa-skull-crossbones";

    protected MPMSettings() {
    }

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

    public static <M extends Module> M searchModule(IRPactInputParser parser, String name, Class<M> type) throws ParsingException {
        Module module = searchModule(parser, name);
        if(type.isInstance(module)) {
            return type.cast(module);
        } else {
            throw new ParsingException("class mismatch: '{}' != '{}'", module.getClass(), type);
        }
    }
}
