package de.unileipzig.irpact.io.param.input.process.modular.ca.component.eval;

import de.unileipzig.irpact.commons.exception.ParsingException;
import de.unileipzig.irpact.core.logging.IRPLogging;
import de.unileipzig.irpact.core.process.modular.ca.components.eval.DoNothingModule;
import de.unileipzig.irpact.core.start.IRPactInputParser;
import de.unileipzig.irpact.develop.Dev;
import de.unileipzig.irpact.io.param.ParamUtil;
import de.unileipzig.irpact.io.param.input.InRootUI;
import de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentEvaluationModule;
import de.unileipzig.irpact.io.param.input.process.modular.ca.component.InConsumerAgentModule;
import de.unileipzig.irptools.defstructure.annotation.Definition;
import de.unileipzig.irptools.defstructure.annotation.FieldDefinition;
import de.unileipzig.irptools.defstructure.annotation.GraphEdge;
import de.unileipzig.irptools.defstructure.annotation.GraphNode;
import de.unileipzig.irptools.util.CopyCache;
import de.unileipzig.irptools.util.TreeAnnotationResource;
import de.unileipzig.irptools.util.log.IRPLogger;

import java.lang.invoke.MethodHandles;

import static de.unileipzig.irpact.io.param.ParamUtil.*;
import static de.unileipzig.irpact.io.param.input.process.modular.ca.MPMSettings.*;

/**
 * @author Daniel Abitz
 */
@Definition(
        graphNode = @GraphNode(
                id = MPM_GRAPH,
                label = EVAL_LABEL,
                shape = EVAL_SHAPE,
                color = EVAL_COLOR,
                border = EVAL_BORDER,
                tags = {EVAL_GRAPHNODE}
        )
)
public class InDoNothingModule_evalgraphnode implements InConsumerAgentEvaluationModule {

    private static final MethodHandles.Lookup L = MethodHandles.lookup();
    public static Class<?> thisClass() {
        return L.lookupClass();
    }
    public static String thisName() {
        return thisClass().getSimpleName();
    }

    public static void initRes(TreeAnnotationResource res) {
    }
    public static void applyRes(TreeAnnotationResource res) {
        putClassPath(res, thisClass(), InRootUI.PROCESS_MODULAR2_COMPONENTS_EVAL_DONOTHING);
        setShapeColorBorder(res, thisClass(), EVAL_SHAPE, EVAL_COLOR, EVAL_BORDER);

        addEntry(res, thisClass(), "inputModule_graphedge");
    }

    private static final IRPLogger LOGGER = IRPLogging.getLogger(thisClass());

    public String _name;
    @Override
    public String getName() {
        return _name;
    }
    public void setName(String name) {
        this._name = name;
    }

    @FieldDefinition(
            graphEdge = @GraphEdge(
                    id = MPM_GRAPH,
                    label = EVAL_EDGE_LABEL,
                    color = EVAL_EDGE_COLOR,
                    tags = {"InDoNothingModule inputModule"}
            )
    )
    public InConsumerAgentModule[] inputModule_graphedge;
    public void setInputModules(InConsumerAgentModule[] inputModules) {
        this.inputModule_graphedge = inputModules;
    }
    public InConsumerAgentModule[] getInputModules() throws ParsingException {
        return ParamUtil.getNonEmptyArray(inputModule_graphedge, "inputModule");
    }

    public InDoNothingModule_evalgraphnode() {
    }

    @Override
    public InDoNothingModule_evalgraphnode copy(CopyCache cache) {
        return cache.copyIfAbsent(this, this::newCopy);
    }

    public InDoNothingModule_evalgraphnode newCopy(CopyCache cache) {
        InDoNothingModule_evalgraphnode copy = new InDoNothingModule_evalgraphnode();
        return Dev.throwException();
    }

    @Override
    public DoNothingModule parse(IRPactInputParser parser) throws ParsingException {
        if(parser.isRestored()) {
            return MPMSettings.searchModule(parser, thisName(), DoNothingModule.class);
        }

        DoNothingModule module = new DoNothingModule();
        module.setName(getName());
        module.setEnvironment(parser.getEnvironment());
        for(InConsumerAgentModule inModule: getInputModules()) {
            module.addSubModule(parser.parseEntityTo(inModule));
        }

        return module;
    }
}
